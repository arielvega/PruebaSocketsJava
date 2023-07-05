package conexion;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    private DataOutputStream salida;
    private DataInputStream entrada;

    private String ultimaInstruccion;

    public SocketCliente() {
        try {
            this.socket = new Socket(Constantes.HOST, Constantes.PUERTO);
            this.createStreams();
        } catch (IOException ex) {
            Logger.getLogger(SocketCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.ultimaInstruccion = "";
    }

    public SocketCliente(Socket socket) {
        this.socket = socket;
        this.createStreams();
    }

    private void createStreams() {
        try {
            this.salida = new DataOutputStream(this.socket.getOutputStream());
            this.entrada = new DataInputStream(this.socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(SocketCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void escribir(String dato) {
        try {
            this.salida.writeUTF(dato);
        } catch (IOException ex) {
            Logger.getLogger(SocketCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String leer() {
        String respuesta = "";
        try {
            respuesta = this.entrada.readUTF();
        } catch (IOException ex) {
            Logger.getLogger(SocketCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respuesta;
    }

    public void desconectar() {
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

    public String getInstruccion() {
        String instruccion = this.leer();
        if (Instrucciones.esInstruccion(instruccion)) {
            this.ultimaInstruccion = instruccion;
            return this.ultimaInstruccion;
        }
        return "";
    }

    public boolean esInstruccionArchivo() {
        String instruccion = this.getInstruccion();
        if (Instrucciones.esInstruccionArchivo(instruccion)) {
            return true;
        }
        return false;
    }

    public boolean esInstruccionChat() {
        String instruccion = this.getInstruccion();
        if (Instrucciones.esInstruccionChat(instruccion)) {
            return true;
        }
        return false;
    }

    public boolean esInstruccionSalir() {
        String instruccion = this.getInstruccion();
        if (Instrucciones.esInstruccionSalir(instruccion)) {
            return true;
        }
        return false;
    }

    public void recibir() {
        this.escribir(Instrucciones.RECIBIR);
    }

    public void recibido() {
        this.escribir(Instrucciones.RECIBIDO);
    }

    public boolean esInstruccionRecibir() {
        String instruccion = this.getInstruccion();
        if (Instrucciones.esInstruccionRecibir(instruccion)) {
            return true;
        }
        return false;
    }

    public boolean esInstruccionRecibido() {
        String instruccion = this.getInstruccion();
        if (Instrucciones.esInstruccionRecibido(instruccion)) {
            return true;
        }
        return false;
    }

    //METODOS PARA ENVIAR Y RECIBIR MENSAJES DE CHAT
    public void enviarChat(String mensaje) {
        this.escribir(Instrucciones.ENVIAR_CHAT);
        if (this.esInstruccionRecibir()) {
            this.escribir(mensaje);
        }
    }

    public String recibirChat() {
        this.recibir();
        return this.leer();
    }

    public boolean enviarArchivo(String ubicacion) {
        //la extension del archivo
        //sale de la ubicacion del archivo
        // asumimos que el archivo termina en ".<ALGO>"
        // donde <ALGO> es la extension
        // por ejemplo la ubicacion va a ser C:/DATOS/archivo.txt
        // por tanto el nombre del archivo es desde el ultimo 
        // separador de carpetas "/" hacia adelante
        // entonces el archivo es "archivo.txt"
        // debemos saber cual es el separador de archivos,
        // a veces es '\' (Windows)
        // y otras veces es '/' (Linux, Unix, MacOS, Android, IOS, Web)
        // algunos lenguajes, por ejemplo python es una constante que se 
        // llama os.PathSeparator, en java es File.separator
        String nombreArchivo = ubicacion.substring(ubicacion.lastIndexOf(File.separator) + 1);
        // la extension sale del archivo, va a ser "txt"
        String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf('.') + 1);
        this.escribir(Instrucciones.getInstruccionArchivo(extension));
        if (this.esInstruccionRecibir()) {
            FileInputStream stream = null;
            try {
                //se hace lo necesario para enviar el archivo
                File archivo = new File(ubicacion);
                //enviamos el tamaño del archivo
                this.salida.writeLong(archivo.length());
                //para enviar necesita enviar por "partes"...
                //entonces debe ser un stream de bytes...
                stream = new FileInputStream(archivo);
                //se envia byte por byte, por "partes"
                // cada "parte" va a tener un tamaño maximo de 1Kb
                byte[] parte = new byte[1024];
                int bytesLeidos;
                bytesLeidos = stream.read(parte);
                while (bytesLeidos != -1) {
                    this.salida.write(parte, 0, bytesLeidos);
                    this.salida.flush();
                    bytesLeidos = stream.read(parte);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SocketCliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SocketCliente.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    stream.close();
                } catch (IOException ex) {
                    Logger.getLogger(SocketCliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return this.esInstruccionRecibido();
    }

    //retorna la direccion donde se guardo el archivo recibido
    //necesitamos saber la extencion del archivo!!!
    public String recibirArchivo() {
        this.recibir();
        String ubicacion = null;
        FileOutputStream stream = null;
        try {
            String instruccion = this.getUltimaInstruccion();
            String extension = Instrucciones.getExtensionArchivo(instruccion);
            if (extension.length() > 0) {// si tiene extension
                extension = "." + extension; //.extension
            }
            //se hace lo necesario para recibir el archivo
            //primero crear un archivo local para recibir los datos...
            // en este caso, vamos a trabajar con archivos temporales...
            // los archivos temporales se guardan en carpetas temporales
            // estas carpetas dependen del sistema operativo
            //  -En Windows (c:/windows/temp) o (c:/users/usuario/localdata/temp)
            //  -En Linux, Unix, MacOS (/tmp)
            //  -En Android o IOS tienen su propia ubicacion
            // como pasa para el separador de archivos, tambien exite una constante
            // para saber cual es la carpeta temporal cada lenguaje tiene su constante
            // y en java nos ayuda con una funcion para crear archivos temporales
            // en la carpeta temporal File.createTempFile();
            File archivo = File.createTempFile("temp_", extension);
            //obtenemos el tamaño del archivo
            Long longitudArchivo = this.entrada.readLong();
            // creamos el stream
            stream = new FileOutputStream(archivo);
            //creamos la "parte" que se va a recibir
            byte[] parte = new byte[1024];
            int bytesLeidos;

            while (longitudArchivo > 0) {
                //leemos la "parte"
                bytesLeidos = this.entrada.read(parte, 0, parte.length);
                stream.write(parte, 0, bytesLeidos);
                stream.flush();
                longitudArchivo = longitudArchivo - bytesLeidos;
            }
            ubicacion = archivo.getAbsolutePath();
        } catch (IOException ex) {
            Logger.getLogger(SocketCliente.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stream.close();
            } catch (IOException ex) {
                Logger.getLogger(SocketCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.recibido();
        return ubicacion;
    }
}
