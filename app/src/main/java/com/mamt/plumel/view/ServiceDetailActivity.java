package com.mamt.plumel.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mamt.plumel.R;
import com.mamt.plumel.VolleyS;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ServiceDetailActivity extends AppCompatActivity {

    private TextView usernameDetails;
    private TextView addressServiceDetails;
    private TextView dateservicessDetails;
    private TextView serviceType;
    private TextView serviceDescriptionDetail;
    private Button btn_cerrar_caso;
    private Button btn_save;
    private VolleyS volley;
    protected RequestQueue fRequestQueue;
    private int id;
    private String idServicio;
    private String estatusServicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

        volley = com.mamt.plumel.VolleyS.getInstance(this);
        fRequestQueue = volley.getRequestQueue();


        btn_cerrar_caso = (Button) findViewById(R.id.btn_cerrar_caso);
        usernameDetails  = (TextView) findViewById(R.id.usernameDetails);
        addressServiceDetails  = (TextView) findViewById(R.id.addressServiceDetails);
        dateservicessDetails  = (TextView) findViewById(R.id.dateservicessDetails);
        serviceType  = (TextView) findViewById(R.id.serviceType);
        serviceDescriptionDetail  = (TextView) findViewById(R.id.serviceDescriptionDetail);

        Bundle b = getIntent().getExtras();
        if(b != null)
        {
            usernameDetails.setText(b.getString("fechaServicio"));
            addressServiceDetails.setText(b.getString("direccion"));
            dateservicessDetails.setText(b.getString("horaServicio"));
            serviceType.setText(b.getString("tipoServicio"));
            serviceDescriptionDetail.setText(b.getString("descripcion"));
            estatusServicio = b.getString("estatusServicio");
            id = b.getInt("idServicio");
            idServicio = Integer.toString(id);
            Toast.makeText(this,idServicio,Toast.LENGTH_LONG).show();
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ServiceDetailActivity.this);
        String rolUsuario = prefs.getString("rolUsuario","");

        if(rolUsuario.equals("cliente"))
        {
            btn_cerrar_caso.setVisibility(View.INVISIBLE);
        }

        btn_cerrar_caso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CerrarServicio();
            }
        });

        showToolbar("Servicios",true);
    }

    public void CerrarServicio()
    {
        String url = "http://transportesaguilera.com.mx/actualizar_servicio.php";

        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("idServicio", idServicio);
        postParam.put("estatusServicio", "Resuelto");


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, new JSONObject(postParam), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ServiceDetailActivity.this,response.toString(),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ServiceDetailActivity.this, ConteinerActivity.class);
                        startActivity(intent);
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(ServiceDetailActivity.this,error.toString(), Toast.LENGTH_SHORT).show();

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
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
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
