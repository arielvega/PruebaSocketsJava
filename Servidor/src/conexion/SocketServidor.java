package conexion;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luis Ariel Vega Soliz
 */
public class SocketServidor {
    private ServerSocket socketServer;

    public SocketServidor() {
        try {
            this.socketServer = new ServerSocket(Constantes.PUERTO);
        } catch (IOException ex) {
            Logger.getLogger(SocketServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public SocketCliente recibirCliente(){
        SocketCliente cliente = null;
        try {
            Socket socketJava = this.socketServer.accept();
            cliente = new SocketCliente(socketJava);
        } catch (IOException ex) {
            Logger.getLogger(SocketServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cliente;
    }
}
