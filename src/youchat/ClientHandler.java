/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

// CODE REFERENCE - TUTORIAL: https://www.youtube.com/watch?v=gLfuZrrfKes&ab_channel=WittCode
package youchat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Yuri Rieiro - 2020347
 */
public final class ClientHandler implements Runnable {

    // ArrayList for the ClientHandler
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    
    
    // Read Username , add User to the List and Broadcast a message to my Server
    public ClientHandler(Socket socket) {

        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage("SERVER: " + clientUsername + " has entered the chat!");

        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    
     public String getClientUsername() {
        return clientUsername;
    }

    @Override
    public void run() {
        String messageFromClient;

        //  While the server is on Read and Broadcast the message to all users connected.
        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }

    }

    // For every message sent, check the sender and exclude it from receiving its own message
    public void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }
    
      // When the user disconnect then remove it from the client handler list and broacast a message
        public void removedClientHandler(){
            clientHandlers.remove(this);
            broadcastMessage("SERVER: " + clientUsername + " has left the chat!");
        }
        
        // Close everything - Socket, Buffered reader and writer
        public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter ){
            removedClientHandler();
            
            try {
                if (bufferedReader != null){
                    this.bufferedReader.close();
                }
                if (bufferedWriter != null){
                    this.bufferedWriter.close();
                }
                if (socket != null){
                    this.socket.close();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
            
        }
}
