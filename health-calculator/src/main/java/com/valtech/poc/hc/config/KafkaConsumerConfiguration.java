package com.valtech.poc.hc.config;

import com.valtech.poc.core.dto.NotificationDetail;
import com.valtech.poc.core.dto.ProcessedBatteryHealthData;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfiguration {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * @return consumer factory for processed battery health data
     */
    @Bean
    public ConsumerFactory<String, ProcessedBatteryHealthData> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), new JsonDeserializer<>(ProcessedBatteryHealthData.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProcessedBatteryHealthData> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ProcessedBatteryHealthData> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

}
