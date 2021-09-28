package de.dps.quarkusconsumer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class InputPaymentMsg {

    @JsonProperty("batchId")
    private Integer batchId;

    @JsonProperty("content")
    private String content;

    @JsonProperty("xpath")
    private String xpath;

    @JsonProperty("sentTimestamp")
    private Timestamp sentTimestamp;

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public Timestamp getSentTimestamp() {
        return sentTimestamp;
    }

    public void setSentTimestamp(Timestamp sentTimestamp) {
        this.sentTimestamp = sentTimestamp;
    }
}
