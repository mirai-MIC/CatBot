package org.Simbot.plugins.video.data;

import com.google.gson.annotations.SerializedName;

/**
 * @author mirai
 * @version 1.0
 * @className Vdata
 * @data 2023/01/20 21:53
 * @description
 */
public class VData {

    @SerializedName("code")
    private Integer code;
    @SerializedName("pic")
    private String pic;
    @SerializedName("title")
    private String title;
    @SerializedName("actors")
    private Object actors;
    @SerializedName("type")
    private String type;
    @SerializedName("msg")
    private String msg;
    @SerializedName("play_title")
    private String playTitle;
    @SerializedName("url")
    private String url;
    @SerializedName("tips")
    private String tips;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getActors() {
        return actors;
    }

    public void setActors(Object actors) {
        this.actors = actors;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPlayTitle() {
        return playTitle;
    }

    public void setPlayTitle(String playTitle) {
        this.playTitle = playTitle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}
