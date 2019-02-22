package com.example.socialapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUp extends AppCompatActivity implements View.OnClickListener {


    private EditText edtPassword, edtUsername;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle("Sign Up");

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);

        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    onClick(btnSignUp);
                }
                return false;
            }
        });

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(SignUp.this);


    }

    @Override
    public void onClick(View v) {

        if (edtUsername.getText().toString().equals("") ||
            edtPassword.getText().toString().equals("")) {
            FancyToast.makeText(SignUp.this, "User, email and password are required to sign up.",
                    FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
        } else {
            final ParseUser appUser = new ParseUser();
            appUser.setUsername(edtUsername.getText().toString());
            appUser.setPassword(edtPassword.getText() + "");

            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Loading...");
            dialog.show();

            appUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        FancyToast.makeText(SignUp.this, appUser.getUsername() + ", you signed up successfully", FancyToast.LENGTH_SHORT,
                                FancyToast.SUCCESS, false).show();
                        transitionToTwitterUsers();
                    } else {
                        FancyToast.makeText(SignUp.this, "Hey " + appUser.getUsername() + ", there was an error with the sign up", FancyToast.LENGTH_SHORT,
                                FancyToast.ERROR, false).show();
                    }
                    dialog.dismiss();
                }
            });
        }

    }

    private void transitionToTwitterUsers() {
        Intent intent = new Intent(SignUp.this, TwitterUsers.class);
        startActivity(intent);
        finish();
    }

    public void rootLayoutTapped(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
