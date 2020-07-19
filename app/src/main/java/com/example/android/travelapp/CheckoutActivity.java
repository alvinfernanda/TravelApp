package com.example.android.travelapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.travelapp.activity.keranjang.KeranjangActivity;
import com.example.android.travelapp.activity.tiket.TiketView;
import com.example.android.travelapp.api.ApiClient;
import com.example.android.travelapp.api.ApiInterface;
import com.example.android.travelapp.model.Tiket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity implements TiketView {

    ImageView uploadBukti;
    Button btnCheckout;
    Bitmap bitmap;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        uploadBukti = findViewById(R.id.upload);
        btnCheckout = findViewById(R.id.btn_checkout);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        uploadBukti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pilihFile();
            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bukti = null;
                if (bitmap == null) {
                    bukti = "";
                } else {
                    bukti = getStringImage(bitmap);
                }

                if (uploadBukti.getDrawable() == null){
                    btnCheckout.setEnabled(false);
                } else {
                    Date date = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    String tanggalhariini = simpleDateFormat.format(date);
                    String username = Preferences.getLoggedInUser(getBaseContext());
                    uploadBukti(username, tanggalhariini, bukti);

                    startActivity(new Intent(getApplicationContext(), KeranjangActivity.class));
                }
            }
        });
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void pilihFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                uploadBukti.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    void uploadBukti(String username, String tanggal, String bukti){
        showProgress();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Tiket> call = apiInterface.uploadBukti(username, tanggal, bukti);
        call.enqueue(new Callback<Tiket>() {
            @Override
            public void onResponse(@NonNull Call<Tiket> call, @NonNull Response<Tiket> response) {
                hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    Boolean success = response.body().getSuccess();
                    if (success) {
                        onAddSuccess(response.body().getMessage());
                    } else {
                        onAddError(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Tiket> call, @NonNull Throwable t) {
                hideProgress();
                onAddError(t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.hide();
    }

    @Override
    public void onAddSuccess(String message) {
        Toast.makeText(CheckoutActivity.this, message, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onAddError(String message) {
        Toast.makeText(CheckoutActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
