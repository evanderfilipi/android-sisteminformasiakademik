package com.akademik.sisteminformasi.aplikasisia.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.akademik.sisteminformasi.aplikasisia.model.CircularNetworkImageView;
import com.akademik.sisteminformasi.aplikasisia.R;
import com.akademik.sisteminformasi.aplikasisia.ServerData;
import com.akademik.sisteminformasi.aplikasisia.data.DataSiswa;
import com.akademik.sisteminformasi.aplikasisia.Controller;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

/**
 * Created by Evander Filipi on 30/10/2018.
 */

public class NilaiAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataSiswa> items;
    ImageLoader imageLoader= Controller.getInstance().getmImageLoader();
    private static String url_def_img = ServerData.URL + "foto_profil/default/user.png";

    public NilaiAdapter(Activity activity, List<DataSiswa> items) {
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
            view = inflater.inflate(R.layout.list_row_datanilai, null);

        if (imageLoader == null)
            imageLoader=Controller.getInstance().getmImageLoader();

        CircularNetworkImageView imageView= (CircularNetworkImageView) view.findViewById(R.id.foto_Siswa_NilaiImg);

        TextView nama = view.findViewById(R.id.nama_Siswa_NilaiTxt);
        TextView nis = view.findViewById(R.id.nis_Siswa_NilaiTxt);
        TextView kelas = view.findViewById(R.id.kelas_Siswa_NilaiTxt);

        DataSiswa dataSiswa = items.get(position);

        if(dataSiswa.getFoto().toString().isEmpty()){
            imageView.setImageUrl(url_def_img, imageLoader);
        }
        else {
            imageView.setImageUrl(ServerData.URL+""+dataSiswa.getFoto(), imageLoader);
        }
        nama.setText(dataSiswa.getNama_siswa());
        nis.setText(dataSiswa.getNis());
        kelas.setText("Kelas "+dataSiswa.getKelas()+"-"+dataSiswa.getSub_kelas()+" ("+dataSiswa.getKode_kelas()+")");

        return view;
    }
}
