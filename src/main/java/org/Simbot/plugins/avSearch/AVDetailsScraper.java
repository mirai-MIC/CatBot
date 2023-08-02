package org.Simbot.plugins.avSearch;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.Simbot.plugins.avSearch.entity.AvDetail;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class AVDetailsScraper {

    private static final String BASE_URL = "https://www.javbus.com/";

    private static final Pattern MESSAGE_PATTERN = Pattern.compile("var gid = (.*?);\\s*var uc = (.*?);", Pattern.DOTALL);

    private static final Pattern IMAGE_PATTERN = Pattern.compile("img = '(.*?)';", Pattern.DOTALL);

    public AvDetail getAVDetail(final String avNumber) throws IOException {
        if (StrUtil.isBlank(avNumber)) {
            return null;
        }
        final String url = BASE_URL + avNumber;
        final Document doc = Jsoup.connect(url).get();
        if (doc.select("h3").isEmpty()) {
            return null;
        }

        final var title = getTitle(doc);
        final var actors = getActors(doc);
        final var releaseDate = getReleaseDate(doc);
        final var coverImage = getCoverImage(doc);
        final var categories = getCategories(doc);
        final var previewImages = getPreviewImages(doc);
        final var magnetLink = getMagnetLink(avNumber, doc);
        return new AvDetail(null,
                avNumber,
                title,
                actors,
                coverImage,
                JSONUtil.toJsonStr(magnetLink),
                null,
                null,
                null,
                title,
                null,
                JSONUtil.toJsonStr(categories),
                DateUtil.parseDate(releaseDate).toTimestamp(),
                previewImages);
    }

    private String getTitle(final Document doc) {
        return Objects.requireNonNull(doc.select("h3").first()).text();
    }

    private String getActors(final Document doc) {
        return doc.select(".star-name a").stream()
                .map(Element::text)
                .collect(Collectors.joining(", "));
    }

    private String getReleaseDate(final Document doc) {
        return doc.select(".info p").get(1).text().split(":")[1].trim();
    }

    private List<String> getCategories(final Document doc) {
        return doc.select(".genre a")
                .stream()
                .map(Element::text)
                .toList();
    }

    private String getCoverImage(final Document doc) {
        final Matcher m = IMAGE_PATTERN.matcher(doc.html());
        return m.find() ? BASE_URL + m.group(1) : "";
    }

    private List<String> getPreviewImages(final Document doc) {
        final List<String> images = doc.select("#sample-waterfall a.sample-box").eachAttr("href");
        final var strings = new ArrayList<String>();
        for (final String image : images) {
            if (!image.startsWith("http")) {// 有些图片不是外链的, 缺少前缀
                strings.add(BASE_URL + image);
            } else {
                strings.add(image);
            }
        }
        Collections.shuffle(strings);
        return strings;
    }

    private List<String> getMagnetLink(final String avNumber, final Document doc) throws IOException {
        final Matcher m = MESSAGE_PATTERN.matcher(doc.html());
        if (m.find()) {
            final String gid = m.group(1);
            final String uc = m.group(2);
            final String detailUrl = BASE_URL + "ajax/uncledatoolsbyajax.php?gid=" + gid + "&lang=zh&uc=" + uc;
            final Document detailDoc = Jsoup.connect(detailUrl)
                    .header("Referer", BASE_URL + avNumber)
                    .get();

            final Elements links = detailDoc.select("a[href]");

            List<String> resultLinks = links.stream()
                    .map(link -> link.attr("href"))
                    .distinct()
                    .filter(this::checkString)
                    .toList();

            if (resultLinks.isEmpty()) {
                resultLinks = links.stream()
                        .map(link -> link.attr("href"))
                        .distinct()
                        .toList();
            }
            return resultLinks;
        }
        return Collections.emptyList();
    }

    private boolean checkString(final String s) {
        final int dnIndex = s.indexOf("&dn=");
        if (dnIndex == -1) {
            return false;
        }
        final String substring = s.substring(dnIndex);
        return substring.indexOf('c') != -1 || substring.indexOf('C') != -1;
    }
}
