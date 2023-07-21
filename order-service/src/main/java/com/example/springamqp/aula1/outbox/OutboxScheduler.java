package com.example.springamqp.aula1.outbox;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OutboxScheduler {

    private final OutboxService outboxService;

    public OutboxScheduler(OutboxService outboxService) {
        this.outboxService = outboxService;
    }

    @Scheduled(fixedRate = 30000L) //30 segundos
    public void sendPendingScheduler() {
        outboxService.sendTopPending();
    }

    @Scheduled(fixedRate = 10000L) //1 minuto
    public void deleteSentMessages() {
        outboxService.deleteSentMessages();
    }

}
