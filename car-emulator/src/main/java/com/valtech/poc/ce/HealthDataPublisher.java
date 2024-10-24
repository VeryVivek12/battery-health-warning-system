package com.valtech.poc.ce;

import com.valtech.poc.core.dto.BatteryHealthData;
import com.valtech.poc.core.dto.CellHealthData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class HealthDataPublisher {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Value(value = "${spring.profiles.active}")
    private String carId;

    @Value(value = "${spring.kafka.data-collection-topic}")
    private String dataCollectionTopic;

    @Autowired
    private KafkaTemplate<String, BatteryHealthData> kafkaTemplate;


    @Bean
    CommandLineRunner publishHealthData() {
        return args -> {

            final List<Integer> healthDataPoints = List.of(100, 90, 80, 70);

            // send each health data point for 1 min
            for (Integer healthDataPoint : healthDataPoints) {
                for (int i = 0; i < 12; i++) {
                    // get health data
                    BatteryHealthData batteryHealthData = new BatteryHealthData(carId, generateCellHealthData(healthDataPoint));

                    //prepare message
                    var message = MessageBuilder.withPayload(batteryHealthData)
                            .setHeader(KafkaHeaders.TOPIC, dataCollectionTopic)
                            .setHeader(KafkaHeaders.KEY, carId)
                            .setHeader("system", "OBD")
                            .build();

                    // send to kafka
                    kafkaTemplate.send(message);
                    log.info("Time: {} Car: {} health data: {}", dateFormat.format(new Date()), carId, batteryHealthData);

                    //wait for next data collection time
                    Thread.sleep(500);
                }
            }
        };
    }

    private Set<CellHealthData> generateCellHealthData(int healthPercentage) {
        var data = new HashSet<CellHealthData>();
        data.add(new CellHealthData(1L, healthPercentage));
        data.add(new CellHealthData(2L, healthPercentage));
        data.add(new CellHealthData(3L, healthPercentage));
        data.add(new CellHealthData(4L, healthPercentage));
        return data;
    }
}
