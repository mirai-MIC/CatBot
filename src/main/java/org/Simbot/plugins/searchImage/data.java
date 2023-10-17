package org.Simbot.plugins.searchImage;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class data {

    @SerializedName("header")
    private HeaderDTO header;
    @SerializedName("results")
    private List<ResultsDTO> results;

    public void setHeader(HeaderDTO header) {
        this.header = header;
    }

    public void setResults(List<ResultsDTO> results) {
        this.results = results;
    }

    public static class HeaderDTO {
        @SerializedName("user_id")
        private String userId;
        @SerializedName("account_type")
        private String accountType;
        @SerializedName("short_limit")
        private String shortLimit;
        @SerializedName("long_limit")
        private String longLimit;
        @SerializedName("long_remaining")
        private Integer longRemaining;
        @SerializedName("short_remaining")
        private Integer shortRemaining;
        @SerializedName("status")
        private Integer status;
        @SerializedName("results_requested")
        private Integer resultsRequested;
        @SerializedName("index")
        private IndexDTO index;
        @SerializedName("search_depth")
        private String searchDepth;
        @SerializedName("minimum_similarity")
        private Double minimumSimilarity;
        @SerializedName("query_image_display")
        private String queryImageDisplay;
        @SerializedName("query_image")
        private String queryImage;
        @SerializedName("results_returned")
        private Integer resultsReturned;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getShortLimit() {
            return shortLimit;
        }

        public void setShortLimit(String shortLimit) {
            this.shortLimit = shortLimit;
        }

        public String getLongLimit() {
            return longLimit;
        }

        public void setLongLimit(String longLimit) {
            this.longLimit = longLimit;
        }

        public Integer getLongRemaining() {
            return longRemaining;
        }

        public void setLongRemaining(Integer longRemaining) {
            this.longRemaining = longRemaining;
        }

        public Integer getShortRemaining() {
            return shortRemaining;
        }

        public void setShortRemaining(Integer shortRemaining) {
            this.shortRemaining = shortRemaining;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public Integer getResultsRequested() {
            return resultsRequested;
        }

        public void setResultsRequested(Integer resultsRequested) {
            this.resultsRequested = resultsRequested;
        }

        public IndexDTO getIndex() {
            return index;
        }

        public void setIndex(IndexDTO index) {
            this.index = index;
        }

        public String getSearchDepth() {
            return searchDepth;
        }

        public void setSearchDepth(String searchDepth) {
            this.searchDepth = searchDepth;
        }

        public Double getMinimumSimilarity() {
            return minimumSimilarity;
        }

        public void setMinimumSimilarity(Double minimumSimilarity) {
            this.minimumSimilarity = minimumSimilarity;
        }

        public String getQueryImageDisplay() {
            return queryImageDisplay;
        }

        public void setQueryImageDisplay(String queryImageDisplay) {
            this.queryImageDisplay = queryImageDisplay;
        }

        public String getQueryImage() {
            return queryImage;
        }

        public void setQueryImage(String queryImage) {
            this.queryImage = queryImage;
        }

        public Integer getResultsReturned() {
            return resultsReturned;
        }

        public void setResultsReturned(Integer resultsReturned) {
            this.resultsReturned = resultsReturned;
        }

        public static class IndexDTO {
            @SerializedName("0")
            private _$0DTO $0;
            @SerializedName("2")
            private _$0DTO $2;
            @SerializedName("5")
            private _$0DTO $5;
            @SerializedName("6")
            private _$0DTO $6;
            @SerializedName("8")
            private _$0DTO $8;
            @SerializedName("9")
            private _$0DTO $9;
            @SerializedName("10")
            private _$0DTO $10;
            @SerializedName("11")
            private _$0DTO $11;
            @SerializedName("12")
            private _$0DTO $12;
            @SerializedName("16")
            private _$0DTO $16;
            @SerializedName("18")
            private _$0DTO $18;
            @SerializedName("19")
            private _$0DTO $19;
            @SerializedName("20")
            private _$0DTO $20;
            @SerializedName("21")
            private _$0DTO $21;
            @SerializedName("22")
            private _$0DTO $22;
            @SerializedName("23")
            private _$0DTO $23;
            @SerializedName("24")
            private _$0DTO $24;
            @SerializedName("25")
            private _$0DTO $25;
            @SerializedName("26")
            private _$0DTO $26;
            @SerializedName("27")
            private _$0DTO $27;
            @SerializedName("28")
            private _$0DTO $28;
            @SerializedName("29")
            private _$0DTO $29;
            @SerializedName("30")
            private _$0DTO $30;
            @SerializedName("31")
            private _$0DTO $31;
            @SerializedName("32")
            private _$0DTO $32;
            @SerializedName("33")
            private _$0DTO $33;
            @SerializedName("34")
            private _$0DTO $34;
            @SerializedName("35")
            private _$0DTO $35;
            @SerializedName("36")
            private _$0DTO $36;
            @SerializedName("37")
            private _$0DTO $37;
            @SerializedName("38")
            private _$0DTO $38;
            @SerializedName("39")
            private _$0DTO $39;
            @SerializedName("40")
            private _$0DTO $40;
            @SerializedName("41")
            private _$0DTO $41;
            @SerializedName("42")
            private _$0DTO $42;
            @SerializedName("43")
            private _$0DTO $43;
            @SerializedName("44")
            private _$0DTO $44;
            @SerializedName("51")
            private _$0DTO $51;
            @SerializedName("52")
            private _$0DTO $52;
            @SerializedName("53")
            private _$0DTO $53;
            @SerializedName("211")
            private _$0DTO $211;
            @SerializedName("341")
            private _$0DTO $341;
            @SerializedName("371")
            private _$0DTO $371;

            public _$0DTO get$0() {
                return $0;
            }

            public void set$0(_$0DTO $0) {
                this.$0 = $0;
            }

            public _$0DTO get$2() {
                return $2;
            }

            public void set$2(_$0DTO $2) {
                this.$2 = $2;
            }

            public _$0DTO get$5() {
                return $5;
            }

            public void set$5(_$0DTO $5) {
                this.$5 = $5;
            }

            public _$0DTO get$6() {
                return $6;
            }

            public void set$6(_$0DTO $6) {
                this.$6 = $6;
            }

            public _$0DTO get$8() {
                return $8;
            }

            public void set$8(_$0DTO $8) {
                this.$8 = $8;
            }

            public _$0DTO get$9() {
                return $9;
            }

            public void set$9(_$0DTO $9) {
                this.$9 = $9;
            }

            public _$0DTO get$10() {
                return $10;
            }

            public void set$10(_$0DTO $10) {
                this.$10 = $10;
            }

            public _$0DTO get$11() {
                return $11;
            }

            public void set$11(_$0DTO $11) {
                this.$11 = $11;
            }

            public _$0DTO get$12() {
                return $12;
            }

            public void set$12(_$0DTO $12) {
                this.$12 = $12;
            }

            public _$0DTO get$16() {
                return $16;
            }

            public void set$16(_$0DTO $16) {
                this.$16 = $16;
            }

            public _$0DTO get$18() {
                return $18;
            }

            public void set$18(_$0DTO $18) {
                this.$18 = $18;
            }

            public _$0DTO get$19() {
                return $19;
            }

            public void set$19(_$0DTO $19) {
                this.$19 = $19;
            }

            public _$0DTO get$20() {
                return $20;
            }

            public void set$20(_$0DTO $20) {
                this.$20 = $20;
            }

            public _$0DTO get$21() {
                return $21;
            }

            public void set$21(_$0DTO $21) {
                this.$21 = $21;
            }

            public _$0DTO get$22() {
                return $22;
            }

            public void set$22(_$0DTO $22) {
                this.$22 = $22;
            }

            public _$0DTO get$23() {
                return $23;
            }

            public void set$23(_$0DTO $23) {
                this.$23 = $23;
            }

            public _$0DTO get$24() {
                return $24;
            }

            public void set$24(_$0DTO $24) {
                this.$24 = $24;
            }

            public _$0DTO get$25() {
                return $25;
            }

            public void set$25(_$0DTO $25) {
                this.$25 = $25;
            }

            public _$0DTO get$26() {
                return $26;
            }

            public void set$26(_$0DTO $26) {
                this.$26 = $26;
            }

            public _$0DTO get$27() {
                return $27;
            }

            public void set$27(_$0DTO $27) {
                this.$27 = $27;
            }

            public _$0DTO get$28() {
                return $28;
            }

            public void set$28(_$0DTO $28) {
                this.$28 = $28;
            }

            public _$0DTO get$29() {
                return $29;
            }

            public void set$29(_$0DTO $29) {
                this.$29 = $29;
            }

            public _$0DTO get$30() {
                return $30;
            }

            public void set$30(_$0DTO $30) {
                this.$30 = $30;
            }

            public _$0DTO get$31() {
                return $31;
            }

            public void set$31(_$0DTO $31) {
                this.$31 = $31;
            }

            public _$0DTO get$32() {
                return $32;
            }

            public void set$32(_$0DTO $32) {
                this.$32 = $32;
            }

            public _$0DTO get$33() {
                return $33;
            }

            public void set$33(_$0DTO $33) {
                this.$33 = $33;
            }

            public _$0DTO get$34() {
                return $34;
            }

            public void set$34(_$0DTO $34) {
                this.$34 = $34;
            }

            public _$0DTO get$35() {
                return $35;
            }

            public void set$35(_$0DTO $35) {
                this.$35 = $35;
            }

            public _$0DTO get$36() {
                return $36;
            }

            public void set$36(_$0DTO $36) {
                this.$36 = $36;
            }

            public _$0DTO get$37() {
                return $37;
            }

            public void set$37(_$0DTO $37) {
                this.$37 = $37;
            }

            public _$0DTO get$38() {
                return $38;
            }

            public void set$38(_$0DTO $38) {
                this.$38 = $38;
            }

            public _$0DTO get$39() {
                return $39;
            }

            public void set$39(_$0DTO $39) {
                this.$39 = $39;
            }

            public _$0DTO get$40() {
                return $40;
            }

            public void set$40(_$0DTO $40) {
                this.$40 = $40;
            }

            public _$0DTO get$41() {
                return $41;
            }

            public void set$41(_$0DTO $41) {
                this.$41 = $41;
            }

            public _$0DTO get$42() {
                return $42;
            }

            public void set$42(_$0DTO $42) {
                this.$42 = $42;
            }

            public _$0DTO get$43() {
                return $43;
            }

            public void set$43(_$0DTO $43) {
                this.$43 = $43;
            }

            public _$0DTO get$44() {
                return $44;
            }

            public void set$44(_$0DTO $44) {
                this.$44 = $44;
            }

            public _$0DTO get$51() {
                return $51;
            }

            public void set$51(_$0DTO $51) {
                this.$51 = $51;
            }

            public _$0DTO get$52() {
                return $52;
            }

            public void set$52(_$0DTO $52) {
                this.$52 = $52;
            }

            public _$0DTO get$53() {
                return $53;
            }

            public void set$53(_$0DTO $53) {
                this.$53 = $53;
            }

            public _$0DTO get$211() {
                return $211;
            }

            public void set$211(_$0DTO $211) {
                this.$211 = $211;
            }

            public _$0DTO get$341() {
                return $341;
            }

            public void set$341(_$0DTO $341) {
                this.$341 = $341;
            }

            public _$0DTO get$371() {
                return $371;
            }

            public void set$371(_$0DTO $371) {
                this.$371 = $371;
            }

            public static class _$0DTO {
                @SerializedName("status")
                private Integer status;
                @SerializedName("parent_id")
                private Integer parentId;
                @SerializedName("id")
                private Integer id;
                @SerializedName("results")
                private Integer results;

                public Integer getStatus() {
                    return status;
                }

                public void setStatus(Integer status) {
                    this.status = status;
                }

                public Integer getParentId() {
                    return parentId;
                }

                public void setParentId(Integer parentId) {
                    this.parentId = parentId;
                }

                public Integer getId() {
                    return id;
                }

                public void setId(Integer id) {
                    this.id = id;
                }

                public Integer getResults() {
                    return results;
                }

                public void setResults(Integer results) {
                    this.results = results;
                }
            }
        }
    }

    public static class ResultsDTO {
        @SerializedName("header")
        private HeaderDTOX header;
        @SerializedName("data")
        private DataDTO data;

        public HeaderDTOX getHeader() {
            return header;
        }

        public void setHeader(HeaderDTOX header) {
            this.header = header;
        }

        public DataDTO getData() {
            return data;
        }

        public void setData(DataDTO data) {
            this.data = data;
        }

        public static class HeaderDTOX {
            @SerializedName("similarity")
            private String similarity;
            @SerializedName("thumbnail")
            private String thumbnail;
            @SerializedName("index_id")
            private Integer indexId;
            @SerializedName("index_name")
            private String indexName;
            @SerializedName("dupes")
            private Integer dupes;
            @SerializedName("hidden")
            private Integer hidden;

            public String getSimilarity() {
                return similarity;
            }

            public void setSimilarity(String similarity) {
                this.similarity = similarity;
            }

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
            }

            public Integer getIndexId() {
                return indexId;
            }

            public void setIndexId(Integer indexId) {
                this.indexId = indexId;
            }

            public String getIndexName() {
                return indexName;
            }

            public void setIndexName(String indexName) {
                this.indexName = indexName;
            }

            public Integer getDupes() {
                return dupes;
            }

            public void setDupes(Integer dupes) {
                this.dupes = dupes;
            }

            public Integer getHidden() {
                return hidden;
            }

            public void setHidden(Integer hidden) {
                this.hidden = hidden;
            }
        }

        public static class DataDTO {
            @SerializedName("title")
            private String title;
            @SerializedName("pixiv_id")
            private Integer pixivId;
            @SerializedName("member_name")
            private String memberName;
            @SerializedName("member_id")
            private Integer memberId;
            @SerializedName("ext_urls")
            private List<String> extUrls;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public Integer getPixivId() {
                return pixivId;
            }

            public void setPixivId(Integer pixivId) {
                this.pixivId = pixivId;
            }

            public String getMemberName() {
                return memberName;
            }

            public void setMemberName(String memberName) {
                this.memberName = memberName;
            }

            public Integer getMemberId() {
                return memberId;
            }

            public void setMemberId(Integer memberId) {
                this.memberId = memberId;
            }

            public List<String> getExtUrls() {
                return extUrls;
            }

            public void setExtUrls(List<String> extUrls) {
                this.extUrls = extUrls;
            }
        }
    }
}