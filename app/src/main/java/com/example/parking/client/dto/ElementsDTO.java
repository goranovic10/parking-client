package com.example.parking.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ElementsDTO {
    private DistanceDurationDetailsDTO distance;
    private DistanceDurationDetailsDTO duration;
    String status;
}
