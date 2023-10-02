package com.example.starter.aws.sqs.client;

import com.example.starter.aws.sqs.properties.AwsSqsProperties;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import software.amazon.awssdk.regions.Region;


/**
 * Register the AwsSqsTemplate instance by different queue name
 *
 * @author Yunfeng Zhao
 */
public class AwsSqsClientRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * Dynamic register bean, use alias as the bean name
     *
     * @param importingClassMetadata importingClassMetadata
     * @param registry registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AwsSqsProperties awsSqsProperties = Binder.get(environment)
                .bind("aws.sqs", AwsSqsProperties.class)
                .orElse(null);
        for (AwsSqsProperties.AwsSqsPropertiesItem item: awsSqsProperties.getQueues()) {
            AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(AwsSqsTemplate.class,
                    () -> new AwsSqsTemplate(
                            item.getName(),
                            Region.of(item.getRegion()),
                            item.getMaxNumberOfMessages(),
                            item.getWaitTimeSeconds())
            ).getBeanDefinition();
            registry.registerBeanDefinition(item.getAlias() + "AwsSqsTemplate", beanDefinition);
        }
    }
}
