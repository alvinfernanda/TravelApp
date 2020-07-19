/*
 * Copyright (C) 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.travelapp.activity.main;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.travelapp.Preferences;
import com.example.android.travelapp.activity.keranjang.KeranjangAdapter;
import com.example.android.travelapp.activity.keranjang.KeranjangPresenter;
import com.example.android.travelapp.activity.keranjang.KeranjangView;
import com.example.android.travelapp.activity.mytiket.MyTiketActivity;
import com.example.android.travelapp.R;
import com.example.android.travelapp.DetailActivity;
import com.example.android.travelapp.ProfilActivity;
import com.example.android.travelapp.activity.keranjang.KeranjangActivity;
import com.example.android.travelapp.model.Destinasi;
import com.example.android.travelapp.model.Tiket;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DestinasiView{

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;

    DestinasiPresenter presenter;
    DestinasiAdapter adapter;
    DestinasiAdapter.ItemClickListener itemClickListener;
    List<Destinasi> destinasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefresh = findViewById(R.id.swipe_refresh);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        presenter = new DestinasiPresenter(this);
        presenter.getData("");

        swipeRefresh.setOnRefreshListener(
                () -> presenter.getData("")
        );

        itemClickListener = ((view, position) -> {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("id", destinasi.get(position).getId());
            intent.putExtra("destinasi", destinasi.get(position).getNamaDestinasi());
            intent.putExtra("harga", destinasi.get(position).getHargaDestinasi());
            intent.putExtra("lokasi", destinasi.get(position).getLokasi());
            intent.putExtra("detail", destinasi.get(position).getDetail());
            intent.putExtra("gambar", destinasi.get(position).getGambar());
            startActivity(intent);
        });

        // bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        // membuat highlight icon saat diklik
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        break;
                    case R.id.keranjang:
                        Intent intent = new Intent(getApplicationContext(), KeranjangActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
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
    public void onGetResult(List<Destinasi> destinasis) {
        adapter = new DestinasiAdapter(this, destinasis, itemClickListener);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        destinasi = destinasis;
    }

    @Override
    public void onErrorLoading(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.getData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.getData(newText);
                return false;
            }
        });
        return true;
    }
}
