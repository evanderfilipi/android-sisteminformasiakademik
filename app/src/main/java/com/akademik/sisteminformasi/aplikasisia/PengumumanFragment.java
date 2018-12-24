package com.akademik.sisteminformasi.aplikasisia;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.akademik.sisteminformasi.aplikasisia.adapter.PengumumanAdapter;
import com.akademik.sisteminformasi.aplikasisia.data.DataPengumuman;
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
 * Created by Evander Filipi on 11/12/2018.
 */

public class PengumumanFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView;
    LinearLayoutManager llm;
    PengumumanAdapter pengumumanAdapter;
    List<DataPengumuman> pengumumanArr = new ArrayList<DataPengumuman>();
    SwipeRefreshLayout swipePengumuman;
    TextView notif;

    private static final String TAG = PengumumanFragment.class.getSimpleName();

    public static final String TAG_ID_PENGUMUMAN = "id_pengumuman";
    public static final String TAG_JUDUL = "judul";
    public static final String TAG_DESKRIPSI = "deskripsi";
    public static final String TAG_TANGGAL = "tanggal_pelaksanaan";

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pengumuman, container, false);
        getActivity().setTitle("Pengumuman");
        notif = (TextView)v.findViewById(R.id.notifikasiTxt);
        notif.setVisibility(View.INVISIBLE);
        swipePengumuman = v.findViewById(R.id.pengumumanSR);
        swipePengumuman.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        swipePengumuman.setOnRefreshListener(this);

        swipePengumuman.post(new Runnable() {
                            @Override
                            public void run() {
                                swipePengumuman.setRefreshing(true);
                                getPengumuman();
                            }
                        }
        );
        recyclerView = v.findViewById(R.id.recyclerPengumuman);
        llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);

        pengumumanAdapter = new PengumumanAdapter(getActivity(), pengumumanArr);
        recyclerView.setAdapter(pengumumanAdapter);

        return v;
    }

    public void onRefresh() {
        getPengumuman();
    }

    private void getPengumuman(){
        String url_tampiljadwal = ServerData.URL + "tampil_pengumuman.php";
        swipePengumuman.setRefreshing(true);

        pengumumanArr.clear();
        JsonArrayRequest jArr = new JsonArrayRequest(url_tampiljadwal, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                if(response.length() <= 0){
                    notif.setVisibility(View.VISIBLE);
                    notif.setText("Tidak Ada Pengumuman");
                } else {
                    notif.setVisibility(View.INVISIBLE);
                    notif.setText("");

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);

                            DataPengumuman item = new DataPengumuman();

                            item.setId_pengumuman(obj.getString(TAG_ID_PENGUMUMAN));
                            item.setJudul(obj.getString(TAG_JUDUL));
                            item.setDeskripsi(obj.getString(TAG_DESKRIPSI));
                            item.setTanggal(obj.getString(TAG_TANGGAL));

                            pengumumanArr.add(item);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                pengumumanAdapter.notifyDataSetChanged();

                swipePengumuman.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                swipePengumuman.setRefreshing(false);
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                notif.setVisibility(View.INVISIBLE);
                notif.setText("");
                Toast.makeText(getActivity(), "Tidak dapat mengambil data. Pastikan koneksi internet anda aktif!", Toast.LENGTH_LONG).show();
            }
        });

        Controller.getInstance().addToRequestQueue(jArr);
    }
}
