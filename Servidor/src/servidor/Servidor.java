package servidor;

import conexion.Instrucciones;
import conexion.SocketCliente;
import conexion.SocketServidor;
import java.io.Console;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class Servidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SocketServidor servidor = new SocketServidor();

        SocketCliente cliente1 = servidor.recibirCliente();

        String dato = "";
        String instruccion = "";

        String ubicacion;

        Scanner consola = new Scanner(System.in);

        while (!cliente1.esInstruccionSalir()) {
            instruccion = cliente1.getUltimaInstruccion();
            if (Instrucciones.esInstruccionArchivo(instruccion)) {
                ubicacion = cliente1.recibirArchivo();
                //mostramos o abrimos el archivo
                //System.out.println(ubicacion);
                //abrirArchivo(ubicacion);
                mostarMensaje(ubicacion);
            } else if (Instrucciones.esInstruccionChat(instruccion)) {
                dato = cliente1.recibirChat();
                System.out.println("el cliente 1 dice: " + dato);

                System.out.println("Escribe tu mensaje para el cliente:");
                cliente1.enviarChat(consola.nextLine());
            }
        }

        System.out.println("el servidor finalizó!!!");
    }

    private static void abrirArchivo(String ubicacion) {
        try {
            String comando = "cmd /k \"start " + ubicacion + "\"";
            Process p = Runtime.getRuntime().exec(comando);
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void mostarMensaje(String texto) {
        String fechaHora = formatearFechaHoraActual();
        System.out.println(fechaHora + ": " + texto);
    }

    private static String formatearFechaHoraActual() {
        Date fecha = new java.util.Date();
        DateFormat format = new SimpleDateFormat("[dd/MM/yyyy HH:mm:ss]");
        return format.format(fecha);
    }
}
