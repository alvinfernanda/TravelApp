package com.example.android.travelapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.travelapp.activity.main.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    EditText etUsername, etPassword, etRepass;
    Button btnDaftar;
    DatabaseReference reference_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etRepass = findViewById(R.id.et_repass);
        btnDaftar = findViewById(R.id.btn_daftar);

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String repass = etRepass.getText().toString();

                if(username.equals("")){
                    etUsername.setError("Username harus diisi!");
                    etUsername.requestFocus();
                } else if(password.equals("")){
                    etPassword.setError("Password harus diisi!");
                    etPassword.requestFocus();
                } else if(repass.equals("")) {
                    etRepass.setError("Password harus diisi!");
                    etRepass.requestFocus();
                } else if (!repass.equals(password)) {
                    etRepass.setError("Password tidak sama!");
                    etRepass.requestFocus();
                } else {
                    reference_username = FirebaseDatabase.getInstance().getReference().child("users").child(username);
                    reference_username.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                etUsername.setError("Username ini sudah tersedia!");
                                etUsername.requestFocus();
                            } else {
                                Preferences.setRegisteredUser(getBaseContext(),username);
                                Intent intent = new Intent(RegisterActivity.this, Register2Activity.class);
                                intent.putExtra("exUsername", username);
                                intent.putExtra("exPassword", password);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }
}
