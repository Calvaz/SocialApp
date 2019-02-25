package com.example.wazzi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.parse.ParseInstallation;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogIn, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogIn = findViewById(R.id.btnLogIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    public void rootLayoutTapped(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnLogIn:
                Intent intentLogIn = new Intent(MainActivity.this, LogInActivity.class);
                startActivity(intentLogIn);
                break;
            case R.id.btnSignUp:
                Intent intentSignUp = new Intent(MainActivity.this, SignUp.class);
                startActivity(intentSignUp);
                break;
        }

    }
}
