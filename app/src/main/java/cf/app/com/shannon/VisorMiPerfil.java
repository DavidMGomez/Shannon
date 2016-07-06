package cf.app.com.shannon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import cf.app.com.shannon.Users.Usuario;
import cf.app.com.shannon.estaticas.Contenedor;
import cf.app.com.shannon.estaticas.staticConexion;

public class VisorMiPerfil extends AppCompatActivity {
    public String SERVER = staticConexion.UPLOADIMGPERFIL;
    public static final String UPLOAD_KEY = "image";
    private static final String TAG =VisorMiPerfil.class.getSimpleName();
    Usuario usuarioconectado;
    Toolbar toolbar;
    ImageView imageperfil;
    TextView txtNombre;
    EditText txtContacto;
    EditText txtDescripcion;
    String descripcion;
    ProgressDialog pDialog;
    FloatingActionButton fbutoncamara;
    String foto;
    static private final int SELECT_PHOTO = 100;
    Bitmap bitmap = null;
    String encoded_string;
    RatingBar barcalficacion ;
    TextView txtCalificacion;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        usuarioconectado = Contenedor.usuario;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visor_miperfil);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageperfil = (ImageView) findViewById(R.id.image_perfil);
        txtContacto = (EditText) findViewById(R.id.txtContacto);
        txtDescripcion = (EditText) findViewById(R.id.txtDescripcion);
        fbutoncamara = (FloatingActionButton) findViewById(R.id.btn_camara);
        txtNombre = (TextView) findViewById(R.id.textNombre);
        barcalficacion = (RatingBar) findViewById(R.id.rbCalificacion);
        LayerDrawable stars = (LayerDrawable) barcalficacion.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        barcalficacion.setEnabled(false);

        txtCalificacion = (TextView) findViewById(R.id.txtRep);
        cargarDatosuser();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    public void cargarDatosuser() {
        if (usuarioconectado != null) {
            toolbar.setTitle("");
            imageperfil.setImageBitmap(usuarioconectado.getUriImagePerfil());
            txtNombre.setText(usuarioconectado.getNombre());
            setSupportActionBar(toolbar);
            txtContacto.setText(usuarioconectado.getTelefono());
            txtDescripcion.setText(usuarioconectado.getDescripcion());
            if(usuarioconectado.getCalificacionPromedio()>0){
            barcalficacion.setRating(usuarioconectado.getCalificacionPromedio());}
            else{
                barcalficacion.setRating(0);
                txtCalificacion.setText("No hay calificaiones");
            }
        }

    }

    public void onClicksave(View v) {
        descripcion = txtDescripcion.getText().toString();
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Guardando...");
        hilomod hilo = new hilomod();
        hilo.execute();
    }


    public void Onclickcamerabuton(View v) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri selectedImageURI = imageReturnedIntent.getData();
                    File imageFile = new File(getRealPathFromURI(selectedImageURI));
                    bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds=false;
                    options.inSampleSize = 3;
                    bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()),null,options);
                    imageperfil.setImageBitmap(bitmap);
                    usuarioconectado.setUriImagePerfil(bitmap);
                    pDialog = new ProgressDialog(this);
                    pDialog.setCancelable(false);
                    pDialog.setMessage("Subiendo...");
                    new hiloimgupload().execute();

                }
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    ///TAREAS ASINCRONAS ::::::::::::::
    private class hilomod extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String retorno = "";
            try {
                URL url = new URL(staticConexion.EDITDESCRIPCION);
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("CORREOREGISTRO", usuarioconectado.getCorreo());
                jsonParam.put("NUEVADESCRIPCION", descripcion);
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
                    if (resultJSON == "1") {
                        retorno = "se ha modificado correctamente";
                    }
                } else {
                    retorno = "error de conexion a la pagina";
                }

            } catch (IOException r) {
                retorno = "Error de internet";
            } catch (JSONException j) {
                j.printStackTrace();
            } catch (Exception e) {
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
            Toast toast = Toast.makeText(VisorMiPerfil.this, text, duration);
            toast.show();
            pDialog.dismiss();
            usuarioconectado.setDescripcion(descripcion);

        }


    }
    private class hiloimgupload extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String retorno = "";
            try {
                URL url = new URL(staticConexion.UPLOADIMGPERFIL);
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("CORREOREGISTRO", usuarioconectado.getCorreo());
                jsonParam.put("image", getStringImage(bitmap));
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
                    if (resultJSON == "1") {
                        retorno = "se ha modificado correctamente";
                    }

                } else {
                    retorno = "error de conexion a la pagina";
                }

            } catch (IOException r) {
                retorno = "Error de internet";
            } catch (JSONException j) {
                j.printStackTrace();
            } catch (Exception e) {
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
            Toast toast = Toast.makeText(VisorMiPerfil.this, text, duration);
            toast.show();
            pDialog.dismiss();

        }


    }


}
