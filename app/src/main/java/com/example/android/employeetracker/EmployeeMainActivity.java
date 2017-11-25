package com.example.android.employeetracker;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmployeeMainActivity extends AppCompatActivity implements View.OnClickListener{

    // Firebase methods
    private FirebaseAuth firebaseAuth;

    // Location methods
    LocationManager locationManager;
    LocationListener locationListener;

    private FirebaseUser user;

    private TextView textViewUserEmail;

    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_main);

        firebaseAuth = FirebaseAuth.getInstance();

        // If user is NOT logged in
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        user = firebaseAuth.getCurrentUser();

        textViewUserEmail = (TextView) findViewById(R.id.welcomeUser);

        textViewUserEmail.setText("Welcome "+user.getEmail());

        buttonLogout = (Button) findViewById(R.id.employeeLogoutButton);

        buttonLogout.setOnClickListener(this);

        // Location
        // TODO: Check if GPS is enables. Else, request permission
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener(user);

        try {
            // Store GPS every 5 minutes (300000 milliseconds)
            locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,5000, 0, locationListener);
        }
        catch (SecurityException e) {
            //ERROR
            System.err.println("ERROR: Couldn't retrieve GPS.");
        }

    }

    public FirebaseUser getUser(){
        return user;
    }

    @Override
    public void onClick(View view){
        if(view == buttonLogout){
            firebaseAuth.signOut();
            locationManager.removeUpdates(locationListener);
            locationManager = null;
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
