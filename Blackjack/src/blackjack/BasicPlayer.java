package blackjack;

import java.io.Serializable;
import java.util.List;

/**
 * Class: BasicPlayer.java
 * Description: Basic player method used to define the simplest form
 *  of a Blackjack player.
 * Author: Paulo Jorge.
 */
public class BasicPlayer implements Serializable, Player {
    // Players hand
    protected Hand hand;
    // Players balance
    protected int balance = 200;
    // Bet placed
    protected int betPlaced;
    // Player type
    protected String playerType;
    
    /**
     * Constructor method used to initialise a new basic player object.
     */
    public BasicPlayer () {
        // Initialise new hand
        newHand();
        
        // Player type
        this.playerType = "Basic"; 
    }
    
    
     /**
     * Method to clear the previous hand and get ready for new cards.
     * @return Old hand object cleared.
     **/    
    @Override
    public final Hand newHand() {
        // Reset bet placed
        this.betPlaced = 0;        
        
        // Store previous hand to return and initialise new hand
        Hand temp = this.hand;
        this.hand = new Hand();
        return temp;
    }

    /**
     * Method used to place a bet. The bet can not be greater the 
     *  players balance and should be called prior to any cards 
     *  being dealt. 
     * @return Integer value placed.
     */    
    @Override
    public int makeBet() {
        return (this.betPlaced = 10);
    }

    /**
     * Method to get the bet placed.
     * @return Integer value of the bet placed for the current game.
     */      
    @Override
    public int getBet() {
        return this.betPlaced;
    }

    /**
     * Method to get and return the players balance.
     * @return Integer value of the the players current total balance. 
     */    
    @Override
    public int getBalance() {
        return this.balance;
    }

    /**
     * Method used to determine whether the player wants to take a card
     *  or not. 
     * @return Boolean true if a card is required, false otherwise. 
     */    
    @Override
    public boolean hit() {
        // Request cards until hands value is 17 or greater
        return !this.hand.isOver(16);
    }

     /**
     * If a card is requested by hit() it should be added to the 
     *  players hand with this method.
     * @param card Card object to take.
     */       
    @Override
    public void takeCard(Card card) {
        this.hand.add(card);
    }

    /**
    * The value passed is positive if the player won, negative 
    *   otherwise. 
    * @param bet Integer value of the bet placed.
    * @return Boolean true if the player has funds remaining, 
    *   false otherwise. 
    */    
    @Override
    public boolean settleBet(int bet) {
        // Add/subtract bet from total balance
        this.balance += bet;
        // Check and return balance state
        return (this.balance >= 0);
    }

     /**
     * Method to get the total score of the current hand.
     * @return Integer total score of the current hand. If the payer has 
     *  one or more aces, this returns the highest hard total that is 
     *  less than 21. For example:
     *   - ACE, THREE           returns 14.
     *   - ACE, THREE, TEN      returns 14.
     *   - ACE, ACE, TWO, THREE returns 17.
     *   - ACE, ACE, TEN        returns 12.
     *  Whereas: 
     *   - TWO, QUEEN, KING     returns 22.
     */   
    @Override
    public int getHandTotal() {
        return this.hand.getHighestBelowOrEqual(21);
    }
    
    /**
     * Method to get and return string type of player.
     * @return String type of player.
     */
    @Override
    public String getPlayerType() {
        return this.playerType;
    }
    
    /**
     * Method to determine if the current hand is a blackjack.
     * @return Boolean true if the current hand is a blackjack. For
     *  example a ACE + TEN (Or a picture card).
     */ 
    @Override
    public boolean blackjack() {                
        return this.hand.isBlackjack();
    }

    /**
     * Method to determine if the current hand is a bust.
     * @return Boolean true if the current hand is greater than 21, 
     *  false otherwise.
     */       
    @Override
    public boolean isBust() {
        return (this.hand.isOver(21));
    }

    /**
     * Method to get the current players hand.
     * @return Hand object containing the players hand.
     */        
    @Override
    public Hand getHand() {
        return this.hand;
    }

    /**
     * Method to allow the dealer to show the player their card. 
     * @param card Card object of the dealers first card.
     */    
    @Override
    public void viewDealerCard(Card card) {}

    /**
     * This method allows the dealer to show all the cards 
     *  that were played after a hand is finished. If the player is 
     *  card counting, they will need this info.
     * @param cards List of type card containing all cards played.
     */
    @Override
    public void viewCards(List<Card> cards) {}
    
    /**
     * This method is called by the dealer to tell them a new 
     *  deck has been created.
     */    
    @Override
    public void newDeck() {}
    
    /**
     * Method to get and return player string containing player
     *  information.
     * @return Player information string.
     */
    @Override
    public String toString() { 
        StringBuilder str = new StringBuilder();
                
        str.append(String.format("\n%17s","Player Type: "));
        str.append(this.playerType).append(".\n");
        str.append(this.hand.toString());        
        str.append(String.format("%17s","Value of Hand: "));
        str.append(getHandTotal());
        str.append(String.format(".\n%18s","Balance: £"));
        str.append(getBalance()).append(".\n");
            
        return str.toString();
    }
}
