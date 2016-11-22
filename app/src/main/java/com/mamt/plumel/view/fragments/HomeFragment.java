package com.mamt.plumel.view.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mamt.plumel.Adapters.ServiceAdapterRecyclerView;
import com.mamt.plumel.LoginActivity;
import com.mamt.plumel.Models.Service;
import com.mamt.plumel.Models.Servicio;
import com.mamt.plumel.Models.Usuario;
import com.mamt.plumel.R;
import com.mamt.plumel.VolleyS;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private VolleyS volley;
    protected RequestQueue fRequestQueue;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        showToolbar("Inicio", false, view);
        volley = VolleyS.getInstance(getActivity());
        fRequestQueue = volley.getRequestQueue();
        obtenerServicios();
        return view;
    }



    public void obtenerServicios() {
        String url = "http://transportesaguilera.com.mx/obtener_servicios.php";
        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        List<Servicio> serviceArray = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {

                            try {
                                int id = response.getJSONObject(i).getInt("idServicio");
                                String descripcionServicio = response.getJSONObject(i).getString("descripcionServicio");
                                String fechaServicio = response.getJSONObject(i).getString("fechaServicio");
                                String horaServicio = response.getJSONObject(i).getString("horaServicio");
                                String referenciasServicio = response.getJSONObject(i).getString("referenciasServicio");
                                String tipoServicio = response.getJSONObject(i).getString("tipoServicio");
                                String direccionServicio = response.getJSONObject(i).getString("direccionServicio");
                                String latitudServicio = response.getJSONObject(i).getString("latitudServicio");
                                String longuitudServicio = response.getJSONObject(i).getString("longuitudServicio");
                                int idCliente = response.getJSONObject(i).getInt("idCliente");
                                String estatusServicio = response.getJSONObject(i).getString("estatusServicio");


                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

                                String rolUsuario = prefs.getString("rolUsuario", "");
                                int id_User = prefs.getInt("idUsuario",0);

                                if(rolUsuario.equals("tecnico"))
                                {
                                    if(estatusServicio.equals("Pendiente"))
                                    {
                                        Servicio servicio = new Servicio(id, descripcionServicio, fechaServicio, horaServicio, referenciasServicio, tipoServicio,
                                                direccionServicio, latitudServicio, longuitudServicio, idCliente,estatusServicio);
                                        serviceArray.add(servicio);
                                    }
                                }
                                else {

                                    if(idCliente == id_User)
                                    {
                                        Servicio servicio = new Servicio(id, descripcionServicio, fechaServicio, horaServicio, referenciasServicio, tipoServicio,
                                                direccionServicio, latitudServicio, longuitudServicio, idCliente,estatusServicio);
                                        serviceArray.add(servicio);
                                    }

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        RecyclerView servicesRecycler = (RecyclerView) getView().findViewById(R.id.serviceRecycler);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
                        servicesRecycler.setLayoutManager(linearLayoutManager);
                        ServiceAdapterRecyclerView serviceAdapterRecyclerView = new ServiceAdapterRecyclerView(serviceArray, R.layout.cardview_service, getActivity());
                        servicesRecycler.setAdapter(serviceAdapterRecyclerView);
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        addToQueue(jsObjRequest);
    }

    public void showToolbar(String title, boolean upButton,View view)
    {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar); //para versiones anteriores
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);

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
