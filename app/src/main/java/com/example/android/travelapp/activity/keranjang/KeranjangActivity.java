package com.example.android.travelapp.activity.keranjang;

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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.travelapp.CheckoutActivity;
import com.example.android.travelapp.Preferences;
import com.example.android.travelapp.activity.mytiket.MyTiketActivity;
import com.example.android.travelapp.R;
import com.example.android.travelapp.ProfilActivity;
import com.example.android.travelapp.activity.editor.EditorActivity;
import com.example.android.travelapp.activity.main.MainActivity;
import com.example.android.travelapp.model.Tiket;

import java.util.List;

public class KeranjangActivity extends AppCompatActivity implements KeranjangView {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;
    Button btnCheckout;

    KeranjangPresenter presenter;
    KeranjangAdapter adapter;
    KeranjangAdapter.ItemClickListener itemClickListener;
    List<Tiket> tiket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        swipeRefresh = findViewById(R.id.swipe);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnCheckout = findViewById(R.id.btn_checkout);

        String usernamelocal = Preferences.getLoggedInUser(getBaseContext());

        presenter = new KeranjangPresenter(this);
        presenter.getData(usernamelocal);

        swipeRefresh.setOnRefreshListener(
                () -> presenter.getData(usernamelocal)
        );

        itemClickListener = ((view, position) -> {
            Intent intent = new Intent(this, EditorActivity.class);
            intent.putExtra("id", tiket.get(position).getId());
            intent.putExtra("id_destinasi", tiket.get(position).getId_destinasi());
            intent.putExtra("destinasi", tiket.get(position).getDestinasi());
            intent.putExtra("tempat", tiket.get(position).getTempat());
            intent.putExtra("harga", tiket.get(position).getHarga());
            intent.putExtra("jumlah", tiket.get(position).getJumlah());
            intent.putExtra("total", tiket.get(position).getTotal());
            intent.putExtra("tanggal", tiket.get(position).getTanggal());
            intent.putExtra("bukti", tiket.get(position).getBukti());
            startActivity(intent);
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CheckoutActivity.class));
            }
        });

        // bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.keranjang);
        // membuat highlight icon saat diklik
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
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
                        break;
                    case R.id.tiket:
                        startActivity(new Intent(getApplicationContext(), MyTiketActivity.class));
                        overridePendingTransition(0,0);
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
        adapter = new KeranjangAdapter(this, tikets, itemClickListener);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        tiket = tikets;

        if (tiket.size() == 0){
            btnCheckout.setVisibility(View.GONE);
        }

    }

    @Override
    public void onErrorLoading(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
