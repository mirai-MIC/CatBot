package org.Simbot.plugins.gameSearch;

import cn.hutool.core.collection.CollUtil;
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
import org.Simbot.plugins.gameSearch.entity.BlackBoxSearchEntity;
import org.Simbot.plugins.gameSearch.entity.GameDlc;
import org.Simbot.plugins.gameSearch.entity.GameInfo;
import org.Simbot.plugins.gameSearch.entity.GamePrice;
import org.Simbot.utils.AsyncHttpClientUtil;
import org.Simbot.utils.SendMsgUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

/**
 * @author ：ycvk
 * @description ：游戏搜索监听器
 * @date ：2023/08/11 15:57
 */
@Slf4j
@Component
public class GameSearchListener {

    @Listener
    @Filter(value = "/game ", matchType = MatchType.TEXT_STARTS_WITH)
    @SneakyThrows
    public void getAVMessage(@NotNull final GroupMessageEvent event) {
        //获取用户输入的内容
        final String next = event.getMessageContent().getPlainText().substring(6);
        final var messageReceipt = SendMsgUtil.sendReplyGroupMsg(event, "正在检索中，请稍候");
        final BlackBoxSearchEntity searchEntity = SteamSearchScraper.searchByName(next);
        SendMsgUtil.withdrawMessage(messageReceipt, 15);
        if (searchEntity == null) {
            SendMsgUtil.sendReplyGroupMsg(event, "未找到相关游戏");
            return;
        }
        final ByteArrayInputStream stream = AsyncHttpClientUtil.downloadImage(searchEntity.getImage());

        final int appId = searchEntity.getSteamAppid();
        final GameInfo gameInfo = SteamSearchScraper.searchByGameId(appId);
        final String introduction = SteamSearchScraper.searchIntroductionById(appId);
        //构建转发消息链
        final var chain = new MiraiForwardMessageBuilder(ForwardMessage.DisplayStrategy.Default);
        final MessagesBuilder builder = new MessagesBuilder();
        final GamePrice price = gameInfo.getPrice();
        final List<GameDlc> dlcs = gameInfo.getDlcs();
        final int discount = price.getDiscount();
        builder.image(Resource.of(stream)).text("\n")
                .text("游戏名称：").text(gameInfo.getName()).text("\n")
                .text("steam链接：").text("https://store.steampowered.com/app/" + appId).text("\n")
                .text("游戏类型：").text(gameInfo.getGenres().stream().reduce((a, b) -> a + " " + b).orElse("")).text("\n")
                .text("游戏评分：").text(searchEntity.getScore()).text("\n")
                .text(gameInfo.getPositiveDesc()).text("\n")//好评率
        ;
        if (gameInfo.getSupportChinese() == 1) {
            builder.text("是否支持中文：").text("是").text("\n");
        }
        Optional.ofNullable(gameInfo.getNameEn()).ifPresent(nameEn -> builder.text("英文名称：").text(nameEn).text("\n"));

        final MessagesBuilder priceBuilder = buildPriceBuilder(price);

        chain.add(event.getBot(), builder.build());
        chain.add(event.getBot(), priceBuilder.build());

        if (CollUtil.isNotEmpty(dlcs)) {
            chain.add(event.getBot(), "DLC列表：\n");
            final List<MessagesBuilder> dlcList = buildDlcList(dlcs);
            dlcList.parallelStream().forEach(dlc -> chain.add(event.getBot(), dlc.build()));
        }
        chain.add(event.getBot(), "游戏简介：\n" + introduction);

        event.getSource().sendAsync(chain.build());
    }

    public MessagesBuilder buildPriceBuilder(final GamePrice price) {
        final MessagesBuilder priceBuilder = new MessagesBuilder();
        priceBuilder.text("steam国区目前价格：").text(price.getCurrent()).text("\n");
        if (price.getDiscount() != 0) {
            priceBuilder.text("steam目前折扣：   ").text("-" + price.getDiscount() + "%").text("\n")
                    .text("是否为史低折扣：   ").text(price.getIsLowest() == 0 ? "否" : "是").text("\n")
                    .text("折扣剩余时间：    ").text(price.getDeadlineDate()).text("\n")
            ;
        }
        final var lowestPrice = Optional.ofNullable(price.getLowestPriceRaw()).orElse(String.valueOf(price.getLowestPrice()));
        priceBuilder.text("steam历史最低价：").text(lowestPrice).text("\n")
                .text("steam历史最低折扣：").text("-" + price.getLowestDiscount() + "%").text("\n");
        return priceBuilder;
    }

    public List<MessagesBuilder> buildDlcList(final List<GameDlc> dlcs) {
        return dlcs.parallelStream().map(this::buildDlcBuilder).toList();
    }

    @SneakyThrows
    private MessagesBuilder buildDlcBuilder(final GameDlc dlc) {
        final MessagesBuilder builder = new MessagesBuilder();
        final ByteArrayInputStream stream = AsyncHttpClientUtil.downloadImage(dlc.getImage());
        final GamePrice price = dlc.getPrice();
        final MessagesBuilder priceBuilder = buildPriceBuilder(price);
        builder.image(Resource.of(stream)).text("\n")
                .text("DLC名称：").text(dlc.getName()).text("\n")
        ;
        builder.append(priceBuilder.build());
        return builder;
    }

}
