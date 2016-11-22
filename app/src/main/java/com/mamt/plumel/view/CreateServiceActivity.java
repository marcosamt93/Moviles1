package com.mamt.plumel.view;

import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mamt.plumel.LoginActivity;
import com.mamt.plumel.Models.Usuario;
import com.mamt.plumel.R;
import com.mamt.plumel.VolleyS;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CreateServiceActivity extends AppCompatActivity {

    private TextInputEditText txt_service_date;
    private TextInputEditText txt_hour_service;
    private TextInputEditText txt_description_service;
    private TextInputEditText txt_serviceReferencias;
    private RadioGroup radio_service;
    private RadioButton radioButton;
    private VolleyS volley;
    protected RequestQueue fRequestQueue;
    private String latitud;
    private String longuitud;
    private Button btn_createServiceComplete;
    private String address;
    private String id_User;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_service);

        volley = VolleyS.getInstance(CreateServiceActivity.this);
        fRequestQueue = volley.getRequestQueue();




        txt_service_date = (TextInputEditText) findViewById(R.id.txt_service_date);
        txt_hour_service = (TextInputEditText) findViewById(R.id.txt_hour_service);
        txt_description_service = (TextInputEditText) findViewById(R.id.txt_description_service);
        txt_serviceReferencias = (TextInputEditText) findViewById(R.id.txt_serviceReferencias);
        btn_createServiceComplete = (Button) findViewById(R.id.btn_createServiceComplete);
        radio_service = (RadioGroup) findViewById(R.id.radio_service);
        int selectedId = radio_service.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(CreateServiceActivity.this);

        int id = prefs.getInt("idUsuario",0);
        id_User = Integer.toString(id);



        Bundle b = getIntent().getExtras();

        if(b != null)
        {
            latitud = b.getString("latitud");
            longuitud = b.getString("longuitud");
            Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
            try {
                double lat = Double.parseDouble(latitud);
                double lon = Double.parseDouble(longuitud);
                List<Address> addresses = geoCoder.getFromLocation(lat, lon, 1);


                if (addresses.size() > 0) {
                    for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++)
                        address += addresses.get(0).getAddressLine(i) + "\n";
                }

                Toast.makeText(this, address, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        btn_createServiceComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fecha = txt_service_date.getText().toString();
                String hora = txt_hour_service.getText().toString();
                String descripcion = txt_description_service.getText().toString();
                String referencias = txt_serviceReferencias.getText().toString();
                String tipoServicio = radioButton.getText().toString();

                String estatus = getResources().getString(R.string.servicio_pendiente);
                createService(descripcion,fecha,hora,referencias,tipoServicio,address,latitud,longuitud,estatus,id_User,"actrivo");
            }
        });



        showToolbar("",true);
    }

    public void createService(String descripcionServicio, String fechaServicio, String horaServicio, String referenciasServicio, String tipoServicio,String direccionServicio, String latitudServicio, String longuitudServicio, String estatusServicio, String idCliente, String activoServicio)
    {
        String url = "http://transportesaguilera.com.mx/insertar_servicio.php";

        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("descripcionServicio", descripcionServicio);
        postParam.put("fechaServicio", fechaServicio);
        postParam.put("horaServicio", horaServicio);
        postParam.put("referenciasServicio", referenciasServicio);
        postParam.put("tipoServicio", tipoServicio);
        postParam.put("direccionServicio", direccionServicio);
        postParam.put("latitudServicio", latitudServicio);
        postParam.put("longuitudServicio", longuitudServicio);
        postParam.put("estatusServicio", estatusServicio);
        postParam.put("idCliente", idCliente);
        postParam.put("activoServicio", activoServicio);


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, new JSONObject(postParam), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(CreateServiceActivity.this,response.toString(), Toast.LENGTH_SHORT).show();
                                          }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(CreateServiceActivity.this,error.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
        addToQueue(jsObjRequest);
    }

    public void showToolbar(String title, boolean upButton)
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //para versiones anteriores
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);

    }

    public void addToQueue(Request request) {
        if (request != null) {
            request.setTag(this);
            if (fRequestQueue == null)
                fRequestQueue = volley.getRequestQueue();
            request.setRetryPolicy(new DefaultRetryPolicy(
                    60000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            fRequestQueue.add(request);
        }
    }
}
