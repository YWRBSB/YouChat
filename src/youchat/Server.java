/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

// CODE REFERENCE - TUTORIAL: https://www.youtube.com/watch?v=gLfuZrrfKes&ab_channel=WittCode
package youchat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Yuri Ribeiro -  2020347
 */
public class Server {

    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        // accept incoming connection if the Server is running
        try {

            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                   
                ClientHandler clientHandler = new ClientHandler(socket);
                String clientUsername = clientHandler.getClientUsername();
                System.out.println( clientUsername + " has connected!");
               
// Start a new thread to client handler
                Thread thread = new Thread(clientHandler);
                thread.start();

            }
        } catch (IOException e) {

        }
    }

    // close the server 
    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        // instantiation of the ServerSocket ( Server )
        
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
