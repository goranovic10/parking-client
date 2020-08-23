package com.example.parking.client;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.parking.client.adapters.RowAdapter;
import com.example.parking.client.dto.ParkingDTO;
import com.example.parking.client.dto.RowDTO;
import com.example.parking.client.rest.ParkingRESTService;
import com.example.parking.client.service.LocationService;
import com.example.parking.client.sql.FavoriteDbHelper;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.parking.client.utils.ParkingConstants.GOOGLE_DIRECTIONS_API;

public class MainActivity extends AppCompatActivity {

  private LocationService locationService;
  private ParkingRESTService parkingRESTService;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    locationService = new LocationService(this);
    resolveParkingService();
    refreshParkingSpaces();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    if (item.getItemId() == R.id.refresh) {
      refreshParkingSpaces();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.parking_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  private void resolveParkingService() {
    OkHttpClient okHttpClient =
            new OkHttpClient()
                    .newBuilder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build();
    Retrofit parkingRetrofit =
            new Retrofit.Builder()
                    .baseUrl("http://192.168.0.12:8080/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
    parkingRESTService = parkingRetrofit.create(ParkingRESTService.class);
  }

  private void refreshParkingSpaces() {
    Context context = this;

    setContentView(R.layout.activity_main);

    Location currentLocation = locationService.getLocation();
    if (currentLocation == null) {
      locationService.showSettingsAlert();
      return;
    }

    final double latitude = 44.801971;
    final double longitude = 20.4892391;

    Call<List<ParkingDTO>> parkingSpaceCall =
        parkingRESTService.getParkingSpaces(latitude, longitude);
    parkingSpaceCall.enqueue(
        new Callback<List<ParkingDTO>>() {
          @SneakyThrows
          @Override
          public void onResponse(Call<List<ParkingDTO>> call, Response<List<ParkingDTO>> response) {
            final ListView lv = findViewById(R.id.userList);
            List<ParkingDTO> parkings = resolveParkings(response.body());

            lv.setAdapter(
                new RowAdapter(
                    context,
                    parkings.stream()
                        .map(
                            parking ->
                                new RowDTO(
                                    parking.getName(),
                                    parking.getDistance(),
                                    parking.getFreeSpace(),
                                    parking.isCriticalCapacity(),
                                    parking.getPrice(),
                                    parking.isFavorite()))
                        .collect(Collectors.toList())));
            lv.setOnItemClickListener(
                (a, v, position, id) -> {
                  RowDTO row = (RowDTO) lv.getItemAtPosition(position);
                  Intent intent =
                      new Intent(
                          Intent.ACTION_VIEW,
                          Uri.parse(
                              String.format(GOOGLE_DIRECTIONS_API, getOrigin(latitude, longitude), row.parseParkingAdress())));
                  startActivity(intent);
                });
          }

          @Override
          public void onFailure(Call<List<ParkingDTO>> call, Throwable t) {
            System.err.println(t);
          }
        });
  }

  private String getOrigin(double latitude, double longitude) {
    return StringUtils.join(latitude, ",", longitude);
  }

  private List<ParkingDTO> resolveParkings(List<ParkingDTO> parkings) {
    FavoriteDbHelper dbHandler = new FavoriteDbHelper(this);
    final List<String> favorites = Arrays.asList(dbHandler.selectAll().split(","));
    parkings.forEach(
            parking -> {
              parking.setFavorite(favorites.contains(parking.getName()));
            });

    Comparator<ParkingDTO> comparator = Comparator.comparing(parking -> !parking.isFavorite());
    comparator =
            comparator.thenComparing(parking -> Double.valueOf(parking.getDistance().split(" ")[0]));

    return parkings.stream().sorted(comparator).collect(Collectors.toList());
  }
}
