package com.example.parking.client.dto;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RowDTO {
  private String name;
  private String distance;
  private String freeSpace;
  private boolean criticalCapacity;
  private String price;
  private boolean favorite;

  public String parseParkingAdress() {
    return StringUtils.join(StringUtils.replace(name.trim(), " ", "+"), "+Beograd+garaza");
  }
}
