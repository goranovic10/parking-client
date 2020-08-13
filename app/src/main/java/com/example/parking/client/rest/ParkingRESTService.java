package com.example.parking.client.rest;

import com.example.parking.client.dto.DistanceMatrixDTO;
import com.example.parking.client.dto.ParkingDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ParkingRESTService {

  @GET("parking")
  Call<List<ParkingDTO>> getParkingSpaces(@Query("latitude") Double latitude,
                                          @Query("longitude") Double longitude);
}
