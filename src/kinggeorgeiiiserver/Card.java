/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kinggeorgeiiiserver;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author coolsmileman
 */
public class Card implements Serializable{
    private final int value;
    private final String suit;
    private enum cardValues{hearts, clubs, diamonds, spades};
        
    public Card(int value, String suit){
        
        boolean check = true;
        for(cardValues cards: cardValues.values())
            if(cards.name().equals(suit))
                check = false;
        
        if ((value<2 && value>14) || check){
            this.value = 6;
            this.suit = cardValues.hearts.name();
        } else{
            this.value = value;
            this.suit = suit;
        }
    }
    
    public int getValue(){
        return value;
    }
    
    public String getSuit(){
        return suit;
    }
    
    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }
        if (!Card.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Card other = (Card) obj;
        if ((this.suit == null) ? (other.suit != null) : !this.suit.equals(other.suit)) {
            return false;
        }
        if (this.value != other.value) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + this.value;
        hash = 41 * hash + Objects.hashCode(this.suit);
        return hash;
    }
    
    @Override
    public String toString(){
        return("Card:"+value+" "+suit);
    }
    
}
