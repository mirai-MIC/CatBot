package org.Simbot.utils.HttpClientUtils;

import java.util.Map;

/**
 * @BelongsProject: TD
 * @BelongsPackage: org.td.utils
 * @Author: mi
 * @CreateTime: 2023/8/2 13:01
 * @Description:
 * @Version: 1.0
 */


public class GetQueryParams {
    public static String appendQueryParams(String url, Map<String, String> params) {
        if (params != null && !params.isEmpty()) {
            StringBuilder queryString = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (!queryString.isEmpty()) {
                    queryString.append("&");
                }
                queryString.append(entry.getKey()).append("=").append(entry.getValue());
            }
            url += "?" + queryString;
        }
        return url;
    }
}
