package edu.sjtu.cse.codestorm.test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {
    private ServerSocket server;
    private int port = 7777;
    
    private class ConnectionHandler implements Runnable {
        private Socket socket;
        
        public ConnectionHandler(Socket socket) {
            this.socket = socket;
        }
        
        @Override
        public void run() {
            ObjectInputStream ois = null;
            ObjectOutputStream oos = null;
            
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                String msg = (String)ois.readObject();
                
                System.out.println("Message received: " + msg);
                
                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(msg);
                
                System.out.println("Waiting for client msg......");
                
            } catch (IOException exp) {
                exp.printStackTrace();
            } catch (ClassNotFoundException exp) {
                exp.printStackTrace();
            } finally {
                try {
                    if (ois != null) {
                        ois.close();
                    }

                    if (oos != null) {
                        oos.close();
                    }

                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException exp) {}
            }
        }
    }
    
    public TestServer() {
        try {
            server = new ServerSocket(port);
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }
    
    public void handleConnection() {
        System.out.println("Server is initialized......");
        
        while (true) {
            try {
                Socket socket = server.accept();
                new Thread(new ConnectionHandler(socket)).start();
            } catch (IOException exp) {
                exp.printStackTrace();
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        TestServer testServer = new TestServer();
        testServer.handleConnection();
    }
}
