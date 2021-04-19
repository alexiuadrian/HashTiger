package com.adialexiu.hashtiger;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.internal.$Gson$Preconditions;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class loginactivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    public static final String PREFERENCES_KEY = "preferences key";
    public static final String PREFERENCES_ID_KEY = "preferences id key";

    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;

    Button loginButton, registerButton;
    EditText username, password;
    TextView additionalMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);
        databaseHelper = new DatabaseHelper(this);

        // databaseHelper.onUpgrade(databaseHelper.getWritableDatabase(), 2, 3);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }

            }
        });

        additionalMessage = findViewById(R.id.additional_text);

        username = findViewById(R.id.usernameText);
        password = findViewById(R.id.passwordText);

        loginButton = findViewById(R.id.login_local);
        registerButton = findViewById(R.id.register_local);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().length() == 0 || password.getText().toString().length() == 0) {
                    additionalMessage.setText("One or more of the inputs are empty!");
                    additionalMessage.setTextColor(Color.RED);
                    return;
                }

                String usernameText = username.getText().toString();
                String passwordText = password.getText().toString();
                String passwordToSHA1 = encryptThisString(passwordText);

                // Cauta in baza de date contul cu numele username si parola passwordToSHA1
                boolean result = databaseHelper.checkLoginCredentialsInDb(usernameText, passwordToSHA1);

                if(result) {
                    // Creeaza Shared Preferences
                    makeSharedPreferences(usernameText);
                    Intent intent = new Intent(loginactivity.this, MainMenu.class);
                    startActivity(intent);
                }
                else {
                    additionalMessage.setText("Wrong username or password!");
                    additionalMessage.setTextColor(Color.RED);
                }
            }
        });



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().length() == 0 || password.getText().toString().length() == 0) {
                    additionalMessage.setText("One or more of the inputs are empty!");
                    additionalMessage.setTextColor(Color.RED);
                    return;
                }

                String usernameText = username.getText().toString();
                String passwordText = password.getText().toString();
                String passwordToSHA1 = encryptThisString(passwordText);

                // Adauga in baza de date contul cu numele username si parola passwordToSHA1

                // Cauta user in DB
                boolean isUserInDb = databaseHelper.isUserInDb(usernameText);
                // Daca nu il gaseste, il adauga
                if(!isUserInDb) {
                    // Insereaza userul in DB
                    boolean result = databaseHelper.insertUser(usernameText, passwordToSHA1);
                    // Add success message if it succeeds

                    if (result == true) {
                        additionalMessage.setText("Account added successfully!");
                        additionalMessage.setTextColor(Color.GREEN);
                    } else {
                        additionalMessage.setText("Account could not be added!");
                        additionalMessage.setTextColor(Color.RED);
                    }
                }
                else {
                    additionalMessage.setText("Username already taken!");
                    additionalMessage.setTextColor(Color.RED);
                }
            }
        });

    }

    private void makeSharedPreferences(String usernameText) {
        SharedPreferences preferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREFERENCES_ID_KEY, usernameText);
        editor.apply();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Cauta user in DB
            boolean isUserInDb = databaseHelper.isUserInDb(account.getEmail());
            // Daca nu il gaseste, il adauga
            if(!isUserInDb) {
                // Insereaza userul in DB
                boolean result = databaseHelper.insertUser(account.getEmail(), "");
                // Add success message if it succeeds

                if (result == true) {
                    Toast.makeText(this, "Account added successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Could not create account!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Logged in successfully!", Toast.LENGTH_SHORT).show();
            }

            makeSharedPreferences(account.getEmail());

            // Signed in successfully, show authenticated UI.
            Intent intent = new Intent(this, MainMenu.class);
            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        // Intent here to next activity
    }

    public static String encryptThisString(String input)
    {
        try {
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}