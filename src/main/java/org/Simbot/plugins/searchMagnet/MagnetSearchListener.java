package org.Simbot.plugins.searchMagnet;

import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;
import org.Simbot.plugins.searchMagnet.entity.MagnetSearchData;
import org.Simbot.plugins.searchMagnet.entity.Screenshots;
import org.Simbot.utils.AsyncHttpClientUtil;
import org.Simbot.utils.SendMsgUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author ：ycvk
 * @description ：磁力搜索监听器
 * @date ：2023/08/08 15:27
 */
@Component
@Slf4j
public class MagnetSearchListener {

    @Listener
    @Filter(value = "/磁力 ", matchType = MatchType.TEXT_STARTS_WITH)
    @SneakyThrows
    public void magnetSearch(@NotNull final GroupMessageEvent event) {
        //获取用户输入的内容
        final String next = event.getMessageContent().getPlainText()
                .substring(4);
        if (!next.startsWith("magnet:?xt=urn:btih:")) {
            SendMsgUtil.sendReplyGroupMsg(event, "磁力链接格式不正确");
            return;
        }
        final var messageReceipt = SendMsgUtil.sendReplyGroupMsg(event, "正在检索中，请稍候");
        //搜索磁力链接
        final MagnetSearchData searchData = MagnetSearcher.search(next);
        if (searchData == null) {
            SendMsgUtil.sendReplyGroupMsg(event, "未找到相关资源");
            return;
        }
        //撤回消息
        SendMsgUtil.withdrawMessage(messageReceipt, 15);
        final var builder = new MessagesBuilder();
        final List<Screenshots> screenshots = searchData.getScreenshots();
        final var stringBuilder = new StringBuilder()
                .append("标题 : ").append(searchData.getName()).append("\n")
                .append("大小 : ").append(DataSizeUtil.format(searchData.getSize())).append("\n")
                .append("文件数 : ").append(searchData.getCount()).append("\n")
                .append("预览图 : ").append("\n");

        builder.text(stringBuilder.toString());
        Optional.ofNullable(screenshots)
                .orElse(Collections.emptyList())
                .parallelStream()
                .map(Screenshots::getScreenshot)
                .filter(StrUtil::isNotBlank)
                .map(AsyncHttpClientUtil::downloadImage)
                .forEach(inputStream -> {
                    try {
                        builder.image(Resource.of(inputStream));
                    } catch (final IOException e) {
                        builder.text("下载图片失败");
                        log.error("下载图片失败", e);
                    }
                });
        //发送消息
        event.getSource().sendBlocking(builder.build());
    }
}
