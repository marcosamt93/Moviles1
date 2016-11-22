package com.mamt.plumel.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;
import com.mamt.plumel.LoginActivity;
import com.mamt.plumel.R;
import com.mamt.plumel.view.fragments.HomeFragment;
import com.mamt.plumel.view.fragments.ProfileFragment;
import com.mamt.plumel.view.fragments.SearchFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.List;

public class ConteinerActivity extends AppCompatActivity {

    GestureOverlayView m_gestureOverlayView;
    GestureDetector m_detector;
    GestureLibrary m_gestureLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conteiner);
        final ConteinerActivity context = this;
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottombar);
        BottomBar bottomBar_tecnico = (BottomBar) findViewById(R.id.bottombar_tecnico);

        bottomBar.setVisibility(View.GONE);
        bottomBar_tecnico.setVisibility(View.GONE);
        m_gestureOverlayView = (GestureOverlayView) findViewById(R.id.gesture_overlay);
        m_gestureLibrary = GestureLibraries.fromRawResource(this,R.raw.gestures);




        if(!m_gestureLibrary.load())
        {
            finish();
        }

        m_gestureOverlayView.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
            @Override
            public void onGesturePerformed(GestureOverlayView gestureOverlayView, Gesture gesture) {
                List<Prediction> predictions =  m_gestureLibrary.recognize(gesture);
                if(predictions.size() >0)
                {
                    Prediction prediction = predictions.get(0);
                    if(prediction.score > 1.0)
                    {
                        String name =  prediction.name;
                        if(name.equals("rojo")) //Triangulo
                        {
                            Intent intent = new Intent(context,ServiceActivity.class);
                            startActivity(intent);
                        }
                        else if (name.equals("verde")) //Reloj
                        {
                            Intent intent = new Intent(context,LoginActivity.class);
                            startActivity(intent);
                        }
                        else if(name.equals("azul")) //Circulo
                        {
                            finish();
                        }
                    }
                }
            }
        });


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String userrol = prefs.getString("rolUsuario", "");
        if(userrol.equals("cliente"))
        {
            bottomBar.setVisibility(View.VISIBLE);
            bottomBar.setDefaultTab(R.id.home_tab);
            bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelected(@IdRes int tabId) {
                    switch (tabId)
                    {
                        case R.id.home_tab:
                            HomeFragment homeFragment = new HomeFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment)
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                            break;
                        case R.id.profile_tab:
                            ProfileFragment profileFragment = new ProfileFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.container,profileFragment)
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                            break;
                        case R.id.search_tab:
                            //Intent intent = new Intent(this, ServiceActivity.class);
                            Intent intent = new Intent(context,ServiceActivity.class);
                            startActivity(intent);
                            break;
                    }
                }
            });
        }
        else
        {
            bottomBar_tecnico.setVisibility(View.VISIBLE);
            bottomBar_tecnico.setDefaultTab(R.id.home_tab_tecnico);
            bottomBar_tecnico.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelected(@IdRes int tabId) {
                    switch (tabId)
                    {
                        case R.id.home_tab_tecnico:
                            HomeFragment homeFragment = new HomeFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment)
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                            break;
                        case R.id.profile_tab_tecnico:
                            ProfileFragment profileFragment = new ProfileFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.container,profileFragment)
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
                            break;
                    }
                }
            });
        }

    }
}
