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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akademik.sisteminformasi.aplikasisia.adapter.SubJadwalAdapter;
import com.akademik.sisteminformasi.aplikasisia.data.DataJadwal;
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

public class JadwalSiswaActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener{

    ListView listJadwal;
    TextView tvnamasiswa, tvnotif;
    SwipeRefreshLayout swipeJadwal;
    List<DataJadwal> itemListJadwalSiswa = new ArrayList<DataJadwal>();
    SubJadwalAdapter subJadwalAdapter;
    AlertDialog.Builder dialog;
    LayoutInflater inflaters;
    View dialogView;
    EditText idJadwal_ET, kodeKelas_ET, kodeMapel_ET, jamMulai_ET, jamSelesai_ET, tahunAjaran_ET, nis_ET, nip_ET;
    Spinner hariSpin, jenisJadwalSpin;
    int success;
    String id_jadwal, nip, kd_kelas, kd_mapel, nm_mapel, nis, nama_siswa, hari, jam_mulai, jam_selesai, tahun_ajaran, jenis_jadwal;
    private static final String TAG = JadwalSiswaActivity.class.getSimpleName();

    private String[] opsiHari = {"-Pilih Hari-", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"};
    private String[] jenisJadwal = {"-Pilih Jenis Jadwal-", "Harian", "UTS", "UAS", "UN", "Lainnya"};
    private static String url_insert = ServerData.URL + "simpan_jadwal.php";
    private static String url_edit = ServerData.URL + "edit_jadwal.php";
    private static String url_update = ServerData.URL + "update_jadwal.php";
    private static String url_delete = ServerData.URL + "hapus_jadwal.php";

    public static final String TAG_ID_JADWAL = "id_jadwal";
    public static final String TAG_KODE_KELAS = "kode_kelas";
    public static final String TAG_KODE_MAPEL = "kode_mapel";
    public static final String TAG_HARI = "hari";
    public static final String TAG_JAM_MULAI = "jam_mulai";
    public static final String TAG_JAM_SELESAI = "jam_selesai";
    public static final String TAG_TAHUN_AJARAN = "tahun_ajaran";
    public static final String TAG_JENIS_JADWAL = "jenis_jadwal";
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
        setContentView(R.layout.activity_jadwal_siswa);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogForm("", "", "", "", "", "", "", "", "", "",  "SIMPAN");
            }
        });
        nip = getIntent().getStringExtra(PUT_NIP);
        kd_mapel = getIntent().getStringExtra(PUT_KODE_MAPEL);
        nm_mapel = getIntent().getStringExtra(PUT_NAMA_MAPEL);
        nis = getIntent().getStringExtra(TAG_NIS);
        nama_siswa = getIntent().getStringExtra(TAG_NAMA_SISWA);
        kd_kelas = getIntent().getStringExtra(TAG_KODE_KELAS);
        this.setTitle("Data Jadwal "+nm_mapel);
        tvnotif = (TextView)findViewById(R.id.notif2Txt);
        tvnotif.setVisibility(View.INVISIBLE);
        swipeJadwal = findViewById(R.id.swipe_refresh22);
        swipeJadwal.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        listJadwal = (ListView)findViewById(R.id.list_jadwal_sub);
        subJadwalAdapter = new SubJadwalAdapter(JadwalSiswaActivity.this, itemListJadwalSiswa);
        listJadwal.setAdapter(subJadwalAdapter);
        swipeJadwal.setOnRefreshListener(this);

        swipeJadwal.post(new Runnable() {
                            @Override
                            public void run() {
                                swipeJadwal.setRefreshing(true);
                                itemListJadwalSiswa.clear();
                                subJadwalAdapter.notifyDataSetChanged();
                                getData();
                            }
                        }
        );

        listJadwal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final String id_jadwalx = itemListJadwalSiswa.get(position).getId_jadwal();

                final CharSequence[] dialogitem = {"EDIT", "HAPUS"};
                dialog = new AlertDialog.Builder(JadwalSiswaActivity.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:
                                edit(id_jadwalx);
                                break;
                            case 1:
                                delete(id_jadwalx);
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
        itemListJadwalSiswa.clear();
        subJadwalAdapter.notifyDataSetChanged();
        getData();
    }

    private void kosong(){
        idJadwal_ET.setText(null);
        kodeKelas_ET.setText(null);
        kodeMapel_ET.setText(null);
        jamMulai_ET.setText("00:00:00");
        jamSelesai_ET.setText("00:00:00");
        tahunAjaran_ET.setText(null);
        nis_ET.setText(null);
        nip_ET.setText(null);
    }

    private void DialogForm(String idJ, String kdKls, String kdMpl, final String day, String jamMulai, String jamSelesai, String thnAjar, final String jnsJadwal, String nisN, String nipN, String button) {
        dialog = new AlertDialog.Builder(JadwalSiswaActivity.this);
        inflaters = getLayoutInflater();
        dialogView = inflaters.inflate(R.layout.form_jadwal, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.icon_apk);
        dialog.setTitle("Form Jadwal "+nm_mapel);

        tvnamasiswa = (TextView) dialogView.findViewById(R.id.namaSiswa2Tv);
        idJadwal_ET = (EditText) dialogView.findViewById(R.id.idJadwalEdt);
        kodeKelas_ET = (EditText) dialogView.findViewById(R.id.kodeKelasEdt);
        kodeMapel_ET = (EditText) dialogView.findViewById(R.id.kodeMapel2Edt);
        jamMulai_ET = (EditText) dialogView.findViewById(R.id.jamMulaiEdt);
        jamSelesai_ET = (EditText) dialogView.findViewById(R.id.jamSelesaiEdt);
        tahunAjaran_ET = (EditText) dialogView.findViewById(R.id.tahunAjaran2Edt);
        nis_ET = (EditText) dialogView.findViewById(R.id.nis2Edt);
        nip_ET = (EditText) dialogView.findViewById(R.id.nip2Edt);

        hariSpin = (Spinner) dialogView.findViewById(R.id.hariSpn);
        final ArrayAdapter<String> adapterSpin = new ArrayAdapter<String>(JadwalSiswaActivity.this, android.R.layout.simple_list_item_1, opsiHari);
        hariSpin.setAdapter(adapterSpin);
        hariSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterSpin.getItem(i).equalsIgnoreCase("-Pilih Hari-")){
                    hari = "";
                }
                else {
                    hari = adapterSpin.getItem(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if(day.equalsIgnoreCase("Senin") || day.equalsIgnoreCase("Selasa") || day.equalsIgnoreCase("Rabu") || day.equalsIgnoreCase("Kamis") || day.equalsIgnoreCase("Jumat") || day.equalsIgnoreCase("Sabtu") || day.equalsIgnoreCase("Minggu")){
                    hari = day;
                } else {
                    hari = "";
                }
            }
        });

        jenisJadwalSpin = (Spinner) dialogView.findViewById(R.id.jenisJadwalSpn);
        final ArrayAdapter<String> adapter2Spin = new ArrayAdapter<String>(JadwalSiswaActivity.this, android.R.layout.simple_list_item_1, jenisJadwal);
        jenisJadwalSpin.setAdapter(adapter2Spin);
        jenisJadwalSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapter2Spin.getItem(i).equalsIgnoreCase("-Pilih Jenis Jadwal-")){
                    jenis_jadwal = "";
                }
                else {
                    jenis_jadwal = adapter2Spin.getItem(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if(jnsJadwal.equalsIgnoreCase("Harian") || jnsJadwal.equalsIgnoreCase("UTS") || jnsJadwal.equalsIgnoreCase("UAS") || jnsJadwal.equalsIgnoreCase("UN") || jnsJadwal.equalsIgnoreCase("Lainnya")){
                    jenis_jadwal = jnsJadwal;
                } else {
                    jenis_jadwal = "";
                }
            }
        });

        tvnamasiswa.setText(nama_siswa);

        if (!idJ.isEmpty()){
            idJadwal_ET.setText(idJ);
            kodeKelas_ET.setText(kdKls);
            kodeMapel_ET.setText(kdMpl);
            jamMulai_ET.setText(jamMulai);
            jamSelesai_ET.setText(jamSelesai);
            tahunAjaran_ET.setText(thnAjar);
            nis_ET.setText(nipN);
            nip_ET.setText(nisN);

            if(day.equalsIgnoreCase("Senin")){
                hariSpin.setSelection(adapterSpin.getPosition(day));
            } else if(day.equalsIgnoreCase("Selasa")){
                hariSpin.setSelection(adapterSpin.getPosition(day));
            } else if(day.equalsIgnoreCase("Rabu")){
                hariSpin.setSelection(adapterSpin.getPosition(day));
            } else if(day.equalsIgnoreCase("Kamis")){
                hariSpin.setSelection(adapterSpin.getPosition(day));
            } else if(day.equalsIgnoreCase("Jumat")){
                hariSpin.setSelection(adapterSpin.getPosition(day));
            } else if(day.equalsIgnoreCase("Sabtu")){
                hariSpin.setSelection(adapterSpin.getPosition(day));
            } else if(day.equalsIgnoreCase("Minggu")){
                hariSpin.setSelection(adapterSpin.getPosition(day));
            } else {
                hariSpin.setSelection(adapterSpin.getPosition("-Pilih Hari-"));
            }

            if(jnsJadwal.equalsIgnoreCase("Harian")){
                jenisJadwalSpin.setSelection(adapter2Spin.getPosition(jnsJadwal));
            } else if(jnsJadwal.equalsIgnoreCase("UTS")){
                jenisJadwalSpin.setSelection(adapter2Spin.getPosition(jnsJadwal));
            } else if(jnsJadwal.equalsIgnoreCase("UAS")){
                jenisJadwalSpin.setSelection(adapter2Spin.getPosition(jnsJadwal));
            } else if(jnsJadwal.equalsIgnoreCase("UN")){
                jenisJadwalSpin.setSelection(adapter2Spin.getPosition(jnsJadwal));
            } else {
                jenisJadwalSpin.setSelection(adapter2Spin.getPosition("-Pilih Jenis Jadwal-"));
            }

            tahunAjaran_ET.setText(thnAjar);
            nis_ET.setText(nisN);
            nip_ET.setText(nipN);
        } else {
            kosong();
            hariSpin.setSelection(adapterSpin.getPosition("-Pilih Hari-"));
            jenisJadwalSpin.setSelection(adapter2Spin.getPosition("-Pilih Jenis Jadwal-"));
        }

        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                id_jadwal = idJadwal_ET.getText().toString();
                jam_mulai = jamMulai_ET.getText().toString();
                jam_selesai = jamSelesai_ET.getText().toString();
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
                hariSpin.setSelection(adapterSpin.getPosition("-Pilih Hari-"));
                jenisJadwalSpin.setSelection(adapter2Spin.getPosition("-Pilih Jenis Jadwal-"));
            }
        });

        dialog.show();
    }

    private void getData(){
        itemListJadwalSiswa.clear();
        subJadwalAdapter.notifyDataSetChanged();
        swipeJadwal.setRefreshing(true);
        String url_tampiljadwal = ServerData.URL + "tampildata_jadwalsiswa.php?nis="+nis+"&kode_mapel="+kd_mapel;

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

                            DataJadwal item = new DataJadwal();

                            item.setId_jadwal(obj.getString(TAG_ID_JADWAL));
                            item.setKode_kelas(obj.getString(TAG_KODE_KELAS));
                            item.setKode_mapel(obj.getString(TAG_KODE_MAPEL));
                            item.setMapel(nm_mapel);
                            item.setHari(obj.getString(TAG_HARI));
                            item.setJam_mulai(obj.getString(TAG_JAM_MULAI));
                            item.setJam_selesai(obj.getString(TAG_JAM_SELESAI));
                            item.setTahun_ajaran(obj.getString(TAG_TAHUN_AJARAN));
                            item.setJenis_jadwal(obj.getString(TAG_JENIS_JADWAL));
                            item.setNis(obj.getString(TAG_NIS));

                            // menambah item ke array
                            itemListJadwalSiswa.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // notifikasi adanya perubahan data pada adapter
                subJadwalAdapter.notifyDataSetChanged();

                swipeJadwal.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipeJadwal.setRefreshing(false);
                Toast.makeText(JadwalSiswaActivity.this, "Tidak dapat menampilkan data. Pastikan koneksi internet anda aktif!", Toast.LENGTH_LONG).show();
            }
        });

        // menambah request ke request queue
        Controller.getInstance().addToRequestQueue(jArr);
    }

    private void simpan_update() {
        String url;
        // jika id kosong maka simpan, jika id ada nilainya maka update
        if (id_jadwal.isEmpty()){
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
                        subJadwalAdapter.notifyDataSetChanged();

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
                Toast.makeText(JadwalSiswaActivity.this, "Tidak dapat menyimpan data. Pastikan koneksi internet anda aktif!", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                // jika id kosong maka simpan, jika id ada nilainya maka update
                if (id_jadwal.isEmpty()){
                    params.put("kode_kelas", kd_kelas);
                    params.put("kode_mapel", kd_mapel);
                    params.put("hari", hari);
                    params.put("jam_mulai", jam_mulai);
                    params.put("jam_selesai", jam_selesai);
                    params.put("tahun_ajaran", tahun_ajaran);
                    params.put("jenis_jadwal", jenis_jadwal);
                    params.put("nis", nis);
                    params.put("nip", nip);
                } else {
                    params.put("id_jadwal", id_jadwal);
                    params.put("kode_kelas", kd_kelas);
                    params.put("kode_mapel", kd_mapel);
                    params.put("hari", hari);
                    params.put("jam_mulai", jam_mulai);
                    params.put("jam_selesai", jam_selesai);
                    params.put("tahun_ajaran", tahun_ajaran);
                    params.put("jenis_jadwal", jenis_jadwal);
                    params.put("nis", nis);
                    params.put("nip", nip);
                }

                return params;
            }

        };

        Controller.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void edit(final String id_jadwalx){
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
                        String v1 = jObj.getString(TAG_ID_JADWAL);
                        String v2 = jObj.getString(TAG_KODE_KELAS);
                        String v3 = jObj.getString(TAG_KODE_MAPEL);
                        String v4 = jObj.getString(TAG_HARI);
                        String v5 = jObj.getString(TAG_JAM_MULAI);
                        String v6 = jObj.getString(TAG_JAM_SELESAI);
                        String v7 = jObj.getString(TAG_TAHUN_AJARAN);
                        String v8 = jObj.getString(TAG_JENIS_JADWAL);
                        String v9 = nis;
                        String v10 = nip;

                        DialogForm(v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, "UPDATE");

                        subJadwalAdapter.notifyDataSetChanged();

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
                Toast.makeText(JadwalSiswaActivity.this, "Tidak dapat mengambil data. Pastikan koneksi internet anda aktif!", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_jadwal", id_jadwalx);

                return params;
            }

        };

        Controller.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void delete(final String id_jadwalx){
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

                        subJadwalAdapter.notifyDataSetChanged();

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
                Toast.makeText(JadwalSiswaActivity.this, "Tidak dapat menghapus data. Pastikan koneksi internet anda aktif!", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_jadwal", id_jadwalx);

                return params;
            }

        };

        Controller.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void cariData(String keyword){
        itemListJadwalSiswa.clear();
        subJadwalAdapter.notifyDataSetChanged();
        swipeJadwal.setRefreshing(true);
        String url_tampiljadwal = ServerData.URL + "caridata_jadwalsiswa.php?nis="+nis+"&kode_mapel="+kd_mapel+"&keyword="+keyword;

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

                            DataJadwal item = new DataJadwal();

                            item.setId_jadwal(obj.getString(TAG_ID_JADWAL));
                            item.setKode_kelas(obj.getString(TAG_KODE_KELAS));
                            item.setKode_mapel(obj.getString(TAG_KODE_MAPEL));
                            item.setMapel(nm_mapel);
                            item.setHari(obj.getString(TAG_HARI));
                            item.setJam_mulai(obj.getString(TAG_JAM_MULAI));
                            item.setJam_selesai(obj.getString(TAG_JAM_SELESAI));
                            item.setTahun_ajaran(obj.getString(TAG_TAHUN_AJARAN));
                            item.setJenis_jadwal(obj.getString(TAG_JENIS_JADWAL));
                            item.setNis(obj.getString(TAG_NIS));

                            // menambah item ke array
                            itemListJadwalSiswa.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // notifikasi adanya perubahan data pada adapter
                subJadwalAdapter.notifyDataSetChanged();

                swipeJadwal.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipeJadwal.setRefreshing(false);
                Toast.makeText(JadwalSiswaActivity.this, "Tidak dapat mencari data. Pastikan koneksi internet anda aktif!", Toast.LENGTH_LONG).show();
            }
        });

        // menambah request ke request queue
        Controller.getInstance().addToRequestQueue(jArr);
    }
}