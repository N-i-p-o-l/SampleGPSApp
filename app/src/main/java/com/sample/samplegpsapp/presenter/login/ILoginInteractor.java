package com.sample.samplegpsapp.presenter.login;

public interface ILoginInteractor {
  void login(String phoneLoginNumber, String pinCode, OnLoginFinishedListener listener);
}
