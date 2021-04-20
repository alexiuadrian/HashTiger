package com.adialexiu.hashtiger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AccountForm extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    TextView serviceName, userName, password;
    TextView randomPassword;
    Button submit;
    TextView additionalMessage;

    SharedPreferences sharedPref;
    public static final String PREFERENCES_KEY = "preferences key";
    public static final String PREFERENCES_ID_KEY = "preferences id key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_form);

        databaseHelper = new DatabaseHelper(this);

        serviceName = findViewById(R.id.serviceName);
        userName = findViewById(R.id.username);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submit);
        additionalMessage = findViewById(R.id.additional_text);

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


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName.getText().toString().length() == 0 || password.getText().toString().length() == 0) {
                    additionalMessage.setText("One or more of the inputs are empty!");
                    additionalMessage.setTextColor(Color.RED);
                    return;
                }

                String serviceText = serviceName.getText().toString();
                String usernameText = userName.getText().toString();
                String passwordText = password.getText().toString();

                boolean result = databaseHelper.insertCredentials(serviceText, usernameText, passwordText, username);
                // Add success message if it succeeds

                if (result == true) {
                    additionalMessage.setText("Credentials added successfully!");
                    additionalMessage.setTextColor(Color.GREEN);
                    Intent intent = new Intent(AccountForm.this, MainMenu.class);
                    startActivity(intent);
                } else {
                    additionalMessage.setText("Credentials could not be added!");
                    additionalMessage.setTextColor(Color.RED);
                }
            }
        });

    }
}