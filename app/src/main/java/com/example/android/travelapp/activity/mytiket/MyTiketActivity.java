package com.example.android.travelapp.activity.mytiket;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.travelapp.DetailTiket;
import com.example.android.travelapp.Preferences;
import com.example.android.travelapp.R;
import com.example.android.travelapp.ProfilActivity;
import com.example.android.travelapp.activity.editor.EditorActivity;
import com.example.android.travelapp.activity.keranjang.KeranjangActivity;
import com.example.android.travelapp.activity.main.MainActivity;
import com.example.android.travelapp.model.Tiket;

import java.util.List;

public class MyTiketActivity extends AppCompatActivity implements MyTiketView {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;
    MyTiketAdapter adapter;
    MyTiketPresenter presenter;
    MyTiketAdapter.ItemClickListener itemClickListener;
    List<Tiket> tiket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tiket);

        swipeRefresh = findViewById(R.id.swipe_refresh);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String usernamelocal = Preferences.getLoggedInUser(getBaseContext());

        presenter = new MyTiketPresenter(this);
        presenter.getData(usernamelocal);

        swipeRefresh.setOnRefreshListener(
                () -> presenter.getData(usernamelocal)
        );

        itemClickListener = ((view, position) -> {
            int id = tiket.get(position).getId();
            String destinasi = tiket.get(position).getDestinasi();
            String tempat = tiket.get(position).getTempat();
            int harga = tiket.get(position).getHarga();
            int jumlah = tiket.get(position).getJumlah();
            int total = tiket.get(position).getTotal();
            String tanggal = tiket.get(position).getTanggal();

            Intent intent = new Intent(this, DetailTiket.class);
            intent.putExtra("id", id);
            intent.putExtra("destinasi", destinasi);
            intent.putExtra("tempat", tempat);
            intent.putExtra("harga", harga);
            intent.putExtra("jumlah", jumlah);
            intent.putExtra("total", total);
            intent.putExtra("tanggal", tanggal);
            intent.putExtra("bukti", tiket.get(position).getBukti());
            startActivity(intent);
        });

        // bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        // membuat highlight icon saat diklik
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.keranjang:
                        startActivity(new Intent(getApplicationContext(), KeranjangActivity.class));
                        overridePendingTransition(0,0);
                        break;
                    case R.id.tiket:
                        break;
                    case R.id.akun:
                        startActivity(new Intent(getApplicationContext(), ProfilActivity.class));
                        overridePendingTransition(0,0);
                        break;
                }
                return false;
            }
        });
    }


    @Override
    public void showLoading() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onGetResult(List<Tiket> tikets) {
        adapter = new MyTiketAdapter(this, tikets, itemClickListener);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        tiket = tikets;
    }

    @Override
    public void onErrorLoading(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
