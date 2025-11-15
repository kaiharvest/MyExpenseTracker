package com.informatika.indradwiprabowo.myexpensetracker;

import java.util.UUID;

public class Transaksi {
    private String id;
    private String namaTransaksi;
    private double nominal;
    private String kategori;
    private String tanggal;

    public Transaksi(String namaTransaksi, double nominal, String kategori, String tanggal) {
        this.id = UUID.randomUUID().toString();
        this.namaTransaksi = namaTransaksi;
        this.nominal = nominal;
        this.kategori = kategori;
        this.tanggal = tanggal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaTransaksi() {
        return namaTransaksi;
    }

    public void setNamaTransaksi(String namaTransaksi) {
        this.namaTransaksi = namaTransaksi;
    }

    public double getNominal() {
        return nominal;
    }

    public void setNominal(double nominal) {
        this.nominal = nominal;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
