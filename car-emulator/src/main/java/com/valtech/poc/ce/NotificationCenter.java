package com.valtech.poc.ce;

import com.valtech.poc.core.dto.NotificationDetail;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationCenter {

    @KafkaListener(topics = "poc-ui-${spring.profiles.active}", groupId = "poc-test-${spring.profiles.active}-group")
    public void listen(NotificationDetail message) {
        System.out.println("-----------------" + message.getType() + "-----------------");
        System.out.println(message.getDescription());
        System.out.println("----------------------------------");
    }
}
