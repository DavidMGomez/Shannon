package cf.app.com.shannon.Users;

import android.graphics.Bitmap;
import android.util.EventLogTags;

import java.security.SecureRandom;

/**
 * Created by David on 9/04/2016.
 */
public abstract class Usuario {
    protected String correo;
    protected String Nombre;
    protected   String telefono;
    protected Bitmap uriImagePerfil;
    protected String urlImagePerfil;
    protected   int estadocuenta;
    protected   String nickname;
    protected String descripcion;
    protected float calificacionPromedio;

    public String getUrlImagePerfil() {
        return urlImagePerfil;
    }

    public void setUrlImagePerfil(String urlImagePerfil) {
        this.urlImagePerfil = urlImagePerfil;
    }

    protected String ubicacionGps;

    /// abtract

    //

    public String getUbicacionGps() {
        return ubicacionGps;
    }

    public void setUbicacionGps(String ubicacionGps) {
        this.ubicacionGps = ubicacionGps;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Bitmap getUriImagePerfil() {
        return uriImagePerfil;
    }

    public void setUriImagePerfil(Bitmap uriImagePerfil) {
        this.uriImagePerfil = uriImagePerfil;
    }

    public int getEstadocuenta() {
        return estadocuenta;
    }

    public void setEstadocuenta(int estadocuenta) {
        this.estadocuenta = estadocuenta;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getCalificacionPromedio() {
        return calificacionPromedio;
    }

    public void setCalificacionPromedio(float calificacionPromedio) {
        this.calificacionPromedio = calificacionPromedio;
    }
}
