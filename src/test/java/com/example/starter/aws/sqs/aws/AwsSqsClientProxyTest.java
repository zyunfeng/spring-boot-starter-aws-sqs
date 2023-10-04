package com.example.starter.aws.sqs.aws;

import com.example.starter.aws.sqs.aws.model.AwsMessage;
import com.example.starter.aws.sqs.configuration.AwsSqsAutoConfiguration;
import com.example.starter.aws.sqs.properties.AwsSqsProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {AwsSqsAutoConfiguration.class})
@EnableConfigurationProperties
class AwsSqsClientProxyTest {

    @Autowired
    private AwsSqsClientProxy awsSqsClientProxy;

    @Autowired
    private AwsSqsProperties awsSqsProperties;

    @Test
    @DisplayName("When the awsSqsClientProxy is null")
    void awsSqsClientProxyInjectSuccessful() {
        assertNotNull(awsSqsClientProxy);
    }

    @Test
    @DisplayName("When the awsSqsProperties is null")
    void awsSqsPropertiesInjectSuccessful() {
        assertNotNull(awsSqsProperties);
    }

    @Test
    @DisplayName("When get queue url successful")
    void getQueueUrlSuccessful() {
        // https://sqs.us-east-1.amazonaws.com/321357466685/MyQueueTest
        String queueUrl = getQueueUrl();
        assertNotNull(queueUrl);
        assertTrue(queueUrl.matches("^https?://.*$"));
    }

    @Test
    @DisplayName("When send, receive and delete message successful")
    void sendReceiveAndDeleteMessage() {
        String messageBody = "test";
        awsSqsClientProxy.sendMessage(getQueueUrl(), messageBody);
        List<AwsMessage> awsMessageList = awsSqsClientProxy.receiveMessages(getQueueUrl(),
                getMaxNumberOfMessages(),
                getMaxNumberOfMessages());
        // must not be null
        assertNotNull(awsMessageList);
        // size must be 1
        assertEquals(1, awsMessageList.size());
        // body content equals
        assertEquals(messageBody, awsMessageList.get(0).getBody());
        // delete
        awsSqsClientProxy.deleteMessage(getQueueUrl(),  awsMessageList.get(0));
        List<AwsMessage> awsMessageListDeleted = awsSqsClientProxy.receiveMessages(getQueueUrl(),
                getMaxNumberOfMessages(),
                getMaxNumberOfMessages());
        assertEquals(0, awsMessageListDeleted.size());
    }

    @Test
    @DisplayName("When batch send, receive and delete message successful")
    void sendBatchReceiveAndDeleteMessage() {
        List<AwsMessage> sendMessageList = Arrays.asList(
                new AwsMessage("1", "test1", "test-receipt-1"),
                new AwsMessage("2", "test2", "test-receipt-2")
        );
        awsSqsClientProxy.sendBatchMessages(getQueueUrl(), sendMessageList);
        List<AwsMessage> awsMessageList = awsSqsClientProxy.receiveMessages(getQueueUrl(),
                getMaxNumberOfMessages(),
                getMaxNumberOfMessages());
        // must not be null
        assertNotNull(awsMessageList);
        // size must be 1
        assertEquals(sendMessageList.size(), awsMessageList.size());
        // delete
        for (AwsMessage awsMessage: awsMessageList) {
            awsSqsClientProxy.deleteMessage(getQueueUrl(), awsMessage);
        }
        List<AwsMessage> awsMessageListDeleted = awsSqsClientProxy.receiveMessages(getQueueUrl(),
                getMaxNumberOfMessages(),
                getMaxNumberOfMessages());
        assertEquals(0, awsMessageListDeleted.size());
    }

    /**
     * 获取queueUrl
     * @return
     */
    private String getQueueUrl() {
        return awsSqsClientProxy.getQueueUrl(awsSqsProperties.getQueues().get(0).getName());
    }

    /**
     * 获取queueUrl
     * @return
     */
    private int getMaxNumberOfMessages() {
        return awsSqsProperties.getQueues().get(0).getMaxNumberOfMessages();
    }

    /**
     * 获取queueUrl
     * @return
     */
    private int getWaitTimeSeconds() {
        return awsSqsProperties.getQueues().get(0).getWaitTimeSeconds();
    }
}