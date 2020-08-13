package com.example.parking.client.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DistanceMatrixDTO {
    List<String> origin_addresses;
    List<String> destination_addresses;
    List<DistanceDTO> rows;
    String status;
}
