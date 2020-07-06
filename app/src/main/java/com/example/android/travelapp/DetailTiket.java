package com.example.android.travelapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailTiket extends AppCompatActivity {

    TextView tv_destinasi, tv_lokasi, tv_total, tv_jumlah, tv_tanggal;
    ProgressDialog progressDialog;

    int id, jumlah, total;
    String destinasi, lokasi, tanggal, sjumlah, stotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tiket);

        tv_destinasi = findViewById(R.id.tv_destinasi);
        tv_lokasi = findViewById(R.id.tv_tempat);
        tv_total = findViewById(R.id.total_bayar);
        tv_jumlah = findViewById(R.id.teks_jml);
        tv_tanggal = findViewById(R.id.tanggal);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        destinasi = intent.getStringExtra("destinasi");
        lokasi = intent.getStringExtra("tempat");
        jumlah = intent.getIntExtra("jumlah", 0);
        sjumlah = Integer.toString(jumlah);
        total = intent.getIntExtra("total", 0);
        stotal = Integer.toString(total);
        tanggal = intent.getStringExtra("tanggal");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        tv_destinasi.setText(destinasi);
        tv_lokasi.setText(lokasi);
        tv_total.setText(stotal);
        tv_jumlah.setText(sjumlah);
        tv_tanggal.setText(tanggal);
    }
}
