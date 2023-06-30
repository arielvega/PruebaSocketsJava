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
    private String ultimaInstruccion;

    public SocketCliente() {
        try {
            this.socket = new Socket(Constantes.HOST, Constantes.PUERTO);
        } catch (IOException ex) {
            Logger.getLogger(SocketCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.ultimaInstruccion = "";
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
    
    // PROTOCOLO
    public String getUltimaInstruccion() {
        return ultimaInstruccion;
    }
    
    public String getInstruccion(){
        return this.leer();
    }
    
    public boolean esInstruccionArchivo(){
        String instruccion = this.getInstruccion();
        if (Instrucciones.esInstruccionArchivo(instruccion)){
            this.ultimaInstruccion = instruccion;
            return true;
        }
        return false;
    }
    
    public boolean esInstruccionChat(){
        String instruccion = this.getInstruccion();
        if (Instrucciones.esInstruccionChat(instruccion)){
            this.ultimaInstruccion = instruccion;
            return true;
        }
        return false;
    }
    
    public boolean esInstruccionSalir(){
        String instruccion = this.getInstruccion();
        if (Instrucciones.esInstruccionSalir(instruccion)){
            this.ultimaInstruccion = instruccion;
            return true;
        }
        return false;
    }
    
    public void recibir(){
        this.escribir(Instrucciones.RECIBIR);
    }
    
    public boolean esRecibir(){
        String instruccion = this.getInstruccion();
        if (Instrucciones.esInstruccionRecibir(instruccion)){
            this.ultimaInstruccion = instruccion;
            return true;
        }
        return false;
    }
    
    //METODOS PARA ENVIAR Y RECIBIR MENSAJES DE CHAT
    public void enviarChat(String mensaje){
        this.escribir(Instrucciones.ENVIAR_CHAT);
        if (this.esRecibir()){
            this.escribir(mensaje);
        }
    }
    
    public String recibirChat(){
        this.recibir();
        return this.leer();
    }
    
    public void enviarArchivo(String ubicacion){
        String extension = "";//sale de la ubicacion del archivo
        this.escribir(Instrucciones.getInstruccionArchivo(extension));
        //se hace lo necesario para enviar el archivo
    }
    
    //retorna la direccion donde se guardo el archivo recibido
    //necesitamos saber la extencion del archivo!!!
    public String recibirArchivo(){
        String instruccion = this.getUltimaInstruccion();
        String extension = Instrucciones.getExtensionArchivo(instruccion);
        //se hace lo necesario para recibir el archivo 
        return "";
    }
}
