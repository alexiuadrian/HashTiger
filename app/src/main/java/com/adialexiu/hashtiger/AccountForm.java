package com.adialexiu.hashtiger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AccountForm extends AppCompatActivity {

    TextView username, password;
    TextView randomPassword;
    SharedPreferences sharedPref;
    public static final String PREFERENCES_KEY = "preferences key";
    public static final String PREFERENCES_ID_KEY = "preferences id key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_form);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        sharedPref = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);

        String username = sharedPref.getString(PREFERENCES_ID_KEY, "Nu merge nimica");



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