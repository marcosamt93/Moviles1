package com.mamt.plumel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mamt.plumel.Models.Usuario;
import com.mamt.plumel.view.ConteinerActivity;
import com.mamt.plumel.view.CreateAccountActivity;
import com.mamt.plumel.view.ServiceActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private plumelDB MDB;
    private boolean IsLogin;
    private VolleyS volley;
    protected RequestQueue fRequestQueue;
    private LoginButton login_button;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);
        MDB = new plumelDB(getApplicationContext());

        login_button = (LoginButton)findViewById(R.id.login_button);
        editTextEmail = (TextInputEditText) findViewById(R.id.username);
        editTextPassword = (TextInputEditText) findViewById(R.id.password);
        login_button.setReadPermissions("email");


        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                getFaceBookProfileDetails(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                String mensaje = "Cancelar";
                Toast.makeText(LoginActivity.this,mensaje, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException e) {
                editTextEmail.setText(e.toString());
            }
        });

        volley = VolleyS.getInstance(LoginActivity.this);
        fRequestQueue = volley.getRequestQueue();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        IsLogin = prefs.getBoolean("IsLogin", false); // get value of last login status


        Button button = (Button) findViewById(R.id.login);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validarLogin(editTextEmail.getText().toString(),editTextPassword.getText().toString());
            }
        });

    }


    private void getFaceBookProfileDetails(final AccessToken accessToken) {

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            //object retorna lo indicado en paramters.putString("fields", "email") en este caso, solo contiene el email
            @Override
            public void onCompleted(final JSONObject object, GraphResponse response) {
                try {

                    Profile profileDefault = Profile.getCurrentProfile();

                    String nicknameUsuario = profileDefault.getFirstName()+profileDefault.getLastName();
                    String nombreCompleto = profileDefault.getFirstName()+" "+profileDefault.getMiddleName()+" "+profileDefault.getLastName();
                    String correoelectronico = object.getString("email");

                    validarNick(nicknameUsuario,nombreCompleto,correoelectronico);


                } catch (Exception e) {
                    Log.e("E-MainActivity", "getFaceBook" + e.toString());
                }
            }
        });

        Bundle parameters = new Bundle();
        //solicitando el campo email
        parameters.putString("fields", "email");
        request.setParameters(parameters);
        request.executeAsync();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void GoCreateAccount(View view)
    {
        Intent intent = new Intent(this, CreateAccountActivity.class);  //en donde estoy y a donde quiero ir
        startActivity(intent);
    }

    public void validarLogin(String nickname, final String contra)
    {

        String url = "http://transportesaguilera.com.mx/obtener_usuario_pornick.php?nicknameUsuario="+nickname;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject jObj = null;
                        try {
                            jObj = response.getJSONObject("usuario");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            if(jObj != null)
                            {
                                int id = jObj.getInt("idUsuario");
                                String nicknameUsuario = jObj.getString("nicknameUsuario");
                                String nombreUsuario = jObj.getString("nombreUsuario");
                                String emailUsuario = jObj.getString("emailUsuario");
                                String contraseniaUsuario = jObj.getString("contraseniaUsuario");
                                String avatarUsuario = jObj.getString("avatarUsuario");
                                String direccionUsuario = jObj.getString("direccionUsuario");
                                String rolUsuario = jObj.getString("rolUsuario");
                                String activoUsuario = jObj.getString("activoUsuario");
                                Usuario usuario = new Usuario(id,nicknameUsuario,nombreUsuario,emailUsuario,contraseniaUsuario,avatarUsuario,
                                        direccionUsuario,rolUsuario,activoUsuario);
                                if(contraseniaUsuario.equals(contra))
                                {
                                    boolean IsLogin = true;

                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    prefs.edit().putInt("idUsuario", id).apply(); // IsLogin is a boolean value of your login status
                                    prefs.edit().putString("nicknameUsuario", nicknameUsuario).apply(); // IsLogin is a boolean value of your login status
                                    prefs.edit().putString("nombreUsuario", nombreUsuario).apply(); // IsLogin is a boolean value of your login status
                                    prefs.edit().putString("contraseniaUsuario", contraseniaUsuario).apply();
                                    prefs.edit().putString("emailUsuario", emailUsuario).apply();// IsLogin is a boolean value of your login status
                                    prefs.edit().putString("avatarUsuario", avatarUsuario).apply(); // IsLogin is a boolean value of your login status
                                    prefs.edit().putString("direccionUsuario", direccionUsuario).apply(); // IsLogin is a boolean value of your login status
                                    prefs.edit().putString("rolUsuario", rolUsuario).apply(); // IsLogin is a boolean value of your login status
                                    prefs.edit().putBoolean("IsLogin", IsLogin).apply(); // IsLogin is a boolean value of your login status


                                    Toast.makeText(LoginActivity.this,"Bienvenido", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this,ConteinerActivity.class);  //en donde estoy y a donde quiero ir
                                    Bundle b = new Bundle();
                                    b.putInt("idUsuario",id);
                                    b.putString("nicknameUsuario",nicknameUsuario);
                                    b.putString("nombreUsuario",nombreUsuario);
                                    b.putString("emailUsuario",emailUsuario);
                                    b.putString("contraseniaUsuario",contraseniaUsuario);
                                    b.putString("avatarUsuario",avatarUsuario);
                                    b.putString("direccionUsuario",direccionUsuario);
                                    b.putString("rolUsuario",rolUsuario);
                                    intent.putExtras(b); //Put your id to your next Intent
                                    startActivity(intent);
                            }
                                else
                                {
                                    Toast.makeText(LoginActivity.this,"Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                                }


                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this,"Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        editTextEmail.setText("ERROR: " + error.toString());

                    }
                });
        addToQueue(jsObjRequest);
    }

    public void validarNick(final String nickname, final String fullname, final String email)
    {

        String url = "http://transportesaguilera.com.mx/obtener_usuario_pornick.php?nicknameUsuario="+nickname;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        JSONObject jObj = null;
                        try {
                            jObj = response.getJSONObject("usuario");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            if(jObj != null)
                            {
                                int id = jObj.getInt("idUsuario");
                                String nicknameUsuario = jObj.getString("nicknameUsuario");
                                String nombreUsuario = jObj.getString("nombreUsuario");
                                String emailUsuario = jObj.getString("emailUsuario");
                                String contraseniaUsuario = jObj.getString("contraseniaUsuario");
                                String avatarUsuario = jObj.getString("avatarUsuario");
                                String direccionUsuario = jObj.getString("direccionUsuario");
                                String rolUsuario = jObj.getString("rolUsuario");
                                String activoUsuario = jObj.getString("activoUsuario");
                                Usuario usuario = new Usuario(id,nicknameUsuario,nombreUsuario,emailUsuario,contraseniaUsuario,avatarUsuario,
                                        direccionUsuario,rolUsuario,activoUsuario);

                                boolean IsLogin = true;

                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                prefs.edit().putInt("idUsuario", id).apply(); // IsLogin is a boolean value of your login status
                                prefs.edit().putString("nicknameUsuario", nicknameUsuario).apply(); // IsLogin is a boolean value of your login status
                                prefs.edit().putString("nombreUsuario", nombreUsuario).apply(); // IsLogin is a boolean value of your login status
                                prefs.edit().putString("contraseniaUsuario", contraseniaUsuario).apply();
                                prefs.edit().putString("emailUsuario", emailUsuario).apply();// IsLogin is a boolean value of your login status
                                prefs.edit().putString("avatarUsuario", avatarUsuario).apply(); // IsLogin is a boolean value of your login status
                                prefs.edit().putString("direccionUsuario", direccionUsuario).apply(); // IsLogin is a boolean value of your login status
                                prefs.edit().putString("rolUsuario", rolUsuario).apply(); // IsLogin is a boolean value of your login status
                                prefs.edit().putBoolean("IsLogin", IsLogin).apply(); // IsLogin is a boolean value of your login status
                                Toast.makeText(LoginActivity.this,"Bienvenido", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this,ConteinerActivity.class);  //en donde estoy y a donde quiero ir
                                Bundle b = new Bundle();
                                b.putInt("idUsuario",id);
                                b.putString("nicknameUsuario",nicknameUsuario);
                                b.putString("nombreUsuario",nombreUsuario);
                                b.putString("emailUsuario",emailUsuario);
                                b.putString("contraseniaUsuario",contraseniaUsuario);
                                b.putString("avatarUsuario",avatarUsuario);
                                b.putString("direccionUsuario",direccionUsuario);
                                b.putString("rolUsuario",rolUsuario);
                                intent.putExtras(b); //Put your id to your next Intent
                                startActivity(intent);
                            }

                            else
                            {
                                CrearUsuario(nickname,fullname,email,"","cliente");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        editTextEmail.setText("ERROR: " + error.toString());

                    }
                });
        addToQueue(jsObjRequest);
    }

    public void CrearUsuario(final String nicknameUsuario, final String nombreUsuario, final String emailUsuario, final String contraseniaUsuario, final String rolUsuario)
    {
        String url = "http://transportesaguilera.com.mx/insertar_usuario.php";

        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("nicknameUsuario", nicknameUsuario);
        postParam.put("nombreUsuario", nombreUsuario);
        postParam.put("emailUsuario", emailUsuario);
        postParam.put("contraseniaUsuario", contraseniaUsuario);
        postParam.put("rolUsuario", rolUsuario);


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, new JSONObject(postParam), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(LoginActivity.this,response.toString(),Toast.LENGTH_SHORT).show();
                        validarLogin(nicknameUsuario,contraseniaUsuario);
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Toast.makeText(LoginActivity.this,error.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
        addToQueue(jsObjRequest);
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
