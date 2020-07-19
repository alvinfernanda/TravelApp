package com.example.android.travelapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.travelapp.activity.keranjang.KeranjangActivity;
import com.example.android.travelapp.activity.keranjang.KeranjangPresenter;
import com.example.android.travelapp.activity.keranjang.KeranjangView;
import com.example.android.travelapp.activity.main.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin;
    TextView buatAkun;

    DatabaseReference reference;
    KeranjangPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        buatAkun = findViewById(R.id.buat_akun);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (username.equals("")){
                    etUsername.setError("Username harus diisi!");
                    etUsername.requestFocus();
                } else if (password.equals("")){
                    etPassword.setError("Password harus diisi!");
                    etPassword.requestFocus();
                } else {
                    reference = FirebaseDatabase.getInstance().getReference().child("users").child(username);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // ambil data password dari firebase
                                String passFirebase = dataSnapshot.child("password").getValue().toString();

                                //validasi password dengan password yang ada di firebase
                                if (password.equals(passFirebase)){
                                    // menyimpan data username ke sharedpreference sebagai session
                                    Preferences.setRegisteredUser(getBaseContext(),username);
                                    Preferences.setLoggedInUser(getBaseContext(),Preferences.getRegisteredUser(getBaseContext()));
                                    Preferences.setLoggedInStatus(getBaseContext(),true);

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);

                                } else {
                                    etPassword.setError("Password salah!");
                                    etPassword.requestFocus();
                                }
                            } else {
                                etUsername.setError("Username ini belum terdaftar!");
                                etUsername.requestFocus();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "Database Error!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        buatAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoregister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(gotoregister);
            }
        });
    }

    /** ke MainActivity jika data Status Login dari Data Preferences bernilai true */
    @Override
    protected void onStart() {
        super.onStart();
        if (Preferences.getLoggedInStatus(getBaseContext())){
            startActivity(new Intent(getBaseContext(),MainActivity.class));
            finish();
        }
    }

}
