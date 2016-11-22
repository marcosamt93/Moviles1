package com.mamt.plumel.view.fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.mamt.plumel.Adapters.ServiceAdapterRecyclerView;
import com.mamt.plumel.LoginActivity;

import com.mamt.plumel.Models.Service;
import com.mamt.plumel.R;
import com.mamt.plumel.VolleyS;
import com.mamt.plumel.view.ConteinerActivity;
import com.mamt.plumel.view.CreateAccountActivity;
import com.mamt.plumel.view.ServiceActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import static android.R.attr.permission;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private Button button;
    private boolean IsLogin;
    private TextView profile_email;
    private TextView profile_name;
    private TextView profile_user;
    private TextView profile_contra;
    private TextView userNameProfile;
    private Button btn_save;
    private VolleyS volley;
    protected RequestQueue fRequestQueue;

    private Button btn_cargar_imagen;
    private Bitmap bitmap;
    private String UPLOAD_URL ="http://transportesaguilera.com.mx/upload.php";
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    private Uri fileUri; // file url to store image/video
    public String idUsuario;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private ImageView imageView;
    private  String image;

    private static String TAG = "PermissionDemo";
    private static final int RECORD_REQUEST_CODE = 101;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);



        volley = com.mamt.plumel.VolleyS.getInstance(getActivity());
        fRequestQueue = volley.getRequestQueue();

        profile_name = (TextView)  view.findViewById(R.id.profile_name);
        profile_email = (TextView)  view.findViewById(R.id.profile_email);
        profile_contra = (TextView)  view.findViewById(R.id.profile_contra);
        userNameProfile = (TextView)  view.findViewById(R.id.userNameProfile);
        imageView  = (ImageView) view.findViewById(R.id.imageView);




        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }






        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        int id = prefs.getInt("idUsuario",0);
        idUsuario = Integer.toString(id);
        String username = prefs.getString("nombreUsuario", "");
        String email = prefs.getString("emailUsuario", "");
        String password = prefs.getString("contraseniaUsuario", "");
        String nickname = prefs.getString("nicknameUsuario", "");
        String avatarUsuario = prefs.getString("avatarUsuario", "");

        profile_name.setText(username);
        profile_email.setText(email);
        profile_contra.setText(password);
        userNameProfile.setText(nickname);

        if(!avatarUsuario.equals(""))
        {

            Picasso.with(getContext()).load(avatarUsuario).into(imageView);
        }

        btn_save = (Button) view.findViewById(R.id.btn_save_change);
        button = (Button) view.findViewById(R.id.cerrar_sesion);
        btn_cargar_imagen = (Button) view.findViewById(R.id.btn_cargar_imagen);

        btn_cargar_imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IsLogin = false;
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                prefs.edit().putBoolean("IsLogin", IsLogin).apply(); // IsLogin is a boolean value of your login status
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActualizarUsuario(idUsuario);
            }
        });
        return view;

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void previewCapturedImage() {
        try {

            imageView.setVisibility(View.VISIBLE);

            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 2;

            bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);


            imageView.setImageBitmap(bitmap);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }



    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }



    public void ActualizarUsuario(String idUsuario)
    {
        final String nombreUsuario = profile_name.getText().toString();
        final String emailUsuario = profile_email.getText().toString();
        final String contraseniaUsuario = profile_contra.getText().toString();

        String url = "http://transportesaguilera.com.mx/actualizar_usuario.php";

        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("idUsuario", idUsuario);
        postParam.put("nombreUsuario", nombreUsuario);
        postParam.put("emailUsuario", emailUsuario);
        postParam.put("contraseniaUsuario", contraseniaUsuario);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, new JSONObject(postParam), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_SHORT).show();
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                        prefs.edit().putString("nombreUsuario", nombreUsuario).apply(); // IsLogin is a boolean value of your login status
                        prefs.edit().putString("contraseniaUsuario", contraseniaUsuario).apply();
                        prefs.edit().putString("emailUsuario", emailUsuario).apply();// IsLogin is a boolean value of your login status


                        uploadImage();
                        //Intent intent = new Intent(getActivity(), ConteinerActivity.class);
                        //startActivity(intent);
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(),error.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
        addToQueue(jsObjRequest);
    }

    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(getContext(),"Cargando...","espere por favor...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(getActivity(), s , Toast.LENGTH_LONG).show();
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                        image = getStringImage(bitmap);
                        prefs.edit().putString("avatarUsuario","http://transportesaguilera.com.mx/uploads/"+idUsuario+".png").apply(); // IsLogin is a boolean value of your login status
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(getActivity(), volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                 image = getStringImage(bitmap);
                 //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("image", image);
                params.put("name", "prueba");
                params.put("idUsuario",idUsuario);

                //returning parameters
                return params;
            }
        };


        addToQueue(stringRequest);
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