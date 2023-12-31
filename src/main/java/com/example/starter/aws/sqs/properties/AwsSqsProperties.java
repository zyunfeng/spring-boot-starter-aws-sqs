package com.example.starter.aws.sqs.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;

import java.util.List;
import java.util.Map;

/**
 * Aws Sqs config properties
 *
 * @author Yunfeng Zhao
 */

@ConfigurationProperties(prefix = "aws.sqs")
@Data
public class AwsSqsProperties {

    private Boolean enabled;

    private String region;

    /**
     * Key of the map is queueName,
     * value AwsSqsPropertiesItem is the concrete config of each queue
     */
    private List<AwsSqsPropertiesItem> queues;

    @Data
    public static class AwsSqsPropertiesItem {

        private String name;

        private String alias;
        private int maxNumberOfMessages;
        private int waitTimeSeconds;
    }
}
