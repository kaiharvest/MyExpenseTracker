package com.informatika.indradwiprabowo.myexpensetracker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.List;

public class TambahEditActivity extends AppCompatActivity {

    public static final String EXTRA_TRANSACTION_ID = "extra_transaction_id";

    private EditText editTextNamaTransaksi, editTextNominal, editTextTanggal;
    private Spinner spinnerKategori;
    private Button buttonSimpan;
    private TextView textViewTitle;

    private boolean isEditMode = false;
    private String transactionId;
    private List<Transaksi> daftarTransaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_edit);

        editTextNamaTransaksi = findViewById(R.id.edit_text_nama_transaksi);
        editTextNominal = findViewById(R.id.edit_text_nominal);
        spinnerKategori = findViewById(R.id.spinner_kategori);
        editTextTanggal = findViewById(R.id.edit_text_tanggal);
        buttonSimpan = findViewById(R.id.button_simpan);
        textViewTitle = findViewById(R.id.text_view_title);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.kategori_pengeluaran, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKategori.setAdapter(adapter);

        editTextTanggal.setOnClickListener(v -> showDatePickerDialog());

        daftarTransaksi = DataManager.loadTransactions(this);

        if (getIntent().hasExtra(EXTRA_TRANSACTION_ID)) {
            isEditMode = true;
            transactionId = getIntent().getStringExtra(EXTRA_TRANSACTION_ID);
            textViewTitle.setText("Edit Pengeluaran");
            loadTransactionData();
        } else {
            textViewTitle.setText("Tambah Pengeluaran Baru");
        }

        buttonSimpan.setOnClickListener(v -> saveData());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    editTextTanggal.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void loadTransactionData() {
        for (Transaksi transaksi : daftarTransaksi) {
            if (transaksi.getId().equals(transactionId)) {
                editTextNamaTransaksi.setText(transaksi.getNamaTransaksi());
                editTextNominal.setText(String.valueOf(transaksi.getNominal()));
                editTextTanggal.setText(transaksi.getTanggal());
                // Set spinner selection
                ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerKategori.getAdapter();
                int spinnerPosition = adapter.getPosition(transaksi.getKategori());
                spinnerKategori.setSelection(spinnerPosition);
                break;
            }
        }
    }

    private void saveData() {
        String nama = editTextNamaTransaksi.getText().toString().trim();
        String nominalStr = editTextNominal.getText().toString().trim();
        String tanggal = editTextTanggal.getText().toString().trim();
        String kategori = spinnerKategori.getSelectedItem().toString();

        if (nama.isEmpty() || nominalStr.isEmpty() || tanggal.isEmpty()) {
            Toast.makeText(this, "Harap isi semua field", Toast.LENGTH_SHORT).show();
            return;
        }

        double nominal;
        try {
            nominal = Double.parseDouble(nominalStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Nominal yang dimasukkan tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isEditMode) {
            for (int i = 0; i < daftarTransaksi.size(); i++) {
                if (daftarTransaksi.get(i).getId().equals(transactionId)) {
                    Transaksi transaksiToUpdate = daftarTransaksi.get(i);
                    transaksiToUpdate.setNamaTransaksi(nama);
                    transaksiToUpdate.setNominal(nominal);
                    transaksiToUpdate.setKategori(kategori);
                    transaksiToUpdate.setTanggal(tanggal);
                    break;
                }
            }
        } else {
            daftarTransaksi.add(new Transaksi(nama, nominal, kategori, tanggal));
        }

        DataManager.saveTransactions(this, daftarTransaksi);
        Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
        finish();
    }
}
