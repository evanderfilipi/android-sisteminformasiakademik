package com.akademik.sisteminformasi.aplikasisia;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.akademik.sisteminformasi.aplikasisia.adapter.NilaiAdapter;
import com.akademik.sisteminformasi.aplikasisia.data.DataSiswa;
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

/**
 * Created by Evander Filipi on 02/11/2018.
 */

public class DataNilaiFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener {

    ListView listSiswa;
    TextView tvnotif;
    SwipeRefreshLayout swipeSiswa;
    List<DataSiswa> itemListNilaiSiswa = new ArrayList<DataSiswa>();
    NilaiAdapter nilaiAdapter;
    int success;
    String nip, kd_mapel, nm_mapel;
    private static final String TAG = DataNilaiFragment.class.getSimpleName();

    private static String url_select = ServerData.URL + "tampildata_siswa.php";

    public static final String TAG_NIS = "nis";
    public static final String TAG_NAMA_SISWA = "nama_siswa";
    public static final String TAG_KODE_KELAS = "kd_kelas";
    public static final String TAG_KELAS = "kelas";
    public static final String TAG_SUB_KELAS = "sub_kelas";
    public static final String TAG_FOTO = "foto";

    public static final String PUT_NIP = "nip";
    public static final String PUT_KODE_MAPEL = "kd_mapel";
    public static final String PUT_NAMA_MAPEL = "nm_mapel";

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_datanilai, container, false);
        getActivity().setTitle("Nilai Siswa");
        setHasOptionsMenu(true);
        tvnotif = (TextView)v.findViewById(R.id.notifSiswa3Txt);
        tvnotif.setVisibility(View.INVISIBLE);
        swipeSiswa = v.findViewById(R.id.swipe_refresh1);
        swipeSiswa.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        listSiswa = (ListView)v.findViewById(R.id.list_nilai);
        nilaiAdapter = new NilaiAdapter(getActivity(), itemListNilaiSiswa);
        listSiswa.setAdapter(nilaiAdapter);
        if (getArguments()!=null){
            nip = getArguments().getString(PUT_NIP);
            kd_mapel = getArguments().getString(PUT_KODE_MAPEL);
            nm_mapel = getArguments().getString(PUT_NAMA_MAPEL);
        }
        swipeSiswa.setOnRefreshListener(this);

        swipeSiswa.post(new Runnable() {
                       @Override
                       public void run() {
                           swipeSiswa.setRefreshing(true);
                           getData();
                       }
                   }
        );

        listSiswa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
                String nisnya = itemListNilaiSiswa.get(position).getNis();
                String namasiswanya = itemListNilaiSiswa.get(position).getNama_siswa();
                Intent intent = new Intent(getActivity(), NilaiSiswaActivity.class);
                intent.putExtra(TAG_NIS, nisnya);
                intent.putExtra(TAG_NAMA_SISWA, namasiswanya);
                intent.putExtra(PUT_NIP, nip);
                intent.putExtra(PUT_KODE_MAPEL, kd_mapel);
                intent.putExtra(PUT_NAMA_MAPEL, nm_mapel);
                startActivity(intent);
            }
        });

        return v;
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
    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint("Cari NIS/Nama/Kode Kelas");
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(this);
    }

    public void onRefresh() {
        getData();
    }

    private void getData(){
        itemListNilaiSiswa.clear();
        swipeSiswa.setRefreshing(true);

        // membuat request JSON
        JsonArrayRequest jArr = new JsonArrayRequest(url_select, new Response.Listener<JSONArray>() {
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

                            DataSiswa item = new DataSiswa();

                            item.setNis(obj.getString(TAG_NIS));
                            item.setNama_siswa(obj.getString(TAG_NAMA_SISWA));
                            item.setKode_kelas(obj.getString(TAG_KODE_KELAS));
                            item.setKelas(obj.getString(TAG_KELAS));
                            item.setSub_kelas(obj.getString(TAG_SUB_KELAS));
                            item.setFoto(obj.getString(TAG_FOTO));

                            // menambah item ke array
                            itemListNilaiSiswa.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // notifikasi adanya perubahan data pada adapter
                nilaiAdapter.notifyDataSetChanged();

                swipeSiswa.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipeSiswa.setRefreshing(false);
                Toast.makeText(getActivity(), "Tidak dapat mengambil data. Pastikan koneksi internet anda aktif!", Toast.LENGTH_LONG).show();
            }
        });

        // menambah request ke request queue
        Controller.getInstance().addToRequestQueue(jArr);
    }

    private void cariData(String keyword){
        itemListNilaiSiswa.clear();
        nilaiAdapter.notifyDataSetChanged();
        swipeSiswa.setRefreshing(true);
        String url_tampilsiswa = ServerData.URL + "caridata_siswa.php?keyword="+keyword;

        JsonArrayRequest jArr = new JsonArrayRequest(url_tampilsiswa, new Response.Listener<JSONArray>() {
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

                            DataSiswa item = new DataSiswa();

                            item.setNis(obj.getString(TAG_NIS));
                            item.setNama_siswa(obj.getString(TAG_NAMA_SISWA));
                            item.setKode_kelas(obj.getString(TAG_KODE_KELAS));
                            item.setKelas(obj.getString(TAG_KELAS));
                            item.setSub_kelas(obj.getString(TAG_SUB_KELAS));
                            item.setFoto(obj.getString(TAG_FOTO));

                            // menambah item ke array
                            itemListNilaiSiswa.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // notifikasi adanya perubahan data pada adapter
                nilaiAdapter.notifyDataSetChanged();

                swipeSiswa.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                swipeSiswa.setRefreshing(false);
                Toast.makeText(getActivity(), "Tidak dapat mencari data. Pastikan koneksi internet anda aktif!", Toast.LENGTH_LONG).show();
            }
        });

        // menambah request ke request queue
        Controller.getInstance().addToRequestQueue(jArr);
    }
}
