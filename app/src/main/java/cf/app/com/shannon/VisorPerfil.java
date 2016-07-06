package cf.app.com.shannon;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cf.app.com.shannon.Users.Usuario;
import cf.app.com.shannon.estaticas.Contenedor;
import cf.app.com.shannon.plantilla.TemplateActivity;

public class VisorPerfil extends TemplateActivity {
    Usuario usuarioseleccionado;
    ImageView imagePerfil;
    TextView txtNombre;
    TextView txtDescripcion;

    @Override
    public void vistaInicioSesion() {
        imagePerfil = (ImageView) findViewById(R.id.imagePerfil);
        txtNombre = (TextView) findViewById(R.id.txtName);
        txtDescripcion = (TextView) findViewById(R.id.txtDescripcion);
        usuarioseleccionado = Contenedor.usuarioSelected;
        if(usuarioseleccionado!=null){llenarDatos();}
    }

    @Override
    public void configurarContent() {
        setContentView(R.layout.activity_visor_perfil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void vistaNoSesion() {
         vistaInicioSesion();
    }


    public  void llenarDatos() {
        Picasso.with(VisorPerfil.this).load(usuarioseleccionado.getUrlImagePerfil()).into(imagePerfil);
        txtNombre.setText(usuarioseleccionado.getNombre());
        txtDescripcion.setText(usuarioseleccionado.getDescripcion());
    }

}
