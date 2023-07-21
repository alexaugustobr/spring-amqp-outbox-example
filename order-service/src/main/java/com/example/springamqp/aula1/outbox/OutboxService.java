package com.example.springamqp.aula1.outbox;

import com.example.springamqp.aula1.core.JsonConverter;
import com.example.springamqp.aula1.core.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class OutboxService {

    private final Logger log = LoggerFactory.getLogger(OutboxScheduler.class);

    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private JsonConverter jsonConverter;

    @Autowired
    private MessageSender messageSender;

    @Transactional
    public Outbox store(String destination, Object content) {
        var json = jsonConverter.toJson(content);
        var outbox = Outbox.brandNew(destination, json);
        return outboxRepository.save(outbox);
    }

    @Transactional
    public void deleteSentMessages() {
        outboxRepository.deleteByStatus(Outbox.Status.SENT);
    }

    @Transactional
    public void sendTopPending() {
        log.debug("Sending pending messages");//TODO debug
        var pendingMessages = outboxRepository.findFirst10ByStatusOrderByCreatedAtAsc(Outbox.Status.PENDING);
        for (Outbox pendingMessage : pendingMessages) {
            pendingMessage.increaseTentatives();
            try {
                messageSender.send(pendingMessage.getDestination(), pendingMessage.getContent());
            } catch (Exception e) {
                log.info("Message send error");
                if (pendingMessage.getTentatives() >= 20) {
                    pendingMessage.setStatus(Outbox.Status.ERROR);
                }
                outboxRepository.save(pendingMessage);
                continue;
            }
            log.info("Message send successfully");
            pendingMessage.setStatus(Outbox.Status.SENT);
            //TODO poderia deletar a mensagem aqui
            outboxRepository.save(pendingMessage);
        }
        log.debug("Pending messages sent");
    }
}
