// package chatmessenger;

// import java.io.BufferedReader;
// import java.io.BufferedWriter;
// import java.io.IOException;
// import java.io.InputStreamReader;
// import java.io.OutputStreamWriter;
// import java.net.ServerSocket;
// import java.net.Socket;

// import javafx.scene.layout.VBox;

// /*
//  * This part of the code is the backend functionality for the client to recieve and send messages 
//  */
// public class Client {

//     private Socket socket;
//     private BufferedReader bufferedReader;
//     private BufferedWriter bufferedWriter;

//     public Client(Socket socket) {
        
//         try {
//             this.socket = socket;
//             this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//             this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//         } catch (IOException e) {
//             System.out.println("Error creating client");
//             e.printStackTrace();
//             closeEverything(socket, bufferedReader, bufferedWriter);
//         }
        
//     }


//     public void receiveMessageFromServer(VBox vbox_messages) {
//          //has to be run on separate thread so it doesnt block outgoing messages
//         //so create new thread and pass in a runnable object
//         new Thread(new Runnable() {
//             @Override
//             public void run() {
//                 while(socket.isConnected()){
//                     String messageFromServer;
//                     try {
//                         messageFromServer = bufferedReader.readLine();
//                         //we pass vbox as argument as vbox isnt a static variable, but add label is static
//                         PrimaryController.addLabel(messageFromServer, vbox_messages);
//                     } catch (IOException e) {
//                         e.printStackTrace();
//                         System.out.println("Error recieving message from the server");
//                         closeEverything(socket, bufferedReader, bufferedWriter);
//                         break;//break from while loop
//                     }
                    
//                 }
                
//             }
            
//         }).start();
//     }

//     public void sendMessageToServer(String messageToSend) {
//         try {
//             bufferedWriter.write(messageToSend);
//             //sending over a new line character is saying that this is the end of message
//             //so client doesnt keep expecting more characters
//             bufferedWriter.newLine();
//             //because messages only get sent when buffer is full, so manually flush buffer
//             bufferedWriter.flush();
//         } catch (IOException e) {
//             e.printStackTrace();
//             System.out.println("Error sending message to server");
//             closeEverything(socket, bufferedReader, bufferedWriter);
//         }

//     }

//     public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
//         //closes all the sockets and streams, even underlying wrapped streams
//         try {
//             //checks if its null to avoid a null pointer exception
//             if(bufferedReader != null){
//                 bufferedReader.close();
//             }
//             if(bufferedWriter != null){
//                 bufferedWriter.close();
//             }
//             if(socket != null){
//                 socket.close();
//             }
//         } catch (IOException e) {
            
//         }
//     }
    
// }
