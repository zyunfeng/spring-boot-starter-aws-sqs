package com.example.starter.aws.sqs.configuration;

import com.example.starter.aws.sqs.aws.AwsSqsClientProxy;
import com.example.starter.aws.sqs.client.AwsSqsClientRegistrar;
import com.example.starter.aws.sqs.properties.AwsSqsProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;

/**
 * Entrance autoConfiguration of the starter
 *
 * @author Yunfeng Zhao
 */
@Configuration
@ConditionalOnProperty(
        prefix = "aws.sqs",
        name = "enabled"
)
@Import({AwsSqsClientRegistrar.class, AwsSqsProperties.class})
public class AwsSqsAutoConfiguration {

    @Bean
    public AwsSqsProperties awsSqsProperties() {
        return new AwsSqsProperties();
    }
}
