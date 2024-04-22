package io.github.douglasliebl.msorders.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${mq.configuration.exchange}")
    private String exchangeInstance;

    public static String EXCHANGE;

    @Value("${mq.configuration.queue}")
    private String queueInstance;

    public static String QUEUE;

    @Value("${mq.configuration.routing-key}")
    private String routingKeyInstance;

    public static String ROUTING_KEY;

    @PostConstruct
    public void init() {
        EXCHANGE = exchangeInstance;
        QUEUE = queueInstance;
        ROUTING_KEY = routingKeyInstance;
    }

    @Bean
    public Queue queue() {
        return new Queue(QUEUE, false, false, false);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE, false, false);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(queue())
                .to(directExchange())
                .with(ROUTING_KEY);
    }
}
