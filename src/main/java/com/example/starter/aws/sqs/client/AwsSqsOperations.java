package com.example.starter.aws.sqs.client;

import com.example.starter.aws.sqs.aws.model.AwsMessage;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.List;

/**
 * Aws sqs operations interface
 *
 * @author Yunfeng Zhao
 */
public interface AwsSqsOperations {

    void sendMessage(String messageBody);

    void sendBatchMessages(List<AwsMessage> batchMessageList);

    List<AwsMessage> receiveMessages();

    void deleteMessage(AwsMessage message);

    void deleteBatchMessages(List<AwsMessage> messageList);
}
