package com.example.starter.aws.sqs.aws;

import com.example.starter.aws.sqs.aws.model.AwsMessage;
import com.example.starter.aws.sqs.client.AwsSqsTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.model.*;
import software.amazon.awssdk.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Proxy of the SqsClient class
 *
 * @author Yunfeng Zhao
 */
public class AwsSqsClientProxy implements InitializingBean, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(AwsSqsTemplate.class);

    /**
     * Client of the original sqsClient
     */
    private SqsClient sqsClient;

    /**
     * Region of the sqs service
     */
    private final String region;

    public AwsSqsClientProxy(String region) {
        this.region = region;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        sqsClient = SqsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .build();
        LOGGER.info("Aws sqs client started region: " + region);
    }

    /**
     * GetQueueUrl
     * @param queueName Queue name
     * @return String
     */
    public String getQueueUrl(String queueName) {
        return sqsClient.getQueueUrl(GetQueueUrlRequest.builder()
                .queueName(queueName)
                .build())
                .queueUrl();
    }

    /**
     * Send message
     * @param queueUrl Queue url
     * @param messageBody Message need to send
     */
    public void sendMessage(String queueUrl, String messageBody) {
        sqsClient.sendMessage(SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .build());
    }

    /**
     * Send batch messages
     * @param queueUrl Queue url
     * @param batchMessageList BatchMessage list
     */
    public void sendBatchMessages(String queueUrl, List<AwsMessage> batchMessageList) {
        List<SendMessageBatchRequestEntry> entryList = new ArrayList<>();
        int i = 1;
        for (AwsMessage awsMessage: batchMessageList) {
            entryList.add(SendMessageBatchRequestEntry.builder()
                    .id(awsMessage.getMessageId())
                    .messageBody(awsMessage.getBody())
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
     * @param queueUrl Queue url
     * @param maxNumberOfMessages Max number when receive messages
     * @param waitTimeSeconds wait seconds when receive messages
     * @return AwsMessage list
     */
    public List<AwsMessage> receiveMessages(String queueUrl, int maxNumberOfMessages, int waitTimeSeconds) {
        List<Message> messageList = sqsClient.receiveMessage(ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(maxNumberOfMessages)
                .waitTimeSeconds(waitTimeSeconds)
                .build())
                .messages();
        List<AwsMessage> awsMessageList = new ArrayList<>();
        if (CollectionUtils.isNullOrEmpty(messageList)) {
            return awsMessageList;
        }
        for (Message message: messageList) {
            awsMessageList.add(new AwsMessage(message.messageId(),
                    message.body(),
                    message.receiptHandle()));
        }
        return awsMessageList;
    }

    /**
     * Delete message
     * @param awsMessage Message to delete
     */
    public void deleteMessage(String queueUrl, AwsMessage awsMessage) {
        sqsClient.deleteMessage(DeleteMessageRequest.builder()
                .queueUrl(queueUrl)
                .receiptHandle(awsMessage.getReceiptHandle())
                .build());
    }

    @Override
    public void destroy() throws Exception {
        sqsClient.close();
        LOGGER.info("Aws sqs client destroyed region: " + region);
    }
}
