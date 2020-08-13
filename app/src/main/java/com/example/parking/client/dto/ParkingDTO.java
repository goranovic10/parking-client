package com.example.parking.client.dto;

import android.support.annotation.Nullable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingDTO {
  private String name;
  private String freeSpace;
  private boolean criticalCapacity;
  @Nullable private String price;
  @Nullable private String distance;
  @Nullable private boolean favorite;

  public ParkingDTO(ParkingDTO parkingDTO) {
    this.name = parkingDTO.getName();
    this.freeSpace = parkingDTO.getFreeSpace();
    this.criticalCapacity = parkingDTO.isCriticalCapacity();
    this.price = parkingDTO.getPrice();
    this.distance = parkingDTO.getDistance();
    this.favorite = parkingDTO.favorite;
  }
}
