package com.sample.samplegpsapp.view.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.sample.samplegpsapp.R;
import com.sample.samplegpsapp.presenter.login.ILoginView;
import com.sample.samplegpsapp.presenter.login.LoginPresenter;
import com.sample.samplegpsapp.presenter.login.LoginPresenterImpl;
import com.sample.samplegpsapp.view.main.DistanceActivity;

public class LoginActivity extends AppCompatActivity implements ILoginView, View.OnClickListener {

  private ProgressBar progressLogin;
  private EditText loginPhoneNumber;
  private EditText pinCode;
  private LoginPresenter presenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    progressLogin = (ProgressBar) findViewById(R.id.progressLogin);
    loginPhoneNumber = (EditText) findViewById(R.id.loginPhoneNumber);
    pinCode = (EditText) findViewById(R.id.pinCode);
    findViewById(R.id.signIn).setOnClickListener(this);

    presenter = new LoginPresenterImpl(this);

  }

  @Override protected void onDestroy() {
    presenter.onDestroy();
    super.onDestroy();
  }

  @Override public void showProgress() {
    progressLogin.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideProgress() {
    progressLogin.setVisibility(View.GONE);
  }

  @Override
  public void setAuthError() {
    //Произошла ошибка
  }

  @Override
  public void navigateToHome() {
    startActivity(new Intent(this, DistanceActivity.class));
    finish();
  }

  @Override public void onClick(View v) {
    presenter.validateCredentials(loginPhoneNumber.getText().toString(), pinCode.getText().toString());
  }
}
