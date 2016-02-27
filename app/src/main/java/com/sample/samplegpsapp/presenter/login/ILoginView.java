package com.sample.samplegpsapp.presenter.login;

public interface ILoginView {
    void showProgress();
    void hideProgress();
    void setAuthError();
    void navigateToHome();
}
