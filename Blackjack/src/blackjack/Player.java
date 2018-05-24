package blackjack;

import java.util.List;

/**
 * Class: Dealer.java
 * Description: Interface class for a player object.
 * Author: Paulo Jorge.
 */
public interface Player {

    /**
     * Method to clear the previous hand and get ready for new cards.
     * @return Old hand object cleared.
     **/
    Hand newHand();
    
    /**
     * Method used to place a bet. The bet can not be greater the 
     *  players balance and should be called prior to any cards 
     *  being dealt. 
     * @return Integer value placed.
     */
    int makeBet();
    
    /**
     * Method to get the bet placed.
     * @return Integer value of the bet placed for the current game.
     */    
    int getBet();

    /**
     * Method to get and return the players balance.
     * @return Integer value of the the players current total balance. 
     */
    int getBalance();
    
    /**
     * Method to get and return string type of player.
     * @return String type of player.
     */
    String getPlayerType();
    
    /**
     * Method used to determine whether the player wants to take a card
     *  or not. 
     * @return Boolean true if a card is required, false otherwise. 
     */
    boolean hit();
    
    /**
     * If a card is requested by hit() it should be added to the 
     *  players hand with this method.
     * @param card Card object to take.
     */    
    void takeCard(Card card);

    /**
    * The value passed is positive if the player won, negative 
    *   otherwise. 
    * @param bet Integer value of the bet placed.
    * @return Boolean true if the player has funds remaining, 
    *   false otherwise. 
    */
    boolean settleBet(int bet);
    
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
    int getHandTotal();
    
    /**
     * Method to determine if the current hand is a blackjack.
     * @return Boolean true if the current hand is a blackjack. For
     *  example a ACE + TEN (Or a picture card).
     */    
    boolean blackjack();
    
    /**
     * Method to determine if the current hand is a bust.
     * @return Boolean true if the current hand is bust, false 
     *  otherwise.
     */    
    boolean isBust();
    
    /**
     * Method to get the current players hand.
     * @return Hand object containing the players hand.
     */    
    Hand getHand();
    
    /**
     * Method to allow the dealer to show the player their card. 
     * @param card Card object of the dealers first card.
     */
    void viewDealerCard(Card card);

    /**
     * This method allows the dealer to show all the cards 
     *  that were played after a hand is finished. If the player is 
     *  card counting, they will need this info.
     * @param cards List of type card containing all cards played.
     */    
    void viewCards(List<Card> cards);
    
    /**
     * This method is called by the dealer to tell them a new 
     *  deck has been created.
     */    
    void newDeck();
}