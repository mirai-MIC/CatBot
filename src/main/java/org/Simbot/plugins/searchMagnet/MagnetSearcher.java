package org.Simbot.plugins.searchMagnet;

import cn.hutool.core.lang.Pair;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.Simbot.utils.AsyncHttpClientUtil;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;

/**
 * @author ：ycvk
 * @description ：搜索磁力链接预览图
 * @date ：2023/08/07 23:17
 */
@Slf4j
public class MagnetSearcher {
    private static final String searchUrl = "https://whatslink.info/api/v1/link?url=";

    /**
     * 搜索磁力链接预览图
     *
     * @param keyword 关键字
     * @return {@link MagnetSearchData}
     */
    public static MagnetSearchData search(final String keyword) {
        final Pair<Request, Response> responsePair = AsyncHttpClientUtil.doGet(searchUrl + keyword);
        final String body = responsePair.getValue().getResponseBody();
        //把JSONObject转换为对象
        final MagnetSearchData bean = JSONUtil.toBean(body, MagnetSearchData.class, true);
        log.info("磁力预览搜索结果: {}", bean);
        return bean;
    }
}
