package com.akademik.sisteminformasi.aplikasisia.data;

/**
 * Created by Evander Filipi on 06/11/2018.
 */

public class DataSiswa {
    private String nis, nama_siswa, kode_kelas, kelas, sub_kelas, foto;

    public DataSiswa() {
    }

    public DataSiswa(String nis, String nama_siswa, String kode_kelas, String kelas, String sub_kelas, String foto) {
        this.nis = nis;
        this.nama_siswa = nama_siswa;
        this.kode_kelas = kode_kelas;
        this.kelas = kelas;
        this.sub_kelas = sub_kelas;
        this.foto = foto;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getNama_siswa() {
        return nama_siswa;
    }

    public void setNama_siswa(String nama_siswa) {
        this.nama_siswa = nama_siswa;
    }

    public String getKode_kelas() {
        return kode_kelas;
    }

    public void setKode_kelas(String kode_kelas) {
        this.kode_kelas = kode_kelas;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getSub_kelas() {
        return sub_kelas;
    }

    public void setSub_kelas(String sub_kelas) {
        this.sub_kelas = sub_kelas;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
