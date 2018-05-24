package blackjack;

import java.util.Iterator;

/**
 * Class: IntermediatePlayer.java
 * Description: Intermediate player that extends the capabilities 
 *  of a basic player, by basing their decision to hit or stick
 *  based on dealers first card.
 * Author: Paulo Jorge.
 */
public class IntermediatePlayer extends BasicPlayer {    
    // Dealers first card
    protected Card dealerCard;

    /**
     * Constructor method used to initialise a new intermediate 
     *  player object.
     */
    public IntermediatePlayer() {
        // Initialise new hand
        newHand();
                
        // Player type
        this.playerType = "Intermediate"; 
    }
    
    /**
     * Method used to determine whether the player wants to take a card
     *  or not. 
     * @return Boolean true if a card is required, false otherwise. 
     */    
    @Override
    public boolean hit() {
        boolean hasAce = false;
        // Check if hand contains an ace
        for (Iterator<Card> i = this.hand.iterator(); i.hasNext();) {
            Card card = i.next();
            if (card.getRank().equals(Card.Rank.ACE))
                hasAce = true;
        }
        
        // If hand has an ace, stick on soft total of 9 or higher
        if (hasAce)
            return !this.hand.isOver(8);
        // If dealers card is 7 or over hit until 17 or higher
        else if (this.dealerCard.getRank().getValue() >= 7)
            return !this.hand.isOver(16);
        // Else hit until 12 or higher
        return !this.hand.isOver(11);
    }
    
    /**
     * Method to allow the dealer to show the player their card. 
     * @param card Card object of the dealers first card.
     */    
    @Override
    public void viewDealerCard(Card card) {
        this.dealerCard = card;
    }
}