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
import android.widget.Toast;

import com.akademik.sisteminformasi.aplikasisia.adapter.AbsensiSiswaAdapter;
import com.akademik.sisteminformasi.aplikasisia.data.DataAbsen;
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

public class AbsensiSiswaFragment extends Fragment {
    LinearLayout tampil_linlay;
    Spinner semesterSpn;
    EditText tahunAjaranEdt;
    Button tampilBtn;
    RecyclerView recyclerView;
    LinearLayoutManager llm;
    AbsensiSiswaAdapter absensiSiswaAdapter;
    List<DataAbsen> absensiArr = new ArrayList<DataAbsen>();
    ProgressDialog pDialog;

    private static final String TAG = AbsensiSiswaFragment.class.getSimpleName();

    public static final String PUT_NIS = "nis";
    public static final String PUT_NAMA_SISWA = "nama_siswa";

    public static final String TAG_ID_ABSEN = "id_absen";
    public static final String TAG_KODE_MAPEL = "kd_mpl";
    public static final String TAG_NAMA_MAPEL = "nama_mapel";
    public static final String TAG_SAKIT = "sakit";
    public static final String TAG_IZIN = "izin";
    public static final String TAG_TANPA_KETERANGAN = "tanpa_keterangan";
    public static final String TAG_TOTAL_PERTEMUAN = "total_pertemuan";
    public static final String TAG_NIP = "id_guru";
    public static final String TAG_NAMA_GURU = "nm_guru";

    String nis, nama_siswa, semester;
    String thn_ajaran = "";
    private String[] opsiSemester = {"-Pilih-", "Ganjil", "Genap"};

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_absensi_siswa, container, false);
        getActivity().setTitle("Data Absensi");
        if (getArguments()!=null){
            nis = getArguments().getString(PUT_NIS);
            nama_siswa = getArguments().getString(PUT_NAMA_SISWA);
        }
        tampil_linlay = (LinearLayout)v.findViewById(R.id.isiAbsensiLinlay);
        tampil_linlay.setVisibility(View.INVISIBLE);
        semesterSpn = (Spinner)v.findViewById(R.id.semesterSiswaSpn);
        final ArrayAdapter<String> adapterSpin = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, opsiSemester);
        semesterSpn.setAdapter(adapterSpin);
        semesterSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterSpin.getItem(i).equalsIgnoreCase("-Pilih-")){
                    semester = "";
                }
                else {
                    semester = adapterSpin.getItem(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                semester = "";
            }
        });
        tahunAjaranEdt = (EditText)v.findViewById(R.id.tahunAjaranSiswaEdt);
        tampilBtn = (Button)v.findViewById(R.id.tampilkanAbsensiBtn);
        tampilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thn_ajaran = tahunAjaranEdt.getText().toString();
                if(thn_ajaran.equalsIgnoreCase("") || semester.equalsIgnoreCase("")){
                    Toast.makeText(getActivity(), "Pilih Semester dan isi kolom Tahun Ajaran terlebih dahulu!", Toast.LENGTH_LONG).show();
                } else {
                    tampilkan(nis, semester, thn_ajaran);
                }
            }
        });
        recyclerView = v.findViewById(R.id.recyclerAbsensi);
        llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(llm);

        return v;
    }

    private void tampilkan(String aa, String bb, String cc){
        String url_tampilabsen = ServerData.URL + "tampil_absensi.php?nis="+aa+"&semester="+bb+"&tahun_ajaran="+cc;
        pDialog= new ProgressDialog(getActivity());
        pDialog.setMessage("Mencari data...");
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        showDialog();

        absensiArr.clear();
        JsonArrayRequest jArr = new JsonArrayRequest(url_tampilabsen, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                if(response.length() <= 0){
                    tampil_linlay.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(), "Data absensi siswa tidak ditemukan. Silahkan coba lagi!", Toast.LENGTH_LONG).show();
                } else {
                    tampil_linlay.setVisibility(View.VISIBLE);

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            DataAbsen item = new DataAbsen();

                            item.setId_absen(obj.getString(TAG_ID_ABSEN));
                            item.setKode_mapel(obj.getString(TAG_KODE_MAPEL));
                            item.setMapel(obj.getString(TAG_NAMA_MAPEL));
                            item.setSakit(obj.getString(TAG_SAKIT));
                            item.setIzin(obj.getString(TAG_IZIN));
                            item.setTanpa_keterangan(obj.getString(TAG_TANPA_KETERANGAN));
                            item.setTotal_pertemuan(obj.getString(TAG_TOTAL_PERTEMUAN));
                            item.setNip(obj.getString(TAG_NIP));
                            item.setNama_guru(obj.getString(TAG_NAMA_GURU));

                            absensiArr.add(item);
                            absensiSiswaAdapter = new AbsensiSiswaAdapter(getActivity(), absensiArr);
                            recyclerView.setAdapter(absensiSiswaAdapter);
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
