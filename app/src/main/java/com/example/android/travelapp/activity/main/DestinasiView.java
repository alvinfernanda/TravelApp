package com.example.android.travelapp.activity.main;

import java.util.List;
import com.example.android.travelapp.model.Destinasi;

public interface DestinasiView {
    void showLoading();
    void hideLoading();
    void onGetResult(List<Destinasi> destinasis);
    void onErrorLoading(String message);
}
