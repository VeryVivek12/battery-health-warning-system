package com.valtech.poc.hc;

import com.valtech.poc.core.dto.NotificationDTO;
import com.valtech.poc.core.dto.ProcessedBatteryHealthData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import static com.valtech.poc.core.Constants.AVERAGE;
import static com.valtech.poc.core.Constants.CRITICAL;

@Component
public class BatteryHealthCalculator {

    @Autowired
    private KafkaTemplate<String, NotificationDTO> kafkaTemplate;

    @KafkaListener(topics = "poc-battery-data-processed", groupId = "poc-battery-data-processed-group")
    public void listen(ProcessedBatteryHealthData message) {

        if (message.getRating().equals(AVERAGE) || message.getRating().equals(CRITICAL)) {
            //prepare message
            var notificationDetailMessage = MessageBuilder.withPayload(NotificationDTO.builder()
                            .carId(message.getCarId())
                            .type("Warning")
                            .description("Battery Health is declining.Currently at "
                                    + message.getAverageBatteryHealthPercentage()
                                    + "%. please consult with service center.").build())
                    .setHeader(KafkaHeaders.TOPIC, "poc-battery-data-notification")
                    .setHeader(KafkaHeaders.KEY, message.getCarId())
                    .setHeader("system", "HC")
                    .build();

            // send to kafka
            kafkaTemplate.send(notificationDetailMessage);
        }

    }
}
