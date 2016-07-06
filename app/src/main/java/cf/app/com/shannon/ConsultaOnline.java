package cf.app.com.shannon;

import android.os.AsyncTask;

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

/**
 * Created by David on 1/04/2016.
 */
public class ConsultaOnline extends  AsyncTask<JSONObject,Void,JSONObject>  {
    private URL url;

    public ConsultaOnline(String url) throws MalformedURLException {
        this.url = new URL(url);
    }

    public JSONObject consultarGET() throws IOException, JSONException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                "(Linux; Android 1.5; es-ES) Ejemplo HTTP");
        int respuesta = connection.getResponseCode();
        StringBuilder result = new StringBuilder();
        if (respuesta == HttpURLConnection.HTTP_OK) {
            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            line = reader.readLine();
            while (line != null) {
                result.append(line);
                System.out.println(line + "<--------");
                line = reader.readLine();
            }
            //line="{\"estado\":1,\"categorias\":[{\"id_Categoria\":\"1\",\"NombreCategoria\":\"Productos Software\"},{\"id_Categoria\":\"3\",\"NombreCategoria\":\"Servicios Web\"},{\"id_Categoria\":\"2\",\"NombreCategoria\":\"comidas\"},{\"id_Categoria\":\"4\",\"NombreCategoria\":\"Abogados\"}]}";
            //result.append(line);
            System.out.println("HOLUU");
            JSONObject respuestaJSON = new JSONObject(result.toString());
            return respuestaJSON;
        }

        return null;
    }


    public JSONObject consultarPOST(JSONObject jsonParam) throws IOException, JSONException {
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

               return respuestaJSON;
           }


        return null;
    }


    @Override
    protected JSONObject doInBackground(JSONObject... params) {


        return null;
    }
}