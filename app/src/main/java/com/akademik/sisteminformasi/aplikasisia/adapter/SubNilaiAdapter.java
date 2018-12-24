package com.akademik.sisteminformasi.aplikasisia.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.Filter;
import android.widget.TextView;

import com.akademik.sisteminformasi.aplikasisia.R;
import com.akademik.sisteminformasi.aplikasisia.data.DataNilai;

import java.util.List;

/**
 * Created by Evander Filipi on 08/11/2018.
 */

public class SubNilaiAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataNilai> items;

    public SubNilaiAdapter(Activity activity, List<DataNilai> items) {
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
            view = inflater.inflate(R.layout.list_row_datanilai_sub, null);

        TextView id_nilai = view.findViewById(R.id.id_NilaiTxt);
        TextView tahun_ajaran = view.findViewById(R.id.tahun_Ajaran_NilaiTxt);
        TextView semester = view.findViewById(R.id.semester_NilaiTxt);
        TextView nama_mapel = view.findViewById(R.id.nama_Mapel_NilaiTxt);

        DataNilai dataNilai = items.get(position);

        id_nilai.setText(dataNilai.getId_nilai());
        tahun_ajaran.setText(dataNilai.getTahun_ajaran());
        semester.setText(dataNilai.getSemester());
        nama_mapel.setText(dataNilai.getMapel());

        return view;
    }
}
