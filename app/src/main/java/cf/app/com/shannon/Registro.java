package cf.app.com.shannon;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;


import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import cf.app.com.shannon.estaticas.staticConexion;
import cf.app.com.shannon.plantilla.TemplateActivity;

/**
 * Created by David on 30/03/2016.
 */
public class Registro extends TemplateActivity {

    ConsultaOnline consultaOnline;
    EditText nombreText;
    EditText emailText;
    EditText passText;
    EditText apellidoText;
    EditText telefonoText;
    Button _signupButton;
     ProgressDialog pDialog;
    String urlregistro ;
    JSONObject jsonParam;
    EditText nickText;



    @Override
    public void vistaInicioSesion() {

    }

    @Override
    public void configurarContent() {
        setContentView(R.layout.activity_registrousuario);
        //-----------
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_my_toolbar);
        toolbar.setTitle("Registro");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Registro.this, Shannon.class);
                startActivity(i);
            }
        });
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    public void vistaNoSesion() {
        //Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        //url conexion
        urlregistro = staticConexion.REGISTRARSE ;
        //ligar variables
        nombreText = (EditText) findViewById(R.id.etRNombre);
        emailText = (EditText) findViewById(R.id.etREmail);
        passText = (EditText) findViewById(R.id.etRPass);
        apellidoText = (EditText) findViewById(R.id.etRApellido);
        telefonoText = (EditText) findViewById(R.id.etRTel);
        _signupButton = (Button) findViewById(R.id.btnRegistrar);
        nickText = (EditText) findViewById(R.id.textNombreUsuario);
        //back button

        //
    }

    public void onClickRegistrar(View v) {

        if (!validar()) {
            return;
        }

        _signupButton.setEnabled(false);
       pDialog.setMessage("Creando cuenta...");

        String name = nombreText.getText().toString().trim();
        String email = emailText.getText().toString().trim();
        String password = passText.getText().toString();
        String telefono = telefonoText.getText().toString().trim();
        String apellido = apellidoText.getText().toString().trim();
        String nickname = nickText.getText().toString().trim();

        try {
            //Conexion
            hiloRegistro hiloreg = new hiloRegistro();
            jsonParam = new JSONObject();
            jsonParam.put("CORREOREGISTRO",email);
            jsonParam.put("PASSWORD", password);
            jsonParam.put("TELEFONOREGISTRO", telefono);
            jsonParam.put("NICKNAME",nickname);
            jsonParam.put("NOMBRE",name);
            jsonParam.put("APELLIDOS", apellido);

            hiloreg.execute();
        } catch ( JSONException e) {
            e.printStackTrace();
        }

       /* new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);*/


    }



    public boolean validar() {
        boolean valid = true;
        String email = emailText.getText().toString();
        String nombre = nombreText.getText().toString();

         String pass = passText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("No es correo valido");
            valid = false;
        } else {
            emailText.setError(null);
            email = email.trim();
        }
        if (pass.length()<6 ) {
            passText.setError("entre 6 y 20 caracteres");
            valid = false;
        } else {
            passText.setError(null);
            pass = pass.trim();
        }

        if (nombre.isEmpty()) {
            nombreText.setError("Campo Obligatorio");
            valid = false;
        } else {
            nombreText.setError(null);
            nombre  = nombre.trim();
        }

        if (telefonoText.getText().toString().isEmpty()) {
            telefonoText.setError("Campo Obligatorio");
            valid = false;
        } else {
            telefonoText.setError(null);
        }

        return valid;


    }



   private   class hiloRegistro extends AsyncTask <Void,Void,String>{

       @Override
       protected String doInBackground(Void... params) {
           String retorno="";
           try {
                URL url = new URL(urlregistro);
               HttpURLConnection urlConn;
               DataOutputStream printout;
               urlConn = (HttpURLConnection) url.openConnection();
               urlConn.setDoInput(true);
               urlConn.setDoOutput(true);
               urlConn.setUseCaches(false);
               urlConn.setRequestProperty("Content-Type", "application/json");
               urlConn.setRequestProperty("Accept", "application/json");
               urlConn.connect();
               OutputStream os = urlConn.getOutputStream();
               BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
               writer.write(jsonParam.toString());
               writer.flush();
               writer.close();
               int respuesta = urlConn.getResponseCode();
               StringBuilder result = new StringBuilder();
               if (respuesta == HttpURLConnection.HTTP_OK) {
                   String line;
                   BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                   while ((line = br.readLine()) != null) {
                       result.append(line);
                   }

                   JSONObject respuestaJSON = new JSONObject(result.toString());
                   String resultJSON = respuestaJSON.getString("estado");
                   if(resultJSON=="1"){
                       retorno= "se ha registrado correctamente";
                   }
               }
               else {
                   retorno="error de conexion a la pagina";
               }

           }
           catch (IOException r ){
               retorno = "Error de internet";
           }
           catch (JSONException j){
               retorno = "La cuenta ya existe";
           }
           catch(Exception e ){
               retorno = "error";
           }

           return retorno;
       }

       public void onPreExecute() {
        pDialog.show();
       }


       @Override
       protected void onPostExecute(String s) {
           CharSequence text = s;
           int duration = Toast.LENGTH_SHORT;
           Toast toast = Toast.makeText(Registro.this, text, duration);
           toast.show();
           _signupButton.setEnabled(true);
           pDialog.dismiss();
           startActivity(new Intent(getBaseContext(), Shannon.class)
                   .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
           finish();
       }


   }


}
