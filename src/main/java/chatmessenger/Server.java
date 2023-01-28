package chatmessenger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.scene.layout.VBox;
/*
 * This part of the code is the backend functionality for the server to send and receive messages
 */
public class Server{
    //used to listen for incoming connections
    private ServerSocket serverSocket;
    //created by serversocket to communicate with whoever connected 
    //allows communication through streams, input/output streams
    private Socket socket;
    //way to read data from client, wraps input stream from socket, improve efficiency
    private BufferedReader bufferedReader;
    //wrap sockets output stream, send messages to client more efficiently
    private BufferedWriter bufferedWriter;

    //CONSTRUCTOR
    public Server(ServerSocket serverSocket) {
        
        try {
            this.serverSocket = serverSocket;
              //blocks until connection is made, creates new socket
            this.socket = serverSocket.accept();
       
                //2 types of java streams - character, byte; we are using character as I/O is text
                //wrap input stream with character stream, then wrap with buffered reader 
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            
        } catch (IOException e) {
            System.out.println("Error creating server");
             e.printStackTrace();
             closeEverything(socket, bufferedReader, bufferedWriter);
             
        }

    }


    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        //closes all the sockets and streams, even underlying wrapped streams
        try {
            //checks if its null to avoid a null pointer exception
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        } catch (IOException e) {
            
        }
    }

    public void receiveMessageFromClient(VBox vbox_messages) {
        //has to be run on separate thread so it doesnt block outgoing messages
        //so create new thread and pass in a runnable object
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()){
                    String messageFromClient;
                    try {
                        messageFromClient = bufferedReader.readLine();
                        //we pass vbox as argument as vbox isnt a static variable, but add label is static
                        PrimaryController.addLabel(messageFromClient, vbox_messages);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error recieving message from the client");
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;//break from while loop
                    }
                    
                }
                
            }
            
        }).start();
    }
//send message from server to client using buffered writer
    public void sendMessageToClient(String messageToSend) {
        try {
            bufferedWriter.write(messageToSend);
            //sending over a new line character is saying that this is the end of message
            //so client doesnt keep expecting more characters
            bufferedWriter.newLine();
            //because messages only get sent when buffer is full, so manually flush buffer
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error sending message to client");
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
}