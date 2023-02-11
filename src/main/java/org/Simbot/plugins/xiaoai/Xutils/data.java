package org.Simbot.plugins.xiaoai.Xutils;

/**
 * @author mirai
 * @version 1.0
 * @packAge: org.Simbot.Plugins.xiaoAi.Xutils
 * @date 2022/12/6 15:11
 */
public class data {

    private String requestId;
    private int requestType;
    private int resultCode;
    private String requestText;
    private String domain;
    private String responseText;
    private DirectiveDTO directive;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getRequestText() {
        return requestText;
    }

    public void setRequestText(String requestText) {
        this.requestText = requestText;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public DirectiveDTO getDirective() {
        return directive;
    }

    public void setDirective(DirectiveDTO directive) {
        this.directive = directive;
    }

    public static class DirectiveDTO {
        private String displayText;
        private String url;
        private int index;
        private int duration;
        private int offset;

        public String getDisplayText() {
            return displayText;
        }

        public void setDisplayText(String displayText) {
            this.displayText = displayText;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }
    }
}
