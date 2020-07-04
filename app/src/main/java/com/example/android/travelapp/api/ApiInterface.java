package com.example.android.travelapp.api;

import com.example.android.travelapp.model.Destinasi;
import com.example.android.travelapp.model.Tiket;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    // fungsi untuk menyimpan data
    @FormUrlEncoded
    @POST("save.php")
    Call<Tiket> saveTiket(
          @Field("destinasi") String destinasi,
          @Field("tempat") String tempat,
          @Field("harga") int harga,
          @Field("jumlah") int jumlah,
          @Field("total_bayar") int total,
          @Field("tanggal") String tanggal
    );

    // fungsi untuk memanggil data dari database
    @GET("tikets.php")
    Call<List<Tiket>> getTikets();

    // fungsi untuk mengedit data
    @FormUrlEncoded
    @POST("update.php")
    Call<Tiket> updateTiket(
            @Field("id") int id,
            @Field("destinasi") String destinasi,
            @Field("tempat") String tempat,
            @Field("harga") int harga,
            @Field("jumlah") int jumlah,
            @Field("total_bayar") int total,
            @Field("tanggal") String tanggal
    );

    @FormUrlEncoded
    @POST("delete.php")
    Call<Tiket> deleteTiket(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("upload_bukti.php")
    Call<Tiket> uploadBukti(
            @Field("id") int id,
            @Field("bukti") String bukti
    );

    @GET("get_destinasi.php")
    Call<List<Destinasi>> getDestinasi(@Query("key") String keyword);
}
