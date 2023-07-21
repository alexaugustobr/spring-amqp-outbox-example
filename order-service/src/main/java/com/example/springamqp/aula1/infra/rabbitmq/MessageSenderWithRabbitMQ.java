package com.example.springamqp.aula1.infra.rabbitmq;

import com.example.springamqp.aula1.core.JsonConverter;
import com.example.springamqp.aula1.core.MessageSender;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageSenderWithRabbitMQ implements MessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private JsonConverter jsonConverter;

    @Override
    public void send(String destination, Object content) {
        var json = jsonConverter.toJson(content);
        rabbitTemplate.send(destination, "", MessageBuilder.withBody(json.getBytes()).build());
    }

    @Override
    public void send(String destination, String rawContent) {
        rabbitTemplate.send(destination, "", MessageBuilder.withBody(rawContent.getBytes()).build());
    }
}
