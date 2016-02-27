package com.sample.samplegpsapp.view.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import com.sample.samplegpsapp.R;
import com.sample.samplegpsapp.service.location.LocationNotify;
import com.sample.samplegpsapp.service.location.LocationService;

public class DistanceActivity extends AppCompatActivity implements LocationNotify.Observer {

  private TextView txt_speed, txt_distance;
  private Chronometer chronometer;
  private Button startButton, endButton;

  private LocationService locationService;
  private boolean bound = false;

  private float currentSpeed, distanceSummary;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.measurement_layout);

    txt_speed = (TextView) findViewById(R.id.txt_speed);
    txt_distance = (TextView) findViewById(R.id.txt_distance);
    startButton = (Button) findViewById(R.id.start_button);
    endButton = (Button) findViewById(R.id.end_button);
    chronometer = (Chronometer) findViewById(R.id.chronometer);

    SetEnabledOrDisabledButton(endButton);

    startButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {

        SetEnabledOrDisabledButton(startButton);
        SetEnabledOrDisabledButton(endButton);

        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        //Start GPS Service
        Intent intent = new Intent(getApplicationContext(), LocationService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
      }
    });

    endButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {

        //ToDo Send Result Data to Server
        SetEnabledOrDisabledButton(startButton);
        SetEnabledOrDisabledButton(endButton);

        chronometer.stop();
        locationService.stopSelf();

      }
    });

    LocationNotify.getInstance().registerObserver(this);
  }

  public void SetEnabledOrDisabledButton(Button button) {
    if (button.isEnabled()) {
      button.setEnabled(false);
      button.setAlpha(.3f);
      button.setClickable(false);
    } else {
      button.setEnabled(true);
      button.setAlpha(1f);
      button.setClickable(true);
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (locationService != null) {locationService.stopSelf();}
    LocationNotify.getInstance().unregisterObserver(this);
  }

  private ServiceConnection serviceConnection = new ServiceConnection() {
    @Override public void onServiceConnected(ComponentName name, IBinder service) {
      LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
      locationService = binder.getService();
      bound = true;
    }

    @Override public void onServiceDisconnected(ComponentName name) {
      bound = false;
    }
  };

  @Override public void onLocationChangedNotify(LocationNotify locationNotify) {
    distanceSummary = locationService.getDistanceAndSpeed()[0];
    currentSpeed = locationService.getDistanceAndSpeed()[1];
    txt_distance.setText(String.valueOf(distanceSummary));
    txt_speed.setText(String.valueOf(currentSpeed));
  }
}
