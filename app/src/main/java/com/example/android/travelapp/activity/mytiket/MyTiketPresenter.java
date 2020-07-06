package com.example.android.travelapp.activity.mytiket;

import android.support.annotation.NonNull;

import com.example.android.travelapp.api.ApiClient;
import com.example.android.travelapp.api.ApiInterface;
import com.example.android.travelapp.model.Tiket;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTiketPresenter {
    private MyTiketView view;

    public MyTiketPresenter(MyTiketView view) {
        this.view = view;
    }

    void getData() {
        view.showLoading();

        //request to server
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Tiket>> call = apiInterface.getMyTikets();
        call.enqueue(new Callback<List<Tiket>>() {
            @Override
            public void onResponse(@NonNull Call<List<Tiket>> call, @NonNull Response<List<Tiket>> response) {
                view.hideLoading();
                if (response.isSuccessful() && response.body() != null){
                    view.onGetResult(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Tiket>> call, @NonNull Throwable t) {
                view.hideLoading();
                view.onErrorLoading(t.getLocalizedMessage());
            }
        });

    }
}
