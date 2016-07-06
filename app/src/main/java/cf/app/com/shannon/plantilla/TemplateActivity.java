package cf.app.com.shannon.plantilla;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import cf.app.com.shannon.R;
import cf.app.com.shannon.estaticas.Contenedor;
import cf.app.com.shannon.estaticas.staticConexion;

/**
 * Created by David on 18/05/2016.
 */
public abstract class TemplateActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configurarContent();
        if(Contenedor.usuario!=null){
            vistaInicioSesion();
        }
        else{
            vistaNoSesion();
        }
    }

    public abstract  void vistaInicioSesion();
    public abstract  void configurarContent();
    public abstract  void vistaNoSesion();




}
