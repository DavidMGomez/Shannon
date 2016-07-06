package cf.app.com.shannon;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cf.app.com.shannon.Users.Usuario;
import cf.app.com.shannon.estaticas.Contenedor;
import cf.app.com.shannon.estaticas.staticConexion;
import cf.app.com.shannon.plantilla.TemplateActivity;

public class Shannon extends TemplateActivity
        implements NavigationView.OnNavigationItemSelectedListener,SearchView.OnQueryTextListener{
    private Usuario usuarioConectado;
    private SearchView mSearchView;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private CardView cardView;
    private RecyclerView.Adapter mAdapter;
    private TextView  textNombreuser;
    private ImageView imageperfil;
    SwipeRefreshLayout swipeContainer;
    private List<Servicio> servicios;
    private  ListView lvservicios;
    private Usuario usuarioconectado;
    private ServiceAdapter adapterService;
    JSONArray misserviciosJSON;
    NavigationView navigationView;
    private  vpAdapter miAdapterViewPage;
   ScrollView scroll;
    private int lastTop = 0;
    private ImageView imageheader;
    SwipeRefreshLayout swipeRefreshLayouthome;

    public void parallax(final View v) {
        final Rect r = new Rect();
        v.getLocalVisibleRect(r);

        if (lastTop != r.top) {
            lastTop = r.top;
            v.post(new Runnable() {
                @Override
                public void run() {
                    v.setY((float) (r.top / 2.0));
                }
            });
        }
    }


    public void configurarListaServiciosHome(){
        lvservicios = (ListView) findViewById(R.id.lvServices);
        LayoutInflater inflater = getLayoutInflater();
        LinearLayout listHeaderView = (LinearLayout)inflater.inflate(
                R.layout.headhomelist, null);

        lvservicios.addHeaderView(listHeaderView);

        final ViewPager  myviewpagerhome = (ViewPager) findViewById(R.id.myviewpagerhome);
        miAdapterViewPage = new vpAdapter();
        myviewpagerhome.setAdapter(miAdapterViewPage);


        lvservicios.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                parallax(myviewpagerhome);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                parallax(myviewpagerhome);
            }
        });

        lvservicios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    position -= lvservicios.getHeaderViewsCount();
                    Toast.makeText(Shannon.this, "Ha seleccionado " + position + servicios.get(position).getName(), Toast.LENGTH_SHORT).show();
                    Contenedor.servicioSelected = servicios.get(position);
                    Intent i = new Intent(Shannon.this, VisorOferta.class);
                    startActivity(i);
                }
            }
        });
    }

    public void init() {
        setContentView(R.layout.activity_shannon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Shannon");
        setSupportActionBar(toolbar);
        configurarListaServiciosHome();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
         swipeRefreshLayouthome = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_home);
        swipeRefreshLayouthome.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);
        swipeRefreshLayouthome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new hiloobtenerservices().execute();
            }
        });
    }

    private void cargarDatosUsuario(){
        textNombreuser.setText(usuarioConectado.getNombre());
        imageperfil.setImageBitmap(usuarioConectado.getUriImagePerfil());
    }



    @Override
    public void vistaInicioSesion() {
      enlazarVariables();
        navigationView.inflateMenu(R.menu.activity_shannon_drawer);
        verificalogin();
        new hiloobtenerservices().execute();
    }

    @Override
    public void configurarContent() {
        init();
        usuarioConectado  = Contenedor.usuario;
    }

    @Override
    public void vistaNoSesion() {
        enlazarVariables();
        navigationView.inflateMenu(R.menu.activity_shannon_drawer_nosesion);
        verificalogin();
        new hiloobtenerservices().execute();
    }
    public void enlazarVariables(){
        View v = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        textNombreuser = (TextView) v.findViewById(R.id.txtNombreUser);
        imageperfil = (ImageView) v.findViewById(R.id.imgperfil);
    }

    private void verificalogin() {
        if(!(usuarioConectado ==null)){
           //TODO  QUITAR INICIO PONER SALIR
            System.out.println(usuarioConectado.getNickname());
            cargarDatosUsuario();
        }

    }

//OVERRIDE

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shannon, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Contenedor.PalabraBusqueda= query;
                Intent i = new Intent(Shannon.this,Visor_busqueda.class);
                startActivity(i);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //System.out.println("EL TEXTO HA CAMBIADO ");
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle igation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_inisesion) {
            Intent i = new Intent(this,InicioSesion.class);
            startActivity(i);
            finish();
        }else  if (id == R.id.nav_Miperfil) {
            Intent i = new Intent(this,VisorMiPerfil.class);
            startActivity(i);
        }else if (id == R.id.nav_salir) {
            Contenedor.usuario = null;
            Intent i = new Intent(this,Shannon.class);
            startActivity(i);

        }
        else if (id == R.id.nav_datosper) {
            Intent i = new Intent(this,DatosPersonales.class);
            startActivity(i);
        }else if (id == R.id.nav_agregarServicio) {
            Intent i = new Intent(this,AgregarServicio.class);
            startActivity(i);
        }
        else if (id == R.id.nav_misServicios) {
            Intent i = new Intent(this,MisServicios.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private class hiloobtenerservices extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String retorno = "";
            try {

                URL url = new URL(staticConexion.OBTENERALLSERVICIOS);

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
                    while ( line!= null) {
                        result.append(line);
                        line = reader.readLine();
                    }
                    System.out.println(":v");
                    JSONObject respuestaJSON = new JSONObject(result.toString());

                    String resultJSON = respuestaJSON.getString("estado");
                    if (resultJSON == "1") {
                        misserviciosJSON = respuestaJSON.getJSONArray("categorias");
                    }
                    else{
                        misserviciosJSON = new JSONArray();
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
            obtenerServicios();
            swipeRefreshLayouthome.setRefreshing(false);

        }


    }

    private void obtenerServicios(){
        if(misserviciosJSON!=null){
        servicios = new ArrayList <Servicio>();
        adapterService = new ServiceAdapter(this,servicios);
        ListView listServices  = (ListView) findViewById(R.id.lvServices);
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


    private class vpAdapter extends PagerAdapter{


        @Override
        public void finishUpdate(ViewGroup container) {
            super.finishUpdate(container);
        }

        @Override
        public void startUpdate(ViewGroup container) {
            super.startUpdate(container);
        }

        @Override
        public Parcelable saveState() {
           return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = (LayoutInflater)container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = null;
            switch (position){
                case 0:
                    v = inflater.inflate(R.layout.slide1home,null);
                    break;
                case 1:
                    v = inflater.inflate(R.layout.slide2home,null);
                    break;
                case 2:
                    v = inflater.inflate(R.layout.slide3home,null);
                    break;
            }
            container.addView(v,0);
            return v;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view ==( (LinearLayout)object);
        }
    }


}
