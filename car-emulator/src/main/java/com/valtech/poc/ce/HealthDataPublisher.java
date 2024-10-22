package com.valtech.poc.ce;

import com.valtech.poc.ce.dto.BatteryHealthData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class HealthDataPublisher {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Value(value = "${spring.profiles.active}")
    private String carId;

    @Value(value = "${spring.kafka.data-collection-topic}")
    private String dataCollectionTopic;

    private KafkaTemplate<String, BatteryHealthData> kafkaTemplate;

    @Scheduled(fixedRate = 5000)
    public void publishHealthData() {
        log.info("Car {} health data published at {}", carId, dateFormat.format(new Date()));
//        kafkaTemplate.send(dataCollectionTopic, null);
    }
}
