package com.example.starter.aws.sqs.client;

import com.example.starter.aws.sqs.aws.AwsSqsClientProxy;
import com.example.starter.aws.sqs.aws.model.AwsMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.utils.CollectionUtils;
import software.amazon.awssdk.utils.StringUtils;

import java.util.List;

/**
 * Implement of AwsSqsOperations, each sqs hold different instance by alias
 *
 * @author Yunfeng Zhao
 */
public class AwsSqsTemplate implements AwsSqsOperations {

    private static final Logger LOGGER = LoggerFactory.getLogger(AwsSqsTemplate.class);

    /**
     * Queue name of the sqs
     */
    private final String queueName;

    /**
     * Max number when receive Messages
     */
    private final int maxNumberOfMessages;

    /**
     * Wait time seconds when long polling messages
     */
    private final int waitTimeSeconds;

    /**
     * Queue url of the sqs
     */
    private final String queueUrl;

    /**
     * Proxy of the awsSqsClient
     */
    private final AwsSqsClientProxy awsSqsClientProxy;

    public AwsSqsTemplate(String queueName,
                          int maxNumberOfMessages,
                          int waitTimeSeconds,
                          AwsSqsClientProxy awsSqsClientProxy) {
        this.queueName = queueName;
        this.maxNumberOfMessages = maxNumberOfMessages;
        this.waitTimeSeconds = waitTimeSeconds;
        this.awsSqsClientProxy = awsSqsClientProxy;
        this.queueUrl = awsSqsClientProxy.getQueueUrl(queueName);
    }


    /**
     * Send message
     * @param messageBody Message body
     */
    @Override
    public void sendMessage(String messageBody) {
        if (StringUtils.isEmpty(messageBody)) {
            throw new IllegalArgumentException("messageBody must not be empty");
        }
        awsSqsClientProxy.sendMessage(queueUrl, messageBody);
        LOGGER.info("Aws sqs client send message successfully queueName: " + queueName);
    }

    /**
     * Send batch messages
     * @param awsMessageList Message list
     */
    @Override
    public void sendBatchMessages(List<AwsMessage> awsMessageList) {
        if (CollectionUtils.isNullOrEmpty(awsMessageList)) {
            throw new IllegalArgumentException("awsMessageList must not be null or empty");
        }
        awsSqsClientProxy.sendBatchMessages(queueUrl, awsMessageList);
        LOGGER.info("Aws sqs client send batch messages successfully queueName: " + queueName);
    }

    /**
     * Receive messages
     * @return List of the message
     */
    @Override
    public List<AwsMessage> receiveMessages() {
        List<AwsMessage> messageList = awsSqsClientProxy.receiveMessages(queueUrl, maxNumberOfMessages, waitTimeSeconds);
        LOGGER.info("Aws sqs client receive messages successfully queueName: " + queueName);
        return messageList;
    }

    /**
     * Delete message
     * @param awsMessage The message to be deleted
     */
    @Override
    public void deleteMessage(AwsMessage awsMessage) {
        if (awsMessage == null) {
            throw new IllegalArgumentException("awsMessage must not be null");
        }
        awsSqsClientProxy.deleteMessage(queueUrl, awsMessage);
        LOGGER.info("Aws sqs client delete message successfully queueName: " + queueName);
    }

    /**
     * Delete batch messages
     * @param awsMessageList Message list to be deleted
     */
    @Override
    public void deleteBatchMessages(List<AwsMessage> awsMessageList) {
        if (CollectionUtils.isNullOrEmpty(awsMessageList)) {
            throw new IllegalArgumentException("awsMessageList must not be null or empty");
        }
        for (AwsMessage message: awsMessageList) {
            deleteMessage(message);
        }
        LOGGER.info("Aws sqs client delete batch messages successfully queueName: " + queueName);
    }
}
