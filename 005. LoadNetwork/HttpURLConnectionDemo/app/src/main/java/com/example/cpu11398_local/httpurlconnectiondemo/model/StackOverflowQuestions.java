package com.example.cpu11398_local.httpurlconnectiondemo.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StackOverflowQuestions {

    @SerializedName("items")
    @Expose
    private List<Question> Questions = null;
    @SerializedName("has_more")
    @Expose
    private Boolean hasMore;
    @SerializedName("quota_max")
    @Expose
    private Integer quotaMax;
    @SerializedName("quota_remaining")
    @Expose
    private Integer quotaRemaining;

    public List<Question> getQuestions() {
        return Questions;
    }

    public void setQuestions(List<Question> Questions) {
        this.Questions = Questions;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    public Integer getQuotaMax() {
        return quotaMax;
    }

    public void setQuotaMax(Integer quotaMax) {
        this.quotaMax = quotaMax;
    }

    public Integer getQuotaRemaining() {
        return quotaRemaining;
    }

    public void setQuotaRemaining(Integer quotaRemaining) {
        this.quotaRemaining = quotaRemaining;
    }
}