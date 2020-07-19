package com.example.android.travelapp.activity.tiket;

import android.widget.Toast;

import com.example.android.travelapp.api.ApiClient;
import com.example.android.travelapp.api.ApiInterface;
import com.example.android.travelapp.model.Tiket;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TiketPresenter {

    private TiketView view;

    public TiketPresenter(TiketView view) {
        this.view = view;
    }

    void saveTiket(final int id_destinasi, final int jumlah, final int total, final String tanggal, final String username) {

        view.showProgress();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Tiket> call = apiInterface.saveTiket(id_destinasi, jumlah, total, tanggal, username);

        call.enqueue(new Callback<Tiket>() {
            @Override
            public void onResponse(Call<Tiket> call, Response<Tiket> response) {
                view.hideProgress();

                if (response.isSuccessful() && response.body() != null) {
                    Boolean success = response.body().getSuccess();
                    if (success) {
                        view.onAddSuccess(response.body().getMessage());
                    } else {
                        view.onAddError(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Tiket> call, Throwable t) {
                view.hideProgress();
                view.onAddError(t.getLocalizedMessage());
            }
        });
    }

}
