package cf.app.com.shannon;

import cf.app.com.shannon.Users.Usuario;

/**
 * Created by David on 31/03/2016.
 */
public class Servicio {
    private String mName;
    private int mThumbnail;
    private String Descripcion;
    private String Categoria;
    private String uriImageService;
    private String codigo;
    private String idOfertador;
    private Usuario ofertador;
    private  float calificacion;

    public String getCodigo() {
        return codigo;
    }

    public String getIdOfertador() {
        return idOfertador;
    }

    public void setIdOfertador(String idOfertador) {
        this.idOfertador = idOfertador;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(float calificacion) {
        this.calificacion = calificacion;
    }

    public Servicio(String idOfertador,String mName, String uriImageService, String descripcion, String categoria, String codigo,float calificacionprom) {
        this.mName = mName;
        this.uriImageService = uriImageService;
        Descripcion = descripcion;
        Categoria = categoria;
        this.codigo = codigo;
        this.idOfertador= idOfertador;
        this.calificacion = calificacionprom;
    }

    public String getName() {
        return mName;
    }

    public Usuario getOfertador() {
        return ofertador;
    }

    public void setOfertador(Usuario ofertador) {
        this.ofertador = ofertador;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmThumbnail() {
        return mThumbnail;
    }

    public void setmThumbnail(int mThumbnail) {
        this.mThumbnail = mThumbnail;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getUriImageService() {
        return uriImageService;
    }

    public void setUriImageService(String uriImageService) {
        this.uriImageService = uriImageService;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.mThumbnail = thumbnail;
    }
}