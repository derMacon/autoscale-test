package de.dps.quarkusconsumer.model;

import io.vertx.core.json.JsonObject;

import java.sql.Timestamp;

import static de.dps.quarkusconsumer.utils.MyTimeUtils.now;

/**
 * Message pushed to the persistence queue
 */
public class OutputPaymentMsg {

    private String containerId;
    private int batchId;
    private LogicalServiceName serviceName;
    private String extractedElement;
    private Timestamp sentTimestamp;
    private Timestamp receivedTimestamp;
    private Timestamp processedTimestamp;
    private String content;

    public OutputPaymentMsg(InputPaymentMsg inputMsg) {
        this.serviceName = LogicalServiceName.QUARKUS;
        this.batchId = inputMsg.getBatchId();
        this.sentTimestamp = inputMsg.getSentTimestamp();
        this.receivedTimestamp = now();
        this.content = inputMsg.getContent();
    }

    public String getContainerId() {
        return containerId;
    }

    public int getBatchId() {
        return batchId;
    }

    public LogicalServiceName getServiceName() {
        return serviceName;
    }

    public String getExtractedElement() {
        return extractedElement;
    }

    public Timestamp getSentTimestamp() {
        return sentTimestamp;
    }

    public Timestamp getReceivedTimestamp() {
        return receivedTimestamp;
    }

    public Timestamp getProcessedTimestamp() {
        return processedTimestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public void setServiceName(LogicalServiceName serviceName) {
        this.serviceName = serviceName;
    }

    public void setExtractedElement(String extractedElement) {
        this.extractedElement = extractedElement;
    }

    public void setSentTimestamp(Timestamp sentTimestamp) {
        this.sentTimestamp = sentTimestamp;
    }

    public void setReceivedTimestamp(Timestamp receivedTimestamp) {
        this.receivedTimestamp = receivedTimestamp;
    }

    public void setProcessedTimestamp(Timestamp processedTimestamp) {
        this.processedTimestamp = processedTimestamp;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
