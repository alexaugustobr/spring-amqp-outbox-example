package com.example.springamqp.aula1.core;

public interface MessageSender {
    void send(String destination, Object contentObject);
    void send(String destination, String rawContent);
}
