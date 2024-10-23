package com.valtech.poc.ns;

import com.valtech.poc.core.dto.NotificationDTO;
import com.valtech.poc.core.dto.NotificationDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class NotificationDistributor {

    @Autowired
    private KafkaTemplate<String, NotificationDetail> kafkaTemplate;

    @KafkaListener(topics = "poc-battery-data-notification", groupId = "poc-battery-data-notification-group")
    public void listen(NotificationDTO message) {
        //prepare message
        var notificationDetail = MessageBuilder.withPayload(NotificationDetail.builder()
                        .type(message.getType())
                        .description(message.getDescription()).build())
                .setHeader(KafkaHeaders.TOPIC, "poc-ui-" + message.getCarId())
                .setHeader(KafkaHeaders.KEY, message.getCarId())
                .setHeader("system", "NS")
                .build();

        // send to kafka
        kafkaTemplate.send(notificationDetail);
    }
}
