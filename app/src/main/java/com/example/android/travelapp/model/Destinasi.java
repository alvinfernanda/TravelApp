package com.example.android.travelapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Destinasi {
    @Expose
    @SerializedName("id") private int id;
    @Expose
    @SerializedName("destinasi") private String destinasi;
    @Expose
    @SerializedName("harga") private int harga;
    @Expose
    @SerializedName("lokasi") private String lokasi;
    @Expose
    @SerializedName("detail") private String detail;
    @Expose
    @SerializedName("gambar") private String gambar;
    @Expose
    @SerializedName("success") private Boolean success;
    @Expose
    @SerializedName("message") private String message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaDestinasi() {
        return destinasi;
    }

    public void setNamaDestinasi(String destinasi) {
        this.destinasi = destinasi;
    }

    public int getHargaDestinasi() {
        return harga;
    }

    public void setHargaDestinasi(int harga) {
        this.harga = harga;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
