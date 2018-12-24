package com.akademik.sisteminformasi.aplikasisia.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akademik.sisteminformasi.aplikasisia.R;
import com.akademik.sisteminformasi.aplikasisia.data.DataJadwal;

import java.util.List;

/**
 * Created by Evander Filipi on 10/12/2018.
 */

public class JadwalSiswaAdapter extends RecyclerView.Adapter<JadwalSiswaAdapter.ViewHolder> {
    private List<DataJadwal> jadwal;
    Activity activity;

    public JadwalSiswaAdapter(Activity activity, List<DataJadwal> jadwal) {
        this.jadwal = jadwal;
        this.activity = activity;
    }

    @Override
    public JadwalSiswaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.isi_jadwal, null);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mapel.setText(String.valueOf(jadwal.get(position).getMapel()));
        holder.mulai.setText(String.valueOf(jadwal.get(position).getJam_mulai()));
        holder.selesai.setText(String.valueOf(jadwal.get(position).getJam_selesai()));
        holder.namaguru.setText(String.valueOf(jadwal.get(position).getNama_guru()));
    }

    @Override
    public int getItemCount() {
        return jadwal.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mapel, mulai, selesai, namaguru;

        public ViewHolder(View itemView) {
            super(itemView);
            mapel = (TextView)itemView.findViewById(R.id.mapelAbsensiTxt);
            mulai = (TextView)itemView.findViewById(R.id.jamMulaiJadwalTxt);
            selesai = (TextView)itemView.findViewById(R.id.jamSelesaiJadwalTxt);
            namaguru = (TextView)itemView.findViewById(R.id.guru2Txt);
        }
    }
}
