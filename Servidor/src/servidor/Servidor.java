package servidor;

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
        
        Scanner consola = new Scanner(System.in);
        
        while(!dato.equals("SALIR")) {
            dato = cliente1.leer();
            System.out.println("el cliente 1 dice: " + dato);
            
            System.out.println("Escribe tu mensaje para el cliente:");
            cliente1.escribir(consola.nextLine());
        }
        
        System.out.println("el servidor finalizó!!!");
    }
    
}
