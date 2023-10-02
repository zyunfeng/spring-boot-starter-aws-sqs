package com.example.starter.aws.sqs.client;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases
 *
 * @author Yunfeng Zhao
 */
@ExtendWith(MockitoExtension.class)
class AwsSqsTemplateTest {

    private static AwsSqsTemplate awsSqsTemplate;

    @BeforeAll
    static void setup() {
        awsSqsTemplate = new AwsSqsTemplate(
                "MyQueueTest",
                Region.US_EAST_1,
                2,
                0);
        awsSqsTemplate.afterPropertiesSet();
    }

    @Test
    @DisplayName("When the send message is empty or null")
    void sendMessageEmptyOrNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            awsSqsTemplate.sendMessage("");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            awsSqsTemplate.sendMessage(null);
        });
    }

    @Test
    @DisplayName("When send message successfully")
    void sendMessageSuccessful() {
        assertDoesNotThrow(() -> {
            awsSqsTemplate.sendMessage("test");
        });
    }

    @Test
    @DisplayName("When the send batch messages are empty or null")
    void sendBatchMessagesEmptyOrNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            awsSqsTemplate.sendBatchMessages(new ArrayList<>());
        });
        assertThrows(IllegalArgumentException.class, () -> {
            awsSqsTemplate.sendBatchMessages(null);
        });
    }

    @Test
    @DisplayName("When send batch message successfully")
    void sendBatchMessageSuccessful() {
        assertDoesNotThrow(() -> {
            awsSqsTemplate.sendBatchMessages(Arrays.asList("test-message", "test-message-2"));
        });
    }

    @Test
    @DisplayName("When receive  messages successfully")
    void receiveMessagesSuccessfully() {
        sendBatchMessageSuccessful();
        assertDoesNotThrow(() -> {
            List<Message> messageList = awsSqsTemplate.receiveMessages();
            assertEquals(2, messageList.size());
        });
    }

    @Test
    @DisplayName("When the delete message is  null")
    void deleteMessageNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            awsSqsTemplate.deleteMessage(null);
        });
    }

    @Test
    @DisplayName("When delete message successfully")
    void deleteMessageSuccessfully() {
        sendBatchMessageSuccessful();
        assertDoesNotThrow(() -> {
            List<Message> messageList = awsSqsTemplate.receiveMessages();
            awsSqsTemplate.deleteMessage(messageList.get(0));
        });
    }

    @Test
    @DisplayName("When the delete messages are empty or null")
    void deleteBatchMessagesEmptyOrNull() {
        sendBatchMessageSuccessful();
        assertThrows(IllegalArgumentException.class, () -> {
            awsSqsTemplate.deleteBatchMessages(null);
        });
    }

    @Test
    @DisplayName("When delete batch messages successfully")
    void deleteBatchMessagesSuccessfully() {
        sendBatchMessageSuccessful();
        assertDoesNotThrow(() -> {
            List<Message> messageList = awsSqsTemplate.receiveMessages();
            awsSqsTemplate.deleteBatchMessages(messageList);
        });
    }

    @AfterAll
    static void destroy() {
        awsSqsTemplate.destroy();
    }
}