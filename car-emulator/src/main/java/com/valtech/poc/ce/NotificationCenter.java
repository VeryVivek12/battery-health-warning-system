package com.valtech.poc.ce;

import com.valtech.poc.core.dto.NotificationDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationCenter {

    @KafkaListener(topics = "poc-ui-${spring.profiles.active}", groupId = "poc-test-${spring.profiles.active}-group")
    public void listen(NotificationDetail message) {
        log.info("-----------------" + message.getType() + "-----------------");
        log.info(message.getDescription());
        log.info("----------------------------------");
    }
}
