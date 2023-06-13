/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pruebasockets;

import conexion.SocketServidor;
import conexion.SocketCliente;

/**
 *
 * @author vluis
 */
public class PruebaSockets {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SocketServidor server = new SocketServidor();
        //server.escuchar();
        
        SocketCliente cliente1 = server.recibirCliente();
        cliente1.escribir("hola cliente1 desde Java");
    }
    
}
