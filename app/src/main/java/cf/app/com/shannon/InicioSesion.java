package cf.app.com.shannon;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

import cf.app.com.shannon.Users.Usuario;
import cf.app.com.shannon.Users.UsuarioPersonal;
import cf.app.com.shannon.estaticas.Contenedor;
import cf.app.com.shannon.estaticas.staticConexion;
import cf.app.com.shannon.plantilla.TemplateActivity;


public class InicioSesion extends TemplateActivity {

    Usuario usuarioConectado;
    Button btnIniciarSecion;
    CallbackManager callbackManager;
    EditText _emailText;
    EditText _passwordText;
    ProgressDialog pDialog;
    String pass;
    String email;
    String nombre;

    @Override
    public void vistaInicioSesion() {
        btnIniciarSecion = (Button) findViewById(R.id.btnIniciar);
        _passwordText = (EditText) findViewById(R.id.etPasword);
        _emailText = (EditText) findViewById(R.id.etUsername);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_my_toolbar);
        toolbar.setTitle("Inicio sesion");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InicioSesion.this, Shannon.class);
                startActivity(i);
            }
        });
        //PDIALOG
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
    }

    @Override
    public void configurarContent() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_iniciar_sesion);
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    email = object.getString("email");// 01/31/1980 format

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });


                CharSequence text = email;
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(InicioSesion.this, text, duration);
                toast.show();


            }

            @Override
            public void onCancel() {
                CharSequence text = "cancelado papu :v";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(InicioSesion.this, text, duration);
                toast.show();

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    public void vistaNoSesion() {
       vistaInicioSesion();
    }


    public void onClickInicioSesion(View v ){
        if(validar ()){
            pDialog.setMessage("Iniciando sesion...");
            pass = _passwordText.getText().toString();
            hiloIniciosesion hiloIniciosesion = new hiloIniciosesion() ;
            hiloIniciosesion.execute(_emailText.getText().toString().trim());
        }
   }

    public void  postHilo(){
        if(!(usuarioConectado == null)){
            Contenedor.usuario = usuarioConectado;
            Intent i = new Intent(this,Shannon.class);
            startActivity(new Intent(getBaseContext(), Shannon.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shannon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickrecuoerarpass(View v){
        CharSequence text = "TODO RECUPERAR CONTRASEÑA ";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }

    public void  onClickRegistro(View v){
        Intent i = new Intent(this,Registro.class);

        startActivity(i);

        CharSequence text = "Vamos a registrarnos  ";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }

    public boolean validar (){

        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("No es correo valido");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 20) {
            _passwordText.setError("entre 6 y 20 caracteres ");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    //CLASE HILO

    private   class hiloIniciosesion extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            String correo = params[0];
            String retorno="";
            try {
                URL url = new URL(staticConexion.GETCUENTA_BY_ID+"?CORREOREGISTRO="+correo);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                        "(Linux; Android 1.5; es-ES) Ejemplo HTTP");
                int respuesta = connection.getResponseCode();
                StringBuilder result = new StringBuilder();
                if(respuesta == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    line = reader.readLine();
                    while ( line!= null) {
                        result.append(line);
                        System.out.println(line+"<--------");
                        line = reader.readLine();
                    }
                    //line="{\"estado\":1,\"categorias\":[{\"id_Categoria\":\"1\",\"NombreCategoria\":\"Productos Software\"},{\"id_Categoria\":\"3\",\"NombreCategoria\":\"Servicios Web\"},{\"id_Categoria\":\"2\",\"NombreCategoria\":\"comidas\"},{\"id_Categoria\":\"4\",\"NombreCategoria\":\"Abogados\"}]}";
                    //result.append(line);
                    JSONObject rJSON = new JSONObject(result.toString());
                    String reJ  = rJSON.getString("Se encontro?");
                    JSONObject objectjson  = rJSON.getJSONObject("Cuenta");
                    if (reJ.equals("1") ) {

                            if(pass.equals(objectjson.getString("PASSWORD"))) {
                            JSONObject jsonParam= new JSONObject();
                            jsonParam.put("CORREOREGISTRO",correo);
                            url = new URL(staticConexion.GETPERFIL_BY_ID);
                            HttpURLConnection urlConn;
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
                            respuesta = urlConn.getResponseCode();
                            StringBuilder resultado = new StringBuilder();

                            if (respuesta == HttpURLConnection.HTTP_OK) {
                                String lin;
                                BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                                while ((lin = br.readLine()) != null) {
                                    resultado.append(lin);
                                    System.out.println(lin);
                                }


                                JSONObject respuestaJSON = new JSONObject(resultado.toString());

                                JSONObject perfiljson  = respuestaJSON.getJSONObject("Datos perfil solicitado");
                                usuarioConectado = new UsuarioPersonal(objectjson.getString("CORREOREGISTRO")
                                        , perfiljson.getString("NOMBRE")
                                        , perfiljson.getString("APELLIDOS")
                                        , perfiljson.getString("DESCRIPCIONPERFIL")
                                        , objectjson.getString("TELEFONOREGISTRO")
                                        , objectjson.getString("NICKNAME")
                                        ,descargarImagen(staticConexion.IMAGENES+perfiljson.getString("IMAGENPERFIL")));
                                usuarioConectado.setCalificacionPromedio(Float.parseFloat(perfiljson.getString("CALIFICACIONPROMEDIO")));
                                retorno  = "sesion iniciada :)";
                            }

                        }
                        else  {
                            retorno = "CONTRASEÑA INVALIDA ;V";
                        }
                    }
                    else retorno = "No hay usuario registrado con ese correo ";

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return retorno;
        }

        public void onPreExecute() {
            pDialog.show();
        }

        private Bitmap descargarImagen (String imageHttpAddress){
            URL imageUrl = null;
            Bitmap imagen = null;
            try{
                imageUrl = new URL(imageHttpAddress);
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                conn.connect();
                BitmapFactory.Options options = new  BitmapFactory.Options();
                options.inSampleSize=calculateInSampleSize(options,300,300);
                imagen = BitmapFactory.decodeStream(conn.getInputStream(),null,options);
            }catch(IOException ex){
                ex.printStackTrace();
            }

            return imagen;
        }

        public int calculateInSampleSize(
                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;
            if (height > reqHeight || width > reqWidth) {
                final int halfHeight = height / 2;
                final int halfWidth = width / 2;
                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }


        @Override
        protected void onPostExecute(String s) {
            CharSequence text = s;
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(InicioSesion.this, text, duration);
            toast.show();
            btnIniciarSecion.setEnabled(true);
            pDialog.dismiss();
            postHilo();

        }


    }


}
