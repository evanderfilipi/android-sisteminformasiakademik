package com.akademik.sisteminformasi.aplikasisia.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akademik.sisteminformasi.aplikasisia.R;
import com.akademik.sisteminformasi.aplikasisia.data.DataPengumuman;

import java.util.List;

/**
 * Created by Evander Filipi on 11/12/2018.
 */

public class PengumumanAdapter extends RecyclerView.Adapter<PengumumanAdapter.ViewHolder> {
    private List<DataPengumuman> pengumuman;
    Activity activity;

    public PengumumanAdapter(Activity activity, List<DataPengumuman> pengumuman) {
        this.pengumuman = pengumuman;
        this.activity = activity;
    }

    @Override
    public PengumumanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.isi_pengumuman, null);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.judul.setText(String.valueOf(pengumuman.get(position).getJudul()));
        holder.deskripsi.setText(String.valueOf(pengumuman.get(position).getDeskripsi()));
        holder.tanggal.setText(String.valueOf(pengumuman.get(position).getTanggal()));
    }

    @Override
    public int getItemCount() {
        return pengumuman.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView judul, deskripsi, tanggal;

        public ViewHolder(View itemView) {
            super(itemView);
            judul = (TextView)itemView.findViewById(R.id.judulTxt);
            deskripsi = (TextView)itemView.findViewById(R.id.deskripsiTxt);
            tanggal = (TextView)itemView.findViewById(R.id.tanggalTxt);
        }
    }
}
