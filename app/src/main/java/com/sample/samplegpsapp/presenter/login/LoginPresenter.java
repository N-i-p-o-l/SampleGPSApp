package com.sample.samplegpsapp.presenter.login;

public interface LoginPresenter {
  void validateCredentials(String phoneNumberLogin, String pinCode);
  void onDestroy();
}
