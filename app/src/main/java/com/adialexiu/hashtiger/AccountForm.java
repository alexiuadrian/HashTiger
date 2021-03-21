package com.adialexiu.hashtiger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AccountForm extends AppCompatActivity {

    TextView username, password;
    TextView randomPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_form);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        randomPassword = findViewById(R.id.random_password);

        randomPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Call API to generate a password

                Toast.makeText(AccountForm.this, "Password Generated", Toast.LENGTH_SHORT).show();
            }
        });
    }
}