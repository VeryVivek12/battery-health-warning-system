package com.valtech.poc.hdp.serde;

import com.valtech.poc.hdp.dto.ProcessedBatteryHealthData;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class ProcessedBatteryHealthDataSerde extends Serdes.WrapperSerde<ProcessedBatteryHealthData> {

    public ProcessedBatteryHealthDataSerde() {
        super(new JsonSerializer<>(), new JsonDeserializer<>(ProcessedBatteryHealthData.class));
    }
}
