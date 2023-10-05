# AWS SQS integration with Spring Boot

[![License](https://img.shields.io/:license-apache-brightgreen.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

## Features
* Automatically configures and runs the AWS SQS with `@AwsTemplate`
* Supports configuring multiple queues, each queue will auto created the bean named `@AliasAwsTemplate`

## Usage
To add a dependency using Maven, use the following:
````xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>spring-boot-starter-aws-sqs</artifactId>
    <version>0.0.1</version>
</dependency>
````
Configure the .yml file as follows:
````yaml
aws:
  sqs:
    enabled: true # Feature enabled
    region: us-east-1 # Region of the sqs service
    queues: # Multiple queues as list
      - name: MyQueueTest # QueueName
        alias: myQueueTest # QueueAlias used to create different beans
        max-number-of-messages: 5 # Max number when receive Messages
        wait-time-seconds: 20 # Wait time seconds when long polling messages
````


## Example-Projects

Read more about our example projects [here](https://github.com/zyunfeng/aws-sqs-starter-example).

