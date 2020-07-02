package com.example.android.travelapp.activity.tiket;

public interface TiketView {

    void showProgress();
    void hideProgress();
    void onAddSuccess(String message);
    void onAddError(String message);
}
