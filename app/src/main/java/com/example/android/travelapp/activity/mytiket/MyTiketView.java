package com.example.android.travelapp.activity.mytiket;

import com.example.android.travelapp.model.Tiket;

import java.util.List;

public interface MyTiketView {
    void showLoading();
    void hideLoading();
    void onGetResult(List<Tiket> tikets);
    void onErrorLoading(String message);
}
