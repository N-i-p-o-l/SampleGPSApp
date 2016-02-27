package com.sample.samplegpsapp.presenter.login;

public class LoginPresenterImpl implements LoginPresenter, OnLoginFinishedListener {

  private ILoginView iLoginView;
  private ILoginInteractor iLoginInteractor;

  public LoginPresenterImpl(ILoginView loginView) {
    this.iLoginView = loginView;
    this.iLoginInteractor = new LoginInteractorImpl();
  }

  @Override public void validateCredentials(String phoneNumberLogin, String pinCode) {
    if (iLoginView != null) {
      iLoginView.showProgress();
    }

    iLoginInteractor.login(phoneNumberLogin, pinCode, this);
  }

  @Override public void onDestroy() {
    iLoginView = null;
  }

  @Override public void onAuthError() {
    if (iLoginView != null) {
      iLoginView.setAuthError();
      iLoginView.hideProgress();
    }
  }

  @Override public void onSuccess() {
    if (iLoginView != null) {
      iLoginView.navigateToHome();
    }
  }
}
