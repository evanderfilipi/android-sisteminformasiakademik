package com.akademik.sisteminformasi.aplikasisia;

import android.app.AlertDialog;
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

import com.akademik.sisteminformasi.aplikasisia.adapter.SubAbsenAdapter;
import com.akademik.sisteminformasi.aplikasisia.data.DataAbsen;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbsenSiswaActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener{
    ListView listAbsen;
    TextView tvnamasiswa, tvnotif;
    SwipeRefreshLayout swipeAbsen;
    List<DataAbsen> itemListAbsenSiswa = new ArrayList<DataAbsen>();
    SubAbsenAdapter subAbsenAdapter;
    AlertDialog.Builder dialog;
    LayoutInflater inflaters;
    View dialogView;
    EditText idAbsen_ET, kodeMapel_ET, kodeKelas_ET, sakit_ET, izin_ET, tanpaKeterangan_ET, totalPertemuan_ET, tahunAjaran_ET, nis_ET, nip_ET;
    Spinner semesterSpin;
    int success;
    String id_absen, nip, kd_kelas, kd_mapel, nm_mapel, nis, nama_siswa, sakit, izin, tanpa_keterangan, total_pertemuan, semester, tahun_ajaran;
    private static final String TAG = AbsenSiswaActivity.class.getSimpleName();

    private String[] opsiSemester = {"-Pilih Semester-", "Ganjil", "Genap"};
    private static String url_insert = ServerData.URL + "simpan_absen.php";
    private static String url_edit = ServerData.URL + "edit_absen.php";
    private static String url_update = ServerData.URL + "update_absen.php";
    private static String url_delete = ServerData.URL + "hapus_absen.php";

    public static final String TAG_ID_ABSEN = "id_absen";
    public static final String TAG_KODE_KELAS = "kode_kelas";
    public static final String TAG_KODE_MAPEL = "kode_mapel";
    public static final String TAG_SAKIT = "sakit";
    public static final String TAG_IZIN = "izin";
    public static final String TAG_TANPA_KETERANGAN = "tanpa_keterangan";
    public static final String TAG_TOTAL_PERTEMUAN = "total_pertemuan";
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
        setContentView(R.layout.activity_absen_siswa);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab3);
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
        kd_kelas = getIntent().getStringExtra(TAG_KODE_KELAS);
        tvnotif = (TextView)findViewById(R.id.notifTxt);
        tvnotif.setVisibility(View.INVISIBLE);
        this.setTitle("Data Absen "+nm_mapel);
        swipeAbsen = findViewById(R.id.swipe_refresh33);
        swipeAbsen.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        listAbsen = (ListView)findViewById(R.id.list_absen_sub);
        subAbsenAdapter = new SubAbsenAdapter(AbsenSiswaActivity.this, itemListAbsenSiswa);
        listAbsen.setAdapter(subAbsenAdapter);
        swipeAbsen.setOnRefreshListener(this);

        swipeAbsen.post(new Runnable() {
                             @Override
                             public void run() {
                                 swipeAbsen.setRefreshing(true);
                                 itemListAbsenSiswa.clear();
                                 subAbsenAdapter.notifyDataSetChanged();
                                 getData();
                             }
                         }
        );

        listAbsen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final String id_absenx = itemListAbsenSiswa.get(position).getId_absen();

                final CharSequence[] dialogitem = {"EDIT", "HAPUS"};
                dialog = new AlertDialog.Builder(AbsenSiswaActivity.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:
                                edit(id_absenx);
                                break;
                            case 1:
                                delete(id_absenx);
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
        itemListAbsenSiswa.clear();
        subAbsenAdapter.notifyDataSetChanged();
        getData();
    }

    private void kosong(){
        idAbsen_ET.setText(null);
        kodeMapel_ET.setText(null);
        kodeKelas_ET.setText(null);
        sakit_ET.setText(null);
        izin_ET.setText(null);
        tanpaKeterangan_ET.setText(null);
        totalPertemuan_ET.setText(null);
        tahunAjaran_ET.setText(null);
        nis_ET.setText(null);
        nip_ET.setText(null);
    }

    private void DialogForm(String idA, String kdMpl, String kdKls, String sakitx, String izinx, String tanpaKet, String totalPert, final String smstr, String thnAjar, String nisN, String nipN, String button) {
        dialog = new AlertDialog.Builder(AbsenSiswaActivity.this);
        inflaters = getLayoutInflater();
        dialogView = inflaters.inflate(R.layout.form_absen, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.icon_apk);
        dialog.setTitle("Form Absen "+nm_mapel);

        tvnamasiswa = (TextView) dialogView.findViewById(R.id.namaSiswa3Tv);
        idAbsen_ET = (EditText) dialogView.findViewById(R.id.idAbsenEdt);
        kodeMapel_ET = (EditText) dialogView.findViewById(R.id.kodeMapel3Edt);
        kodeKelas_ET = (EditText) dialogView.findViewById(R.id.kodeKelas2Edt);
        sakit_ET = (EditText) dialogView.findViewById(R.id.sakitEdt);
        izin_ET = (EditText) dialogView.findViewById(R.id.izinEdt);
        tanpaKeterangan_ET = (EditText) dialogView.findViewById(R.id.tanpaKeteranganEdt);
        totalPertemuan_ET = (EditText) dialogView.findViewById(R.id.totalPertemuanEdt);
        tahunAjaran_ET = (EditText) dialogView.findViewById(R.id.tahunAjaran3Edt);
        nis_ET = (EditText) dialogView.findViewById(R.id.nis3Edt);
        nip_ET = (EditText) dialogView.findViewById(R.id.nip3Edt);

        semesterSpin = (Spinner) dialogView.findViewById(R.id.semester2Spn);
        final ArrayAdapter<String> adapterSpin = new ArrayAdapter<String>(AbsenSiswaActivity.this, android.R.layout.simple_list_item_1, opsiSemester);
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
        tvnamasiswa.setText(nama_siswa);

        if (!idA.isEmpty()){
            idAbsen_ET.setText(idA);
            kodeMapel_ET.setText(kdMpl);
            kodeKelas_ET.setText(kdKls);
            sakit_ET.setText(sakitx);
            izin_ET.setText(izinx);
            tanpaKeterangan_ET.setText(tanpaKet);
            totalPertemuan_ET.setText(totalPert);

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
                id_absen = idAbsen_ET.getText().toString();
                sakit = sakit_ET.getText().toString();
                izin = izin_ET.getText().toString();
                tanpa_keterangan = tanpaKeterangan_ET.getText().toString();
                total_pertemuan = totalPertemuan_ET.getText().toString();
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
        itemListAbsenSiswa.clear();
        subAbsenAdapter.notifyDataSetChanged();
        swipeAbsen.setRefreshing(true);
        String url_tampilabsen = ServerData.URL + "tampildata_absensiswa.php?nis="+nis+"&kode_mapel="+kd_mapel;

        JsonArrayRequest jArr = new JsonArrayRequest(url_tampilabsen, new Response.Listener<JSONArray>() {
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

                            DataAbsen item = new DataAbsen();

                            item.setId_absen(obj.getString(TAG_ID_ABSEN));
                            item.setKode_mapel(obj.getString(TAG_KODE_MAPEL));
                            item.setKode_kelas(obj.getString(TAG_KODE_KELAS));
                            item.setMapel(nm_mapel);
                            item.setSakit(obj.getString(TAG_SAKIT));
                            item.setIzin(obj.getString(TAG_IZIN));
                            item.setTanpa_keterangan(obj.getString(TAG_TANPA_KETERANGAN));
                            item.setTotal_pertemuan(obj.getString(TAG_TOTAL_PERTEMUAN));
                            item.setSemester(obj.getString(TAG_SEMESTER));
                            item.setTahun_ajaran(obj.getString(TAG_TAHUN_AJARAN));
                            item.setNis(obj.getString(TAG_NIS));

                            // menambah item ke array
                            itemListAbsenSiswa.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // notifikasi adanya perubahan data pada adapter
                subAbsenAdapter.notifyDataSetChanged();

                swipeAbsen.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipeAbsen.setRefreshing(false);
                Toast.makeText(AbsenSiswaActivity.this, "Tidak dapat menampilkan data. Pastikan koneksi internet anda aktif!", Toast.LENGTH_LONG).show();
            }
        });

        // menambah request ke request queue
        Controller.getInstance().addToRequestQueue(jArr);
    }

    private void simpan_update() {
        String url;
        // jika id kosong maka simpan, jika id ada nilainya maka update
        if (id_absen.isEmpty()){
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
                        subAbsenAdapter.notifyDataSetChanged();

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
                Toast.makeText(AbsenSiswaActivity.this, "Tidak dapat menyimpan data. Pastikan koneksi internet anda aktif!", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                // jika id kosong maka simpan, jika id ada nilainya maka update
                if (id_absen.isEmpty()){
                    params.put("kode_mapel", kd_mapel);
                    params.put("kode_kelas", kd_kelas);
                    params.put("sakit", sakit);
                    params.put("izin", izin);
                    params.put("tanpa_keterangan", tanpa_keterangan);
                    params.put("total_pertemuan", total_pertemuan);
                    params.put("semester", semester);
                    params.put("tahun_ajaran", tahun_ajaran);
                    params.put("nis", nis);
                    params.put("nip", nip);
                } else {
                    params.put("id_absen", id_absen);
                    params.put("kode_mapel", kd_mapel);
                    params.put("kode_kelas", kd_kelas);
                    params.put("sakit", sakit);
                    params.put("izin", izin);
                    params.put("tanpa_keterangan", tanpa_keterangan);
                    params.put("total_pertemuan", total_pertemuan);
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

    private void edit(final String id_absenx){
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
                        String v1 = jObj.getString(TAG_ID_ABSEN);
                        String v2 = jObj.getString(TAG_KODE_MAPEL);
                        String v3 = jObj.getString(TAG_KODE_KELAS);
                        String v4 = jObj.getString(TAG_SAKIT);
                        String v5 = jObj.getString(TAG_IZIN);
                        String v6 = jObj.getString(TAG_TANPA_KETERANGAN);
                        String v7 = jObj.getString(TAG_TOTAL_PERTEMUAN);
                        String v8 = jObj.getString(TAG_SEMESTER);
                        String v9 = jObj.getString(TAG_TAHUN_AJARAN);
                        String v10 = nis;
                        String v11 = nip;

                        DialogForm(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, "UPDATE");

                        subAbsenAdapter.notifyDataSetChanged();

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
                Toast.makeText(AbsenSiswaActivity.this, "Tidak dapat mengambil data. Pastikan koneksi internet anda aktif!", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_absen", id_absenx);

                return params;
            }

        };

        Controller.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void delete(final String id_absenx){
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

                        subAbsenAdapter.notifyDataSetChanged();

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
                Toast.makeText(AbsenSiswaActivity.this, "Tidak dapat menghapus data. Pastikan koneksi internet anda aktif!", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_absen", id_absenx);

                return params;
            }

        };

        Controller.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void cariData(String keyword){
        itemListAbsenSiswa.clear();
        subAbsenAdapter.notifyDataSetChanged();
        swipeAbsen.setRefreshing(true);
        String url_tampiljadwal = ServerData.URL + "caridata_absensiswa.php?nis="+nis+"&kode_mapel="+kd_mapel+"&keyword="+keyword;

        JsonArrayRequest jArr = new JsonArrayRequest(url_tampiljadwal, new Response.Listener<JSONArray>() {
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

                            DataAbsen item = new DataAbsen();

                            item.setId_absen(obj.getString(TAG_ID_ABSEN));
                            item.setKode_mapel(obj.getString(TAG_KODE_MAPEL));
                            item.setKode_kelas(obj.getString(TAG_KODE_KELAS));
                            item.setMapel(nm_mapel);
                            item.setSakit(obj.getString(TAG_SAKIT));
                            item.setIzin(obj.getString(TAG_IZIN));
                            item.setTanpa_keterangan(obj.getString(TAG_TANPA_KETERANGAN));
                            item.setTotal_pertemuan(obj.getString(TAG_TOTAL_PERTEMUAN));
                            item.setSemester(obj.getString(TAG_SEMESTER));
                            item.setTahun_ajaran(obj.getString(TAG_TAHUN_AJARAN));
                            item.setNis(obj.getString(TAG_NIS));

                            // menambah item ke array
                            itemListAbsenSiswa.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // notifikasi adanya perubahan data pada adapter
                subAbsenAdapter.notifyDataSetChanged();

                swipeAbsen.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipeAbsen.setRefreshing(false);
                Toast.makeText(AbsenSiswaActivity.this, "Tidak dapat mencari data. Pastikan koneksi internet anda aktif!", Toast.LENGTH_LONG).show();
            }
        });

        // menambah request ke request queue
        Controller.getInstance().addToRequestQueue(jArr);
    }
}