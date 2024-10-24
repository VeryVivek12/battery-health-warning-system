package com.valtech.poc.hdp.process;

import com.valtech.poc.core.dto.BatteryHealthData;
import com.valtech.poc.core.dto.CellHealthData;
import com.valtech.poc.core.dto.ProcessedBatteryHealthData;
import com.valtech.poc.hdp.serde.ProcessedBatteryHealthDataSerde;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.valtech.poc.core.Constants.*;

@Component
@Slf4j
public class BatteryDataStreamProcessor {

    @Value(value = "${spring.kafka.outgoing-topic}")
    private String pocBatteryDataProcessed;

    public void process(KStream<String, BatteryHealthData> stream) {

        final KTable<String, Long> dataCountsForCar = stream.groupByKey().count();
        dataCountsForCar.toStream().foreach((key, value) -> log.info("{} is connected to Kafka cluster", key));

        stream.mapValues((carId, batteryHealthData) -> {
            // get average cell health
            double averageBatteryHealthPercentage = batteryHealthData.getCellHealthDataSet()
                    .stream()
                    .mapToInt(CellHealthData::getHealthPercentage)
                    .average().orElse(0);

            return ProcessedBatteryHealthData
                    .builder()
                    .carId(carId)
                    .averageBatteryHealthPercentage((int) averageBatteryHealthPercentage)
                    .rating(getRating(averageBatteryHealthPercentage))
                    .build();
        }).to(pocBatteryDataProcessed, Produced.with(Serdes.String(), new ProcessedBatteryHealthDataSerde()));

    }

    private String getRating(double averageBatteryHealthPercentage) {
        if (averageBatteryHealthPercentage >= 95) {
            return EXCELLENT;
        } else if (averageBatteryHealthPercentage >= 85) {
            return GOOD;
        } else if (averageBatteryHealthPercentage >= 75) {
            return AVERAGE;
        } else {
            return CRITICAL;
        }
    }
}

