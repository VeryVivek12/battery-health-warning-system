package com.valtech.poc.ce.dto;

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
