package cf.app.com.shannon.Users;

import android.graphics.Bitmap;

/**
 * Created by David on 9/04/2016.
 */
public class UsuarioPersonal extends Usuario {
    private String Apellido;


    public UsuarioPersonal(String correo ,String nombre,String apellido,String descrip, String Telefono, String nickname,Bitmap imgPerfil){
        this.Nombre = nombre;
        this.correo= correo;
        this.Apellido = apellido;
        this.telefono = Telefono;
        this.nickname = nickname;
        this.uriImagePerfil= imgPerfil;
        this.descripcion = descrip;

    }
    public UsuarioPersonal(String correo ,String nombre,String apellido,String descrip,String imgPerfil){
        this.Nombre = nombre;
        this.correo= correo;
        this.Apellido = apellido;
        this.urlImagePerfil = imgPerfil;
        this.descripcion = descrip;


    }

    public String getApellido() {
        return Apellido;
    }
    public String getNombre (){return super.getNombre()+" "+Apellido;}
    public void setApellido(String apellido) {
        Apellido = apellido;
    }
}
