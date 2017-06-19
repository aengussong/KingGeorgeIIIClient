/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kinggeorgeiiiclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.lang.ClassNotFoundException;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javax.swing.JOptionPane;
import kinggeorgeiiiserver.Card;
import kinggeorgeiiiserver.Stack;


/**
 *
 * @author coolsmileman
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    HBox hbox = new HBox();
    
    Stack myStack;
    
    Socket socket;
    ObjectOutputStream output; 
    ObjectInputStream input;
    
    Card tableCard;
    
    @FXML
    Button endMove = new Button();
    @FXML
    Button getCard = new Button();
    
    @FXML
    Label label = new Label();
    @FXML
    ImageView img = new ImageView();
    
    private void drawTableCard(){
        System.out.println("drawTableCard");
        switch(tableCard.getValue()){
                case 11:label.setText("J");break;
                case 12:label.setText("Q");break;
                case 13:label.setText("K");break;
                case 14:label.setText("A");break;
                default: label.setText(tableCard.getValue()+"");
        }
        img.setImage(new Image("img/"+tableCard.getSuit()+".png"));
    }
    @FXML
    private void getCard(){
        System.out.println("getCard");
        try {
            output.writeObject(new String("Card"));
            
            Object object = input.readObject();
            if(object.getClass().getName().equals("kinggeorgeiiiserver.Card")){
                Card newCard = (Card)object;
                drawCard(newCard);
            }else{
                output.writeObject(myStack.size());
                lost(input.readObject().toString());
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //отправить карту серверу
    private void sendCard(Card card){
        System.out.println("sendCard");
            tableCard = card;
            drawTableCard();
            endMove.setDisable(false);
    }
    
    private void lost(String str){
        System.out.println("lost");
        tableCard = null;            
        label.setText("");
        img.setImage(null);
        endMove.setDisable(true);   
        getCard.setDisable(true);
        hbox.getChildren().clear();
        JOptionPane.showMessageDialog(null, str);
    }
    
    @FXML
    private void endMove(){
        endMove.setDisable(true);
        System.out.println("endMove");
        Object obj = null;
        try {
            if(myStack.isEmpty())
                output.writeObject("You lost");
            else{
                output.writeObject(tableCard);
                boolean check = true;
                while(check){
                    obj = input.readObject();
                    System.out.println(1);
                    System.out.println(obj.getClass().getName());
                    if(obj.getClass().getName().equals("kinggeorgeiiiserver.Card")){
                        System.out.println(2);
                        tableCard = (Card)obj;
                        drawTableCard();
                    }
                    else check = false;
                }
                System.out.println(3);
                String str = obj.toString();
                System.out.println(str);
                switch(str){
                    case "End move": break;
                    case "You lost": lost("You lost"); break;
                    case "You win": lost("You win"); break;
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean checkCard(Card check,Card card){
        System.out.println("checkCard");
        int checkValue = check.getValue();
        int cardValue = card.getValue();
          
        if(checkValue == 2 && cardValue == 14)
            return true;
        if(checkValue == 14 && cardValue == 2)
            return true;
          
        if(checkValue == cardValue)
            return true;
        if(checkValue+1 == cardValue)
            return true;
        if(checkValue-1 == cardValue)
            return true;
          
        return false;
    }
    
    private void cardLine(){
        
    }

    Pane outPane;
    private void drawCard(Card card){
        System.out.println("drawCard");
        int value = card.getValue();
        String suit = card.getSuit();
        Pane pane = new Pane();
        //System.out.println(suit);
        Image image = new Image("img/"+suit+".png");
        String cardValue = "";
        switch(value){
                case 11:cardValue = "J";break;
                case 12:cardValue = "Q";break;
                case 13:cardValue = "K";break;
                case 14:cardValue = "A";break;
                default: cardValue = value+"";
        }
        Button button = new Button(cardValue, new ImageView(image));
        button.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                pane.setPrefWidth(300);
                outPane.setPrefWidth(outPane.getMinWidth());
            }
        });

        button.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                outPane = pane;
            }
        });
        
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if(tableCard == null){
                    sendCard(card);
                    pane.getChildren().remove(button);
                    hbox.getChildren().remove(pane);
                    endMove.setDisable(false);
                    for(Card cards: myStack)
                        if(cards.equals(card)){
                            myStack.remove(card);
                            break;
                        }
                } else{
                    if(checkCard(card, tableCard)){
                        endMove.setDisable(false);
                        pane.getChildren().remove(button);
                        hbox.getChildren().remove(pane);
                        for(Card cards: myStack)
                            if(cards.equals(card)){
                                myStack.remove(card);
                                break;
                            }
                        sendCard(card);
                    }else
                        JOptionPane.showMessageDialog(null, "This move is not allowed");
                }
            }
        });
                
        pane.getChildren().add(button);
        hbox.getChildren().addAll(pane);
    }
    @FXML
    private void newGame(){
        System.out.println("newGame");
        try {
            tableCard = null;
            label.setText("");
            img.setImage(null);
            endMove.setDisable(true);
            getCard.setDisable(false);
            output.writeObject("New game");
            myStack = (Stack)input.readObject();
            hbox.getChildren().clear();
            
            for(Card card: myStack)
                drawCard(card);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("initialize");
        try {
            socket = new Socket("127.0.0.1", 8080);
            
            output = new ObjectOutputStream(socket.getOutputStream()); 
            input = new ObjectInputStream(socket.getInputStream());
            endMove.setDisable(true);
            tableCard = null;
            //потом надо будет зациклить для постоянного выполнения одних и тех же паттернов игры
            //добавить кнопку закончить ход на форму
            //sendCard(myStack.getCard());
            System.out.println("hello");
            newGame();
            
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
