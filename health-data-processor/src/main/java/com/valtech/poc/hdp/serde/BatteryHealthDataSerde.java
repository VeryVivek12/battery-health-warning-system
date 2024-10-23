package com.valtech.poc.hdp.serde;

import com.valtech.poc.core.dto.BatteryHealthData;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

public class BatteryHealthDataSerde extends Serdes.WrapperSerde<BatteryHealthData> {

    public BatteryHealthDataSerde() {
        super(new JsonSerializer<>(), new JsonDeserializer<>(BatteryHealthData.class));
    }
}
