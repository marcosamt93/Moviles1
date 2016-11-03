package com.mamt.plumel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.mamt.plumel.view.ConteinerActivity;
import com.mamt.plumel.view.CreateAccountActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void GoCreateAccount(View view)
    {
        Intent intent = new Intent(this, CreateAccountActivity.class);  //en donde estoy y a donde quiero ir
        startActivity(intent);
    }

    public void LogInBtn(View view)
    {
        Intent intent = new Intent(this, ConteinerActivity.class);
        startActivity(intent);
    }
}
