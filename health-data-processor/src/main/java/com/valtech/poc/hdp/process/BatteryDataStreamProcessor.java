package com.valtech.poc.hdp.process;

import com.valtech.poc.hdp.dto.BatteryHealthData;
import com.valtech.poc.hdp.dto.CellHealthData;
import com.valtech.poc.hdp.dto.ProcessedBatteryHealthData;
import org.apache.kafka.streams.kstream.KStream;
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
        }).to(pocBatteryDataProcessed);

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
