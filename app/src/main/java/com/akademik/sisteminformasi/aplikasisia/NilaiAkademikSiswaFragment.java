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

import com.akademik.sisteminformasi.aplikasisia.adapter.NilaiSiswaAdapter;
import com.akademik.sisteminformasi.aplikasisia.data.DataNilai;
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

public class NilaiAkademikSiswaFragment extends Fragment {
    LinearLayout tampil_linlay;
    Spinner semesterSpn;
    EditText tahunAjaranEdt;
    Button tampilBtn;
    RecyclerView recyclerView;
    LinearLayoutManager llm;
    NilaiSiswaAdapter nilaiSiswaAdapter;
    List<DataNilai> nilaiArr = new ArrayList<DataNilai>();
    ProgressDialog pDialog;

    private static final String TAG = NilaiAkademikSiswaFragment.class.getSimpleName();

    public static final String PUT_NIS = "nis";
    public static final String PUT_NAMA_SISWA = "nama_siswa";

    public static final String TAG_ID_NILAI = "id_nilai";
    public static final String TAG_KODE_MAPEL = "kd_mpl";
    public static final String TAG_NAMA_MAPEL = "nama_mapel";
    public static final String TAG_NILAI_UH = "nilai_uh";
    public static final String TAG_NILAI_TUGAS = "nilai_tgs";
    public static final String TAG_NILAI_UTS = "nilai_uts";
    public static final String TAG_NILAI_UAS = "nilai_uas";
    public static final String TAG_NILAI_AKHIR = "nilai_akhir";
    public static final String TAG_NIP = "id_guru";
    public static final String TAG_NAMA_GURU = "nm_guru";

    String nis, nama_siswa, semester;
    String thn_ajaran = "";
    private String[] opsiSemester = {"-Pilih-", "Ganjil", "Genap"};

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nilai_akademik_siswa, container, false);
        getActivity().setTitle("Nilai Akademik");
        if (getArguments()!=null){
            nis = getArguments().getString(PUT_NIS);
            nama_siswa = getArguments().getString(PUT_NAMA_SISWA);
        }
        tampil_linlay = (LinearLayout)v.findViewById(R.id.isiNilaiLinlay);
        tampil_linlay.setVisibility(View.INVISIBLE);
        semesterSpn = (Spinner)v.findViewById(R.id.semesterSiswa2Spn);
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
        tahunAjaranEdt = (EditText)v.findViewById(R.id.tahunAjaranSiswa2Edt);
        tampilBtn = (Button)v.findViewById(R.id.tampilkanNilaiBtn);
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
        recyclerView = v.findViewById(R.id.recyclerNilai);
        llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(llm);

        return v;
    }

    private void tampilkan(String aa, String bb, String cc){
        String url_tampilnilai = ServerData.URL + "tampil_nilaiakademik.php?nis="+aa+"&semester="+bb+"&tahun_ajaran="+cc;
        pDialog= new ProgressDialog(getActivity());
        pDialog.setMessage("Mencari data...");
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        showDialog();

        nilaiArr.clear();
        JsonArrayRequest jArr = new JsonArrayRequest(url_tampilnilai, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                if(response.length() <= 0){
                    tampil_linlay.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(), "Data nilai akademik siswa tidak ditemukan. Silahkan coba lagi!", Toast.LENGTH_LONG).show();
                } else {
                    tampil_linlay.setVisibility(View.VISIBLE);

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            DataNilai item = new DataNilai();

                            item.setId_nilai(obj.getString(TAG_ID_NILAI));
                            item.setKode_mapel(obj.getString(TAG_KODE_MAPEL));
                            item.setMapel(obj.getString(TAG_NAMA_MAPEL));
                            item.setNilai_uh(obj.getString(TAG_NILAI_UH));
                            item.setNilai_tgs(obj.getString(TAG_NILAI_TUGAS));
                            item.setNilai_uts(obj.getString(TAG_NILAI_UTS));
                            item.setNilai_uas(obj.getString(TAG_NILAI_UAS));
                            item.setNilai_akhir(obj.getString(TAG_NILAI_AKHIR));
                            item.setNip(obj.getString(TAG_NIP));
                            item.setNama_guru(obj.getString(TAG_NAMA_GURU));

                            nilaiArr.add(item);
                            nilaiSiswaAdapter = new NilaiSiswaAdapter(getActivity(), nilaiArr);
                            recyclerView.setAdapter(nilaiSiswaAdapter);
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
