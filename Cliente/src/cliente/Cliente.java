package cliente;

import conexion.Instrucciones;
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

        String instruccion = "";

        String ubicacion;

        ubicacion = "D:\\nuevo45.txt";
        cliente.enviarArchivo(ubicacion);
        cliente.escribir(Instrucciones.SALIR);
    }

}
