package org.Simbot.plugins.avSearch;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.FilterValue;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.component.mirai.message.MiraiForwardMessageBuilder;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;
import net.mamoe.mirai.message.data.ForwardMessage;
import org.Simbot.mybatisplus.mapper.AvDetailMapper;
import org.Simbot.mybatisplus.mapper.AvPreviewMapper;
import org.Simbot.plugins.avSearch.entity.AvDetail;
import org.Simbot.plugins.avSearch.entity.CustomDetailEntity;
import org.Simbot.plugins.avSearch.entity.FC2SearchEntity;
import org.Simbot.utils.AsyncHttpClientUtil;
import org.Simbot.utils.SendMsgUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author ：ycvk
 * @description ：av搜索监听器
 * @date ：2023/07/24 12:39
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AVListener {

    private final AVDetailsScraper avDetailsScraper;

    private final NetflavDetailsScraper netflavDetailsScraper;

    private final AvDetailMapper avDetailMapper;

    private final AvPreviewMapper avPreviewMapper;

    @Listener
    @Filter(value = "/av {{text}}", matchType = MatchType.REGEX_CONTAINS)
    @SneakyThrows
    public void getAVMessage(@NotNull final GroupMessageEvent event, @FilterValue("text") String text) {
        //获取用户输入的内容
        text = text.toUpperCase(Locale.ROOT).replaceAll("\\s", "");
        if (StrUtil.isBlank(text)) {
            SendMsgUtil.sendSimpleGroupMsg(event, "请输入番号");
            return;
        }
        if (!text.startsWith("FC2")) {
            //将输入的内容中间的数字前面加上"-",如果有则不加
            text = text.replaceFirst("(?<=[a-zA-Z])(?!-)(?=\\d)", "-");
        } else {
            text = text.replaceFirst("^FC2(?!-)", "FC2-");
        }

        //构建消息链
        final var chain = new MiraiForwardMessageBuilder(ForwardMessage.DisplayStrategy.Default);

        if (text.startsWith("FC2")) {
            SendMsgUtil.withdrawMessage(SendMsgUtil.sendSimpleGroupMsg(event, "正在检索fc2中，请稍候"), 15);
            final List<FC2SearchEntity> list = FC2Scraper.searchByAvNum(text);
            if (CollUtil.isEmpty(list) || null == list.get(0)) {
                SendMsgUtil.sendSimpleGroupMsg(event, "没有找到相关信息");
                return;
            }
            final MessagesBuilder builder = FC2Scraper.buildFC2Message(list.get(0));
            chain.add(event.getBot(), builder.build());
            event.getSource().sendAsync(chain.build());
            return;
        }
        final var messageReceipt = SendMsgUtil.sendReplyGroupMsg(event, "正在检索中，请稍候");
        final boolean flag;
        final AvDetail avDetail;
        final List<String> previewImages;
        //通过番号获取详情
        final AvDetail avDetailByDB = avDetailMapper.selectByAvNum(text);
        if (BeanUtil.isEmpty(avDetailByDB)) {
            //数据库没有, 通过javbus获取详情
            flag = true;
            avDetail = avDetailsScraper.getAVDetail(text);
        } else {
            flag = false;
            avDetail = avDetailByDB;
        }
        //撤回消息
        SendMsgUtil.withdrawMessage(messageReceipt, 15);
        if (avDetail == null) {
//            SendMsgUtil.sendSimpleGroupMsg(event, "没有找到相关信息");
            getAvDetailByArzon(text, event);
            return;
        }
        //下载封面
        final var arrayInputStream = CompletableFuture.supplyAsync(() -> AsyncHttpClientUtil.downloadImage(avDetail.getCoverUrl()));
        //获取视频播放地址
        final var videoPlayUrl = CompletableFuture.supplyAsync(() -> flag ? netflavDetailsScraper.getVideoUrl(avDetail.getAvNum()) : JSONUtil.toList(avDetail.getOnlinePlayUrl(), String.class));
        //获取磁力链接
        final var netflavMagnetLink = flag ? CompletableFuture.supplyAsync(() -> netflavDetailsScraper.getMagnetLink(avDetail.getAvNum())) : null;
        //获取番号简介
        final var description = CompletableFuture.supplyAsync(() -> flag ? netflavDetailsScraper.getDescription(avDetail.getAvNum()) : avDetail.getDescription());

        final var builder = new MessagesBuilder();


//        final var previewImages = avDetail.getPreviewImages();//javbus方式获取预览图, 有水印, 换为netflav方式获取
        if (flag) {
            final List<String> netflavPreviewImgs = netflavDetailsScraper.getPreviewImages(avDetail.getAvNum());
            if (netflavPreviewImgs.size() > 1 || netflavPreviewImgs.size() >= avDetail.getPreviewImages().size()) {
                previewImages = netflavPreviewImgs;
            } else {
                previewImages = avDetail.getPreviewImages();
            }
        } else {
            previewImages = avPreviewMapper.selectByAvNum(text);
        }
        if (CollUtil.isNotEmpty(previewImages)) {
            avDetail.setPreviewImages(previewImages);
        }
        final String descriptionByNetflav = description.get(15, TimeUnit.SECONDS);
        final var stringBuilder = new StringBuilder()
                .append("番号 : ").append(avDetail.getAvNum()).append("\n")
                .append("标题 : ").append(avDetail.getTitle()).append("\n")
                .append("演员 : ").append(avDetail.getActors()).append("\n")
                .append("简介 : ").append(descriptionByNetflav).append("\n")
                .append("发行日期 : ").append(avDetail.getReleaseDate()).append("\n")
                .append("类别 : ").append(JSONUtil.toList(avDetail.getCategories(), String.class).stream().reduce((a, b) -> a + " " + b).orElse("没有找到相关信息")).append("\n")
                .append("封面 : " + "\n");

        builder.text(stringBuilder.toString())
                .image(Resource.of(arrayInputStream.get(15, TimeUnit.SECONDS)))
                .text("预览图 :\n");

        //并行流下载
        log.info("开始下载预览图");
        Optional.ofNullable(previewImages)
                .orElse(Collections.emptyList())
                .subList(0, Math.min(previewImages.size(), 8))//最多下载8张预览图
                .parallelStream()
                .filter(StrUtil::isNotBlank)
                .map(AsyncHttpClientUtil::downloadImage)
                .forEach(inputStream -> {
                    try {
                        builder.image(Resource.of(inputStream));
                    } catch (final IOException e) {
                        builder.text("下载预览图失败\n");
                        log.error("下载预览图失败", e);
                    }
                });

        chain.add(event.getBot(), builder.build());
        final List<String> videoPlayLink = videoPlayUrl.get(15, TimeUnit.SECONDS);
        avDetail.setOnlinePlayUrl(JSONUtil.toJsonStr(videoPlayLink));
        chain.add(event.getBot(), "在线播放地址 :\n" + videoPlayLink.stream().reduce((a, b) -> a + "\n" + b).orElse("没有找到相关信息"));
        final var magnetMessageBuilder = new MessagesBuilder().text("磁力链接 : \n");
        if (flag) {
            final Map<String, Set<String>> map = netflavMagnetLink.get(15, TimeUnit.SECONDS);
            if (CollUtil.isNotEmpty(map)) {
                final Set<String> magnetHD = map.get("HD");
                avDetail.setMagnetLinkHd(JSONUtil.toJsonStr(magnetHD));
                magnetMessageBuilder.text("[HD]\n")
                        .text(magnetHD.stream().reduce((a, b) -> a + "\n" + b).orElse("没有找到相关信息")).text("\n");
                if (CollUtil.isNotEmpty(map.get("HD[SUB]"))) {
                    final Set<String> magnetSub = map.get("HD[SUB]");
                    magnetMessageBuilder.text("\n[HD][中文字幕]\n")
                            .text(magnetSub.stream().reduce((a, b) -> a + "\n" + b).orElse("没有找到相关信息")).text("\n");
                    avDetail.setMagnetLinkSub(JSONUtil.toJsonStr(magnetSub));
                }
            } else {
                magnetMessageBuilder.text(JSONUtil.toList(avDetail.getMagnetLink(), String.class).stream().reduce((a, b) -> a + "\n" + b).orElse("没有找到相关信息"));
            }
        } else {
            if (StrUtil.isNotBlank(avDetail.getMagnetLinkHd())) {
                final String magnetLinkHd = JSONUtil.toList(avDetail.getMagnetLinkHd(), String.class).stream().reduce((a, b) -> a + "\n" + b).orElse("没有找到相关信息");
                magnetMessageBuilder.text("[HD]\n")
                        .text(magnetLinkHd).text("\n");
                if (StrUtil.isNotBlank(avDetail.getMagnetLinkSub())) {
                    final String magnetLinkSub = JSONUtil.toList(avDetail.getMagnetLinkSub(), String.class).stream().reduce((a, b) -> a + "\n" + b).orElse("没有找到相关信息");
                    magnetMessageBuilder.text("\n[HD][中文字幕]\n")
                            .text(magnetLinkSub).text("\n");
                }
            } else {
                magnetMessageBuilder.text(JSONUtil.toList(avDetail.getMagnetLink(), String.class).stream().reduce((a, b) -> a + "\n" + b).orElse("没有找到相关信息"));
            }
        }

        chain.add(event.getBot(), magnetMessageBuilder.build());
        //发送消息
        log.info("开始发送消息");
        final var sendAsync = event.getSource().sendAsync(chain.build());
        if (flag) {
            avDetail.setDuration(netflavDetailsScraper.getDuration(avDetail.getAvNum()));
            if (StrUtil.isNotBlank(descriptionByNetflav)) {
                avDetail.setDescription(descriptionByNetflav);
            }
            //保存到数据库
            avDetailMapper.insert(avDetail);
            avPreviewMapper.insertList(avDetail.getAvNum(), previewImages);
        }
        //撤回消息
//        SendMsgUtil.withdrawMessage(sendAsync.get(30, TimeUnit.SECONDS), 55);
    }

    @SneakyThrows
    private void getAvDetailByArzon(final String avNum, final GroupMessageEvent event) {
        final List<CustomDetailEntity> entities = ArzonScraper.searchByAvNum(avNum);
        if (CollUtil.isEmpty(entities)) {
            SendMsgUtil.sendSimpleGroupMsg(event, "没有找到相关信息");
            return;
        }
        final CustomDetailEntity detail = entities.stream().filter(entity -> entity.getNumber().equalsIgnoreCase(avNum)).findFirst().orElse(null);
        if (BeanUtil.isEmpty(detail)) {
            SendMsgUtil.sendSimpleGroupMsg(event, "没有找到相关信息");
            return;
        }
        final Map<String, String> headers = ArzonScraper.getHeaders(detail.getHomepage());
        final var document = ArzonScraper.getDocument(detail);
        //获取封面
        final var coverStream = CompletableFuture.supplyAsync(() -> AsyncHttpClientUtil.downloadImage(detail.getCoverUrl(), headers));
        //获取详情
        final String desc = ArzonScraper.getDesc(document);
        //获取预览图
        final List<String> previewImg = ArzonScraper.getPreviewImg(document);

        final var stringBuilder = new StringBuilder()
                .append("番号 : ").append(detail.getNumber()).append("\n")
                .append("标题 : ").append(detail.getTitle()).append("\n")
                .append("演员 : ").append(detail.getActors().stream().reduce((a, b) -> a + " " + b)).append("\n")
                .append("简介 : ").append(desc).append("\n")
                .append("发行日期 : ").append(DateUtil.parse(detail.getReleaseDate()).toDateStr()).append("\n")
                .append("封面 : " + "\n");
        final var builder = new MessagesBuilder();
        builder.text(stringBuilder.toString())
                .image(Resource.of(coverStream.get(15, TimeUnit.SECONDS)));
        if (CollUtil.isNotEmpty(previewImg)) {
            builder.text("预览图 :\n");
            previewImg.subList(0, Math.min(previewImg.size(), 8))//最多下载8张预览图
                    .parallelStream()
                    .filter(StrUtil::isNotBlank)
                    .map(imgUrl -> AsyncHttpClientUtil.downloadImage(imgUrl, headers))
                    .forEach(inputStream -> {
                        try {
                            builder.image(Resource.of(inputStream));
                        } catch (final IOException e) {
                            builder.text("下载预览图失败\n");
                            log.error("下载预览图失败", e);
                        }
                    });
        }
        final var chain = new MiraiForwardMessageBuilder(ForwardMessage.DisplayStrategy.Default);
        chain.add(event.getBot(), builder.build());
        event.getSource().sendAsync(chain.build());
    }
}
