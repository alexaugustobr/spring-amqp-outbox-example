package com.example.springamqp.aula1.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.List;

public interface OutboxRepository extends JpaRepository<Outbox, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Outbox> findFirst10ByStatusOrderByCreatedAtAsc(Outbox.Status status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    void deleteByStatus(Outbox.Status status);
}
