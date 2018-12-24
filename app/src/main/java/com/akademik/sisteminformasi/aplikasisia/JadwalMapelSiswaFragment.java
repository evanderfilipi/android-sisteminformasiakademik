package com.akademik.sisteminformasi.aplikasisia;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akademik.sisteminformasi.aplikasisia.adapter.JadwalSiswaAdapter;
import com.akademik.sisteminformasi.aplikasisia.data.DataJadwal;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evander Filipi on 25/11/2018.
 */

public class JadwalMapelSiswaFragment extends Fragment {
    LinearLayout tampil_linlay;
    Spinner jenisJadwalSpn, hariSpn;
    EditText tahunAjaranEdt;
    Button tampilBtn;
    RecyclerView recyclerView;
    LinearLayoutManager llm;
    JadwalSiswaAdapter jadwalSiswaAdapter;
    List<DataJadwal> jadwalArr = new ArrayList<DataJadwal>();
    ProgressDialog pDialog;

    private static final String TAG = AbsensiSiswaFragment.class.getSimpleName();

    public static final String PUT_NIS = "nis";
    public static final String PUT_NAMA_SISWA = "nama_siswa";

    public static final String TAG_ID_JADWAL = "id_jadwal";
    public static final String TAG_KODE_MAPEL = "kd_mpl";
    public static final String TAG_NAMA_MAPEL = "nama_mapel";
    public static final String TAG_JAM_MULAI = "jam_mulai";
    public static final String TAG_JAM_SELESAI = "jam_selesai";
    public static final String TAG_NIP = "id_guru";
    public static final String TAG_NAMA_GURU = "nm_guru";

    String nis, nama_siswa, hari, jenis_jadwal;
    String thn_ajaran = "";
    private String[] opsiHari = {"-Pilih-", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"};
    private String[] jenisJadwal = {"-Pilih-", "Harian", "UTS", "UAS", "UN", "Lainnya"};

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_jadwal_mapel_siswa, container, false);
        getActivity().setTitle("Jadwal Pelajaran");
        if (getArguments()!=null){
            nis = getArguments().getString(PUT_NIS);
            nama_siswa = getArguments().getString(PUT_NAMA_SISWA);
        }
        tampil_linlay = (LinearLayout)v.findViewById(R.id.isiJadwalLinlay);
        tampil_linlay.setVisibility(View.INVISIBLE);
        jenisJadwalSpn = (Spinner)v.findViewById(R.id.jenisJadwalSiswaSpn);
        hariSpn = (Spinner)v.findViewById(R.id.hariJadwalSiswaSpn);
        final ArrayAdapter<String> adapterSpin = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, jenisJadwal);
        final ArrayAdapter<String> adapterSpin2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, opsiHari);
        jenisJadwalSpn.setAdapter(adapterSpin);
        hariSpn.setAdapter(adapterSpin2);
        jenisJadwalSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterSpin.getItem(i).equalsIgnoreCase("-Pilih-")){
                    jenis_jadwal = "";
                }
                else {
                    jenis_jadwal = adapterSpin.getItem(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                jenis_jadwal = "";
            }
        });
        hariSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterSpin2.getItem(i).equalsIgnoreCase("-Pilih-")){
                    hari = "";
                }
                else {
                    hari = adapterSpin2.getItem(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                hari = "";
            }
        });
        tahunAjaranEdt = (EditText)v.findViewById(R.id.tahunAjaranSiswa3Edt);
        tampilBtn = (Button)v.findViewById(R.id.tampilkanJadwalBtn);
        tampilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thn_ajaran = tahunAjaranEdt.getText().toString();
                if(thn_ajaran.equalsIgnoreCase("") || jenis_jadwal.equalsIgnoreCase("") || hari.equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Pilih Jenis Jadwal, Hari dan isi kolom Tahun Ajaran terlebih dahulu!", Toast.LENGTH_LONG).show();
                } else {
                    tampilkan(nis, hari, jenis_jadwal, thn_ajaran);
                }
            }
        });
        recyclerView = v.findViewById(R.id.recyclerJadwal);
        llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(llm);

        return v;
    }

    private void tampilkan(String aa, String bb, String cc, String dd){
        String url_tampiljadwal = ServerData.URL + "tampil_jadwalpelajaran.php?nis="+aa+"&hari="+bb+"&jenis_jadwal="+cc+"&tahun_ajaran="+dd;
        pDialog= new ProgressDialog(getActivity());
        pDialog.setMessage("Mencari data...");
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        showDialog();

        jadwalArr.clear();
        JsonArrayRequest jArr = new JsonArrayRequest(url_tampiljadwal, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                if(response.length() <= 0){
                    tampil_linlay.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(), "Data jadwal pelajaran siswa tidak ditemukan. Silahkan coba lagi!", Toast.LENGTH_LONG).show();
                } else {
                    tampil_linlay.setVisibility(View.VISIBLE);

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            DataJadwal item = new DataJadwal();

                            item.setId_jadwal(obj.getString(TAG_ID_JADWAL));
                            item.setKode_mapel(obj.getString(TAG_KODE_MAPEL));
                            item.setMapel(obj.getString(TAG_NAMA_MAPEL));
                            item.setJam_mulai(obj.getString(TAG_JAM_MULAI));
                            item.setJam_selesai(obj.getString(TAG_JAM_SELESAI));
                            item.setNip(obj.getString(TAG_NIP));
                            item.setNama_guru(obj.getString(TAG_NAMA_GURU));

                            jadwalArr.add(item);
                            jadwalSiswaAdapter = new JadwalSiswaAdapter(getActivity(), jadwalArr);
                            recyclerView.setAdapter(jadwalSiswaAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                hideDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                tampil_linlay.setVisibility(View.INVISIBLE);
                hideDialog();
                Toast.makeText(getActivity(), "Tidak dapat mengambil data. Pastikan koneksi internet anda aktif!", Toast.LENGTH_LONG).show();
            }
        });

        Controller.getInstance().addToRequestQueue(jArr);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
