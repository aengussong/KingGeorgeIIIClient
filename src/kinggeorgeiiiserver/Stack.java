/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kinggeorgeiiiserver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;


/**
 *
 * @author coolsmileman
 */
public class Stack implements Serializable, Iterable<Card>{
    private static final long serialVersionUID = 4815162342L;
    
    private ArrayList<Card> stack = new ArrayList<>();
    
    public boolean isEmpty(){
        return stack.isEmpty();
    }
    
    public int size(){
        return stack.size();
    }
    
    public void addCard(Card card){
        boolean check = true;
        if(!stack.isEmpty())
            for(Card stackCards: stack)
                if(stackCards.equals(card))
                    check = false;
        if(check)    
            stack.add(card);
    }
    
    public Card getCard(){
        if(stack.isEmpty())
            return null;
        else{
            Card card = stack.get(0);
            stack.remove(0);
            return card;
        }
    }
    
    public void remove(Card card){
        for(Card cards: stack){
            if(cards.equals(card)){
                stack.remove(cards);
                break;
            }
        }
    }
    /*
    public Card showCard(){
        return stack.get(0);
    }
    */
    @Override
    public String toString(){
        String stringStack = "";
        for(Card cards:stack)
            stringStack += cards+"\n";
        return stringStack;
    }

    @Override
    public Iterator<Card> iterator() {
        Iterator<Card> iter = stack.iterator();
        return iter;
    }
}
