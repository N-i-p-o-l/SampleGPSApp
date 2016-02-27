package com.sample.samplegpsapp.service.location;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service
    implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    LocationListener {

  private static final String TAG = "LocationService";
  private final IBinder mBinder = new LocalBinder();
  private static final int REQUEST_RESOLVE_ERROR = 1001;
  private boolean IsResolvingError;
  private boolean IsCurrentlyProcessingLocation;
  private LocationRequest locationRequest;
  private GoogleApiClient googleApiClient;
  private Location lastLocation;
  private boolean isFirstLocation;
  private float distanceSummary;
  private float distanceBetween;
  private float currentSpeed;

  public LocationService() {
  }

  public class LocalBinder extends Binder {
   public LocationService getService() {
      return LocationService.this;
    }
  }

  public int onStartCommand(Intent intent, int flags, int startid) {

    if (!IsCurrentlyProcessingLocation) {
      IsCurrentlyProcessingLocation = true;
      startTracking();
    }

    isFirstLocation = true;

    return START_STICKY;
  }

  private void startTracking() {
    Log.d(TAG, "startTracking");

    // ToDo Check PlayServices
    googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .build();

    if (!googleApiClient.isConnected() || !googleApiClient.isConnecting()) {
      googleApiClient.connect();
    } else {
      Log.e(TAG, "unable to connect to google play services.");
    }
  }

  @Override public IBinder onBind(Intent intent) {
    return mBinder;
  }

  @Override public void onConnected(Bundle bundle) {
    Log.d(TAG, "onConnected");

    locationRequest = LocationRequest.create();
    locationRequest.setInterval(60000); // milliseconds
    locationRequest.setFastestInterval(30000);
    locationRequest.setSmallestDisplacement(500f);
    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
          != PackageManager.PERMISSION_GRANTED
          && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
          != PackageManager.PERMISSION_GRANTED) {
        return;
      }
    }
    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
  }

  @Override public void onConnectionSuspended(int i) {
    Log.e(TAG, "GoogleApiClient connection has been suspend");
  }

  @Override public void onLocationChanged(Location location) {
    if (location != null) {
      Log.e(TAG, "position: "
          + location.getLatitude()
          + ", "
          + location.getLongitude()
          + " accuracy: "
          + location.getAccuracy());
      if (!isFirstLocation) {
        distanceBetween = lastLocation.distanceTo(location);
        distanceSummary =+ distanceBetween;
        currentSpeed = distanceBetween * 3600 / (location.getTime() - lastLocation.getTime());
        lastLocation = location;
        LocationNotify.getInstance().locationObservable.notifyStarted();
      } else {
        lastLocation = location;
        isFirstLocation = false;
      }
    }

  }

  public float[] getDistanceAndSpeed() {
    return new float[] {distanceSummary, currentSpeed};
  }

  private void stopLocationUpdates() {
    if (googleApiClient != null && googleApiClient.isConnected()) {
      googleApiClient.disconnect();
    }
  }

  @Override public void onConnectionFailed(ConnectionResult connectionResult) {
    Log.e(TAG, "onConnectionFailed");
    if (IsResolvingError) {
      return;
    } else if (connectionResult.hasResolution()) {
      try {
        IsResolvingError = true;
        //ToDo send to Activity
        //connectionResult.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
      } catch (Exception e) {
        googleApiClient.connect();
      }
    }
  }
}
