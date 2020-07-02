package com.example.android.travelapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register2Activity extends AppCompatActivity implements FetchAddressTask.OnTaskCompleted {

    EditText etNama, etTelepon;
    TextView tvAlamat;
    Button btnDaftar, btnAlamat;

    DatabaseReference reference;

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private static final String TRACKING_LOCATION_KEY = "tracking_location";

    // Location classes
    private boolean mTrackingLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        etNama = findViewById(R.id.et_nama);
        etTelepon = findViewById(R.id.et_telepon);
        tvAlamat = findViewById(R.id.tv_isiAlamat);
        btnDaftar = findViewById(R.id.btn_daftar);
        btnAlamat = findViewById(R.id.btn_alamat);

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = etNama.getText().toString();
                String telepon = etTelepon.getText().toString();
                String alamat = tvAlamat.getText().toString();
                // mengambil data intent yang dikirimkan register1
                Intent intent = getIntent();
                String username = intent.getStringExtra("exUsername");
                String password = intent.getStringExtra("exPassword");

                if (nama.equals("")){
                    etNama.setError("Nama harus diisi!");
                    etNama.requestFocus();
                } else if (telepon.equals("")){
                    etTelepon.setError("Telepon harus diisi!");
                    etTelepon.requestFocus();
                } else {
                    //menyimpan data username ke sharedpreferences
                    Preferences.setRegisteredUser(getBaseContext(),username);
                    // menyimpan data user ke firebase

                    reference = FirebaseDatabase .getInstance().getReference("users");

                    UsersHelper usersHelper = new UsersHelper(nama, password, username, alamat, telepon);
                    reference.child(username).setValue(usersHelper);

                    Toast.makeText(Register2Activity.this,
                            "Daftar akun berhasil, silahkan masuk",
                            Toast.LENGTH_SHORT).show();

                    Intent gotologin = new Intent(Register2Activity.this, LoginActivity.class);
                    startActivity(gotologin);
                }
            }
        });

        btnAlamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mTrackingLocation) {
                    startTrackingLocation();
                } else {
                    stopTrackingLocation();
                }
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (mTrackingLocation) {
                    new FetchAddressTask(Register2Activity.this, Register2Activity.this)
                            .execute(locationResult.getLastLocation());
                }
            }
        };
    }

    //fungsi untuk mengambil data lokasi
    private void startTrackingLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            mTrackingLocation = true;
            mFusedLocationClient.requestLocationUpdates
                    (getLocationRequest(), mLocationCallback,
                            null /* Looper */);

            // Set a loading text while you wait for the address to be
            // returned
            tvAlamat.setText(getString(R.string.address_text,
                    getString(R.string.loading)));
        }

    }

    private void stopTrackingLocation() {
        if (mTrackingLocation) {
            mTrackingLocation = false;
//            btnAlamat.setText(R.string.start_tracking_location);
            tvAlamat.setText(R.string.textview_hint);
        }
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startTrackingLocation();
                } else {
                    Toast.makeText(this,
                            R.string.location_permission_denied,
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onTaskCompleted(String result) {
        if (mTrackingLocation) {
            // Update the UI
            tvAlamat.setText(getString(R.string.address_text, result));
        }
    }
}
