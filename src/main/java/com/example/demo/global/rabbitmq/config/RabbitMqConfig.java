package com.example.demo.global.rabbitmq.config;

import com.example.demo.global.rabbitmq.properties.RabbitMqProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class RabbitMqConfig {

    private final RabbitMqProperties rabbitMqProperties;

    @Value("${rabbitmq.request.queue.name}")
    private String requestQueueName;

    @Value("${rabbitmq.result.queue.name}")
    private String resultQueueName;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    // 라우팅 키 분리 (application.yml 에서 정의)
    @Value("${rabbitmq.request.routing.key}")
    private String requestRoutingKey;
    @Value("${rabbitmq.result.routing.key}")
    private String resultRoutingKey;

    // org.springframework.amqp.core.Queue
    @Bean
    public Queue requestQueue() {
        return new Queue(requestQueueName);
    }

    @Bean
    public Queue resultQueue() {
        return new Queue(resultQueueName);
    }

    /**
     * 지정된 Exchange 이름으로 Direct Exchange Bean 을 생성
     */
    @Bean
    public DirectExchange submissionExchange() {
        return new DirectExchange(exchangeName);
    }

    /**
     * requestQueue 와 Exchange 을 Binding 하고 requestRoutingKey Key 을 이용하여 Binding Bean 생성
     * Exchange 에 Queue 을 등록한다고 이해하자
     **/
    @Bean
    public Binding requestBinding(Queue requestQueue, DirectExchange submissionExchange) {
        return BindingBuilder.bind(requestQueue).to(submissionExchange).with(requestRoutingKey);
    }

    /**
     * resultQueue 와 Exchange 을 Binding 하고 resultRoutingKey Key 을 이용하여 Binding Bean 생성
     * Exchange 에 Queue 을 등록한다고 이해하자
     **/
    @Bean
    public Binding resultBinding(Queue resultQueue, DirectExchange submissionExchange) {
        return BindingBuilder.bind(resultQueue).to(submissionExchange).with(resultRoutingKey);
    }

    /**
     * RabbitMQ 연동을 위한 ConnectionFactory 빈을 생성하여 반환
     **/
    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(rabbitMqProperties.getHost());
        connectionFactory.setPort(rabbitMqProperties.getPort());
        connectionFactory.setUsername(rabbitMqProperties.getUsername());
        connectionFactory.setPassword(rabbitMqProperties.getPassword());
        return connectionFactory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf,
            MessageConverter converter) {
        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cf);
        factory.setMessageConverter(converter);
        factory.setPrefetchCount(200);
        factory.setConcurrentConsumers(50);
        factory.setMaxConcurrentConsumers(200);
        return factory;
    }

    /**
     * RabbitTemplate
     * ConnectionFactory 로 연결 후 실제 작업을 위한 Template
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    /**
     * 직렬화(메세지를 JSON 으로 변환하는 Message Converter)
     */
    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}