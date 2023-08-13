package org.Simbot.plugins.gameSearch;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.message.Messages;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.resources.Resource;
import org.Simbot.plugins.gameSearch.entity.BlackBoxSearchEntity;
import org.Simbot.plugins.gameSearch.entity.GameInfo;
import org.Simbot.plugins.gameSearch.entity.GameOnlineData;
import org.Simbot.utils.AsyncHttpClientUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * @author ：ycvk
 * @description ：搜索steam游戏信息
 * @date ：2023/08/10 22:35
 */
@Slf4j
public class SteamSearchScraper {

    private static final String STEAM_SEARCH_URL = "https://store.steampowered.com/search/?term=";
    private static final String BLACK_BOX_NAME_URL = "https://api.xiaoheihe.cn/bbs/app/api/general/search/v1?search_type=game&q=";
    private static final String BLACK_BOX_ID_URL = "https://api.xiaoheihe.cn/game/get_game_detail/?h_src=game_rec_a&appid=";
    private static final String BLACK_BOX_INTRODUCTION_URL = "https://api.xiaoheihe.cn/game/game_introduction?steam_appid=";

    /**
     * 根据游戏名搜索游戏
     *
     * @param name 游戏名
     * @return 游戏信息
     */
    public static BlackBoxSearchEntity searchByName(final String name) {
        final var pair = AsyncHttpClientUtil.doGet(BLACK_BOX_NAME_URL + name);
        final String body = pair.getValue().getResponseBody();
        final JSONObject entries = JSONUtil.parseObj(body);
        final JSONArray array = entries.getJSONObject("result").getJSONArray("items");
        if (array.isEmpty()) {
            return null;
        }
        try {
            return array.getJSONObject(0).get("info", BlackBoxSearchEntity.class);
        } catch (final Exception e) {
            log.error("转换JSON为实体类失败", e);
            return null;
        }
    }

    /**
     * 根据游戏名搜索pc游戏
     *
     * @param gameId 游戏id
     * @return 游戏信息
     */
    public static GameInfo searchByGameId(final int gameId) {
        final var responsePair = AsyncHttpClientUtil.doGet(BLACK_BOX_ID_URL + gameId);
        final String body = responsePair.getValue().getResponseBody();
        final JSONObject entries = JSONUtil.parseObj(body);
        final JSONObject result = entries.getJSONObject("result");
        final List<GameOnlineData> list = result.getJSONObject("user_num").getBeanList("game_data", GameOnlineData.class);
        final GameInfo gameInfo = JSONUtil.toBean(result, GameInfo.class);
        gameInfo.setGameOnlineData(list);
        return gameInfo;
    }

    /**
     * 根据游戏id搜索游戏简介
     *
     * @param gameId 游戏id
     * @return 游戏简介
     */
    @SneakyThrows
    public static String searchIntroductionById(final int gameId) {
        final Document document = Jsoup.connect(BLACK_BOX_INTRODUCTION_URL + gameId).get();
        //获取  <div class="content">中的内容
        final Element first = document.select(".content").first();
        return first != null ? processElement(first) : null;
    }

    private static String processElement(final Element element) {
        final StringBuilder result = new StringBuilder();
        element.childNodes().parallelStream().forEachOrdered(node -> {
            if (node instanceof TextNode) {
                result.append(((TextNode) node).text());
            } else if (node instanceof final Element childElement) {
                if ("h2".equals(childElement.tagName()) || "br".equals(childElement.tagName())) {
                    result.append("\n"); // 在标签前添加换行符
                }
                result.append(processElement(childElement)); // 递归处理子元素
            }
        });
        return result.toString();
    }

    /**
     * 根据游戏id搜索主机游戏
     *
     * @param entity 游戏信息
     * @return 消息
     */
    @SneakyThrows
    public static Messages searchConsoleGame(final BlackBoxSearchEntity entity) {
        final MessagesBuilder builder = new MessagesBuilder();
        final ByteArrayInputStream stream = AsyncHttpClientUtil.downloadImage(entity.getImage(), false);
        builder.image(Resource.of(stream))
                .text("游戏名：" + entity.getName() + "\n")
                .text("英文名：" + entity.getNameEn() + "\n")
                .text("游戏平台：" + entity.getPlatforms().get(0) + "\n")
                .text("游戏分数：" + entity.getScore() + "\n")
        ;
        return builder.build();

    }
}
