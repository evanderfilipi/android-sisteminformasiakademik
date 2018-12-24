package com.akademik.sisteminformasi.aplikasisia.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akademik.sisteminformasi.aplikasisia.R;
import com.akademik.sisteminformasi.aplikasisia.data.DataNilai;

import java.util.List;

/**
 * Created by Evander Filipi on 09/12/2018.
 */

public class NilaiSiswaAdapter extends RecyclerView.Adapter<NilaiSiswaAdapter.ViewHolder> {
    private List<DataNilai> nilai;
    Activity activity;

    public NilaiSiswaAdapter(Activity activity, List<DataNilai> nilai) {
        this.nilai = nilai;
        this.activity = activity;
    }

    @Override
    public NilaiSiswaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.isi_nilai, null);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mapel.setText(String.valueOf(nilai.get(position).getMapel()));
        holder.uh.setText(String.valueOf(nilai.get(position).getNilai_uh()));
        holder.tugas.setText(String.valueOf(nilai.get(position).getNilai_tgs()));
        holder.uts.setText(String.valueOf(nilai.get(position).getNilai_uts()));
        holder.uas.setText(String.valueOf(nilai.get(position).getNilai_uas()));
        holder.nilaiakhir.setText(String.valueOf(nilai.get(position).getNilai_akhir()));
        holder.namaguru.setText(String.valueOf(nilai.get(position).getNama_guru()));
    }

    @Override
    public int getItemCount() {
        return nilai.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mapel, uh, tugas, uts, uas, nilaiakhir, namaguru;

        public ViewHolder(View itemView) {
            super(itemView);
            mapel = (TextView)itemView.findViewById(R.id.mapelNilaiTxt);
            uh = (TextView)itemView.findViewById(R.id.uhNilaiTxt);
            tugas = (TextView)itemView.findViewById(R.id.tugasNilaiTxt);
            uts = (TextView)itemView.findViewById(R.id.utsNilaiTxt);
            uas = (TextView)itemView.findViewById(R.id.uasNilaiTxt);
            nilaiakhir = (TextView)itemView.findViewById(R.id.nilaiAkhirTxt);
            namaguru = (TextView)itemView.findViewById(R.id.guru3Txt);
        }
    }
}
