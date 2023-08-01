package org.Simbot.plugins.avSearch;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.component.mirai.message.MiraiForwardMessageBuilder;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;
import net.mamoe.mirai.message.data.ForwardMessage;
import org.Simbot.plugins.avSearch.entity.JavData;
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

    @Listener
    @Filter(value = "/av ", matchType = MatchType.TEXT_STARTS_WITH)
    @SneakyThrows
    public void getAVMessage(@NotNull final GroupMessageEvent event) {
        //获取用户输入的内容
        final String next = event.getMessageContent().getPlainText()
                .substring(4)
                .toUpperCase(Locale.ROOT)
                //将输入的内容中间的数字前面加上"-",如果有则不加
                .replaceFirst("(?<=[a-zA-Z])(?!-)(?=\\d)", "-");
        final var messageReceipt = SendMsgUtil.sendReplyGroupMsg(event, "正在检索中，请稍候");
        //撤回消息
        SendMsgUtil.withdrawMessage(messageReceipt, 15);
        //通过番号获取详情
        final JavData avDetail = avDetailsScraper.getAVDetail(next);

        if (avDetail == null) {
            SendMsgUtil.sendSimpleGroupMsg(event, "没有找到相关信息");
            return;
        }
        //下载封面
        final var arrayInputStream = CompletableFuture.supplyAsync(() -> AsyncHttpClientUtil.downloadImage(avDetail.getCoverImage()));
        //获取视频播放地址
        final var videoPlayUrl = CompletableFuture.supplyAsync(() -> netflavDetailsScraper.getVideoUrl(avDetail.getAvNumber()));
        //获取磁力链接
        final var netflavMagnetLink = CompletableFuture.supplyAsync(() -> netflavDetailsScraper.getMagnetLink(avDetail.getAvNumber()));
        final var builder = new MessagesBuilder();
        //构建消息链
        final var chain = new MiraiForwardMessageBuilder(ForwardMessage.DisplayStrategy.Default);

//        final var previewImages = avDetail.getPreviewImages();//javbus方式获取预览图, 有水印, 换为netflav方式获取
        final var previewImages = netflavDetailsScraper.getPreviewImages(avDetail.getAvNumber());
        final var stringBuilder = new StringBuilder()
                .append("番号 : ").append(avDetail.getAvNumber()).append("\n")
                .append("标题 : ").append(avDetail.getTitle()).append("\n")
                .append("演员 : ").append(avDetail.getActors()).append("\n")
                .append("发行日期 : ").append(avDetail.getReleaseDate()).append("\n")
                .append("类别 : ").append(avDetail.getCategories().stream().reduce((a, b) -> a + " " + b).orElse("没有找到相关信息")).append("\n")
                .append("封面 : " + "\n");

        builder.text(stringBuilder.toString())
                .image(Resource.of(arrayInputStream.get(15, TimeUnit.SECONDS)))
                .text("预览图 :\n");

        //并行流下载
        log.info("开始下载预览图");
        Optional.ofNullable(previewImages)
                .orElse(Collections.emptyList())
//                .subList(0, Math.min(previewImages.size(), 5))//最多下载5张预览图
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
        chain.add(event.getBot(), "在线播放地址 :\n" + videoPlayUrl.get(15, TimeUnit.SECONDS).stream().reduce((a, b) -> a + "\n" + b).orElse("没有找到相关信息"));
        final Map<String, Set<String>> map = netflavMagnetLink.get(15, TimeUnit.SECONDS);
        final var magnetMessageBuilder = new MessagesBuilder().text("磁力链接 : \n");
        if (CollUtil.isNotEmpty(map)) {
            magnetMessageBuilder.text("[HD]\n")
                    .text(map.get("HD").stream().reduce((a, b) -> a + "\n" + b).orElse("没有找到相关信息")).text("\n")
                    .text("\n[HD][中文字幕]\n")
                    .text(map.get("HD[SUB]").stream().reduce((a, b) -> a + "\n" + b).orElse("没有找到相关信息")).text("\n");
        } else {
            magnetMessageBuilder.text(avDetail.getMagnetLink().stream().reduce((a, b) -> a + "\n" + b).orElse("没有找到相关信息"));
        }

        chain.add(event.getBot(), magnetMessageBuilder.build());
        //发送消息
        log.info("开始发送消息");
        final var sendAsync = event.getSource().sendAsync(chain.build());
        //撤回消息
//        SendMsgUtil.withdrawMessage(sendAsync.get(30, TimeUnit.SECONDS), 55);
    }
}
