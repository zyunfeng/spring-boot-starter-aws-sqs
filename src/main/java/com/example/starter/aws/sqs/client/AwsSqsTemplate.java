package com.example.starter.aws.sqs.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;
import software.amazon.awssdk.utils.CollectionUtils;
import software.amazon.awssdk.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Implement of AwsSqsOperations, each sqs hold different instance by alias
 *
 * @author Yunfeng Zhao
 */
public class AwsSqsTemplate implements AwsSqsOperations, InitializingBean, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(AwsSqsTemplate.class);

    /**
     * Queue name of the sqs
     */
    private final String queueName;

    /**
     * Queue region of the sqs
     */
    private final Region region;

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
    private String queueUrl;

    /**
     * Client of the sqs
     */
    private SqsClient sqsClient;

    public AwsSqsTemplate(String queueName, Region region, int maxNumberOfMessages,
                          int waitTimeSeconds) {
        this.queueName = queueName;
        this.region = region;
        this.maxNumberOfMessages = maxNumberOfMessages;
        this.waitTimeSeconds = waitTimeSeconds;
    }

    /**
     * Init the client, and set queue url
     */
    @Override
    public void afterPropertiesSet() {
        setSqsClient();
        setQueueUrl();
        LOGGER.info("Aws sqs started queueName: " + queueName);
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
        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .build();
        sqsClient.sendMessage(sendMessageRequest);
    }

    /**
     * Send batch messages
     * @param messageBodyList Message body list
     */
    @Override
    public void sendBatchMessages(List<String> messageBodyList) {
        if (CollectionUtils.isNullOrEmpty(messageBodyList)) {
            throw new IllegalArgumentException("messageBodyList must not be null or empty");
        }
        List<SendMessageBatchRequestEntry> entryList = new ArrayList<>();
        int i = 1;
        for (String messageBody: messageBodyList) {
            entryList.add(SendMessageBatchRequestEntry.builder()
                            .id(String.valueOf(i))
                            .messageBody(messageBody)
                            .build());
            i++;
        }
        SendMessageBatchRequest sendMessageBatchRequest = SendMessageBatchRequest.builder()
                .queueUrl(queueUrl)
                .entries(entryList)
                .build();
        sqsClient.sendMessageBatch(sendMessageBatchRequest);
    }

    /**
     * Receive messages
     * @return List of the message
     */
    @Override
    public List<Message> receiveMessages() {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(maxNumberOfMessages)
                .waitTimeSeconds(waitTimeSeconds)
                .build();
        return sqsClient.receiveMessage(receiveMessageRequest).messages();
    }

    /**
     * Delete message
     * @param message The message to be deleted
     */
    @Override
    public void deleteMessage(Message message) {
        if (null == message) {
            throw new IllegalArgumentException("message must not be null");
        }
        DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(message.receiptHandle())
                .build();
        sqsClient.deleteMessage(deleteMessageRequest);
    }

    /**
     * Delete batch messages
     * @param messageList Message list to be deleted
     */
    @Override
    public void deleteBatchMessages(List<Message> messageList) {
        if (CollectionUtils.isNullOrEmpty(messageList)) {
            throw new IllegalArgumentException("messageList must not be null or empty");
        }
        for (Message message: messageList) {
            deleteMessage(message);
        }
    }

    /**
     * Set the sqs client
     */
    private void setSqsClient() {
        sqsClient = SqsClient.builder()
                .region(region)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
    }

    /**
     * Set the queue url
     */
    private void setQueueUrl() {
        GetQueueUrlRequest getQueueUrlRequest = GetQueueUrlRequest.builder()
                .queueName(queueName)
                .build();
        queueUrl = sqsClient.getQueueUrl(getQueueUrlRequest).queueUrl();
    }

    /**
     * Close the client connect, when spring boot destroy
     */
    @Override
    public void destroy() {
        sqsClient.close();
        LOGGER.info("Aws sqs destroyed queueName: " + queueName);
    }
}
