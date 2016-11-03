package com.mamt.plumel.view.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mamt.plumel.Adapters.ServiceAdapterRecyclerView;
import com.mamt.plumel.Models.Service;
import com.mamt.plumel.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        showToolbar("Inicio",false,view);
        RecyclerView servicesRecycler = (RecyclerView) view.findViewById(R.id.serviceRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        servicesRecycler.setLayoutManager(linearLayoutManager);
        ServiceAdapterRecyclerView serviceAdapterRecyclerView = new ServiceAdapterRecyclerView(buildServices(),R.layout.cardview_service,getActivity());
        servicesRecycler.setAdapter(serviceAdapterRecyclerView);
        return view;
    }

    public ArrayList<Service> buildServices()
    {
        ArrayList<Service> services = new ArrayList<>();
        services.add(new Service("http://www.paisajesbonitos.org/wp-content/uploads/2015/11/paisajes-bonitos-de-oto%C3%B1o-lago.jpg","Marcos Moreno","Cd. Universitaria","10/11/2016"));
        services.add(new Service("http://puzzles.tuspuzzles.es/images/original/atardecer-568470fbef6d4.jpg","Pablo Gonzalez","San Nicolas","10/11/2016"));
        services.add(new Service("http://www.ambientum.com/img_boletin/noticia/galicia-paisaje.jpg","Alejandro Torres","Av. Santo domingo","10/11/2016"));
        services.add(new Service("http://puzzles.tuspuzzles.es/images/original/atardecer-568470fbef6d4.jpg","Pepe Lepugh","Av. Santo domingo","10/11/2016"));
        services.add(new Service("http://www.paisajesbonitos.org/wp-content/uploads/2015/11/paisajes-bonitos-de-oto%C3%B1o-lago.jpg","Lucas Lobos","El Volcan","10/11/2016"));
        return  services;
    }

    public void showToolbar(String title, boolean upButton,View view)
    {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar); //para versiones anteriores
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);

    }

}
