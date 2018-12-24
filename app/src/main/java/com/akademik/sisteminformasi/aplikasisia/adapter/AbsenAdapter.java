package com.akademik.sisteminformasi.aplikasisia.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.akademik.sisteminformasi.aplikasisia.model.CircularNetworkImageView;
import com.akademik.sisteminformasi.aplikasisia.Controller;
import com.akademik.sisteminformasi.aplikasisia.R;
import com.akademik.sisteminformasi.aplikasisia.ServerData;
import com.akademik.sisteminformasi.aplikasisia.data.DataSiswa;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

/**
 * Created by Evander Filipi on 14/11/2018.
 */

public class AbsenAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataSiswa> items;
    ImageLoader imageLoader= Controller.getInstance().getmImageLoader();
    private static String url_def_img = ServerData.URL + "foto_profil/default/user.png";

    public AbsenAdapter(Activity activity, List<DataSiswa> items) {
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
            view = inflater.inflate(R.layout.list_row_dataabsen, null);

        if (imageLoader == null)
            imageLoader=Controller.getInstance().getmImageLoader();

        CircularNetworkImageView imageView= (CircularNetworkImageView) view.findViewById(R.id.foto_Siswa_AbsenImg);

        TextView nama = view.findViewById(R.id.nama_Siswa_AbsenTxt);
        TextView nis = view.findViewById(R.id.nis_Siswa_AbsenTxt);
        TextView kelas = view.findViewById(R.id.kelas_Siswa_AbsenTxt);

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
