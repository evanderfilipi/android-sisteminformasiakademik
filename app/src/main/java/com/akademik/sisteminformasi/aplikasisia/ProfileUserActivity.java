package com.akademik.sisteminformasi.aplikasisia;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.system.ErrnoException;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akademik.sisteminformasi.aplikasisia.model.CircularNetworkImageView;
import com.akademik.sisteminformasi.aplikasisia.model.CustomNetworkImageView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ProfileUserActivity extends AppCompatActivity {

    Button pilihGambarBtn, kalenderBtn, simpanBtn, gantiPassBtn;
    DatePickerDialog datePicker;
    SimpleDateFormat formatTgl;
    AlertDialog.Builder dialog;
    EditText namaUser_Edt, tanggalLahir_Edt, telepon_Edt, alamat_Edt, passLama_Edt, passBaru_Edt, konfirPass_Edt;
    TextView idTxt, idHeadTxt, statusTxt, pass_notifTxt;
    LayoutInflater inflaters;
    View dialogView;
    Bitmap bitmap, decoded;
    int success;
    int bitmap_size = 60;
    static CircularNetworkImageView fotoProfilImg2;
    CustomNetworkImageView fullImg;
    RelativeLayout rellay;
    String getNamaUser;
    static String url_foto_new;
    Animation anim_in, anim_out;
    ImageView closeImage;

    static ImageLoader imageLoader = Controller.getInstance().getmImageLoader();
    private static String url_def_img = ServerData.URL + "foto_profil/default/user.png";
    private static String update_profil = ServerData.URL + "update_profil.php";
    private static String update_password = ServerData.URL + "update_password.php";

    private static String tampil = ServerData.URL + "lihat_profil.php";
    private static final String TAG_ID_USER = "id_user";
    private static final String TAG_HAK_AKSES = "hak_akses";
    private static final String TAG_NAMA_USER_GURU = "nama_guru";
    private static final String TAG_NAMA_USER_SISWA = "nama_siswa";
    private static final String TAG_NAMA_USER_WALIMURID = "nama_walimurid";
    private static final String TAG_TANGGAL_LAHIR = "tgl_lahir";
    private static final String TAG_NO_TELEPON = "no_telpon";
    private static final String TAG_ALAMAT = "alamat";
    private static final String TAG_FOTO = "foto";
    private static final String TAG_GAMBAR = "gambar";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_FOTO_BARU = "foto_baru";

    public static final String PUT_NIP = "nip";
    public static final String PUT_KODE_MAPEL = "kd_mapel";
    public static final String PUT_NAMA_MAPEL = "nm_mapel";

    public static final String PUT_NIS = "nis";
    public static final String PUT_KODE_KELAS = "kd_kelas";
    public static final String PUT_KELAS = "kelasnya";
    public static final String PUT_SUB_KELAS = "sub_kelasnya";

    public static final String PUT_ID_WALIMURID = "id_walimurid";
    public static final String PUT_NAMA_SISWA = "nama_siswa";
    public final static String PUT_HAK_AKSES = "hk_akses";
    String nip;
    String kd_mapel;
    String nm_mapel;
    String nis;
    String kd_kelas;
    String kelasnya;
    String sub_kelasnya;
    String id_walimurid, nama_siswanya;
    static String hk_akses;
    String pass1;
    String pass2;
    String pass3;
    static String foto;
    String changeImg = "no";
    Boolean showPhoto = false;

    String tag_json_obj = "json_obj_req";

    private static final String TAG = ProfileUserActivity.class.getSimpleName();
    private Uri mCropImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        this.setTitle("Pengaturan Profil");

        hk_akses = getIntent().getStringExtra(PUT_HAK_AKSES);
        simpanBtn = (Button)findViewById(R.id.simpanPBtn);
        simpanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeImg = "no";
                simpanProfil();
            }
        });

        pilihGambarBtn = (Button)findViewById(R.id.pilihBtn);
        pilihGambarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] dialogitem = {"Pilih Foto", "Hapus Foto"};
                dialog = new AlertDialog.Builder(ProfileUserActivity.this);
                dialog.setCancelable(true);
                dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:
                                startActivityForResult(getPickImageChooserIntent(), 200);
                                break;
                            case 1:
                                removeImg();
                                break;
                        }
                    }
                }).show();
            }
        });
        anim_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);
        anim_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out2);
        fotoProfilImg2 = (CircularNetworkImageView)findViewById(R.id.profileImg2);
        rellay = (RelativeLayout)findViewById(R.id.showRL);
        rellay.setVisibility(View.INVISIBLE);
        fullImg = (CustomNetworkImageView)findViewById(R.id.showImg);
        fotoProfilImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(foto.isEmpty()){
                    showPhoto = false;
                    Toast.makeText(getApplicationContext(), "Foto profil anda tidak terpasang. Silahkan pilih foto profil!", Toast.LENGTH_LONG).show();
                } else {
                    showPhoto = true;
                    rellay.setVisibility(View.VISIBLE);
                    fullImg.setImageUrl(ServerData.URL+""+foto, imageLoader);
                    rellay.startAnimation(anim_in);
                }
            }
        });
        formatTgl = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        idTxt = (TextView)findViewById(R.id.userIdTxt);
        namaUser_Edt = (EditText)findViewById(R.id.nama_Edt);
        tanggalLahir_Edt = (EditText)findViewById(R.id.tanggal_Edt);
        telepon_Edt = (EditText)findViewById(R.id.telepon_Edt);
        alamat_Edt = (EditText)findViewById(R.id.alamat_Edt);
        idHeadTxt = (TextView)findViewById(R.id.userIdHeadTxt);
        statusTxt = (TextView)findViewById(R.id.statusTxt);
        if(hk_akses.equalsIgnoreCase("Guru")){
            idHeadTxt.setText("NIP (Nomor Induk Pegawai)");
            nip = getIntent().getStringExtra(PUT_NIP);
            kd_mapel = getIntent().getStringExtra(PUT_KODE_MAPEL);
            nm_mapel = getIntent().getStringExtra(PUT_NAMA_MAPEL);
            statusTxt.setText("(Guru "+nm_mapel+")");
            tampil_profil(nip);
        } else if(hk_akses.equalsIgnoreCase("Murid")){
            idHeadTxt.setText("NIS (Nomor Induk Siswa)");
            nis = getIntent().getStringExtra(PUT_NIS);
            kd_kelas = getIntent().getStringExtra(PUT_KODE_KELAS);
            kelasnya = getIntent().getStringExtra(PUT_KELAS);
            sub_kelasnya = getIntent().getStringExtra(PUT_SUB_KELAS);
            statusTxt.setText("(Siswa kelas "+kelasnya+"-"+sub_kelasnya+")");
            tampil_profil(nis);
        } else if(hk_akses.equalsIgnoreCase("Wali Murid")){
            idHeadTxt.setText("ID Wali Murid");
            id_walimurid = getIntent().getStringExtra(PUT_ID_WALIMURID);
            nama_siswanya = getIntent().getStringExtra(PUT_NAMA_SISWA);
            statusTxt.setText("(Wali Murid "+nama_siswanya+")");
            tampil_profil(id_walimurid);
        }
        kalenderBtn = (Button)findViewById(R.id.calenderBtn);
        kalenderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tanggalDialog();
            }
        });
        gantiPassBtn = (Button)findViewById(R.id.passwordBtn);
        gantiPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogForm("SIMPAN");
            }
        });
        closeImage = (ImageView)findViewById(R.id.closeImg);
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rellay.startAnimation(anim_out);
                rellay.setVisibility(View.INVISIBLE);
                showPhoto = false;
            }
        });
    }

    private void tanggalDialog(){
        Calendar newCalendar = Calendar.getInstance();
        datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tanggalLahir_Edt.setText(""+formatTgl.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    private void DialogForm(String button) {
        dialog = new AlertDialog.Builder(ProfileUserActivity.this);
        inflaters = getLayoutInflater();
        dialogView = inflaters.inflate(R.layout.form_password, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setIcon(R.drawable.icon_apk);
        dialog.setTitle("Ubah Password");

        pass_notifTxt = (TextView) dialogView.findViewById(R.id.passNotifTxt);
        pass_notifTxt.setText("Password Baru Min. 6 Karakter");
        passLama_Edt = (EditText) dialogView.findViewById(R.id.passLama_Edt);
        passBaru_Edt = (EditText) dialogView.findViewById(R.id.passBaru_Edt);
        konfirPass_Edt = (EditText) dialogView.findViewById(R.id.konfirPass_Edt);

        passBaru_Edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().trim().length() < 6) {
                    pass_notifTxt.setText("Password Baru Min. 6 Karakter");
                } else {
                    pass_notifTxt.setText("");
                }
            }
        });

        dialog.setPositiveButton(button, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                pass1 = passLama_Edt.getText().toString().trim();
                pass2 = passBaru_Edt.getText().toString().trim();
                pass3 = konfirPass_Edt.getText().toString().trim();
                if(pass1.isEmpty() || pass2.isEmpty() || pass3.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Kolom password tidak boleh ada yang kosong!",
                            Toast.LENGTH_LONG).show();
                } else {
                    if(pass2.length() < 6){
                        Toast.makeText(getApplicationContext(), "Password baru harus min. 6 karakter!",
                                Toast.LENGTH_LONG).show();
                    } else {
                        if(pass2.equalsIgnoreCase(pass3)){
                            if(hk_akses.equalsIgnoreCase("Guru")){
                                updatePass(nip);
                            } else if(hk_akses.equalsIgnoreCase("Murid")){
                                updatePass(nis);
                            } else if(hk_akses.equalsIgnoreCase("Wali Murid")){
                                updatePass(id_walimurid);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Password baru dengan konfirmasi password tidak sama!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void tampil_profil(final String id_user){
        StringRequest strReq = new StringRequest(Request.Method.POST, tampil, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Cek error node pada json
                    if (success == 1) {
                        if(hk_akses.equalsIgnoreCase("Guru")){
                            Log.d("get edit data", jObj.toString());
                            String namauser = jObj.getString(TAG_NAMA_USER_GURU);
                            String tanggallahir = jObj.getString(TAG_TANGGAL_LAHIR);
                            String telepon = jObj.getString(TAG_NO_TELEPON);
                            String alamat = jObj.getString(TAG_ALAMAT);
                            foto = jObj.getString(TAG_FOTO);
                            if(foto.isEmpty()){
                                fotoProfilImg2.setImageUrl(url_def_img, imageLoader);
                            } else {
                                fotoProfilImg2.setImageUrl(ServerData.URL+""+foto, imageLoader);
                            }

                            idTxt.setText(nip);
                            namaUser_Edt.setText(namauser);
                            tanggalLahir_Edt.setText(tanggallahir);
                            telepon_Edt.setText(telepon);
                            alamat_Edt.setText(alamat);
                        }
                        else if(hk_akses.equalsIgnoreCase("Murid")){
                            Log.d("get edit data", jObj.toString());
                            String namauser = jObj.getString(TAG_NAMA_USER_SISWA);
                            String tanggallahir = jObj.getString(TAG_TANGGAL_LAHIR);
                            String telepon = jObj.getString(TAG_NO_TELEPON);
                            String alamat = jObj.getString(TAG_ALAMAT);
                            foto = jObj.getString(TAG_FOTO);
                            if(foto.isEmpty()){
                                fotoProfilImg2.setImageUrl(url_def_img, imageLoader);
                            } else {
                                fotoProfilImg2.setImageUrl(ServerData.URL+""+foto, imageLoader);
                            }

                            idTxt.setText(nis);
                            namaUser_Edt.setText(namauser);
                            tanggalLahir_Edt.setText(tanggallahir);
                            telepon_Edt.setText(telepon);
                            alamat_Edt.setText(alamat);
                        }
                        else if(hk_akses.equalsIgnoreCase("Wali Murid")){
                            Log.d("get edit data", jObj.toString());
                            String namauser = jObj.getString(TAG_NAMA_USER_WALIMURID);
                            String tanggallahir = jObj.getString(TAG_TANGGAL_LAHIR);
                            String telepon = jObj.getString(TAG_NO_TELEPON);
                            String alamat = jObj.getString(TAG_ALAMAT);
                            foto = jObj.getString(TAG_FOTO);
                            if(foto.isEmpty()){
                                fotoProfilImg2.setImageUrl(url_def_img, imageLoader);
                            } else {
                                fotoProfilImg2.setImageUrl(ServerData.URL+""+foto, imageLoader);
                            }

                            idTxt.setText(id_walimurid);
                            namaUser_Edt.setText(namauser);
                            tanggalLahir_Edt.setText(tanggallahir);
                            telepon_Edt.setText(telepon);
                            alamat_Edt.setText(alamat);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(ProfileUserActivity.this, "Tidak dapat menampilkan profil anda. Pastikan koneksi internet anda aktif!", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_user", id_user);
                params.put("hak_akses", hk_akses);

                return params;
            }

        };

        Controller.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void updatePass(final String id_user){
        final ProgressDialog loading2 = ProgressDialog.show(this, "Update Password", "Mohon tunggu...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, update_password,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                Log.e("v Add", jObj.toString());
                                Toast.makeText(ProfileUserActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(ProfileUserActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loading2.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading2.dismiss();
                        Log.e(TAG, error.getMessage().toString());
                        Toast.makeText(ProfileUserActivity.this, "Tidak dapat memperbarui password. Pastikan koneksi internet anda aktif!", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();

                //menambah parameter yang di kirim ke web servis
                params.put("id_user", id_user);
                params.put("hak_akses", hk_akses);
                params.put("pass_lama", pass1);
                params.put("pass_baru", pass2);
                params.put("konfir_pass", pass3);

                //kembali ke parameters
                Log.e(TAG, "" + params);
                return params;
            }
        };

        Controller.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }

    private void simpanProfil() {
        final ProgressDialog loading = ProgressDialog.show(this, "Update Profile User", "Mohon tunggu...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, update_profil,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                Log.e("v Add", jObj.toString());
                                url_foto_new = jObj.getString(TAG_FOTO_BARU);
                                getNamaUser = namaUser_Edt.getText().toString();
                                MainActivity.updates(getNamaUser, url_foto_new);
                                getImg(url_foto_new);
                                if(hk_akses.equalsIgnoreCase("Guru")){
                                    tampil_profil(nip);
                                } else if(hk_akses.equalsIgnoreCase("Murid")){
                                    tampil_profil(nis);
                                } else if(hk_akses.equalsIgnoreCase("Wali Murid")){
                                    tampil_profil(id_walimurid);
                                }
                                Toast.makeText(ProfileUserActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(ProfileUserActivity.this, jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Log.e(TAG, error.getMessage().toString());
                        Toast.makeText(ProfileUserActivity.this, "Tidak dapat memperbarui profil. Pastikan koneksi internet anda aktif!", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();

                //menambah parameter yang di kirim ke web servis
                if(hk_akses.equalsIgnoreCase("Guru")){
                    if(changeImg.equalsIgnoreCase("no") || changeImg.equalsIgnoreCase("hapus")) {
                        params.put(TAG_ID_USER, idTxt.getText().toString());
                        params.put(TAG_HAK_AKSES, hk_akses);
                        params.put(TAG_NAMA_USER_GURU, namaUser_Edt.getText().toString());
                        params.put(TAG_TANGGAL_LAHIR, tanggalLahir_Edt.getText().toString());
                        params.put(TAG_NO_TELEPON, telepon_Edt.getText().toString());
                        params.put(TAG_ALAMAT, alamat_Edt.getText().toString());
                        params.put(TAG_GAMBAR, changeImg);
                    } else {
                        params.put(TAG_FOTO, getStringImage(decoded));
                        params.put(TAG_ID_USER, idTxt.getText().toString());
                        params.put(TAG_HAK_AKSES, hk_akses);
                        params.put(TAG_NAMA_USER_GURU, namaUser_Edt.getText().toString());
                        params.put(TAG_TANGGAL_LAHIR, tanggalLahir_Edt.getText().toString());
                        params.put(TAG_NO_TELEPON, telepon_Edt.getText().toString());
                        params.put(TAG_ALAMAT, alamat_Edt.getText().toString());
                        params.put(TAG_GAMBAR, changeImg);
                    }
                } else if(hk_akses.equalsIgnoreCase("Murid")){
                    if(changeImg.equalsIgnoreCase("no") || changeImg.equalsIgnoreCase("hapus")) {
                        params.put(TAG_ID_USER, idTxt.getText().toString());
                        params.put(TAG_HAK_AKSES, hk_akses);
                        params.put(TAG_NAMA_USER_SISWA, namaUser_Edt.getText().toString());
                        params.put(TAG_TANGGAL_LAHIR, tanggalLahir_Edt.getText().toString());
                        params.put(TAG_NO_TELEPON, telepon_Edt.getText().toString());
                        params.put(TAG_ALAMAT, alamat_Edt.getText().toString());
                        params.put(TAG_GAMBAR, changeImg);
                    } else {
                        params.put(TAG_FOTO, getStringImage(decoded));
                        params.put(TAG_ID_USER, idTxt.getText().toString());
                        params.put(TAG_HAK_AKSES, hk_akses);
                        params.put(TAG_NAMA_USER_SISWA, namaUser_Edt.getText().toString());
                        params.put(TAG_TANGGAL_LAHIR, tanggalLahir_Edt.getText().toString());
                        params.put(TAG_NO_TELEPON, telepon_Edt.getText().toString());
                        params.put(TAG_ALAMAT, alamat_Edt.getText().toString());
                        params.put(TAG_GAMBAR, changeImg);
                    }
                } else if(hk_akses.equalsIgnoreCase("Wali Murid")){
                    if(changeImg.equalsIgnoreCase("no") || changeImg.equalsIgnoreCase("hapus")) {
                        params.put(TAG_ID_USER, idTxt.getText().toString());
                        params.put(TAG_HAK_AKSES, hk_akses);
                        params.put(TAG_NAMA_USER_WALIMURID, namaUser_Edt.getText().toString());
                        params.put(TAG_TANGGAL_LAHIR, tanggalLahir_Edt.getText().toString());
                        params.put(TAG_NO_TELEPON, telepon_Edt.getText().toString());
                        params.put(TAG_ALAMAT, alamat_Edt.getText().toString());
                        params.put(TAG_GAMBAR, changeImg);
                    } else {
                        params.put(TAG_FOTO, getStringImage(decoded));
                        params.put(TAG_ID_USER, idTxt.getText().toString());
                        params.put(TAG_HAK_AKSES, hk_akses);
                        params.put(TAG_NAMA_USER_WALIMURID, namaUser_Edt.getText().toString());
                        params.put(TAG_TANGGAL_LAHIR, tanggalLahir_Edt.getText().toString());
                        params.put(TAG_NO_TELEPON, telepon_Edt.getText().toString());
                        params.put(TAG_ALAMAT, alamat_Edt.getText().toString());
                        params.put(TAG_GAMBAR, changeImg);
                    }
                }

                //kembali ke parameters
                Log.e(TAG, "" + params);
                return params;
            }
        };

        Controller.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = getPickImageResultUri(data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage,
            // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
            boolean requirePermissions = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    isUriRequiresPermissions(imageUri)) {

                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true;
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }

            if (!requirePermissions) {
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(this);
            }

        } if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri mImgUri = result.getUri();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImgUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setToImageView(getResizedBitmap(bitmap, 768));

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(ProfileUserActivity.this, "Gagal mendapatkan foto/gambar. Silahkan coba lagi!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void removeImg() {
        changeImg = "hapus";
        simpanProfil();
    }

    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
        changeImg = "yes";
        simpanProfil();
    }

    // fungsi resize image
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            CropImage.activity(mCropImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        } else {
            Toast.makeText(ProfileUserActivity.this, "Akses/permission yang dibutuhkan tidak diizinkan oleh sistem!", Toast.LENGTH_LONG).show();
        }
    }

    public Intent getPickImageChooserIntent() {
        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Edit Foto Profil");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    /**
     * Get URI to image received from capture by camera.
     */
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }

    /**
     * Get the URI of the selected image from {@link #getPickImageChooserIntent()}.<br/>
     * Will return the correct URI for camera and gallery image.
     *
     * @param data the returned data of the activity result
     */
    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    /**
     * Test if we can open the given Android URI to test if permission required error is thrown.<br>
     */
    public boolean isUriRequiresPermissions(Uri uri) {
        try {
            ContentResolver resolver = getContentResolver();
            InputStream stream = resolver.openInputStream(uri);
            stream.close();
            return false;
        } catch (FileNotFoundException e) {
            if (e.getCause() instanceof ErrnoException) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static void getImg(String urlnew) {
        if(hk_akses.equalsIgnoreCase("Guru")){
            if(urlnew.isEmpty()){
                fotoProfilImg2.setImageUrl(url_def_img, imageLoader);
            } else {
                fotoProfilImg2.setImageUrl(ServerData.URL+""+urlnew, imageLoader);
            }
        } else if(hk_akses.equalsIgnoreCase("Murid")){
            if(urlnew.isEmpty()){
                fotoProfilImg2.setImageUrl(url_def_img, imageLoader);
            } else {
                fotoProfilImg2.setImageUrl(ServerData.URL+""+urlnew, imageLoader);
            }
        } else if(hk_akses.equalsIgnoreCase("Wali Murid")){
            if(urlnew.isEmpty()){
                fotoProfilImg2.setImageUrl(url_def_img, imageLoader);
            } else {
                fotoProfilImg2.setImageUrl(ServerData.URL+""+urlnew, imageLoader);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(showPhoto){
            rellay.startAnimation(anim_out);
            rellay.setVisibility(View.INVISIBLE);
            showPhoto = false;
        } else {
            super.onBackPressed();
        }
    }
}