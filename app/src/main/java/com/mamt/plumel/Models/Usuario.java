package com.mamt.plumel.Models;



public class Usuario {

    private int idUsuario;
    private String nickname;
    private String nombre;
    private String email;
    private String contra;
    private String avatarURL;
    private String direccion;
    private String rol;
    private String activo;

    public Usuario(String nickname, String nombre, String email, String contra, String avatarURL, String direccion, String rol, String activo) {
        this.nickname = nickname;
        this.nombre = nombre;
        this.email = email;
        this.contra = contra;
        this.avatarURL = avatarURL;
        this.direccion = direccion;
        this.rol = rol;
        this.activo = activo;
    }

    public Usuario(int idUsuario, String nickname, String nombre, String email, String contra, String avatarURL, String direccion, String rol, String activo) {
        this.idUsuario = idUsuario;
        this.nickname = nickname;
        this.nombre = nombre;
        this.email = email;
        this.contra = contra;
        this.avatarURL = avatarURL;
        this.direccion = direccion;
        this.rol = rol;
        this.activo = activo;
    }



    public int getId() {
        return idUsuario;
    }

    public void setId(int id) {
        this.idUsuario = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }
}
