package com.valtech.poc.hdp.process;

import com.valtech.poc.core.dto.BatteryHealthData;
import com.valtech.poc.core.dto.CellHealthData;
import com.valtech.poc.core.dto.ProcessedBatteryHealthData;
import com.valtech.poc.hdp.serde.ProcessedBatteryHealthDataSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BatteryDataStreamProcessor {

    @Value(value = "${spring.kafka.outgoing-topic}")
    private String pocBatteryDataProcessed;

    public void process(KStream<String, BatteryHealthData> stream) {

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
            return "Excellent";
        } else if (averageBatteryHealthPercentage >= 85) {
            return "Good";
        } else if (averageBatteryHealthPercentage >= 75) {
            return "Average";
        } else {
            return "Critical";
        }
    }
}

