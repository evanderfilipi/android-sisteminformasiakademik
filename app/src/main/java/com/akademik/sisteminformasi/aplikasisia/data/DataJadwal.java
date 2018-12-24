package com.akademik.sisteminformasi.aplikasisia.data;

/**
 * Created by Evander Filipi on 12/11/2018.
 */

public class DataJadwal {
    private String id_jadwal, kode_kelas, kode_mapel, mapel, hari, jam_mulai, jam_selesai, tahun_ajaran, jenis_jadwal, nis, nama_siswa, nip, nama_guru;

    public  DataJadwal(){
    }

    public DataJadwal(String id_jadwal, String kode_kelas, String kode_mapel, String mapel, String hari, String jam_mulai, String jam_selesai, String tahun_ajaran, String jenis_jadwal, String nis, String nama_siswa, String nip, String nama_guru) {
        this.id_jadwal = id_jadwal;
        this.kode_kelas = kode_kelas;
        this.kode_mapel = kode_mapel;
        this.mapel = mapel;
        this.hari = hari;
        this.jam_mulai = jam_mulai;
        this.jam_selesai = jam_selesai;
        this.tahun_ajaran = tahun_ajaran;
        this.jenis_jadwal = jenis_jadwal;
        this.nis = nis;
        this.nama_siswa = nama_siswa;
        this.nip = nip;
        this.nama_guru = nama_guru;
    }

    public String getId_jadwal() {
        return id_jadwal;
    }

    public void setId_jadwal(String id_jadwal) {
        this.id_jadwal = id_jadwal;
    }

    public String getKode_kelas() {
        return kode_kelas;
    }

    public void setKode_kelas(String kode_kelas) {
        this.kode_kelas = kode_kelas;
    }

    public String getKode_mapel() {
        return kode_mapel;
    }

    public void setKode_mapel(String kode_mapel) {
        this.kode_mapel = kode_mapel;
    }

    public String getMapel() {
        return mapel;
    }

    public void setMapel(String mapel) {
        this.mapel = mapel;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public String getJam_mulai() {
        return jam_mulai;
    }

    public void setJam_mulai(String jam_mulai) {
        this.jam_mulai = jam_mulai;
    }

    public String getJam_selesai() {
        return jam_selesai;
    }

    public void setJam_selesai(String jam_selesai) {
        this.jam_selesai = jam_selesai;
    }

    public String getTahun_ajaran() {
        return tahun_ajaran;
    }

    public void setTahun_ajaran(String tahun_ajaran) {
        this.tahun_ajaran = tahun_ajaran;
    }

    public String getJenis_jadwal() {
        return jenis_jadwal;
    }

    public void setJenis_jadwal(String jenis_jadwal) {
        this.jenis_jadwal = jenis_jadwal;
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

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getNama_guru() {
        return nama_guru;
    }

    public void setNama_guru(String nama_guru) {
        this.nama_guru = nama_guru;
    }
}
