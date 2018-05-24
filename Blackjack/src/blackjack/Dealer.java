package blackjack;

import java.util.*;

/**
 * Class: Dealer.java
 * Description: Interface class for a dealer object.
 * Author: Paulo Jorge.
 */
public interface Dealer {
    /**
     * Method to assign players to this instance of the game.
     * @param players List object of type Player containing players to 
     *  be assigned to the game.
     */
    void assignPlayers(List<Player> players);
    
    /**
     * Method used to take the bets of all the assigned players.
     */
    void takeBets();    

    /**
     * Method to deal the first two cards to each player, and one
     *  card to the dealer.
     */
     void dealFirstCards();

    /**
     * Method to play the hand of a player. Keep asking if the player 
     *  wants a card until they stick or they are bust.
     * @param player Player object to play the hand with.
     * @return Integer value of the players final score of the hand.
     **/
   int play(Player player);

    /** 
     * Method to play the dealer hand. The dealer must take cards 
     *  until their total is 17 or higher. 
     * @return Integer value of the dealers score.
     */   
   public int playDealer();
   
    /**
     * Method to score hand of a player. Without relying on the player
     *  scoring on their own hand. 
     * @param hand Players hand to score.
     * @return Integer score. Although if there are aces, returned is
     *  the highest possible value that is less than 21. For example:
     *   - ACE, THREE           returns 14.
     *   - ACE, THREE, TEN      returns 14.
     *   - ACE, ACE, TWO, THREE returns 17.
     *   - ACE, ACE, TEN        returns 12.
     *  Whereas: 
     *   - TWO, QUEEN, KING     returns 22.
     */    
    public int scoreHand(Hand hand);
    
    /** 
     * Method to settle all the bets at the end of the hand. Rules are
     *  as follows:
     *   1. Player is bust - Player loses bet
     *   2. Dealer is bust - Player wins sum equal of bet
     *   3. Player has blackjack and dealer doesn't 
     *       - Player wins sum equal of bet
     *   4. Dealer has blackjack and player doesn't - Player loses bet
     *   5. Players score less than dealer score - Player loses bet
     *   6. Player score greater than or equal to dealer score 
     *       - Player retains bets
     */    
    public void settleBets();
}