package com.informatika.indradwiprabowo.myexpensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TransaksiAdapter.OnItemActionClickListener {

    private RecyclerView recyclerView;
    private TransaksiAdapter adapter;
    private List<Transaksi> daftarTransaksi;
    private TextView textViewTotalPengeluaran;
    private TextView textViewEmpty;
    private FloatingActionButton fabTambah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view_transaksi);
        textViewTotalPengeluaran = findViewById(R.id.text_view_total_pengeluaran);
        textViewEmpty = findViewById(R.id.text_view_empty);
        fabTambah = findViewById(R.id.fab_tambah);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        daftarTransaksi = new ArrayList<>();
        adapter = new TransaksiAdapter(this, daftarTransaksi, this);
        recyclerView.setAdapter(adapter);

        fabTambah.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TambahEditActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataAndUpdateUI();
    }

    private void loadDataAndUpdateUI() {
        daftarTransaksi = DataManager.loadTransactions(this);
        adapter.updateData(daftarTransaksi);

        if (daftarTransaksi.isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textViewEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        updateTotal();
    }

    private void updateTotal() {
        double total = 0;
        for (Transaksi transaksi : daftarTransaksi) {
            total += transaksi.getNominal();
        }

        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
        textViewTotalPengeluaran.setText(formatRupiah.format(total));
    }

    @Override
    public void onEditClick(int position) {
        Transaksi transaksi = daftarTransaksi.get(position);
        Intent intent = new Intent(this, TambahEditActivity.class);
        intent.putExtra(TambahEditActivity.EXTRA_TRANSACTION_ID, transaksi.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Transaksi")
                .setMessage("Anda yakin ingin menghapus transaksi ini?")
                .setPositiveButton("Hapus", (dialog, which) -> {
                    daftarTransaksi.remove(position);
                    DataManager.saveTransactions(this, daftarTransaksi);
                    loadDataAndUpdateUI();
                })
                .setNegativeButton("Batal", null)
                .show();
    }
}