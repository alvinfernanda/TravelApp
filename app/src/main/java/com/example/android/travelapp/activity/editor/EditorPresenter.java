package com.example.android.travelapp.activity.editor;

import android.support.annotation.NonNull;

import com.example.android.travelapp.api.ApiClient;
import com.example.android.travelapp.api.ApiInterface;
import com.example.android.travelapp.model.Tiket;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorPresenter {

    private EditorView view;

    public EditorPresenter(EditorView view) {
        this.view = view;
    }

    void saveTiket(final String destinasi, final String tempat, final int harga, final int jumlah, final int total, final String tanggal) {

        view.showProgress();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Tiket> call = apiInterface.saveTiket(destinasi, tempat, harga, jumlah, total, tanggal);

        call.enqueue(new Callback<Tiket>() {
            @Override
            public void onResponse(Call<Tiket> call, Response<Tiket> response) {
                view.hideProgress();

                if (response.isSuccessful() && response.body() != null) {
                    Boolean success = response.body().getSuccess();
                    if (success) {
                        view.onRequestSuccess(response.body().getMessage());
                    } else {
                        view.onRequestError(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Tiket> call, Throwable t) {
                view.hideProgress();
                view.onRequestError(t.getLocalizedMessage());
            }
        });
    }

    void updateTiket(int id, String destinasi, String tempat, int harga, int jumlah, int total, String tanggal){
        view.showProgress();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Tiket> call = apiInterface.updateTiket(id, destinasi, tempat, harga, jumlah, total, tanggal);
        call.enqueue(new Callback<Tiket>() {
            @Override
            public void onResponse(@NonNull Call<Tiket> call, @NonNull Response<Tiket> response) {
                view.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    Boolean success = response.body().getSuccess();
                    if (success) {
                        view.onRequestSuccess(response.body().getMessage());
                    } else {
                        view.onRequestError(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Tiket> call, @NonNull Throwable t) {
                view.hideProgress();
                view.onRequestError(t.getLocalizedMessage());
            }
        });
    }

    void deleteTiket(int id) {
        view.showProgress();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Tiket> call = apiInterface.deleteTiket(id);
        call.enqueue(new Callback<Tiket>() {
            @Override
            public void onResponse(@NonNull Call<Tiket> call, @NonNull Response<Tiket> response) {
                view.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    Boolean success = response.body().getSuccess();
                    if (success) {
                        view.onRequestSuccess(response.body().getMessage());
                    } else {
                        view.onRequestError(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Tiket> call, @NonNull Throwable t) {
                view.hideProgress();
                view.onRequestError(t.getLocalizedMessage());
            }
        });
    }

    void uploadBukti(int id, String bukti){
        view.showProgress();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Tiket> call = apiInterface.uploadBukti(id, bukti);
        call.enqueue(new Callback<Tiket>() {
            @Override
            public void onResponse(@NonNull Call<Tiket> call, @NonNull Response<Tiket> response) {
                view.hideProgress();
                if (response.isSuccessful() && response.body() != null) {
                    Boolean success = response.body().getSuccess();
                    if (success) {
                        view.onRequestSuccess(response.body().getMessage());
                    } else {
                        view.onRequestError(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Tiket> call, @NonNull Throwable t) {
                view.hideProgress();
                view.onRequestError(t.getLocalizedMessage());
            }
        });
    }
}
