package com.example.android.travelapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tiket {

    @Expose
    @SerializedName("id") private int id;
    @Expose
    @SerializedName("id_destinasi") private int id_destinasi;
    @Expose
    @SerializedName("destinasi") private String destinasi;
    @Expose
    @SerializedName("tempat") private String tempat;
    @Expose
    @SerializedName("harga") private int harga;
    @Expose
    @SerializedName("jumlah") private int jumlah;
    @Expose
    @SerializedName("total_bayar") private int total;
    @Expose
    @SerializedName("tanggal") private String tanggal;
    @Expose
    @SerializedName("bukti") private String bukti;
    @Expose
    @SerializedName("username") private String username;
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

    public String getDestinasi() {
        return destinasi;
    }

    public void setDestinasi(String destinasi) {
        this.destinasi = destinasi;
    }

    public String getTempat() {
        return tempat;
    }

    public void setTempat(String tempat) {
        this.tempat = tempat;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getBukti() {
        return bukti;
    }

    public void setBukti(String bukti) {
        this.bukti = bukti;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId_destinasi() {
        return id_destinasi;
    }

    public void setId_destinasi(int id_destinasi) {
        this.id_destinasi = id_destinasi;
    }
}
