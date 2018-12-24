package com.akademik.sisteminformasi.aplikasisia;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.akademik.sisteminformasi.aplikasisia.model.CircularNetworkImageView;
import com.android.volley.toolbox.ImageLoader;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    CountDownTimer timerfrag, timerfrag1;
    FrameLayout fraglayout;
    static String id;
    String nama;
    String kode_mapel;
    static String nama_mapel;
    String kode_kelas;
    static String kelas;
    static String sub_kelas;
    static String hak_akses;
    String foto;
    static TextView tv1;
    static TextView tv2;
    static SharedPreferences sharedpreferences;
    String nisnya;
    static String nama_siswanya;
    static CircularNetworkImageView foto_profil;
    NavigationView navigationView;
    static ImageLoader imageLoader = Controller.getInstance().getmImageLoader();

    private static String url_def_img = ServerData.URL + "foto_profil/default/user.png";

    public static final String TAG_ID = "id";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_KODE_MAPEL = "kode_mapel";
    public static final String TAG_NAMA_MAPEL = "nama_mapel";
    public final static String TAG_KODE_KELAS = "kode_kelas";
    public final static String TAG_KELAS = "kelas";
    public final static String TAG_SUB_KELAS = "sub_kelas";
    public static final String TAG_NIS = "nis";
    public static final String TAG_NAMA_SISWA = "nama_siswa";
    public final static String TAG_HAK_AKSES = "hak_akses";
    public final static String TAG_FOTO = "foto";

    public static final String PUT_NIP = "nip";
    public static final String PUT_KODE_MAPEL = "kd_mapel";
    public static final String PUT_NAMA_MAPEL = "nm_mapel";

    public static final String PUT_ID_WALIMURID = "id_walimurid";
    public static final String PUT_NIS = "nis";
    public static final String PUT_NAMA_SISWA = "nama_siswa";
    public static final String PUT_KODE_KELAS = "kd_kelas";
    public static final String PUT_KELAS = "kelasnya";
    public static final String PUT_SUB_KELAS = "sub_kelasnya";
    public static final String PUT_HAK_AKSES = "hk_akses";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedpreferences = getSharedPreferences(LoginForm.my_shared_preferences, Context.MODE_PRIVATE);

        hak_akses = getIntent().getStringExtra(TAG_HAK_AKSES);
        if(hak_akses.equalsIgnoreCase("Guru")){
            id = getIntent().getStringExtra(TAG_ID);
            nama = getIntent().getStringExtra(TAG_NAMA);
            kode_mapel = getIntent().getStringExtra(TAG_KODE_MAPEL);
            nama_mapel = getIntent().getStringExtra(TAG_NAMA_MAPEL);
            foto = getIntent().getStringExtra(TAG_FOTO);
        } else if(hak_akses.equalsIgnoreCase("Murid")){
            id = getIntent().getStringExtra(TAG_ID);
            nama = getIntent().getStringExtra(TAG_NAMA);
            kode_kelas = getIntent().getStringExtra(TAG_KODE_KELAS);
            kelas = getIntent().getStringExtra(TAG_KELAS);
            sub_kelas = getIntent().getStringExtra(TAG_SUB_KELAS);
            foto = getIntent().getStringExtra(TAG_FOTO);
        } else if(hak_akses.equalsIgnoreCase("Wali Murid")){
            id = getIntent().getStringExtra(TAG_ID);
            nama = getIntent().getStringExtra(TAG_NAMA);
            nisnya = getIntent().getStringExtra(TAG_NIS);
            nama_siswanya = getIntent().getStringExtra(TAG_NAMA_SISWA);
            foto = getIntent().getStringExtra(TAG_FOTO);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(3).setChecked(true);
        PengumumanFragment fragDefault = new PengumumanFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_fragment, fragDefault);
        fragmentTransaction.commit();
        View v = navigationView.getHeaderView(0);
        Menu menu = navigationView.getMenu();
        MenuItem menu1 = menu.findItem(R.id.menu1);
        MenuItem menu2 = menu.findItem(R.id.menu2);
        MenuItem menu3 = menu.findItem(R.id.menu3);
        foto_profil = (CircularNetworkImageView)v.findViewById(R.id.fotoProfilImages);
        tv1 = (TextView)v.findViewById(R.id.iduserTxt);
        tv2 = (TextView)v.findViewById(R.id.namauserTxt);
        if(hak_akses.equalsIgnoreCase("Guru")){
            menu1.setTitle("Kelola Absensi Siswa");
            menu2.setTitle("Kelola Jadwal Siswa");
            menu3.setTitle("Kelola Nilai Siswa");
            tv1.setText(""+id+" (Guru "+nama_mapel+")");
            tv2.setText(""+nama);
            if(foto.isEmpty()){
                foto_profil.setImageUrl(url_def_img, imageLoader);
            } else {
                foto_profil.setImageUrl(ServerData.URL+""+foto, imageLoader);
            }
        } else if(hak_akses.equalsIgnoreCase("Murid")){
            menu1.setTitle("Data Absensi");
            menu2.setTitle("Jadwal Pelajaran");
            menu3.setTitle("Nilai Akademik");
            tv1.setText(""+id+" (Siswa kelas "+kelas+"-"+sub_kelas+")");
            tv2.setText(""+nama);
            if(foto.isEmpty()){
                foto_profil.setImageUrl(url_def_img, imageLoader);
            } else {
                foto_profil.setImageUrl(ServerData.URL+""+foto, imageLoader);
            }
        } else if(hak_akses.equalsIgnoreCase("Wali Murid")) {
            menu1.setTitle("Data Absensi");
            menu2.setTitle("Jadwal Pelajaran");
            menu3.setTitle("Nilai Akademik");
            tv1.setText(""+id+" (Wali Murid "+nama_siswanya+")");
            tv2.setText(""+nama);
            if(foto.isEmpty()){
                foto_profil.setImageUrl(url_def_img, imageLoader);
            } else {
                foto_profil.setImageUrl(ServerData.URL+""+foto, imageLoader);
            }
        }

        fraglayout = (FrameLayout)findViewById(R.id.frame_fragment);
        timerfrag = new CountDownTimer(100, 100) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                fraglayout.setVisibility(View.VISIBLE);
                fraglayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha));
            }
        };
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.tentang) {
            startActivity(new Intent(MainActivity.this, TentangActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu1) {
            fraglayout.setVisibility(View.INVISIBLE);
            fraglayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha2));
            timerfrag1 = new CountDownTimer(500, 500) {
                @Override
                public void onTick(long l) {
                }

                @Override
                public void onFinish() {
                    if (hak_akses.equalsIgnoreCase("Guru")){
                        final String nip = getIntent().getStringExtra(TAG_ID);
                        final String kd_mapel = getIntent().getStringExtra(TAG_KODE_MAPEL);
                        final String nm_mapel = getIntent().getStringExtra(TAG_NAMA_MAPEL);
                        Bundle bundle1 = new Bundle();
                        bundle1.putString(PUT_NIP, nip);
                        bundle1.putString(PUT_KODE_MAPEL, kd_mapel);
                        bundle1.putString(PUT_NAMA_MAPEL, nm_mapel);
                        DataAbsenFragment frag1 = new DataAbsenFragment();
                        frag1.setArguments(bundle1);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_fragment, frag1);
                        fragmentTransaction.commit();
                        timerfrag.start();
                    } else if (hak_akses.equalsIgnoreCase("Murid")){
                        final String nis = getIntent().getStringExtra(TAG_ID);
                        final String nama_siswa = getIntent().getStringExtra(TAG_NAMA);
                        Bundle bundle1 = new Bundle();
                        bundle1.putString(PUT_NIS, nis);
                        bundle1.putString(PUT_NAMA_SISWA, nama_siswa);
                        AbsensiSiswaFragment frag1 = new AbsensiSiswaFragment();
                        frag1.setArguments(bundle1);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_fragment, frag1);
                        fragmentTransaction.commit();
                        timerfrag.start();
                    } else if (hak_akses.equalsIgnoreCase("Wali Murid")) {
                        final String nisx = getIntent().getStringExtra(TAG_NIS);
                        final String nama_siswax = getIntent().getStringExtra(TAG_NAMA_SISWA);
                        Bundle bundle1 = new Bundle();
                        bundle1.putString(PUT_NIS, nisx);
                        bundle1.putString(PUT_NAMA_SISWA, nama_siswax);
                        AbsensiSiswaFragment frag1 = new AbsensiSiswaFragment();
                        frag1.setArguments(bundle1);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_fragment, frag1);
                        fragmentTransaction.commit();
                        timerfrag.start();
                    }
                }
            }.start();
        }
        else if (id == R.id.menu2) {
            fraglayout.setVisibility(View.INVISIBLE);
            fraglayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha2));
            timerfrag1 = new CountDownTimer(500, 500) {
                @Override
                public void onTick(long l) {
                }

                @Override
                public void onFinish() {
                    if (hak_akses.equalsIgnoreCase("Guru")){
                        final String nip = getIntent().getStringExtra(TAG_ID);
                        final String kd_mapel = getIntent().getStringExtra(TAG_KODE_MAPEL);
                        final String nm_mapel = getIntent().getStringExtra(TAG_NAMA_MAPEL);
                        Bundle bundle2 = new Bundle();
                        bundle2.putString(PUT_NIP, nip);
                        bundle2.putString(PUT_KODE_MAPEL, kd_mapel);
                        bundle2.putString(PUT_NAMA_MAPEL, nm_mapel);
                        DataJadwalFragment frag2 = new DataJadwalFragment();
                        frag2.setArguments(bundle2);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_fragment, frag2);
                        fragmentTransaction.commit();
                        timerfrag.start();
                    } else if (hak_akses.equalsIgnoreCase("Murid")){
                        final String nis = getIntent().getStringExtra(TAG_ID);
                        final String nama_siswa = getIntent().getStringExtra(TAG_NAMA);
                        Bundle bundle2 = new Bundle();
                        bundle2.putString(PUT_NIS, nis);
                        bundle2.putString(PUT_NAMA_SISWA, nama_siswa);
                        JadwalMapelSiswaFragment frag2 = new JadwalMapelSiswaFragment();
                        frag2.setArguments(bundle2);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_fragment, frag2);
                        fragmentTransaction.commit();
                        timerfrag.start();
                    } else if (hak_akses.equalsIgnoreCase("Wali Murid")) {
                        final String nisx = getIntent().getStringExtra(TAG_NIS);
                        final String nama_siswax = getIntent().getStringExtra(TAG_NAMA_SISWA);
                        Bundle bundle2 = new Bundle();
                        bundle2.putString(PUT_NIS, nisx);
                        bundle2.putString(PUT_NAMA_SISWA, nama_siswax);
                        JadwalMapelSiswaFragment frag2 = new JadwalMapelSiswaFragment();
                        frag2.setArguments(bundle2);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_fragment, frag2);
                        fragmentTransaction.commit();
                        timerfrag.start();
                    }

                }
            }.start();
        }
        else if (id == R.id.menu3) {
            fraglayout.setVisibility(View.INVISIBLE);
            fraglayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha2));
            timerfrag1 = new CountDownTimer(500, 500) {
                @Override
                public void onTick(long l) {
                }

                @Override
                public void onFinish() {
                    if (hak_akses.equalsIgnoreCase("Guru")){
                        final String nip = getIntent().getStringExtra(TAG_ID);
                        final String kd_mapel = getIntent().getStringExtra(TAG_KODE_MAPEL);
                        final String nm_mapel = getIntent().getStringExtra(TAG_NAMA_MAPEL);
                        Bundle bundle3 = new Bundle();
                        bundle3.putString(PUT_NIP, nip);
                        bundle3.putString(PUT_KODE_MAPEL, kd_mapel);
                        bundle3.putString(PUT_NAMA_MAPEL, nm_mapel);
                        DataNilaiFragment frag3 = new DataNilaiFragment();
                        frag3.setArguments(bundle3);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_fragment, frag3);
                        fragmentTransaction.commit();
                        timerfrag.start();
                    } else if (hak_akses.equalsIgnoreCase("Murid")){
                        final String nis = getIntent().getStringExtra(TAG_ID);
                        final String nama_siswa = getIntent().getStringExtra(TAG_NAMA);
                        Bundle bundle3 = new Bundle();
                        bundle3.putString(PUT_NIS, nis);
                        bundle3.putString(PUT_NAMA_SISWA, nama_siswa);
                        NilaiAkademikSiswaFragment frag3 = new NilaiAkademikSiswaFragment();
                        frag3.setArguments(bundle3);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_fragment, frag3);
                        fragmentTransaction.commit();
                        timerfrag.start();
                    } else if (hak_akses.equalsIgnoreCase("Wali Murid")) {
                        final String nisx = getIntent().getStringExtra(TAG_NIS);
                        final String nama_siswax = getIntent().getStringExtra(TAG_NAMA_SISWA);
                        Bundle bundle3 = new Bundle();
                        bundle3.putString(PUT_NIS, nisx);
                        bundle3.putString(PUT_NAMA_SISWA, nama_siswax);
                        NilaiAkademikSiswaFragment frag3 = new NilaiAkademikSiswaFragment();
                        frag3.setArguments(bundle3);
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_fragment, frag3);
                        fragmentTransaction.commit();
                        timerfrag.start();
                    }

                }
            }.start();
        } else if (id == R.id.menu4) {
            fraglayout.setVisibility(View.INVISIBLE);
            fraglayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha2));
            timerfrag1 = new CountDownTimer(500, 500) {
                @Override
                public void onTick(long l) {
                }

                @Override
                public void onFinish() {
                    PengumumanFragment frag4 = new PengumumanFragment();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_fragment, frag4);
                    fragmentTransaction.commit();
                    timerfrag.start();

                }
            }.start();
        } else if (id == R.id.profil) {
            final String hak_akses = getIntent().getStringExtra(TAG_HAK_AKSES);
            if(hak_akses.equalsIgnoreCase("Guru")){
                final String nip = getIntent().getStringExtra(TAG_ID);
                final String kd_mapel = getIntent().getStringExtra(TAG_KODE_MAPEL);
                final String nm_mapel = getIntent().getStringExtra(TAG_NAMA_MAPEL);
                Intent intent = new Intent(MainActivity.this, ProfileUserActivity.class);
                intent.putExtra(PUT_NIP, nip);
                intent.putExtra(PUT_KODE_MAPEL, kd_mapel);
                intent.putExtra(PUT_NAMA_MAPEL, nm_mapel);
                intent.putExtra(PUT_HAK_AKSES, hak_akses);
                startActivity(intent);
            } else if(hak_akses.equalsIgnoreCase("Murid")){
                final String nis = getIntent().getStringExtra(TAG_ID);
                final String kd_kelas = getIntent().getStringExtra(TAG_KODE_KELAS);
                final String kelasnya = getIntent().getStringExtra(TAG_KELAS);
                final String sub_kelasnya = getIntent().getStringExtra(TAG_SUB_KELAS);
                Intent intent = new Intent(MainActivity.this, ProfileUserActivity.class);
                intent.putExtra(PUT_NIS, nis);
                intent.putExtra(PUT_KODE_KELAS, kd_kelas);
                intent.putExtra(PUT_KELAS, kelasnya);
                intent.putExtra(PUT_SUB_KELAS, sub_kelasnya);
                intent.putExtra(PUT_HAK_AKSES, hak_akses);
                startActivity(intent);
            } else if(hak_akses.equalsIgnoreCase("Wali Murid")){
                final String id_walimurid = getIntent().getStringExtra(TAG_ID);
                final String nama_siswax = getIntent().getStringExtra(TAG_NAMA_SISWA);
                Intent intent = new Intent(MainActivity.this, ProfileUserActivity.class);
                intent.putExtra(PUT_ID_WALIMURID, id_walimurid);
                intent.putExtra(PUT_NAMA_SISWA, nama_siswax);
                intent.putExtra(PUT_HAK_AKSES, hak_akses);
                startActivity(intent);
            }

        } else if (id == R.id.logout) {
            new AlertDialog.Builder(this)
                    .setMessage("Anda yakin ingin logout?")
                    .setCancelable(false)
                    .setPositiveButton("Iya", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int id){
                            final String hak_akses = getIntent().getStringExtra(TAG_HAK_AKSES);
                            if(hak_akses.equalsIgnoreCase("Guru")){
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putBoolean(LoginForm.session_status, false);
                                editor.putString(TAG_ID, null);
                                editor.putString(TAG_NAMA, null);
                                editor.putString(TAG_KODE_MAPEL, null);
                                editor.putString(TAG_NAMA_MAPEL, null);
                                editor.putString(TAG_FOTO, null);
                                editor.putString(TAG_HAK_AKSES, null);
                                editor.commit();
                                Intent intent = new Intent(MainActivity.this, LoginForm.class);
                                finish();
                                startActivity(intent);
                            } else if(hak_akses.equalsIgnoreCase("Murid")){
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putBoolean(LoginForm.session_status, false);
                                editor.putString(TAG_ID, null);
                                editor.putString(TAG_NAMA, null);
                                editor.putString(TAG_KODE_KELAS, null);
                                editor.putString(TAG_KELAS, null);
                                editor.putString(TAG_SUB_KELAS, null);
                                editor.putString(TAG_FOTO, null);
                                editor.putString(TAG_HAK_AKSES, null);
                                editor.commit();
                                Intent intent = new Intent(MainActivity.this, LoginForm.class);
                                finish();
                                startActivity(intent);
                            } else if(hak_akses.equalsIgnoreCase("Wali Murid")){
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putBoolean(LoginForm.session_status, false);
                                editor.putString(TAG_ID, null);
                                editor.putString(TAG_NAMA, null);
                                editor.putString(TAG_NIS, null);
                                editor.putString(TAG_NAMA_SISWA, null);
                                editor.putString(TAG_FOTO, null);
                                editor.putString(TAG_HAK_AKSES, null);
                                editor.commit();
                                Intent intent = new Intent(MainActivity.this, LoginForm.class);
                                finish();
                                startActivity(intent);
                            }
                        }
                    })
                    .setNegativeButton("Tidak", null)
                    .show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void updates (String namaUser, String urlFotoUser){
        try {
            if (hak_akses.equalsIgnoreCase("Guru")) {
                tv1.setText("" + id + " (Guru " + nama_mapel + ")");
                if (urlFotoUser.isEmpty()){
                    foto_profil.setImageUrl(url_def_img, imageLoader);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(TAG_NAMA, namaUser);
                    editor.putString(TAG_FOTO, urlFotoUser);
                    editor.commit();
                } else if (urlFotoUser.equalsIgnoreCase("tidak")){
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(TAG_NAMA, namaUser);
                    editor.commit();
                } else {
                    foto_profil.setImageUrl(ServerData.URL+""+urlFotoUser, imageLoader);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(TAG_NAMA, namaUser);
                    editor.putString(TAG_FOTO, urlFotoUser);
                    editor.commit();
                }
                tv2.setText("" + namaUser);
            } else if (hak_akses.equalsIgnoreCase("Murid")) {
                tv1.setText(""+id+" (Siswa kelas "+kelas+"-"+sub_kelas+")");
                if (urlFotoUser.isEmpty()){
                    foto_profil.setImageUrl(url_def_img, imageLoader);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(TAG_NAMA, namaUser);
                    editor.putString(TAG_FOTO, urlFotoUser);
                    editor.commit();
                } else if (urlFotoUser.equalsIgnoreCase("tidak")){
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(TAG_NAMA, namaUser);
                    editor.commit();
                } else {
                    foto_profil.setImageUrl(ServerData.URL+""+urlFotoUser, imageLoader);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(TAG_NAMA, namaUser);
                    editor.putString(TAG_FOTO, urlFotoUser);
                    editor.commit();
                }
                tv2.setText(""+namaUser);
            } else if (hak_akses.equalsIgnoreCase("Wali Murid")) {
                tv1.setText(""+id+" (Wali Murid "+nama_siswanya+")");
                if (urlFotoUser.isEmpty()){
                    foto_profil.setImageUrl(url_def_img, imageLoader);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(TAG_NAMA, namaUser);
                    editor.putString(TAG_FOTO, urlFotoUser);
                    editor.commit();
                } else if (urlFotoUser.equalsIgnoreCase("tidak")){
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(TAG_NAMA, namaUser);
                    editor.commit();
                } else {
                    foto_profil.setImageUrl(ServerData.URL+""+urlFotoUser, imageLoader);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(TAG_NAMA, namaUser);
                    editor.putString(TAG_FOTO, urlFotoUser);
                    editor.commit();
                }
                tv2.setText(""+namaUser);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}