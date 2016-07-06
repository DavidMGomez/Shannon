package cf.app.com.shannon;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.List;

import cf.app.com.shannon.Users.Usuario;
import cf.app.com.shannon.estaticas.Contenedor;
import cf.app.com.shannon.estaticas.staticConexion;
import cf.app.com.shannon.plantilla.TemplateActivity;

/**
 * Created by David on 17/04/2016.
 */
public class MisServicios extends TemplateActivity{

    private List<Servicio> servicios;
    private Usuario usuarioconectado;
    private ServiceAdapter adapterService;
    JSONArray misserviciosJSON;


    @Override
    public void vistaInicioSesion() {
        usuarioconectado = Contenedor.usuario;
        new hiloobtenerservices().execute();
    }

    @Override
    public void configurarContent() {
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_mis_servicios);
    }

    @Override
    public void vistaNoSesion() {

    }

    private void obtenerMisServicios(){
        servicios = new ArrayList<Servicio>();
        adapterService = new ServiceAdapter(this,servicios);
        ListView listServices  = (ListView) findViewById(R.id.lvmisServices);
        listServices.setAdapter(adapterService);
        try {
            for (int i = 0; i < misserviciosJSON.length(); i++) {
                JSONObject serviceJSON = misserviciosJSON.getJSONObject(i);
                Servicio item = new Servicio(serviceJSON.getString("CORREOREGISTRO"),serviceJSON.getString("TITULOOFERTA"),(staticConexion.IMAGENES+serviceJSON.getString("IMAGENPRESENTACION")),
                        serviceJSON.getString("DESCRIPCION"),serviceJSON.getString("IDCATEGORIASERVICIO"),serviceJSON.getString("CODIGOOFERTA"),Float.parseFloat(serviceJSON.getString("CALIFICACIONPROMEDIO")));
                servicios.add(item);
            }
            listServices.deferNotifyDataSetChanged();
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }



    private class hiloobtenerservices extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String retorno = "";
            try {
                URL url = new URL(staticConexion.OBTENERMISSERVICIOS);
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("CORREOREGISTRO", usuarioconectado.getCorreo());
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
                    System.out.println("------------------------------------");
                    JSONObject respuestaJSON = new JSONObject(result.toString());

                    String resultJSON = respuestaJSON.getString("estado");
                    if (resultJSON == "1") {
                         misserviciosJSON = respuestaJSON.getJSONArray("ofertas perfil solicitado");
                    }
                    else{
                        misserviciosJSON = new  JSONArray();
                    }


                } else {
                    retorno = "error de conexion a la pagina";
                }

            } catch (IOException r) {
                retorno = "Error de internet";
                System.out.println("error internet");
            } catch (JSONException j) {
                j.printStackTrace();
                System.out.println("Error json");
            }

            return retorno;
        }

        @Override
        protected void onPostExecute(String s) {
            obtenerMisServicios();
        }


    }




}
