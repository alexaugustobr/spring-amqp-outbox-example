package com.example.springamqp.aula1.order;

import com.example.springamqp.aula1.outbox.OutboxService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Collection;

@RestController
@RequestMapping(value = "/v1/orders")
public class OrderController {

	@Autowired
	private OrderRepository orders;

	@Autowired
	private OutboxService outboxService;
	
	@PostMapping
	@Transactional
	public Order create(@RequestBody Order order) {
		orders.save(order);
		OrderCreatedEvent event = new OrderCreatedEvent(order.getId(), order.getValue());
		outboxService.store("orders.v1.order-created", event);
		return order;
	}

	@GetMapping
	public Collection<Order> list() {
		return orders.findAll();
	}

	@GetMapping("{id}")
	public Order findById(@PathVariable Long id) {
		return orders.findById(id).orElseThrow();
	}

	@PutMapping("{id}/pay")
	public Order pay(@PathVariable Long id) {
		Order order = orders.findById(id).orElseThrow();
		order.markAsPaid();
		return orders.save(order);
	}
	
}
