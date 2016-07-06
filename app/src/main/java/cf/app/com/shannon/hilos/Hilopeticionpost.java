package cf.app.com.shannon.hilos;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by David on 18/05/2016.
 */
public abstract class Hilopeticionpost extends AsyncTask<String, Void, String> {
    JSONObject jsonParam;
    JSONObject jsonresultado;


    public JSONObject getJsonresultado() {
        return jsonresultado;
    }

    @Override
    protected void onPreExecute() {
        AntesEjecucion();
        jsonParam = llenarJsonParam();
    }
    public abstract JSONObject llenarJsonParam();
    public abstract  void AntesEjecucion();

    @Override
    protected String doInBackground(String... params) {
        String retorno="";
        try{
            URL url = new URL(params[0]);
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
            int respuesta = urlConn.getResponseCode();
            StringBuilder result = new StringBuilder();
            if (respuesta == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                jsonresultado = new JSONObject(result.toString());
                String resultJSON = jsonresultado.getString("estado");
                if (resultJSON == "1") {
                    retorno = "se ha guardado  correctamente";
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


    @Override
    protected void onPostExecute(String s) {
        despuesEjecucion(s,jsonresultado);
    }

    public abstract  void despuesEjecucion(String retorno,JSONObject jsonresultado);

}