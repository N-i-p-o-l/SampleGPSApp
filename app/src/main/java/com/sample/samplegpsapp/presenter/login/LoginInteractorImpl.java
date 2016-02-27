package com.sample.samplegpsapp.presenter.login;

import android.os.Handler;
import android.text.TextUtils;

public class LoginInteractorImpl implements ILoginInteractor {
  @Override
  public void login(final String phoneLoginNumber, final String pinCode,  final OnLoginFinishedListener listener) {
    new Handler().postDelayed(new Runnable() {
      @Override public void run() {
        boolean error = false;
        if (TextUtils.isEmpty(phoneLoginNumber) || TextUtils.isEmpty(pinCode)){
          listener.onAuthError();
          error = true;
        }
        if (!error){
          listener.onSuccess();
        }
      }
    }, 2000);
  }
}
