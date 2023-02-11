package org.Simbot.plugins.load.LoadData;

/**
 * @author mirai
 * @version 1.0
 * @packAge: org.Simbot.Plugins.Load.LoadData
 * @date 2022/12/7 13:45
 */
public class data {

    private int code;
    private String text;
    private DataDTO data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
        private int format;
        private String draw;
        private String annotate;
        private String explain;
        private String details;
        private String source;
        private String image;

        public int getFormat() {
            return format;
        }

        public void setFormat(int format) {
            this.format = format;
        }

        public String getDraw() {
            return draw;
        }

        public void setDraw(String draw) {
            this.draw = draw;
        }

        public String getAnnotate() {
            return annotate;
        }

        public void setAnnotate(String annotate) {
            this.annotate = annotate;
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
