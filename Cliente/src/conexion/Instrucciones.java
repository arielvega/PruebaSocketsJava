package conexion;

/**
 *
 * @author Luis Ariel Vega Soliz
 */
public class Instrucciones {

    public static String ENVIAR_ARCHIVO = "!#ARCHIVO#!";
    public static String ENVIAR_CHAT = "!#CHAT#!";
    public static String RECIBIR = "!#RECIBIR#!";
    public static String RECIBIDO = "!#RECIBIDO#!";
    public static String SALIR = "!#SALIR#!";

    public static boolean esInstruccion(String dato) {
        return esInstruccionArchivo(dato) || esInstruccionChat(dato) || esInstruccionRecibir(dato) || esInstruccionRecibido(dato) || esInstruccionSalir(dato);
    }

    public static boolean esInstruccionArchivo(String dato) {
        return dato.startsWith(Instrucciones.ENVIAR_ARCHIVO);
    }
    
    public static String getInstruccionArchivo(String extension){
        return Instrucciones.ENVIAR_ARCHIVO + extension;
    }
    
    public static String getExtensionArchivo(String instruccion){
        if (Instrucciones.esInstruccionArchivo(instruccion)){
            // la cadena es "!#ARCHIVO#!EXTENSION"
            return instruccion.replace(ENVIAR_ARCHIVO, "");
        }
        return "";
    }

    public static boolean esInstruccionChat(String dato) {
        return dato.equals(Instrucciones.ENVIAR_CHAT);
    }

    public static boolean esInstruccionSalir(String dato) {
        return dato.equals(Instrucciones.SALIR);
    }

    public static boolean esInstruccionRecibir(String dato) {
        return dato.equals(Instrucciones.RECIBIR);
    }

    public static boolean esInstruccionRecibido(String dato) {
        return dato.equals(Instrucciones.RECIBIDO);
    }
}
