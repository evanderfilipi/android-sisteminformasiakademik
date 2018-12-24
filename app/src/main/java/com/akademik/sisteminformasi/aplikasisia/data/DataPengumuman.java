package com.akademik.sisteminformasi.aplikasisia.data;

/**
 * Created by Evander Filipi on 11/12/2018.
 */

public class DataPengumuman {
    private String id_pengumuman, judul, deskripsi, tanggal;

    public DataPengumuman(){

    }

    public DataPengumuman(String id_pengumuman, String judul, String deskripsi, String tanggal) {
        this.id_pengumuman = id_pengumuman;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.tanggal = tanggal;
    }

    public String getId_pengumuman() {
        return id_pengumuman;
    }

    public void setId_pengumuman(String id_pengumuman) {
        this.id_pengumuman = id_pengumuman;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
