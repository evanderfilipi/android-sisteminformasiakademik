package com.akademik.sisteminformasi.aplikasisia.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.akademik.sisteminformasi.aplikasisia.R;
import com.akademik.sisteminformasi.aplikasisia.data.DataJadwal;

import java.util.List;

/**
 * Created by Evander Filipi on 12/11/2018.
 */

public class SubJadwalAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataJadwal> items;

    public SubJadwalAdapter(Activity activity, List<DataJadwal> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null)
            view = inflater.inflate(R.layout.list_row_datajadwal_sub, null);

        TextView id_jadwal = view.findViewById(R.id.id_JadwalTxt);
        TextView tahun_ajaran = view.findViewById(R.id.tahun_Ajaran_JadwalTxt);
        TextView jenis_jadwal = view.findViewById(R.id.jenis_JadwalTxt);
        TextView hari = view.findViewById(R.id.hari_JadwalTxt);

        DataJadwal dataJadwal = items.get(position);

        id_jadwal.setText(dataJadwal.getId_jadwal());
        tahun_ajaran.setText(dataJadwal.getTahun_ajaran());
        hari.setText(dataJadwal.getHari());
        jenis_jadwal.setText("Jadwal "+dataJadwal.getJenis_jadwal());

        return view;
    }
}
