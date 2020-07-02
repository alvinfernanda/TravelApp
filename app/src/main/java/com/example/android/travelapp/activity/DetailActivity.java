package com.example.android.travelapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.travelapp.R;
import com.example.android.travelapp.activity.tiket.TiketActivity;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView destinasiTitle = findViewById(R.id.titleDetail);
        TextView destinasiSubTitle = findViewById(R.id.subTitleDetail);
        ImageView destinasiImage = findViewById(R.id.destinasiImageDetail);
        Button btnbeli = findViewById(R.id.btnBeli);




        destinasiTitle.setText(getIntent().getStringExtra("destinasi"));
//        destinasiSubTitle.setText(getIntent().getStringExtra("detail"));
        Glide.with(this).load(getIntent().getIntExtra("image_resource",0))
                .into(destinasiImage);


        String judul = (destinasiTitle.getText().toString());

        switch (judul) {
            case "Candi Borobudur":
                destinasiSubTitle.setText(R.string.detail_candi);
                break;
            case "Jakarta Aquarium":
                destinasiSubTitle.setText(R.string.detail_aquarium);
                break;
            case "Tanah Lot":
                destinasiSubTitle.setText(R.string.detail_tanahlot);
                break;
            case "Kawah Putih":
                destinasiSubTitle.setText(R.string.detail_kawahputih);
                break;
            case "Monumen Nasional":
                destinasiSubTitle.setText(R.string.detail_monas);
                break;
            case "Raja Ampat":
                destinasiSubTitle.setText(R.string.detail_rajaampat);
                break;
            case "Bali Safari dan Marine Park":
                destinasiSubTitle.setText(R.string.detail_safari);
                break;
            default:
                destinasiSubTitle.setText(R.string.detail_transstudio);
                break;
        }

        btnbeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = destinasiTitle.getText().toString();
                Intent intents = getIntent();
                String dataTempat = intents.getStringExtra("title");
                String dataHarga = intents.getStringExtra("harga");

                Intent intent = new Intent(DetailActivity.this, TiketActivity.class);
                intent.putExtra("exDestinasi", title);
                intent.putExtra("exTempat", dataTempat);
                intent.putExtra("exHarga", dataHarga);
                startActivity(intent);
            }
        });

    }


}
