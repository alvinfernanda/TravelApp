package com.example.android.travelapp.activity.keranjang;

import com.example.android.travelapp.model.Tiket;

import java.util.List;

public interface KeranjangView {
    void showLoading();
    void hideLoading();
    void onGetResult(List<Tiket> tikets);
    void onErrorLoading(String message);
}
