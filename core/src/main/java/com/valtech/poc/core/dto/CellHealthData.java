package com.valtech.poc.core.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CellHealthData {
    private Long id;
    private Integer healthPercentage;
}
