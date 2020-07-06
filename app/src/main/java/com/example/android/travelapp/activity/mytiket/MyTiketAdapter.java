package com.example.android.travelapp.activity.mytiket;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.travelapp.R;
import com.example.android.travelapp.model.Tiket;

import java.util.List;

public class MyTiketAdapter extends RecyclerView.Adapter<MyTiketAdapter.RecyclerViewAdapter> {

    private Context context;
    private List<Tiket> tikets;
    private ItemClickListener itemClickListener;

    public MyTiketAdapter(Context context, List<Tiket> tikets, ItemClickListener itemClickListener) {
        this.context = context;
        this.tikets = tikets;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_mytiket,
                parent, false);
        return new RecyclerViewAdapter(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter holder, int position) {
        Tiket tiket = tikets.get(position);
        holder.tv_destinasi.setText(tiket.getDestinasi());
        holder.tv_tempat.setText(tiket.getTempat());
        holder.tv_jumlah.setText(tiket.getJumlah()+" tiket");
        holder.tv_tanggal.setText(tiket.getTanggal());
    }

    @Override
    public int getItemCount() {
        return tikets.size();
    }

    public class RecyclerViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_destinasi, tv_tempat, tv_jumlah, tv_tanggal;
        CardView card_item;
        ItemClickListener itemClickListener;

        public RecyclerViewAdapter(View itemView, ItemClickListener itemClickListener) {
            super(itemView);

            tv_destinasi = itemView.findViewById(R.id.destinasi);
            tv_tempat = itemView.findViewById(R.id.tempat);
            tv_jumlah = itemView.findViewById(R.id.jumlah);
            tv_tanggal = itemView.findViewById(R.id.tanggal);
            card_item = itemView.findViewById(R.id.card_item);

            this.itemClickListener = itemClickListener;
            card_item.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
