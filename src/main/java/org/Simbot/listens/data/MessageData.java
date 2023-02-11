package org.Simbot.listens.data;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @BelongsProject: simbot
 * @BelongsPackage: org.Simbot.listens.data
 * @Author: MIC
 * @CreateTime: 2023-02-08  20:01
 * @Description:
 * @Version: 1.0
 */

@NoArgsConstructor
@Data
public class MessageData {

    @SerializedName("app")
    private String app;
    @SerializedName("config")
    private ConfigDTO config;
    @SerializedName("desc")
    private String desc;
    @SerializedName("extra")
    private ExtraDTO extra;
    @SerializedName("meta")
    private MetaDTO meta;
    @SerializedName("needShareCallBack")
    private Boolean needShareCallBack;
    @SerializedName("prompt")
    private String prompt;
    @SerializedName("ver")
    private String ver;
    @SerializedName("view")
    private String view;

    @NoArgsConstructor
    @Data
    public static class ConfigDTO {
        @SerializedName("autoSize")
        private Integer autoSize;
        @SerializedName("ctime")
        private Integer ctime;
        @SerializedName("forward")
        private Integer forward;
        @SerializedName("height")
        private Integer height;
        @SerializedName("token")
        private String token;
        @SerializedName("type")
        private String type;
        @SerializedName("width")
        private Integer width;
    }

    @NoArgsConstructor
    @Data
    public static class ExtraDTO {
        @SerializedName("app_type")
        private Integer appType;
        @SerializedName("appid")
        private Integer appid;
        @SerializedName("uin")
        private Long uin;
    }

    @NoArgsConstructor
    @Data
    public static class MetaDTO {
        @SerializedName("detail_1")
        private Detail1DTO detail1;

        @NoArgsConstructor
        @Data
        public static class Detail1DTO {
            @SerializedName("appType")
            private Integer appType;
            @SerializedName("appid")
            private String appid;
            @SerializedName("desc")
            private String desc;
            @SerializedName("gamePoints")
            private String gamePoints;
            @SerializedName("gamePointsUrl")
            private String gamePointsUrl;
            @SerializedName("host")
            private HostDTO host;
            @SerializedName("icon")
            private String icon;
            @SerializedName("preview")
            private String preview;
            @SerializedName("qqdocurl")
            private String qqdocurl;
            @SerializedName("scene")
            private Integer scene;
            @SerializedName("shareTemplateData")
            private ShareTemplateDataDTO shareTemplateData;
            @SerializedName("shareTemplateId")
            private String shareTemplateId;
            @SerializedName("showLittleTail")
            private String showLittleTail;
            @SerializedName("title")
            private String title;
            @SerializedName("url")
            private String url;

            @NoArgsConstructor
            @Data
            public static class HostDTO {
            }

            @NoArgsConstructor
            @Data
            public static class ShareTemplateDataDTO {
            }
        }
    }
}
