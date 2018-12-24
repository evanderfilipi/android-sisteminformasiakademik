package com.akademik.sisteminformasi.aplikasisia;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akademik.sisteminformasi.aplikasisia.adapter.SubNilaiAdapter;
import com.akademik.sisteminformasi.aplikasisia.data.DataNilai;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NilaiSiswaActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener{

    ListView listNilai;
    TextView tvnamasiswa, tvnotif;
    SwipeRefreshLayout swipeNilai;
    List<DataNilai> itemListNilaiSiswa = new ArrayList<DataNilai>();
    SubNilaiAdapter subNilaiAdapter;
    AlertDialog.Builder dialog;
    LayoutInflater inflaters;
    View dialogView;
    EditText idNilai_ET, kodeMapel_ET, nilaiUH_ET, nilaiTgs_ET, nilaiUTS_ET, nilaiUAS_ET, nilaiAkhir_ET, tahunAjaran_ET, nis_ET, nip_ET;
    Button hitungBtn;
    Spinner semesterSpin;
    int success;
    String id_nilai, nip, kd_mapel, nm_mapel, nis, nama_siswa, nilai_uh, nilai_tgs, nilai_uts, nilai_uas, nilai_akhir, semester, tahun_ajaran;
    private static final String TAG = NilaiSiswaActivity.class.getSimpleName();

    private String[] opsiSemester = {"-Pilih Semester-", "Ganjil", "Genap"};
    private static String url_insert = ServerData.URL + "simpan_nilai.php";
    private static String url_edit = ServerData.URL + "edit_nilai.php";
    private static String url_update = ServerData.URL + "update_nilai.php";
    private static String url_delete = ServerData.URL + "hapus_nilai.php";

    public static final String TAG_ID_NILAI = "id_nilai";
    public static final String TAG_KODE_MAPEL = "kode_mapel";
    public static final String TAG_NILAI_UH = "nilai_uh";
    public static final String TAG_NILAI_TGS = "nilai_tgs";
    public static final String TAG_NILAI_UTS = "nilai_uts";
    public static final String TAG_NILAI_UAS = "nilai_uas";
    public static final String TAG_NILAI_AKHIR = "nilai_akhir";
    public static final String TAG_SEMESTER = "semester";
    public static final String TAG_TAHUN_AJARAN = "tahun_ajaran";
    public static final String TAG_NIS = "nis";
    public static final String TAG_NAMA_SISWA = "nama_siswa";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    public static final String PUT_NIP = "nip";
    public static final String PUT_KODE_MAPEL = "kd_mapel";
    public static final String PUT_NAMA_MAPEL = "nm_mapel";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nilai_siswa);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogForm("", "", "", "", "", "", "", "", "", "", "", "SIMPAN");
            }
        });
        nip = getIntent().getStringExtra(PUT_NIP);
        kd_mapel = getIntent().getStringExtra(PUT_KODE_MAPEL);
        nm_mapel = getIntent().getStringExtra(PUT_NAMA_MAPEL);
        nis = getIntent().getStringExtra(TAG_NIS);
        nama_siswa = getIntent().getStringExtra(TAG_NAMA_SISWA);
        this.setTitle("Data Nilai "+nm_mapel);
        tvnotif = (TextView)findViewById(R.id.notif3Txt);
        tvnotif.setVisibility(View.INVISIBLE);
        swipeNilai = findViewById(R.id.swipe_refresh11);
        swipeNilai.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        listNilai = (ListView)findViewById(R.id.list_nilai_sub);
        subNilaiAdapter = new SubNilaiAdapter(NilaiSiswaActivity.this, itemListNilaiSiswa);
        listNilai.setAdapter(subNilaiAdapter);
        swipeNilai.setOnRefreshListener(this);

        swipeNilai.post(new Runnable() {
                            @Override
                            public void run() {
                                swipeNilai.setRefreshing(true);
                                itemListNilaiSiswa.clear();
                                subNilaiAdapter.notifyDataSetChanged();
                                getData();
                            }
                        }
        );

        listNilai.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final String id_nilaix = itemListNilaiSiswa.get(position).getId_nilai();

                final CharSequence[] dialogitem = {"EDIT", "HAPUS"};
                dialog = new AlertDialog.Builder(NilaiSiswaActivity.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:
                                edit(id_nilaix);
                                break;
                            case 1:
                                delete(id_nilaix);
                                break;
                        }
                    }
                }).show();
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        cariData(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint("Cari...");
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    public void onRefresh() {
        itemListNilaiSiswa.clear();
        subNilaiAdapter.notifyDataSetChanged();
        getData();
    }

    private void kosong(){
        idNilai_ET.setText(null);
        kodeMapel_ET.setText(null);
        nilaiUH_ET.setText(null);
        nilaiTgs_ET.setText(null);
        nilaiUTS_ET.setText(null);
        nilaiUAS_ET.setText(null);
        nilaiAkhir_ET.setText(null);
        tahunAjaran_ET.setText(null);
        nis_ET.setText(null);
        nip_ET.setText(null);
    }

    private void DialogForm(String idN, String kdMpl, String n1, String n2, String n3, String n4, String nA, final String smstr, String thnAjar, String nisN, String nipN, String button) {
        dialog = new AlertDialog.Builder(NilaiSiswaActivity.this);
        inflaters = getLayoutInflater();
        dialogView = inflaters.inflate(R.layout.form_nilai, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.icon_apk);
        dialog.setTitle("Form Nilai "+nm_mapel);

        tvnamasiswa = (TextView) dialogView.findViewById(R.id.namaSiswaTv);
        idNilai_ET = (EditText) dialogView.findViewById(R.id.idNilaiEdt);
        kodeMapel_ET = (EditText) dialogView.findViewById(R.id.kodeMapelEdt);
        nilaiUH_ET = (EditText) dialogView.findViewById(R.id.nilaiUHEdt);
        nilaiTgs_ET = (EditText) dialogView.findViewById(R.id.nilaiTugasEdt);
        nilaiUTS_ET = (EditText) dialogView.findViewById(R.id.nilaiUTSEdt);
        nilaiUAS_ET = (EditText) dialogView.findViewById(R.id.nilaiUASEdt);
        nilaiAkhir_ET = (EditText) dialogView.findViewById(R.id.nilaiAkhirEdt);
        tahunAjaran_ET = (EditText) dialogView.findViewById(R.id.tahunAjaranEdt);
        nis_ET = (EditText) dialogView.findViewById(R.id.nisEdt);
        nip_ET = (EditText) dialogView.findViewById(R.id.nipEdt);

        semesterSpin = (Spinner) dialogView.findViewById(R.id.semesterSpn);
        final ArrayAdapter<String> adapterSpin = new ArrayAdapter<String>(NilaiSiswaActivity.this, android.R.layout.simple_list_item_1, opsiSemester);
        semesterSpin.setAdapter(adapterSpin);
        semesterSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterSpin.getItem(i).equalsIgnoreCase("-Pilih Semester-")){
                    semester = "";
                }
                else {
                    semester = adapterSpin.getItem(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if(smstr.equalsIgnoreCase("Ganjil") || smstr.equalsIgnoreCase("Genap")){
                    semester = smstr;
                } else {
                    semester = "";
                }
            }
        });
        hitungBtn = (Button) dialogView.findViewById(R.id.nilaiAkhirBtn);
        hitungBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String x1 = nilaiUH_ET.getText().toString();
                String x2 = nilaiTgs_ET.getText().toString();
                String x3 = nilaiUTS_ET.getText().toString();
                String x4 = nilaiUAS_ET.getText().toString();
                if(x1.isEmpty() || x2.isEmpty() || x3.isEmpty() || x4.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Kolom Nilai Ujian Harian, Tugas, UTS atau UAS tidak boleh kosong!", Toast.LENGTH_LONG).show();
                } else {
                    Double d1 = Double.parseDouble(x1);
                    Double d2 = Double.parseDouble(x2);
                    Double d3 = Double.parseDouble(x3);
                    Double d4 = Double.parseDouble(x4);
                    Double hasil = (d1+d2+d3+d4) / 4;
                    Double hasilDec = (double)Math.round(hasil * 100) / 100;
                    nilaiAkhir_ET.setText(Double.toString(hasilDec));
                }
            }
        });
        tvnamasiswa.setText(nama_siswa);

        if (!idN.isEmpty()){
            idNilai_ET.setText(idN);
            kodeMapel_ET.setText(kdMpl);
            nilaiUH_ET.setText(n1);
            nilaiTgs_ET.setText(n2);
            nilaiUTS_ET.setText(n3);
            nilaiUAS_ET.setText(n4);
            nilaiAkhir_ET.setText(nA);

            if(smstr.equalsIgnoreCase("Ganjil")){
                semesterSpin.setSelection(adapterSpin.getPosition(smstr));
            } else if(smstr.equalsIgnoreCase("Genap")){
                semesterSpin.setSelection(adapterSpin.getPosition(smstr));
            } else {
                semesterSpin.setSelection(adapterSpin.getPosition("-Pilih Semester-"));
            }

            tahunAjaran_ET.setText(thnAjar);
            nis_ET.setText(nisN);
            nip_ET.setText(nipN);
        } else {
            kosong();
            semesterSpin.setSelection(adapterSpin.getPosition("-Pilih Semester-"));
        }

        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                id_nilai = idNilai_ET.getText().toString();
                nilai_uh = nilaiUH_ET.getText().toString();
                nilai_tgs = nilaiTgs_ET.getText().toString();
                nilai_uts = nilaiUTS_ET.getText().toString();
                nilai_uas = nilaiUAS_ET.getText().toString();
                nilai_akhir = nilaiAkhir_ET.getText().toString();
                tahun_ajaran = tahunAjaran_ET.getText().toString();
                simpan_update();
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                kosong();
                semesterSpin.setSelection(adapterSpin.getPosition("-Pilih Semester-"));
            }
        });

        dialog.show();
    }

    private void getData(){
        itemListNilaiSiswa.clear();
        subNilaiAdapter.notifyDataSetChanged();
        swipeNilai.setRefreshing(true);
        String url_tampilnilai = ServerData.URL + "tampildata_nilaisiswa.php?nis="+nis+"&kode_mapel="+kd_mapel;

        JsonArrayRequest jArr = new JsonArrayRequest(url_tampilnilai, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                if(response.length() <= 0){
                    tvnotif.setVisibility(View.VISIBLE);
                    tvnotif.setText("Data Tidak Ditemukan");
                } else {
                    tvnotif.setVisibility(View.INVISIBLE);
                    tvnotif.setText("");
                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            DataNilai item = new DataNilai();

                            item.setId_nilai(obj.getString(TAG_ID_NILAI));
                            item.setKode_mapel(obj.getString(TAG_KODE_MAPEL));
                            item.setMapel(nm_mapel);
                            item.setNilai_uh(obj.getString(TAG_NILAI_UH));
                            item.setNilai_tgs(obj.getString(TAG_NILAI_TGS));
                            item.setNilai_uts(obj.getString(TAG_NILAI_UTS));
                            item.setNilai_uas(obj.getString(TAG_NILAI_UAS));
                            item.setNilai_akhir(obj.getString(TAG_NILAI_AKHIR));
                            item.setSemester(obj.getString(TAG_SEMESTER));
                            item.setTahun_ajaran(obj.getString(TAG_TAHUN_AJARAN));
                            item.setNis(obj.getString(TAG_NIS));

                            // menambah item ke array
                            itemListNilaiSiswa.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // notifikasi adanya perubahan data pada adapter
                subNilaiAdapter.notifyDataSetChanged();

                swipeNilai.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipeNilai.setRefreshing(false);
                Toast.makeText(NilaiSiswaActivity.this, "Tidak dapat menampilkan data. Pastikan koneksi internet anda aktif!", Toast.LENGTH_LONG).show();
            }
        });

        // menambah request ke request queue
        Controller.getInstance().addToRequestQueue(jArr);
    }

    private void simpan_update() {
        String url;
        // jika id kosong maka simpan, jika id ada nilainya maka update
        if (id_nilai.isEmpty()){
            url = url_insert;
        } else {
            url = url_update;
        }

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("Add/update", jObj.toString());

                        getData();
                        kosong();

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        subNilaiAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(NilaiSiswaActivity.this, "Tidak dapat menyimpan data. Pastikan koneksi internet anda aktif!", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                // jika id kosong maka simpan, jika id ada nilainya maka update
                if (id_nilai.isEmpty()){
                    params.put("kode_mapel", kd_mapel);
                    params.put("nilai_uh", nilai_uh);
                    params.put("nilai_tgs", nilai_tgs);
                    params.put("nilai_uts", nilai_uts);
                    params.put("nilai_uas", nilai_uas);
                    params.put("nilai_akhir", nilai_akhir);
                    params.put("semester", semester);
                    params.put("tahun_ajaran", tahun_ajaran);
                    params.put("nis", nis);
                    params.put("nip", nip);
                } else {
                    params.put("id_nilai", id_nilai);
                    params.put("kode_mapel", kd_mapel);
                    params.put("nilai_uh", nilai_uh);
                    params.put("nilai_tgs", nilai_tgs);
                    params.put("nilai_uts", nilai_uts);
                    params.put("nilai_uas", nilai_uas);
                    params.put("nilai_akhir", nilai_akhir);
                    params.put("semester", semester);
                    params.put("tahun_ajaran", tahun_ajaran);
                    params.put("nis", nis);
                    params.put("nip", nip);
                }

                return params;
            }

        };

        Controller.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void edit(final String id_nilaix){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_edit, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("get edit data", jObj.toString());
                        String v1 = jObj.getString(TAG_ID_NILAI);
                        String v2 = jObj.getString(TAG_KODE_MAPEL);
                        String v3 = jObj.getString(TAG_NILAI_UH);
                        String v4 = jObj.getString(TAG_NILAI_TGS);
                        String v5 = jObj.getString(TAG_NILAI_UTS);
                        String v6 = jObj.getString(TAG_NILAI_UAS);
                        String v7 = jObj.getString(TAG_NILAI_AKHIR);
                        String v8 = jObj.getString(TAG_SEMESTER);
                        String v9 = jObj.getString(TAG_TAHUN_AJARAN);
                        String v10 = nis;
                        String v11 = nip;

                        DialogForm(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, "UPDATE");

                        subNilaiAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(NilaiSiswaActivity.this, "Tidak dapat mengambil data. Pastikan koneksi internet anda aktif!", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_nilai", id_nilaix);

                return params;
            }

        };

        Controller.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void delete(final String id_nilaix){
        StringRequest strReq = new StringRequest(Request.Method.POST, url_delete, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        Log.d("delete", jObj.toString());

                        getData();

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        subNilaiAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(NilaiSiswaActivity.this, "Tidak dapat menghapus data. Pastikan koneksi internet anda aktif!", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_nilai", id_nilaix);

                return params;
            }

        };

        Controller.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void cariData(String keyword){
        itemListNilaiSiswa.clear();
        subNilaiAdapter.notifyDataSetChanged();
        swipeNilai.setRefreshing(true);
        String url_tampilnilai = ServerData.URL + "caridata_nilaisiswa.php?nis="+nis+"&kode_mapel="+kd_mapel+"&keyword="+keyword;

        JsonArrayRequest jArr = new JsonArrayRequest(url_tampilnilai, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                if(response.length() <= 0){
                    tvnotif.setVisibility(View.VISIBLE);
                    tvnotif.setText("Data Tidak Ditemukan");
                } else {
                    tvnotif.setVisibility(View.INVISIBLE);
                    tvnotif.setText("");
                    // Parsing json
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            DataNilai item = new DataNilai();

                            item.setId_nilai(obj.getString(TAG_ID_NILAI));
                            item.setKode_mapel(obj.getString(TAG_KODE_MAPEL));
                            item.setMapel(nm_mapel);
                            item.setNilai_uh(obj.getString(TAG_NILAI_UH));
                            item.setNilai_tgs(obj.getString(TAG_NILAI_TGS));
                            item.setNilai_uts(obj.getString(TAG_NILAI_UTS));
                            item.setNilai_uas(obj.getString(TAG_NILAI_UAS));
                            item.setNilai_akhir(obj.getString(TAG_NILAI_AKHIR));
                            item.setSemester(obj.getString(TAG_SEMESTER));
                            item.setTahun_ajaran(obj.getString(TAG_TAHUN_AJARAN));
                            item.setNis(obj.getString(TAG_NIS));

                            // menambah item ke array
                            itemListNilaiSiswa.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // notifikasi adanya perubahan data pada adapter
                subNilaiAdapter.notifyDataSetChanged();

                swipeNilai.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipeNilai.setRefreshing(false);
                Toast.makeText(NilaiSiswaActivity.this, "Tidak dapat mencari data. Pastikan koneksi internet anda aktif!", Toast.LENGTH_LONG).show();
            }
        });

        // menambah request ke request queue
        Controller.getInstance().addToRequestQueue(jArr);
    }
}