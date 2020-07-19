package com.example.android.travelapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelapp.activity.mytiket.MyTiketActivity;
import com.example.android.travelapp.activity.keranjang.KeranjangActivity;
import com.example.android.travelapp.activity.main.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilActivity extends AppCompatActivity {

    EditText etUsername, etPassword, etNama, etAlamat, etTelepon;
    Button btnLogout, btnEdit;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etNama = findViewById(R.id.et_nama);
        etAlamat = findViewById(R.id.et_alamat);
        etTelepon = findViewById(R.id.et_telepon);
        btnEdit = findViewById(R.id.btn_edit);
        btnLogout = findViewById(R.id.btn_logout);
        String usernamelocal = Preferences.getLoggedInUser(getBaseContext());

        reference = FirebaseDatabase.getInstance().getReference().child("users").child(usernamelocal);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                etPassword.setText(dataSnapshot.child("password").getValue().toString());
                etUsername.setText(dataSnapshot.child("username").getValue().toString());
                etUsername.setEnabled(false);
                etNama.setText(dataSnapshot.child("nama").getValue().toString());
                etAlamat.setText(dataSnapshot.child("alamat").getValue().toString());
                etTelepon.setText(dataSnapshot.child("telepon").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = etPassword.getText().toString();
                String username = etUsername.getText().toString();
                String nama = etNama.getText().toString();
                String alamat = etAlamat.getText().toString();
                String telepon = etTelepon.getText().toString();

                if (nama.equals("")){
                    etNama.setError("Nama tidak boleh kosong!");
                    etNama.requestFocus();
                } else if (username.equals("")){
                    etUsername.setError("Username tidak boleh kosong!");
                    etUsername.requestFocus();
                } else if (password.equals("")){
                    etPassword.setError("Password tidak boleh kosong!");
                    etPassword.requestFocus();
                } else if (alamat.equals("")){
                    etAlamat.setError("Alamat tidak boleh kosong!");
                    etAlamat.requestFocus();
                } else if (telepon.equals("")){
                    etTelepon.setError("Telepon tidak boleh kosong!");
                    etTelepon.requestFocus();
                } else {
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String dNama = dataSnapshot.child("nama").getValue().toString();
                            String dUsername = dataSnapshot.child("username").getValue().toString();
                            String dPassword = dataSnapshot.child("password").getValue().toString();
                            String dAlamat = dataSnapshot.child("alamat").getValue().toString();
                            String dTelepon = dataSnapshot.child("telepon").getValue().toString();

                            if (nama.equals(dNama) && username.equals(dUsername) && password.equals(dPassword)
                                && alamat.equals(dAlamat) && telepon.equals(dTelepon)){
                                //tombol edit di disable
                                btnEdit.setEnabled(false);
                            } else {
                                dataSnapshot.getRef().child("nama").setValue(nama);
                                dataSnapshot.getRef().child("username").setValue(username);
                                dataSnapshot.getRef().child("password").setValue(password);
                                dataSnapshot.getRef().child("alamat").setValue(alamat);
                                dataSnapshot.getRef().child("telepon").setValue(telepon);

                                Toast.makeText(ProfilActivity.this,
                                        "Data berhasil diedit",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfilActivity.this);
                alertDialog.setTitle("Konfirmasi!");
                alertDialog.setMessage("Apakah kamu yakin ?");
                alertDialog.setNegativeButton("Ya", (dialog, which) -> {
                    dialog.dismiss();
                    Preferences.clearLoggedInUser(getBaseContext());
                    startActivity(new Intent(getBaseContext(), LoginActivity.class));
                    finish();
                });
                alertDialog.setPositiveButton("Tidak",
                        (dialog, which) -> dialog.dismiss());

                alertDialog.show();
            }
        });

        // bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.akun);
        // membuat highlight icon saat diklik
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.keranjang:
                        startActivity(new Intent(getApplicationContext(), KeranjangActivity.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.tiket:
                        startActivity(new Intent(getApplicationContext(), MyTiketActivity.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.akun:
                        break;
                }
                return false;
            }
        });
    }
}
