package com.example.android.travelapp;

public class UsersHelper {

    String  nama, password, username, alamat, telepon;

    public UsersHelper(){

    }

    public UsersHelper(String nama, String password, String username, String alamat, String telepon) {
        this.nama = nama;
        this.password = password;
        this.username = username;
        this.alamat = alamat;
        this.telepon = telepon;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }
}
