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
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.travelapp.R;
import com.example.android.travelapp.activity.DetailActivity;
import com.example.android.travelapp.model.Destinasi;

import java.util.ArrayList;
import java.util.List;

public class DestinasiAdapter extends RecyclerView.Adapter<DestinasiAdapter.RecyclerViewAdapter> {

    private Context context;
    private List<Destinasi> destinasis;
    private ItemClickListener itemClickListener;

    public DestinasiAdapter(Context context, List<Destinasi> destinasis, ItemClickListener itemClickListener) {
        this.context = context;
        this.destinasis = destinasis;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item,
                parent, false);
        return new RecyclerViewAdapter(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter holder, int position) {
        Destinasi destinasi = destinasis.get(position);
        holder.tv_destinasi.setText(destinasi.getNamaDestinasi());
        holder.tv_harga.setText("Rp. " + destinasi.getHargaDestinasi());
        holder.tv_lokasi.setText(destinasi.getLokasi());
        Glide.with(context).load(destinasi.getGambar()).into(holder.gambar);
    }

    @Override
    public int getItemCount() {
        return destinasis.size();
    }

    class RecyclerViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_lokasi, tv_destinasi, tv_harga;
        ImageView gambar;
        CardView cardView;
        ItemClickListener itemClickListener;

        RecyclerViewAdapter(@NonNull View itemView, ItemClickListener itemClickListener) {
            super(itemView);

            tv_lokasi = itemView.findViewById(R.id.title);
            tv_destinasi = itemView.findViewById(R.id.destinasi);
            tv_harga = itemView.findViewById(R.id.harga_destinasi);
            gambar = itemView.findViewById(R.id.destinasiImage);
            cardView = itemView.findViewById(R.id.card);

            this.itemClickListener = itemClickListener;
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
