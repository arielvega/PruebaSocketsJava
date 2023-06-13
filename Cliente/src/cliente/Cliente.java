package cliente;

import conexion.SocketCliente;
import java.util.Scanner;

/**
 *
 * @author Usuario
 */
public class Cliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SocketCliente cliente = new SocketCliente();

        String dato = "";
        Scanner consola = new Scanner(System.in);

        while (!dato.equals("SALIR")) {
            System.out.println("Escribe tu mensaje para el servidor:");
            cliente.escribir(consola.nextLine());
            
            dato = cliente.leer();
            System.out.println("el servidor dice: " + dato);
        }
    }

}
