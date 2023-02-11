package org.Simbot.plugins.youthlearning.Data;

/**
 * @author mirai
 * @version 1.0
 * @packAge: org.Simbot.Plugins.youthBigLearning.Data
 * @date 2022/12/6 20:19
 */
public class data {

    private int status;
    private String message;
    private DataDTO data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public static class DataDTO {
        private String img;
        private String url;
        private String title;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
