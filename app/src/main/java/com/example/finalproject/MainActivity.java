package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView username = (TextView) findViewById(R.id.username);
        TextView password = (TextView) findViewById(R.id.password);
        SharedPreferences prefs = getSharedPreferences("usernamePreferences", MODE_PRIVATE);

        Button loginbtn = (Button) findViewById(R.id.loginbtn);

        String usernamepref, passwordpref ;

        if (prefs.contains("username")&& prefs.contains("password")){
            usernamepref = prefs.getString("username", "");

            passwordpref = prefs.getString("password", "");
        } else {
            usernamepref="";
            passwordpref="";
        }

        username.setText(usernamepref);
        password.setText(passwordpref);

        Intent toHomePage = new Intent(this, Homepage.class);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this,getApplicationContext().getResources().getString(R.string.to_homepage_toast), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this,getApplicationContext().getResources().getString(R.string.to_username_toast)+username.getText().toString(), Toast.LENGTH_SHORT).show();
                }

                startActivity(toHomePage);
            }
        });
    }

    @Override
    protected void onPause(){
        super.onPause();
        // makes a shared preferences and editor variable
        SharedPreferences prefs = getSharedPreferences("usernamePreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        // gets the view from the ID from the layout file
        TextView username = (TextView) findViewById(R.id.username);
        TextView password = (TextView) findViewById(R.id.password);
        // stores the data in the preferences
        edit.putString("username", username.getText().toString());
        edit.putString("password", password.getText().toString());
        edit.apply();
    }
}