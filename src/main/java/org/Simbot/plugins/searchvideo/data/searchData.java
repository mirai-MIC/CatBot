package org.Simbot.plugins.searchvideo.data;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @BelongsProject: soruly
 * @BelongsPackage: org.example.searchvideo.data
 * @Author: MIC
 * @CreateTime: 2023-02-10  10:43
 * @Description:
 * @Version: 1.0
 */

@NoArgsConstructor
@Data
public class searchData {

    @SerializedName("frameCount")
    private Integer frameCount;
    @SerializedName("error")
    private String error;
    @SerializedName("result")
    private List<ResultDTO> result;

    @NoArgsConstructor
    @Data
    public static class ResultDTO {
        @SerializedName("anilist")
        private AnilistDTO anilist;
        @SerializedName("filename")
        private String filename;
        @SerializedName("episode")
        private Integer episode;
        @SerializedName("from")
        private Double from;
        @SerializedName("to")
        private Double to;
        @SerializedName("similarity")
        private Double similarity;
        @SerializedName("video")
        private String video;
        @SerializedName("image")
        private String image;

        @NoArgsConstructor
        @Data
        public static class AnilistDTO {
            @SerializedName("id")
            private Integer id;
            @SerializedName("idMal")
            private Integer idMal;
            @SerializedName("title")
            private TitleDTO title;
            @SerializedName("synonyms")
            private List<String> synonyms;
            @SerializedName("isAdult")
            private Boolean isAdult;

            @NoArgsConstructor
            @Data
            public static class TitleDTO {
                @SerializedName("native")
                private String nativeX;
                @SerializedName("romaji")
                private String romaji;
                @SerializedName("english")
                private String english;
            }
        }
    }
}
