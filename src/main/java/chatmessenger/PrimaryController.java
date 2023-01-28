package chatmessenger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/* 
 * This part of the code allows the backend functionality to talk to the GUI for the SERVER
 */
public class PrimaryController implements Initializable{
//implements interfaces, interface only contains abstract methods
//interface is a blueprint of a class

    @FXML
    private Button button_send;

    @FXML
    private TextField tf_message;

    @FXML
    private VBox vbox_messages;
    //vertical box

    @FXML
    private ScrollPane sp_main;

    //create a server reference variable
    private Server server;
    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        //Called to initialize a controller after its root element has been completely processed
        try{
            //create a server object, server constructor takes a serversocket object
            //serversocket listens for incoming connections, and when recieved one, creates a server object 
            //used to communicate with whoever connected
            server = new Server(new ServerSocket(1234));
        }
        catch(IOException e) {
            //IOExceptions are Input/Output exceptions (I/O),occur whenever an input or output operation is failed or interpreted
            //https://www.geeksforgeeks.org/throwable-printstacktrace-method-in-java-with-examples/
            e.printStackTrace();
            System.out.println("Error creating server");
        }
       
        //add a listener to our vertical box using height property
        vbox_messages.heightProperty().addListener(new ChangeListener<Number>() {

            @Override
            //scrollpane, scrolls to the bottom of the pane when messages are sent
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //when the height of the vbox changes, set vertical value to the new height of the vbox
                //vbox grows taller everytime a new message is sent 
                sp_main.setVvalue((Double) newValue);
            }
            
        });

        //add message to vertical box, run on separate thread as waiting for messages is a blocking operation
        server.receiveMessageFromClient(vbox_messages);
        button_send.setOnAction(new EventHandler<ActionEvent>() {
            //when button clicked send message to client
            @Override
            public void handle(ActionEvent event) {
                String messageToSend = tf_message.getText();
                if(!messageToSend.isEmpty()){
                    //horizontal box, send message to GUI
                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_RIGHT);
                    hBox.setPadding(new Insets(5, 5, 5, 10));
                     //text class defines a node that displays a text.
                    Text text = new Text(messageToSend);
                     //text flow wraps the text, so if message too long, flows onto next line instead of "...cont"
                    TextFlow textFlow = new TextFlow(text);

                    textFlow.setStyle("-fx-color: rgb(239, 242, 255); " +
                    "-fx-background-color: rgb(170, 103, 117);" +
                    "-fx-background-radius: 20px;" );

                    textFlow.setPadding(new Insets(5, 10, 5, 10));
                    text.setFill(Color.color(0.934,0.945,0.966));

                    hBox.getChildren().add(textFlow);
                    vbox_messages.getChildren().add(hBox);

                    //send message to server 
                    server.sendMessageToClient(messageToSend);
                    tf_message.clear();

                }
                
            }
            
        });
    }
        //RECIEVED messages adding to our GUI
        public static void addLabel(String messageFromClient, VBox vbox){
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            //Insets class stores the inside offsets for the four sides of the rectangular area
            hBox.setPadding(new Insets(5,5,5,10));

            Text text = new Text(messageFromClient);
            TextFlow textFlow = new TextFlow(text);
            textFlow.setStyle("-fx-color: rgb(239, 242, 255); " +
                    "-fx-background-color: rgb(111, 107, 209);" +
                    "-fx-background-radius: 20px;" );
            textFlow.setPadding(new Insets(5, 10, 5, 10));
            hBox.getChildren().add(textFlow);
            //we CAN'T update vbox, as we cant update GUI from a thread other than the application thread
            //so we use javafx Platform class to run method later from server class to add HBox to VBox
            //platform class = utility class - only contains static methods
            Platform.runLater(new Runnable(){
                //runnables are object to be run by a thread or an object of thread class
                @Override
                public void run(){
                    vbox.getChildren().add(hBox);
                }
            }
            );

        }
   
    // @FXML
    // private void switchToSecondary() throws IOException {
    //     App.setRoot("secondary");
    // }
}
