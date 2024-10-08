package com.otc.backend.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
/*
@Service
public class RabbitMQProducer {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    //private String spring_boot_otc;

    @Value("${rabbitmq.routing.key}")
    private String routingkey;
    //private String routing_key_otc;

   // @Value("rabbitmq.queue.name")
   //private String queue;


   private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducer.class);
    
   private RabbitTemplate rabbitTemplate;

  
   public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
       this.rabbitTemplate = rabbitTemplate;
   }
    
   public void sendMessage(String message) {
       LOGGER.info(String.format("message sent -> %s", message));
    
       rabbitTemplate.convertAndSend(exchange, routingkey, message);
    
   }

    
}
*/