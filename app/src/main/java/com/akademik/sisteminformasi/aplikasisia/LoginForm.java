package com.akademik.sisteminformasi.aplikasisia;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginForm extends AppCompatActivity {
    ProgressDialog pDialog;
    Button btn_login;
    EditText txt_username, txt_password;
    Spinner spn_hakakses;
    Intent intent;
    String hakakses;

    int success;
    ConnectivityManager conMgr;

    private String url = ServerData.URL + "login.php";
    private String[] hak_akses = {"Login sebagai:", "Guru", "Murid", "Wali Murid"};

    private static final String TAG = LoginForm.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    public final static String TAG_NAMA = "nama";
    public final static String TAG_ID = "id";
    public final static String TAG_FOTO = "foto";
    public final static String TAG_KODE_MAPEL = "kode_mapel";
    public final static String TAG_NAMA_MAPEL = "nama_mapel";
    public final static String TAG_KODE_KELAS = "kode_kelas";
    public final static String TAG_KELAS = "kelas";
    public final static String TAG_SUB_KELAS = "sub_kelas";
    public final static String TAG_NIS = "nis";
    public final static String TAG_NAMA_SISWA = "nama_siswa";
    public final static String TAG_HAK_AKSES = "hak_akses";

    String tag_json_obj = "json_obj_req";

    SharedPreferences sharedpreferences;
    Boolean session = false;
    String id, nama, kode_mapel, nama_mapel, kode_kelas, kelas, sub_kelas, nis, nama_siswa, foto;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet!",
                        Toast.LENGTH_LONG).show();
            }
        }

        btn_login = (Button)findViewById(R.id.loginBtn);
        txt_username = (EditText)findViewById(R.id.usernameTxt);
        txt_password = (EditText)findViewById(R.id.passwordTxt);
        spn_hakakses = (Spinner)findViewById(R.id.hak_akses);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, hak_akses);
        spn_hakakses.setAdapter(adapter);
        spn_hakakses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapter.getItem(i).equalsIgnoreCase("Login Sebagai:")){
                    hakakses = null;
                }
                else {
                    hakakses = adapter.getItem(i);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                hakakses = null;
            }
        });

        sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(session_status, false);
        id = sharedpreferences.getString(TAG_ID, null);
        nama = sharedpreferences.getString(TAG_NAMA, null);
        kode_mapel = sharedpreferences.getString(TAG_KODE_MAPEL, null);
        nama_mapel = sharedpreferences.getString(TAG_NAMA_MAPEL, null);

        kode_kelas = sharedpreferences.getString(TAG_KODE_KELAS, null);
        kelas = sharedpreferences.getString(TAG_KELAS, null);
        sub_kelas = sharedpreferences.getString(TAG_SUB_KELAS, null);

        nis = sharedpreferences.getString(TAG_NIS, null);
        nama_siswa = sharedpreferences.getString(TAG_NAMA_SISWA, null);

        foto = sharedpreferences.getString(TAG_FOTO, null);
        hakakses = sharedpreferences.getString(TAG_HAK_AKSES, null);

        if (session) {
            if(hakakses.equalsIgnoreCase("Guru")){
                Intent intent = new Intent(LoginForm.this, MainActivity.class);
                intent.putExtra(TAG_ID, id);
                intent.putExtra(TAG_NAMA, nama);
                intent.putExtra(TAG_KODE_MAPEL, kode_mapel);
                intent.putExtra(TAG_NAMA_MAPEL, nama_mapel);
                intent.putExtra(TAG_FOTO, foto);
                intent.putExtra(TAG_HAK_AKSES, hakakses);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            } else if(hakakses.equalsIgnoreCase("Murid")){
                Intent intent = new Intent(LoginForm.this, MainActivity.class);
                intent.putExtra(TAG_ID, id);
                intent.putExtra(TAG_NAMA, nama);
                intent.putExtra(TAG_KODE_KELAS, kode_kelas);
                intent.putExtra(TAG_KELAS, kelas);
                intent.putExtra(TAG_SUB_KELAS, sub_kelas);
                intent.putExtra(TAG_FOTO, foto);
                intent.putExtra(TAG_HAK_AKSES, hakakses);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            } else if(hakakses.equalsIgnoreCase("Wali Murid")){
                Intent intent = new Intent(LoginForm.this, MainActivity.class);
                intent.putExtra(TAG_ID, id);
                intent.putExtra(TAG_NAMA, nama);
                intent.putExtra(TAG_NIS, nis);
                intent.putExtra(TAG_NAMA_SISWA, nama_siswa);
                intent.putExtra(TAG_FOTO, foto);
                intent.putExtra(TAG_HAK_AKSES, hakakses);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }

        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txt_username.getText().toString();
                String password = txt_password.getText().toString();

                if (username.trim().length() > 0 && password.trim().length() > 0) {
                    if (hakakses != null) {
                        if (conMgr.getActiveNetworkInfo() != null
                                && conMgr.getActiveNetworkInfo().isAvailable()
                                && conMgr.getActiveNetworkInfo().isConnected()) {
                            checkLogin(username, password, hakakses);
                        } else {
                            Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext() ,"Pilih hak akses terlebih dahulu!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext() ,"Kolom tidak boleh ada yang kosong!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void checkLogin(final String username, final String password, final String hakakses) {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Memproses...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {
                        if(hakakses.equalsIgnoreCase("Guru")){
                            String username = jObj.getString(TAG_NAMA);
                            String id = jObj.getString(TAG_ID);
                            String kd_mapel = jObj.getString(TAG_KODE_MAPEL);
                            String nm_mapel = jObj.getString(TAG_NAMA_MAPEL);
                            String fotonya = jObj.getString(TAG_FOTO);

                            Log.e("Successfully Login!", jObj.toString());

                            Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                            // menyimpan login ke session
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putBoolean(session_status, true);
                            editor.putString(TAG_ID, id);
                            editor.putString(TAG_NAMA, username);
                            editor.putString(TAG_KODE_MAPEL, kd_mapel);
                            editor.putString(TAG_NAMA_MAPEL, nm_mapel);
                            editor.putString(TAG_FOTO, fotonya);
                            editor.putString(TAG_HAK_AKSES, hakakses);
                            editor.commit();

                            // Memanggil main activity
                            Intent intent = new Intent(LoginForm.this, MainActivity.class);
                            intent.putExtra(TAG_ID, id);
                            intent.putExtra(TAG_NAMA, username);
                            intent.putExtra(TAG_KODE_MAPEL, kd_mapel);
                            intent.putExtra(TAG_NAMA_MAPEL, nm_mapel);
                            intent.putExtra(TAG_FOTO, fotonya);
                            intent.putExtra(TAG_HAK_AKSES, hakakses);
                            finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.animation_enter2, R.anim.animation_leave2);
                        }
                        else if(hakakses.equalsIgnoreCase("Murid")){
                            String username = jObj.getString(TAG_NAMA);
                            String id = jObj.getString(TAG_ID);
                            String kd_kelas = jObj.getString(TAG_KODE_KELAS);
                            String kelasnya = jObj.getString(TAG_KELAS);
                            String sub_kelasnya = jObj.getString(TAG_SUB_KELAS);
                            String fotonya = jObj.getString(TAG_FOTO);

                            Log.e("Successfully Login!", jObj.toString());

                            Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putBoolean(session_status, true);
                            editor.putString(TAG_ID, id);
                            editor.putString(TAG_NAMA, username);
                            editor.putString(TAG_KODE_KELAS, kd_kelas);
                            editor.putString(TAG_KELAS, kelasnya);
                            editor.putString(TAG_SUB_KELAS, sub_kelasnya);
                            editor.putString(TAG_FOTO, fotonya);
                            editor.putString(TAG_HAK_AKSES, hakakses);
                            editor.commit();


                            Intent intent = new Intent(LoginForm.this, MainActivity.class);
                            intent.putExtra(TAG_ID, id);
                            intent.putExtra(TAG_NAMA, username);
                            intent.putExtra(TAG_KODE_KELAS, kd_kelas);
                            intent.putExtra(TAG_KELAS, kelasnya);
                            intent.putExtra(TAG_SUB_KELAS, sub_kelasnya);
                            intent.putExtra(TAG_FOTO, fotonya);
                            intent.putExtra(TAG_HAK_AKSES, hakakses);
                            finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.animation_enter2, R.anim.animation_leave2);
                        }
                        else if (hakakses.equalsIgnoreCase("Wali Murid")){
                            String username = jObj.getString(TAG_NAMA);
                            String id = jObj.getString(TAG_ID);
                            String nisnya = jObj.getString(TAG_NIS);
                            String nama_siswanya = jObj.getString(TAG_NAMA_SISWA);
                            String fotonya = jObj.getString(TAG_FOTO);

                            Log.e("Successfully Login!", jObj.toString());

                            Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putBoolean(session_status, true);
                            editor.putString(TAG_ID, id);
                            editor.putString(TAG_NAMA, username);
                            editor.putString(TAG_NIS, nisnya);
                            editor.putString(TAG_NAMA_SISWA, nama_siswanya);
                            editor.putString(TAG_FOTO, fotonya);
                            editor.putString(TAG_HAK_AKSES, hakakses);
                            editor.commit();


                            Intent intent = new Intent(LoginForm.this, MainActivity.class);
                            intent.putExtra(TAG_ID, id);
                            intent.putExtra(TAG_NAMA, username);
                            intent.putExtra(TAG_NIS, nisnya);
                            intent.putExtra(TAG_NAMA_SISWA, nama_siswanya);
                            intent.putExtra(TAG_FOTO, fotonya);
                            intent.putExtra(TAG_HAK_AKSES, hakakses);
                            finish();
                            startActivity(intent);
                            overridePendingTransition(R.anim.animation_enter2, R.anim.animation_leave2);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(LoginForm.this, "Tidak dapat terhubung ke server. Silahkan coba lagi!", Toast.LENGTH_LONG).show();

                hideDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                params.put("hak_akses", hakakses);

                return params;
            }

        };

        // Adding request to request queue
        Controller.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}