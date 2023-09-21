package org.Simbot.utils;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.SneakyThrows;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ：ycvk
 * @description ：xml工具类
 * @date ：2023/09/21 12:28
 */
public class XmlUtil {

    /**
     * 提取xml中的图片链接
     *
     * @param input 形如<img src="xxx.jpg">的字符串
     * @return 图片链接列表
     */
    public static List<String> extractImgUrls(final String input) {
        if (StrUtil.isBlank(input)) {
            return List.of();
        }
        final List<String> imgUrls = new ArrayList<>();
        final Pattern pattern = Pattern.compile("<img src=\"(.*?)\".*?>");
        final Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            final String s = matcher.group(1);
            if (s.endsWith("jpg") || s.endsWith("png") || s.endsWith("gif")) {
                imgUrls.add(s);
            }
        }
        return imgUrls;
    }

    /**
     * 从url获取xml数据
     *
     * @param url url
     * @return SyndFeed xml数据
     */
    @SneakyThrows
    public static SyndFeed getXmlFeedFromUrl(final String url) {
        final Pair<Request, Response> pair = AsyncHttpClientUtil.doGet(url);
        final InputStream stream = pair.getValue().getResponseBodyAsStream();
        return new SyndFeedInput().build(new XmlReader(stream));
    }
}
