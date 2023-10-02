package com.example.starter.aws.sqs.client;

import software.amazon.awssdk.services.sqs.model.Message;

import java.util.List;

/**
 * Aws sqs operations interface
 *
 * @author Yunfeng Zhao
 */
public interface AwsSqsOperations {

    void sendMessage(String messageBody);

    void sendBatchMessages(List<String> messageBodyList);

    List<Message> receiveMessages();

    void deleteMessage(Message message);

    void deleteBatchMessages(List<Message> messageList);
}
