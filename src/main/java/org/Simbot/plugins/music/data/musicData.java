package org.Simbot.plugins.music.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author mirai
 * @version 1.0
 * @className musicData
 * @data 2023/01/20 20:49
 * @description
 */
public class musicData {

    @SerializedName("code")
    private Integer code;
    @SerializedName("text")
    private String text;
    @SerializedName("data")
    private DataDTO data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public static class DataDTO {
        @SerializedName("Id")
        private Integer Id;
        @SerializedName("Music")
        private String Music;
        @SerializedName("Cover")
        private String Cover;
        @SerializedName("Singer")
        private String Singer;
        @SerializedName("Url")
        private String Url;
        @SerializedName("Music_Url")
        private String MusicUrl;
        @SerializedName("Singer_Array")
        private List<String> SingerArray;

        public Integer getId() {
            return Id;
        }

        public void setId(Integer Id) {
            this.Id = Id;
        }

        public String getMusic() {
            return Music;
        }

        public void setMusic(String Music) {
            this.Music = Music;
        }

        public String getCover() {
            return Cover;
        }

        public void setCover(String Cover) {
            this.Cover = Cover;
        }

        public String getSinger() {
            return Singer;
        }

        public void setSinger(String Singer) {
            this.Singer = Singer;
        }

        public String getUrl() {
            return Url;
        }

        public void setUrl(String Url) {
            this.Url = Url;
        }

        public String getMusicUrl() {
            return MusicUrl;
        }

        public void setMusicUrl(String MusicUrl) {
            this.MusicUrl = MusicUrl;
        }

        public List<String> getSingerArray() {
            return SingerArray;
        }

        public void setSingerArray(List<String> SingerArray) {
            this.SingerArray = SingerArray;
        }
    }
}
