package com.sample.samplegpsapp.service.location;

import android.database.Observable;

public class LocationNotify {

  private static LocationNotify uniqueInstance;

  private LocationNotify() {}

  public static LocationNotify getInstance() {
    if (uniqueInstance == null) {
      uniqueInstance = new LocationNotify();
    }
    return uniqueInstance;
  }

  public final LocationObservable locationObservable = new LocationObservable();

  public interface Observer {
    void onLocationChangedNotify(LocationNotify locationNotify);
  }

  public class LocationObservable extends Observable<Observer> {
    public void notifyStarted() {
      for (final Observer observer : mObservers) {
        observer.onLocationChangedNotify(LocationNotify.this);
      }
    }
  }

  public void registerObserver(final Observer observer) {
    locationObservable.registerObserver(observer);
  }

  public void unregisterObserver(final Observer observer) {
    locationObservable.unregisterObserver(observer);
  }

}
