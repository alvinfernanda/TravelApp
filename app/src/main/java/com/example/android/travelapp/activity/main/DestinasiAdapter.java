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

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.travelapp.R;
import com.example.android.travelapp.activity.DetailActivity;

import java.util.ArrayList;

class DestinasiAdapter extends RecyclerView.Adapter<DestinasiAdapter.ViewHolder> {

    // Member variables.
    private ArrayList<Destinasi> mDestinasiData;
    private Context mContext;


    DestinasiAdapter(Context context, ArrayList<Destinasi> destinasiData) {
        this.mDestinasiData = destinasiData;
        this.mContext = context;
    }

    @Override
    public DestinasiAdapter.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.list_item, parent, false));
    }


    @Override
    public void onBindViewHolder(DestinasiAdapter.ViewHolder holder,
                                 int position) {
        // Get current sport.
        Destinasi currentDestinasi = mDestinasiData.get(position);

        // Populate the textviews with data.
        holder.bindTo(currentDestinasi);
    }

    @Override
    public int getItemCount() {
        return mDestinasiData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Member Variables for the TextViews
        private TextView mTitleText;
        private TextView mDestinasiText;
        private TextView mHarga;
        private ImageView mDestinasiImage;

        ViewHolder(View itemView) {
            super(itemView);

            // Initialize the views.
            mDestinasiText = itemView.findViewById(R.id.destinasi);
            mTitleText = itemView.findViewById(R.id.title);
            mHarga = itemView.findViewById(R.id.harga);
            mDestinasiImage = itemView.findViewById(R.id.destinasiImage);

            itemView.setOnClickListener(this);
        }

        void bindTo(Destinasi currentDestinasi){
            // Populate the textviews with data.
            mTitleText.setText(currentDestinasi.getTitle());
            mDestinasiText.setText(currentDestinasi.getInfo());
            mHarga.setText("Rp. " + currentDestinasi.getHarga());

            Glide.with(mContext).load(currentDestinasi.getImageResource()).into(mDestinasiImage);

        }

        @Override
        public void onClick(View view) {
            Destinasi currentDestinasi = mDestinasiData.get(getAdapterPosition());

            Intent detailIntent = new Intent(mContext, DetailActivity.class);
            detailIntent.putExtra("title", currentDestinasi.getTitle());
            detailIntent.putExtra("destinasi", currentDestinasi.getInfo());
            detailIntent.putExtra("harga", currentDestinasi.getHarga());
            detailIntent.putExtra("image_resource",
                    currentDestinasi.getImageResource());
            mContext.startActivity(detailIntent);
        }
    }
}
