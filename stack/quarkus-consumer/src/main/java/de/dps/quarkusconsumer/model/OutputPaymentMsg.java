package de.dps.quarkusconsumer.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.vertx.core.json.JsonObject;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static de.dps.quarkusconsumer.utils.MyTimeUtils.now;

/**
 * Message pushed to the persistence queue
 */
@RegisterForReflection
public class OutputPaymentMsg {

    private String containerId;
    private int batchId;
    private LogicalServiceName serviceName;
    private String extractedElement;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime sentTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime receivedTimestamp;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime processedTimestamp; // 2021-09-28T15:40:14.978+00:00

    private String content;

    public OutputPaymentMsg(InputPaymentMsg inputMsg) {
        this.serviceName = LogicalServiceName.QUARKUS;
        this.batchId = inputMsg.getBatchId();
        this.sentTimestamp = inputMsg.getSentTimestamp();
        this.receivedTimestamp = LocalDateTime.now();
        this.content = inputMsg.getContent();
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public LogicalServiceName getServiceName() {
        return serviceName;
    }

    public void setServiceName(LogicalServiceName serviceName) {
        this.serviceName = serviceName;
    }

    public String getExtractedElement() {
        return extractedElement;
    }

    public void setExtractedElement(String extractedElement) {
        this.extractedElement = extractedElement;
    }

    public LocalDateTime getSentTimestamp() {
        return sentTimestamp;
    }

    public void setSentTimestamp(LocalDateTime sentTimestamp) {
        this.sentTimestamp = sentTimestamp;
    }

    public LocalDateTime getReceivedTimestamp() {
        return receivedTimestamp;
    }

    public void setReceivedTimestamp(LocalDateTime receivedTimestamp) {
        this.receivedTimestamp = receivedTimestamp;
    }

    public LocalDateTime getProcessedTimestamp() {
        return processedTimestamp;
    }

    public void setProcessedTimestamp(LocalDateTime processedTimestamp) {
        this.processedTimestamp = processedTimestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "OutputPaymentMsg{" +
                "containerId='" + containerId + '\'' +
                ", batchId=" + batchId +
                ", serviceName=" + serviceName +
                ", extractedElement='" + extractedElement + '\'' +
                ", sentTimestamp=" + sentTimestamp +
                ", receivedTimestamp=" + receivedTimestamp +
                ", processedTimestamp=" + processedTimestamp +
                ", content='" + content + '\'' +
                '}';
    }
}
