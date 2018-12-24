package com.akademik.sisteminformasi.aplikasisia.data;

/**
 * Created by Evander Filipi on 14/11/2018.
 */

public class DataAbsen {
    private String id_absen, kode_mapel, mapel, kode_kelas, sakit, izin, tanpa_keterangan, total_pertemuan, semester, tahun_ajaran, nis, nama_siswa, nip, nama_guru;

    public  DataAbsen(){
    }

    public DataAbsen(String id_absen, String kode_mapel, String mapel, String kode_kelas, String sakit, String izin, String tanpa_keterangan, String total_pertemuan, String semester, String tahun_ajaran, String nis, String nama_siswa, String nip, String nama_guru) {
        this.id_absen = id_absen;
        this.kode_mapel = kode_mapel;
        this.mapel = mapel;
        this.kode_kelas = kode_kelas;
        this.sakit = sakit;
        this.izin = izin;
        this.tanpa_keterangan = tanpa_keterangan;
        this.total_pertemuan = total_pertemuan;
        this.semester = semester;
        this.tahun_ajaran = tahun_ajaran;
        this.nis = nis;
        this.nama_siswa = nama_siswa;
        this.nip = nip;
        this.nama_guru = nama_guru;
    }

    public String getId_absen() {
        return id_absen;
    }

    public void setId_absen(String id_absen) {
        this.id_absen = id_absen;
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

    public String getKode_kelas() {
        return kode_kelas;
    }

    public void setKode_kelas(String kode_kelas) {
        this.kode_kelas = kode_kelas;
    }

    public String getSakit() {
        return sakit;
    }

    public void setSakit(String sakit) {
        this.sakit = sakit;
    }

    public String getIzin() {
        return izin;
    }

    public void setIzin(String izin) {
        this.izin = izin;
    }

    public String getTanpa_keterangan() {
        return tanpa_keterangan;
    }

    public void setTanpa_keterangan(String tanpa_keterangan) {
        this.tanpa_keterangan = tanpa_keterangan;
    }

    public String getTotal_pertemuan() {
        return total_pertemuan;
    }

    public void setTotal_pertemuan(String total_pertemuan) {
        this.total_pertemuan = total_pertemuan;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getTahun_ajaran() {
        return tahun_ajaran;
    }

    public void setTahun_ajaran(String tahun_ajaran) {
        this.tahun_ajaran = tahun_ajaran;
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
