package com.example.parking.client.service;

import android.widget.Toast;

import com.example.parking.client.MainActivity;
import com.example.parking.client.dto.ParkingDTO;
import com.example.parking.client.rest.ParkingRESTService;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.util.Collections.emptyList;

public class ParkingService {

    List<ParkingDTO> parkingList;

    private ParkingRESTService parkingRESTService;

    private static ParkingService INSTANCE;

    private ParkingService() {}

    public static ParkingService getInstance() {
        if (INSTANCE == null) {
            //synchronized block to remove overhead
            synchronized (ParkingService.class) {
                if (INSTANCE == null) {
                    // if instance is null, initialize
                    INSTANCE = new ParkingService();
                }

            }
        }
        return INSTANCE;
    }
//
//    public String calculateDistance(double latitude, double longitude, List<ParkingDTO>) {
//
//    }

}
