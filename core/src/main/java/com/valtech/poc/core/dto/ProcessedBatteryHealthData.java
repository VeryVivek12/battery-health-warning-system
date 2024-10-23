package com.valtech.poc.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessedBatteryHealthData {
    private String carId;
    private String rating;
    private Integer averageBatteryHealthPercentage;
}
