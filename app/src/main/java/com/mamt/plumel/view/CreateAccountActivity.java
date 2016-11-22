package com.mamt.plumel.view;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.mamt.plumel.plumelDB;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {

   private Button button;
    private TextInputEditText email;
    private TextInputEditText name;
    private TextInputEditText user;
    private TextInputEditText password_createaccount;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private VolleyS volley;
    protected RequestQueue fRequestQueue;
    private plumelDB plumelDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        button = (Button) findViewById(R.id.btn_createAccount);
        email = (TextInputEditText) findViewById(R.id.email);
        name = (TextInputEditText) findViewById(R.id.name);
        user = (TextInputEditText) findViewById(R.id.user);
        password_createaccount = (TextInputEditText) findViewById(R.id.password_createaccount);
        radioGroup = (RadioGroup) findViewById(R.id.radio_grupo);
        volley = com.mamt.plumel.VolleyS.getInstance(CreateAccountActivity.this);
        fRequestQueue = volley.getRequestQueue();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (email.getText().toString() != null && !email.getText().toString().isEmpty() && !email.getText().toString().equals("null"))
                {
                    if (name.getText().toString() != null && !name.getText().toString().isEmpty() && !name.getText().toString().equals("null"))
                    {
                        if (user.getText().toString() != null && !user.getText().toString().isEmpty() && !user.getText().toString().equals("null"))
                        {
                            if (password_createaccount.getText().toString() != null && !password_createaccount.getText().toString().isEmpty() && !password_createaccount.getText().toString().equals("null"))
                            {
                                int selectedId = radioGroup.getCheckedRadioButtonId();
                                radioButton = (RadioButton) findViewById(selectedId);
                                String correo = email.getText().toString();
                                String nombre = name.getText().toString();
                                String usuario = user.getText().toString();
                                String contra = password_createaccount.getText().toString();
                                String rol = radioButton.getText().toString();
                                CrearUsuario(usuario,nombre,correo,contra,rol);

                            }
                            else
                                Toast.makeText(CreateAccountActivity.this,"Todos los campos son requeridos",Toast.LENGTH_SHORT).show();

                        }
                        else
                            Toast.makeText(CreateAccountActivity.this,"Todos los campos son requeridos",Toast.LENGTH_SHORT).show();

                    }
                    else
                        Toast.makeText(CreateAccountActivity.this,"Todos los campos son requeridos",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(CreateAccountActivity.this,"Todos los campos son requeridos",Toast.LENGTH_SHORT).show();
            }
        });

        showToolbar(getResources().getString(R.string.toolbar_title_createaccount),true);

    }

    public void showToolbar(String title, boolean upButton)
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //para versiones anteriores
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);

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
                        Toast.makeText(CreateAccountActivity.this,response.toString(),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);  //en donde estoy y a donde quiero ir
                        startActivity(intent);
                        finish();
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        plumelDB.InsertarUsuario(nicknameUsuario,nombreUsuario,emailUsuario,contraseniaUsuario,"","",rolUsuario);
                        Toast.makeText(CreateAccountActivity.this,error.toString(), Toast.LENGTH_SHORT).show();

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
