package com.akademik.sisteminformasi.aplikasisia.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.akademik.sisteminformasi.aplikasisia.R;
import com.akademik.sisteminformasi.aplikasisia.data.DataAbsen;

import java.util.List;

/**
 * Created by Evander Filipi on 14/11/2018.
 */

public class SubAbsenAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataAbsen> items;

    public SubAbsenAdapter(Activity activity, List<DataAbsen> items) {
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
            view = inflater.inflate(R.layout.list_row_dataabsen_sub, null);

        TextView id_absen = view.findViewById(R.id.id_AbsenTxt);
        TextView tahun_ajaran = view.findViewById(R.id.tahun_Ajaran_AbsenTxt);
        TextView semester = view.findViewById(R.id.semester_AbsenTxt);
        TextView nama_mapel = view.findViewById(R.id.nama_Mapel_AbsenTxt);

        DataAbsen dataAbsen = items.get(position);

        id_absen.setText(dataAbsen.getId_absen());
        tahun_ajaran.setText(dataAbsen.getTahun_ajaran());
        semester.setText(dataAbsen.getSemester());
        nama_mapel.setText(dataAbsen.getMapel());

        return view;
    }
}
