package cf.app.com.shannon;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cf.app.com.shannon.estaticas.Contenedor;
import cf.app.com.shannon.estaticas.staticConexion;
import cf.app.com.shannon.hilos.Hilopeticionpost;
import cf.app.com.shannon.plantilla.TemplateActivity;

public class Visor_busqueda extends TemplateActivity {

    private List<Servicio> servicios;
    private ListView lvservicios;
    JSONArray misserviciosJSON;
    private ServiceAdapter adapterService;

    @Override
    public void vistaInicioSesion() {
      vistaNoSesion();
    }

    @Override
    public void configurarContent() {
        setContentView(R.layout.activity_visor_busqueda);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Contenedor.PalabraBusqueda);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        lvservicios = (ListView) findViewById(R.id.lvServicesfound);
        lvservicios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Visor_busqueda.this, "Ha seleccionado " + servicios.get(position).getName(), Toast.LENGTH_SHORT).show();
                Contenedor.servicioSelected = servicios.get(position);
                Intent i = new Intent(Visor_busqueda.this,VisorOferta.class);
                startActivity(i);
            }
        });


    }

    @Override
    public void vistaNoSesion() {
        Hilopeticionpost hilo = new Hilopeticionpost(){


            public JSONObject llenarJsonParam() {
                JSONObject json = new JSONObject();
                try {
                    json.put("OFERTA", Contenedor.PalabraBusqueda);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return json;
            }

            @Override
            public void AntesEjecucion() {
                //cargando... algo asi
            }

            @Override
            public void despuesEjecucion(String retorno, JSONObject jsonresultado) {
                try {
                    misserviciosJSON =  getJsonresultado().getJSONArray("oferta");
                    obtenerServicios();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
       if(Contenedor.PalabraBusqueda!=null) hilo.execute(staticConexion.BUSCAR);


    }

    private void obtenerServicios(){
        if(misserviciosJSON!=null){
            servicios = new ArrayList<Servicio>();
            adapterService = new ServiceAdapter(this,servicios);
            ListView listServices  = (ListView) findViewById(R.id.lvServicesfound);
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
    }



}
