package com.example.starter.aws.sqs.client;

import com.example.starter.aws.sqs.configuration.AwsSqsAutoConfiguration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases
 *
 * @author Yunfeng Zhao
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {AwsSqsAutoConfiguration.class})
class AwsSqsTemplateTest {

    @Autowired
    private AwsSqsTemplate myQueueTestAwsSqsTemplate;

    @Test
    @DisplayName("When the aliasAwsSqsTemplate is null")
    void awsSqsTemplateInjectSuccessful() {
        assertNotNull(myQueueTestAwsSqsTemplate);
    }

    @Test
    @DisplayName("When the send message is empty or null")
    void sendMessageEmptyOrNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            myQueueTestAwsSqsTemplate.sendMessage("");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            myQueueTestAwsSqsTemplate.sendMessage(null);
        });
    }


    @Test
    @DisplayName("When the send batch messages are empty or null")
    void sendBatchMessagesEmptyOrNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            myQueueTestAwsSqsTemplate.sendBatchMessages(new ArrayList<>());
        });
        assertThrows(IllegalArgumentException.class, () -> {
            myQueueTestAwsSqsTemplate.sendBatchMessages(null);
        });
    }

    @Test
    @DisplayName("When the delete message is null")
    void deleteMessageNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            myQueueTestAwsSqsTemplate.deleteMessage(null);
        });
    }

    @Test
    @DisplayName("When the delete messages are empty or null")
    void deleteBatchMessagesEmptyOrNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            myQueueTestAwsSqsTemplate.deleteBatchMessages(new ArrayList<>());
        });
        assertThrows(IllegalArgumentException.class, () -> {
            myQueueTestAwsSqsTemplate.deleteBatchMessages(null);
        });
    }
}