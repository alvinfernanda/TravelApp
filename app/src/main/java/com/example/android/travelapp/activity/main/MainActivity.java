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

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.travelapp.R;
import com.example.android.travelapp.activity.ProfilActivity;
import com.example.android.travelapp.activity.keranjang.KeranjangActivity;

import java.util.ArrayList;

/***
 * Main Activity for the Material Me app, a mock sports news application
 * with poor design choices.
 */
public class MainActivity extends AppCompatActivity {

    // Member variables.
    private RecyclerView mRecyclerView;
    private ArrayList<Destinasi> mDestinasiData;
    private DestinasiAdapter mAdapter;

    private static boolean isNightMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerView);

        int gridColumnCount = getResources().getInteger(R.integer.grid_column_count);

        // Set the Layout Manager.
        mRecyclerView.setLayoutManager(
                new GridLayoutManager(this, gridColumnCount));

        // Initialize the ArrayList that will contain the data.
        mDestinasiData = new ArrayList<>();

        // Initialize the adapter and set it to the RecyclerView.
        mAdapter = new DestinasiAdapter(this, mDestinasiData);
        mRecyclerView.setAdapter(mAdapter);

        // Get the data.
        initializeData();

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
                    case R.id.tiket:
                        startActivity(new Intent(getApplicationContext(), KeranjangActivity.class));
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

    /**
     * Initialize the sports data from resources.
     */
    private void initializeData() {
        TypedArray destinasiImageResources =
                getResources().obtainTypedArray(R.array.destinasi_images);
        // Get the resources from the XML file.
        String[] destinasiList = getResources()
                .getStringArray(R.array.destinasi_titles);
        String[] destinasiInfo = getResources()
                .getStringArray(R.array.destinasi_info);
        String[] destinasiHarga = getResources()
                .getStringArray(R.array.destinasi_harga);

        // Clear the existing data (to avoid duplication).
        mDestinasiData.clear();

        // Create the ArrayList of Sports objects with titles and
        // information about each sport.
        for(int i=0;i<destinasiList.length;i++){
            mDestinasiData.add(new Destinasi(destinasiList[i],destinasiInfo[i],destinasiHarga[i],
                    destinasiImageResources.getResourceId(i,0)));
        }

        destinasiImageResources.recycle();

        // Notify the adapter of the change.
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        if(nightMode == AppCompatDelegate.MODE_NIGHT_YES){
            menu.findItem(R.id.night_mode).setTitle(R.string.day_mode);
        } else{
            menu.findItem(R.id.night_mode).setTitle(R.string.night_mode);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.night_mode) {
            // Get the night mode state of the app.
            int nightMode = AppCompatDelegate.getDefaultNightMode();
            //Set the theme mode for the restarted activity
            if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode
                        (AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode
                        (AppCompatDelegate.MODE_NIGHT_YES);
            }
            // Recreate the activity for the theme change to take effect.
            recreate();
        }

        return true;
    }

}
