package com.valtech.poc.hdp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatteryHealthData {
    private String id;
    private Set<CellHealthData> cellHealthDataSet;
}

