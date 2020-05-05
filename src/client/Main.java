package client;

import java.io.ObjectInputStream;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        try {
            Socket cliente = new Socket( "localhost", 30303 );
            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
            
            new PlayerThread( entrada ).start();

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}