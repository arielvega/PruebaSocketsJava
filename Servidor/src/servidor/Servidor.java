package servidor;

import conexion.Instrucciones;
import conexion.SocketCliente;
import conexion.SocketServidor;
import java.io.Console;
import java.util.Scanner;

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
        
        while(!cliente1.esInstruccionSalir()) {
            instruccion = cliente1.getInstruccion();
            if (Instrucciones.esInstruccionArchivo(instruccion)){
                cliente1.recibir();
                ubicacion = cliente1.recibirArchivo();
                //mostramos o abrimos el archivo
                System.out.println(ubicacion);
            } else if (Instrucciones.esInstruccionChat(instruccion)) {
                dato = cliente1.recibirChat();
                System.out.println("el cliente 1 dice: " + dato);
                
                System.out.println("Escribe tu mensaje para el cliente:");
                cliente1.enviarChat(consola.nextLine());
            }
        }
        
        System.out.println("el servidor finalizó!!!");
    }
    
}
