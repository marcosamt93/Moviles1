package com.mamt.plumel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mamt.plumel.Models.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcosmt on 07/11/2016.
 */

public class plumelDB extends SQLiteOpenHelper {

    private static final int VERSION_BASEDATOS = 2;
    private static final String NOMBRE_BASEDATOS = "plumeldb.db";
    private static final String TABLA_USUARIO = "Usuario";
    private static  final String TABLA_SERVICIO = "Servicio";
    private static final String TABLA_USUARIO_CREATE = "CREATE TABLE Usuario" +
            "(idUsuario INTEGER PRIMARY KEY AUTOINCREMENT," +
            "nicknameUsuario TEXT, nombreUsuario TEXT," +
            "emailUsuario TEXT,contraseniaUsuario TEXT," +
            "avatarUsuario TEXT, direccionUsuario TEXT, " +
            "rolUsuario TEXT,activoUsuario TEXT)";

    private static final String TABLA_SERVICIO_CREATE = "CREATE TABLE Servicio" +
            "(idServicio INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "descripcionServicio TEXT, fechaServicio TEXT,"+
            "horaServicio TEXT, referenciaServicio TEXT,"+
            "tipoServicio TEXT, direccionServicio TEXT"+
            "latitudServicio TEXT, longuitudServicio TEXT,"+
            "estatusServicio TEXT, idTecnico INT,"+
            "idCliente INT, activoServicio TEXT)";


    public plumelDB(Context context) {
        super(context, NOMBRE_BASEDATOS, null, VERSION_BASEDATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLA_USUARIO_CREATE);
        db.execSQL(TABLA_SERVICIO_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_USUARIO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_SERVICIO);
        onCreate(db);
    }

    public void InsertarUsuario(String nickname, String nombre, String email, String contrasenia, String avatarURL, String direccion, String rol)
    {
        SQLiteDatabase db = getWritableDatabase();
        if(db != null){
            ContentValues valores = new ContentValues();
            valores.put("nicknameUsuario", nickname);
            valores.put("nombreUsuario", nombre);
            valores.put("emailUsuario", email);
            valores.put("contraseniaUsuario", contrasenia);
            valores.put("avatarUsuario", avatarURL);
            valores.put("direccionUsuario", direccion);
            valores.put("rolUsuario", rol);
            valores.put("activoUsuario", "1");
            db.insert(TABLA_USUARIO, null, valores);
            db.close();
        }
    }

    public void modificarUsuario(int id, String nickname,String nombre,String contrasenia,String avatar, String email){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nicknameUsuario", nickname);
        valores.put("nombreUsuario", nombre);
        valores.put("contraseniaUsuario", contrasenia);
        valores.put("avatarUsuario", avatar);
        valores.put("emailUsuario", email);
        db.update(TABLA_USUARIO, valores, "idUsuario=" + id, null);
        db.close();
    }

    public void borrarUsuario(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLA_USUARIO, "idUsuario="+id, null);
        db.close();
    }

    public Usuario recuperarUsuario(int id)
    {
        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperar = {"idUsuario", "nicknameUsuario", "nombreUsuario", "emailUsuario","contraseniaUsuario","avatarUsuario","direccionUsuario","rolUsuario","activoUsuario"};
        Cursor c = db.query(TABLA_USUARIO, valores_recuperar, "idUsuario=" + id, null, null, null, null, null);
        if(c != null) {

            c.moveToFirst();
        }
        Usuario usuario;
        usuario = new Usuario(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8));
        db.close();
        c.close();
        return usuario;
    }

    public Usuario validarLogin(String username,String contra)
    {
        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperar = {"idUsuario", "nicknameUsuario", "nombreUsuario", "emailUsuario","contraseniaUsuario","avatarUsuario","direccionUsuario","rolUsuario","activoUsuario"};
        String where = "nicknameUsuario = '"+username+"' AND contraseniaUsuario='"+contra+"'";
        Cursor c = db.query(TABLA_USUARIO, valores_recuperar,where,null, null, null, null, null);
        if(c.getCount() > 0)
        {
            if(c != null) {

                c.moveToFirst();
            }
            Usuario usuario;

            usuario = new Usuario(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8));
            db.close();
            c.close();
            return usuario;
        }
        else {
            return null;
        }
    }


    public List<Usuario> recuperarUsuarios() {
        SQLiteDatabase db = getReadableDatabase();
        List<Usuario> lista_usuarios = new ArrayList<Usuario>();
        String[] valores_recuperar = {"idUsuario", "nicknameUsuario", "nombreUsuario", "emailUsuario","contraseniaUsuario","avatarUsuario","direccionUsuario","rolUsuario","activoUsuario"};
        Cursor c = db.query(TABLA_USUARIO, valores_recuperar, null, null, null, null, null, null);
        c.moveToFirst();
        do {
            Usuario usuario;
            usuario = new Usuario(c.getInt(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7),c.getString(8));

            lista_usuarios.add(usuario);
        } while (c.moveToNext());
        db.close();
        c.close();
        return lista_usuarios;
    }
}
