package de.dps.quarkusconsumer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RegisterForReflection
public class InputPaymentMsg {

    private Integer batchId;
    private String content;
    private String xpath;
    private LocalDateTime sentTimestamp;

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

    public LocalDateTime getSentTimestamp() {
        return sentTimestamp;
    }

    public void setSentTimestamp(LocalDateTime sentTimestamp) {
        this.sentTimestamp = sentTimestamp;
    }
}
