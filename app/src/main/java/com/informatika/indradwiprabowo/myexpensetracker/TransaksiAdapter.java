package com.informatika.indradwiprabowo.myexpensetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class TransaksiAdapter extends RecyclerView.Adapter<TransaksiAdapter.ViewHolder> {

    private List<Transaksi> daftarTransaksi;
    private Context context;
    private OnItemActionClickListener listener;

    public interface OnItemActionClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public TransaksiAdapter(Context context, List<Transaksi> daftarTransaksi, OnItemActionClickListener listener) {
        this.context = context;
        this.daftarTransaksi = daftarTransaksi;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_transaksi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaksi transaksi = daftarTransaksi.get(position);

        holder.namaTransaksi.setText(transaksi.getNamaTransaksi());
        holder.kategori.setText(transaksi.getKategori());
        holder.tanggal.setText(transaksi.getTanggal());

        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
        holder.nominal.setText(formatRupiah.format(transaksi.getNominal()));

        holder.editButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(holder.getAdapterPosition());
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return daftarTransaksi.size();
    }

    public void updateData(List<Transaksi> newList) {
        this.daftarTransaksi.clear();
        this.daftarTransaksi.addAll(newList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView namaTransaksi, kategori, tanggal, nominal;
        ImageButton editButton, deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            namaTransaksi = itemView.findViewById(R.id.text_view_nama_transaksi);
            kategori = itemView.findViewById(R.id.text_view_kategori);
            tanggal = itemView.findViewById(R.id.text_view_tanggal);
            nominal = itemView.findViewById(R.id.text_view_nominal);
            editButton = itemView.findViewById(R.id.button_edit);
            deleteButton = itemView.findViewById(R.id.button_delete);
        }
    }
}
