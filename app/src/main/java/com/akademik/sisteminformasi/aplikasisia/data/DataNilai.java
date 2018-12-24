package com.akademik.sisteminformasi.aplikasisia.data;

/**
 * Created by Evander Filipi on 30/10/2018.
 */

public class DataNilai {
    private String id_nilai, kode_mapel, mapel, nilai_uh, nilai_tgs, nilai_uts, nilai_uas, nilai_akhir, semester, tahun_ajaran, nis, nama_siswa, nip, nama_guru;

    public DataNilai(){
    }

    public DataNilai(String id_nilai, String kode_mapel, String mapel, String nilai_uh, String nilai_tgs, String nilai_uts, String nilai_uas, String nilai_akhir, String semester, String tahun_ajaran, String nis, String nama_siswa, String nip, String nama_guru) {
        this.id_nilai = id_nilai;
        this.kode_mapel = kode_mapel;
        this.mapel = mapel;
        this.nilai_uh = nilai_uh;
        this.nilai_tgs = nilai_tgs;
        this.nilai_uts = nilai_uts;
        this.nilai_uas = nilai_uas;
        this.nilai_akhir = nilai_akhir;
        this.semester = semester;
        this.tahun_ajaran = tahun_ajaran;
        this.nis = nis;
        this.nama_siswa = nama_siswa;
        this.nip = nip;
        this.nama_guru = nama_guru;
    }

    public String getId_nilai() {
        return id_nilai;
    }

    public void setId_nilai(String id_nilai) {
        this.id_nilai = id_nilai;
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

    public String getNilai_uh() {
        return nilai_uh;
    }

    public void setNilai_uh(String nilai_uh) {
        this.nilai_uh = nilai_uh;
    }

    public String getNilai_tgs() {
        return nilai_tgs;
    }

    public void setNilai_tgs(String nilai_tgs) {
        this.nilai_tgs = nilai_tgs;
    }

    public String getNilai_uts() {
        return nilai_uts;
    }

    public void setNilai_uts(String nilai_uts) {
        this.nilai_uts = nilai_uts;
    }

    public String getNilai_uas() {
        return nilai_uas;
    }

    public void setNilai_uas(String nilai_uas) {
        this.nilai_uas = nilai_uas;
    }

    public String getNilai_akhir() {
        return nilai_akhir;
    }

    public void setNilai_akhir(String nilai_akhir) {
        this.nilai_akhir = nilai_akhir;
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
