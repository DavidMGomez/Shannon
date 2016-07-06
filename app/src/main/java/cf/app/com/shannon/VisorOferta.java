package cf.app.com.shannon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.StaticLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
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
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cf.app.com.shannon.Users.Usuario;
import cf.app.com.shannon.Users.UsuarioPersonal;
import cf.app.com.shannon.estaticas.Contenedor;
import cf.app.com.shannon.estaticas.staticConexion;

public class VisorOferta extends AppCompatActivity {
    String tituloOferta, descripcion, nombreOfertador, descripcionOfertador, urlImageOferta;
    float calificacion;
    float calificaciondeUsuario;
    ImageView imageOfetador;
    private TextView txtTitulo;
    private TextView txtDescripcion;
    private ImageView imageOferta;
    private Servicio miservicio;
    private RatingBar miratingbarOferta;
    private RatingBar ratingbarMiCalificacion;
    private Usuario usuarioOfertador;
    LinearLayout linearCalificacion;
    TextView textNombreOfertador;
    TextView textDescripcionOfertador;
    private int lastTop = 0;

     public void ligarVariables(){
         txtTitulo = (TextView) findViewById(R.id.txtTituloOferta);
         txtDescripcion = (TextView) findViewById(R.id.txtDescripcionOferta);
         imageOferta = (ImageView) findViewById(R.id.imageOferta);
         textNombreOfertador = (TextView) findViewById(R.id.textNombreOfertador);
         textDescripcionOfertador = (TextView) findViewById(R.id.textDescripcionOfertador);
         miservicio = Contenedor.servicioSelected;
         imageOfetador = (ImageView) findViewById(R.id.imageOfertador);
         tituloOferta = miservicio.getName();
         descripcion = miservicio.getDescripcion();
         miratingbarOferta = (RatingBar) findViewById(R.id.ratingBar);
         ratingbarMiCalificacion = (RatingBar) findViewById(R.id.ratingBar2);
         miratingbarOferta.setIsIndicator(true);

     }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visor_oferta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ligarVariables();//liga todas las Variables
        Drawable progress = miratingbarOferta.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.YELLOW);
        if (Contenedor.usuario != null) {
            progress = ratingbarMiCalificacion.getProgressDrawable();
            DrawableCompat.setTint(progress, Color.YELLOW);
            //obtener la calificacion dado si la hay
            ratingbarMiCalificacion.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    calificaciondeUsuario = rating;
                    new HiloCalificar().execute();
                }
            });

        } else {
            ratingbarMiCalificacion.setEnabled(false);
        }

        calificacion = miservicio.getCalificacion();
        urlImageOferta = miservicio.getUriImageService();
        new Hilomod().execute();//creamos el usuario
        llenarDatos();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //

    }

    private class Hilomod extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String retorno = "";
            try {
                URL url = new URL(staticConexion.GETPERFIL_BY_ID);
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("CORREOREGISTRO", miservicio.getIdOfertador());
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
                    String resultJSON = respuestaJSON.getString("Se encontro?");

                    System.out.println(respuestaJSON.toString());
                    if (resultJSON == "1") {

                        JSONObject jrespuesta = respuestaJSON.getJSONObject("Datos perfil solicitado");
                        JSONObject perfiljson = respuestaJSON.getJSONObject("Datos perfil solicitado");

                        usuarioOfertador = new UsuarioPersonal(perfiljson.getString("CORREOREGISTRO")
                                , perfiljson.getString("NOMBRE")
                                , perfiljson.getString("APELLIDOS")
                                , perfiljson.getString("DESCRIPCIONPERFIL")
                                , (staticConexion.IMAGENES + perfiljson.getString("IMAGENPERFIL")));

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

        }


        @Override
        protected void onPostExecute(String s) {
            llenarDatos();
        }


    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

    private class HiloCalificar extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String retorno = "";
            try {
                URL url = new URL(staticConexion.CALIFICAROFERTA);
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("CODIGOOFERTA", miservicio.getCodigo());
                jsonParam.put("CALIFICACION", calificaciondeUsuario);
                jsonParam.put("CORREOCALIFICADOR", Contenedor.usuario.getCorreo());
                jsonParam.put("FECHACREACION",fechaActual() );
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

                    System.out.println(respuestaJSON.toString());
                    if (resultJSON == "1") {
                        retorno = "Calificacion registrada!!";
                    }
                    else{
                        retorno = "Error al calificar oferta !!";
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

        }


        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(VisorOferta.this,s,Toast.LENGTH_SHORT).show();
        }


    }

    public  void llenarDatos(){
        if(urlImageOferta !=null)  Picasso.with(VisorOferta.this).load(urlImageOferta).into(imageOferta);
       // imageOferta.setImageBitmap(getCircleBitmap(imageOferta.getDrawingCache()));
        if(tituloOferta !=null) txtTitulo.setText(tituloOferta);
        if(descripcion !=null) txtDescripcion.setText("Descripcion: "+ descripcion);
         miratingbarOferta.setRating(calificacion);

        if(usuarioOfertador !=null) {
            Picasso.with(VisorOferta.this).load(usuarioOfertador.getUrlImagePerfil()).into(imageOfetador);
            textNombreOfertador.setText(usuarioOfertador.getNombre());
            textDescripcionOfertador.setText(usuarioOfertador.getDescripcion());
        }
        }


    public void onClickOfertante(View v){
        Contenedor.usuarioSelected = usuarioOfertador;
        Intent i = new Intent(this,VisorPerfil.class);
        startActivity(i);
    }
    public String fechaActual(){
        return new SimpleDateFormat( "yyyy-MM-dd", java.util.Locale.getDefault()).format(Calendar.getInstance() .getTime());
    }
}