package com.example.android.travelapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.travelapp.activity.tiket.TiketActivity;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView destinasi = findViewById(R.id.destinasi);
        TextView detail = findViewById(R.id.detail);
        ImageView destinasiImage = findViewById(R.id.destinasiImageDetail);
        Button btnbeli = findViewById(R.id.btnBeli);

        destinasi.setText(getIntent().getStringExtra("destinasi"));
        detail.setText(getIntent().getStringExtra("detail"));
        String gambar = getIntent().getStringExtra("gambar");
        Glide.with(this).load(gambar)
                .into(destinasiImage);

        btnbeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = destinasi.getText().toString();
                Intent intents = getIntent();
                String dataTempat = intents.getStringExtra("lokasi");
                int dataHarga = intents.getIntExtra("harga", 0);

                Intent intent = new Intent(DetailActivity.this, TiketActivity.class);
                intent.putExtra("exDestinasi", title);
                intent.putExtra("exTempat", dataTempat);
                intent.putExtra("exHarga", dataHarga);
                startActivity(intent);
            }
        });

    }


}
