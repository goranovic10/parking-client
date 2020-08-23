package com.example.parking.client.service;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

public class LocationService extends Service implements LocationListener {
  private final Context mContext;

  private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
  private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

  private LocationManager locationManager;

  public LocationService(Context context) {
    this.mContext = context;
    locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
  }

  public Location getLocation() {
    Location location = getLocationByGPS();
    if (location == null) {
      location = getLocationByNetwork();
    }
    return location;
  }

  private Location getLocationByGPS() {
    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
      if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
              != PackageManager.PERMISSION_GRANTED
          && ActivityCompat.checkSelfPermission(
                  mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
              != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(
            (Activity) mContext,
            new String[] {
              android.Manifest.permission.ACCESS_FINE_LOCATION,
              Manifest.permission.ACCESS_COARSE_LOCATION
            },
            101);
      }
      locationManager.requestLocationUpdates(
          LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
      if (locationManager != null) {
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
      }
    }
    return null;
  }

  private Location getLocationByNetwork() {
    if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
      if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
              != PackageManager.PERMISSION_GRANTED
          && ActivityCompat.checkSelfPermission(
                  mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
              != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(
            (Activity) mContext,
            new String[] {
              android.Manifest.permission.ACCESS_FINE_LOCATION,
              Manifest.permission.ACCESS_COARSE_LOCATION
            },
            101);
      }
      locationManager.requestLocationUpdates(
          LocationManager.NETWORK_PROVIDER,
          MIN_TIME_BW_UPDATES,
          MIN_DISTANCE_CHANGE_FOR_UPDATES,
          this);
      if (locationManager != null) {
        return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
      }
    }
    return null;
  }

  public void showSettingsAlert() {
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
    alertDialog.setTitle("ГПС подешавања");
    alertDialog.setMessage("ГПС није укључен. Да ли желите да идете на подешавања");

    alertDialog.setPositiveButton(
        "Подешавања",
        (dialog, which) -> {
          Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
          mContext.startActivity(intent);
        });
    alertDialog.setNegativeButton("Откажи", (dialog, which) -> dialog.cancel());
    alertDialog.show();
  }

  @Override
  public void onLocationChanged(Location location) {}

  @Override
  public void onProviderDisabled(String provider) {}

  @Override
  public void onProviderEnabled(String provider) {}

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {}

  @Override
  public IBinder onBind(Intent arg0) {
    return null;
  }
}
