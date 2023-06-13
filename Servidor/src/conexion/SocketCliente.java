package conexion;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luis Ariel Vega Soliz
 */
public class SocketCliente {
    private Socket socket;

    public SocketCliente() {
        try {
            this.socket = new Socket(Constantes.HOST, Constantes.PUERTO);
        } catch (IOException ex) {
            Logger.getLogger(SocketCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public SocketCliente(Socket socket) {
        this.socket = socket;
    }
    
    public void escribir(String dato){
        try {
            DataOutputStream servidor = new DataOutputStream(this.socket.getOutputStream());
            servidor.writeUTF(dato);
        } catch (IOException ex) {
            Logger.getLogger(SocketCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String leer(){
        String respuesta = "";
        try {
            DataInputStream servidor = new DataInputStream(this.socket.getInputStream());
            respuesta = servidor.readUTF();
        } catch (IOException ex) {
            Logger.getLogger(SocketCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respuesta;
    } 

    public void desconectar(){
        try {
            this.socket.close();
        } catch (IOException ex) {
            Logger.getLogger(SocketCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
