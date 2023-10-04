package com.example.starter.aws.sqs.aws.model;

import software.amazon.awssdk.services.sqs.model.Message;

import java.util.Map;

/**
 * Encapsulation of the Message class
 *
 * @author Yunfeng Zhao
 */
public class AwsMessage  {

    /**
     * Id of the message
     */
    private String messageId;

    /**
     * Body of the message
     */
    private String body;

    /**
     * ReceiptHandle of the message
     */
    private String receiptHandle;

    public AwsMessage(String messageId, String body, String receiptHandle) {
        this.messageId = messageId;
        this.body = body;
        this.receiptHandle = receiptHandle;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getReceiptHandle() {
        return receiptHandle;
    }

    public void setReceiptHandle(String receiptHandle) {
        this.receiptHandle = receiptHandle;
    }
}
