package blackjack;

import java.util.List;

/**
 * Class: AdvancedPlayer.java
 * Description: Advanced player that extends the capabilities 
 *  of a intermediate player, also utilising the ability to card count
 *  for betting purposes.
 * Author: Paulo Jorge.
 */
public class AdvancedPlayer extends IntermediatePlayer {
    // Used to card count
    private int cardCount = 0;
    
    /**
     * Constructor method used to initialise a new intermediate 
     *  player object.
     */
    public AdvancedPlayer () {
        // Initialise new hand
        newHand();
                
        // Player type
        this.playerType = "Advanced"; 
    }
    
    /**
     * Method used to place a bet. The bet can not be greater the 
     *  players balance and should be called prior to any cards 
     *  being dealt. 
     * @return Integer value placed.
     */    
    @Override
    public int makeBet() {
        // Multiply 10 by card count if count is 1 or higher
        this.betPlaced = (this.cardCount <= 0 ? 10 
                : (10 * this.cardCount));
        return this.betPlaced;
    }
    
    /**
     * This method allows the dealer to show all the cards 
     *  that were played after a hand is finished. If the player is 
     *  card counting, they will need this info.
     * @param cards List of type card containing all cards played.
     */
    @Override
    public void viewCards(List<Card> cards) {
        // Iterate every card
        for (Card card : cards) {
            // Card value
            int value = card.getRank().getValue();
            
            if (value <= 6)
                this.cardCount++;
            else if (value >= 10)
                this.cardCount--;
        }
    }
    
    /**
     * This method is called by the dealer to tell them a new 
     *  deck has been created.
     */    
    @Override
    public void newDeck() {
        // Reset card count
        this.cardCount = 0;
    }
}