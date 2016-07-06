package cf.app.com.shannon;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import cf.app.com.shannon.Users.Usuario;
import cf.app.com.shannon.estaticas.Contenedor;

/**
 * Created by David on 15/04/2016.
 */
public class DatosPersonales extends AppCompatActivity {
    TextView txtNombre;
    TextView txtEmail;
    TextView txtContacto;
    TextView txtNick;
    Usuario usuarioConectado;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datospersonales);
        txtNombre= (TextView) findViewById(R.id.textNombre);
        txtContacto = (TextView) findViewById(R.id.textContacto);
        txtEmail = (TextView) findViewById(R.id.textEmail);
        txtNick  = (TextView) findViewById(R.id.textNombreUsuario);
        usuarioConectado = Contenedor.usuario;
        cargarDatosUser();
    }

    public void cargarDatosUser (){
        if(usuarioConectado!=null){
            txtContacto.setText(usuarioConectado.getTelefono());
            txtNick.setText(usuarioConectado.getNickname());
            txtEmail.setText(usuarioConectado.getCorreo());
            txtNombre.setText(usuarioConectado.getNombre());
        }

    }


}
