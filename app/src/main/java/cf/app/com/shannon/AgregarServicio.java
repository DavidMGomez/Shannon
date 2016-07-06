package cf.app.com.shannon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import cf.app.com.shannon.Users.Usuario;
import cf.app.com.shannon.estaticas.Contenedor;
import cf.app.com.shannon.estaticas.staticConexion;
import cf.app.com.shannon.hilos.Hilopeticionpost;
import cf.app.com.shannon.plantilla.TemplateActivity;

/**
 * Created by David on 16/04/2016.
 */
public class AgregarServicio extends TemplateActivity{

    static private final int SELECT_PHOTO = 100;
    private  Bitmap bitmap ;
    private ImageView imageServicio;
    private Usuario usuarioconectado;
    private ProgressDialog pDialog;
    private ImageButton btnImagenservice ;
    private Button btnGuardar;
    private EditText txtTitulo;
    private EditText txtDescripcion;
    private EditText txtCategoria;
    String titulo,descripcion,categoria;

    @Override
    public void configurarContent() {
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_agregar_servicio);
    }

    @Override
    public void vistaInicioSesion() {
        imageServicio = (ImageView) findViewById(R.id.imgService);
        btnImagenservice = (ImageButton) findViewById(R.id.btn_camara);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        txtTitulo = (EditText) findViewById(R.id.txtTitulo);
        txtDescripcion = (EditText) findViewById(R.id.txtDescripcion);
        txtCategoria = (EditText) findViewById(R.id.txtCategoria);
        usuarioconectado  = Contenedor.usuario;
        bitmap = null;
    }



    @Override
    public void vistaNoSesion() {

    }

    //events
    public void Onclickcamerabuton(View v) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    public void  onClickGuardar (View v ){
       titulo = txtTitulo.getText().toString();
       descripcion = txtDescripcion.getText().toString();
       categoria = txtCategoria.getText().toString();
       pDialog = new ProgressDialog(this);
       pDialog.setMessage("Guardando");
       pDialog.setCancelable(false);
        Hilopeticionpost hilo = new Hilopeticionpost() {
            @Override
            public void AntesEjecucion() {
                pDialog.show();
            }
            @Override
            public JSONObject llenarJsonParam() {
               JSONObject jsonParam = new JSONObject();
                try{
                jsonParam.put("CORREOREGISTRO", usuarioconectado.getCorreo());
                if(bitmap == null){jsonParam.put("IMAGENPRESENTACION", "default");}
                else{jsonParam.put("IMAGENPRESENTACION", getStringImage(bitmap));}
                jsonParam.put("TITULOOFERTA", titulo);
                jsonParam.put("IDCATEGORIASERVICIO", categoria);
                String date = (DateFormat.format("dd-MM-yyyy", new java.util.Date()).toString());
                jsonParam.put("FECHACREACION",date.toString());
                jsonParam.put("DESCRIPCION",descripcion);
                }
                catch (JSONException e) {
                    System.out.println(e.toString());
                }

                return jsonParam;
            }
            @Override
            public void despuesEjecucion(String retorno,JSONObject jsonresultado) {
                CharSequence text = retorno;
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(AgregarServicio.this, text, duration);
                toast.show();
                pDialog.dismiss();
            }
        };
        hilo.execute(staticConexion.AGREGAROFERTA);
    }
    //

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
                    options.inSampleSize = 2;
                    bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()),null,options);
                    imageServicio.setImageBitmap(bitmap);
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



}
