package com.akademik.sisteminformasi.aplikasisia.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akademik.sisteminformasi.aplikasisia.data.DataAbsen;
import com.akademik.sisteminformasi.aplikasisia.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evander Filipi on 09/12/2018.
 */

public class AbsensiSiswaAdapter extends RecyclerView.Adapter<AbsensiSiswaAdapter.ViewHolder> {
    private List<DataAbsen> absensi;
    Activity activity;

    public AbsensiSiswaAdapter(Activity activity, List<DataAbsen> absensi) {
        this.absensi = absensi;
        this.activity = activity;
    }

    @Override
    public AbsensiSiswaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.isi_absensi, null);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mapel.setText(String.valueOf(absensi.get(position).getMapel()));
        holder.sakit.setText(String.valueOf(absensi.get(position).getSakit()));
        holder.izin.setText(String.valueOf(absensi.get(position).getIzin()));
        holder.tanpaket.setText(String.valueOf(absensi.get(position).getTanpa_keterangan()));
        holder.totalper.setText(String.valueOf(absensi.get(position).getTotal_pertemuan()));
        holder.namaguru.setText(String.valueOf(absensi.get(position).getNama_guru()));
    }

    @Override
    public int getItemCount() {
        return absensi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mapel, sakit, izin, tanpaket, totalper, namaguru;

        public ViewHolder(View itemView) {
            super(itemView);
            mapel = (TextView)itemView.findViewById(R.id.mapelAbsensiTxt);
            sakit = (TextView)itemView.findViewById(R.id.sakitTxt);
            izin = (TextView)itemView.findViewById(R.id.izinTxt);
            tanpaket = (TextView)itemView.findViewById(R.id.tanpaKeteranganTxt);
            totalper = (TextView)itemView.findViewById(R.id.pertemuanTxt);
            namaguru = (TextView)itemView.findViewById(R.id.guruTxt);
        }
    }
}
