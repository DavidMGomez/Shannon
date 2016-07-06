package cf.app.com.shannon.estaticas;

/**
 * Created by David on 1/04/2016.
 */
public class staticConexion {
   public static String IP = "http://ec2-52-34-59-106.us-west-2.compute.amazonaws.com/";
   public static String GET = IP + "obtener_categorias.php";
   public static String GETCAT_BY_ID = IP + "obtener_categoria_por_id.php";
   public static String UPDATE = IP + "actualizar_categoria.php";
    public static String DELETE = IP + "borrar_categoria.php";
    public static String INSERT = IP + "insertar_categoria.php";
    public static  String REGISTRARSE = IP + "insertar_cuenta_p.php";
    public  static String GETCUENTA_BY_ID = IP +"obtener_cuenta_por_id.php";
    public  static String GETPERFIL_BY_ID = IP +"obtener_perfil_por_id.php";
    public static  String IMAGENES = IP+"imagenes/";
    public  static  String EDITDESCRIPCION = IP+"editar_descripcion_perfil.php";
    public  static  String UPLOADIMGPERFIL = IP+"uploadImg.php";
    public  static  String AGREGAROFERTA = IP+"insertar_oferta.php";
    public  static  String OBTENERMISSERVICIOS = IP+"obtener_ofertas_por_correo.php";
    public  static  String OBTENERALLSERVICIOS = IP+"obtener_ofertas.php";
    public  static  String OBTENERSERVICIOSID = IP+"obtener_oferta_por_id.php";
    public static String  CALIFICAROFERTA = IP+"insertar_calificacion_oferta.php";
    public static  String BUSCAR = IP+"obtener_busqueda_servicio.php";


}
