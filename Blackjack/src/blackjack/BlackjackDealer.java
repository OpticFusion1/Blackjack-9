package blackjack;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class: BlackjackDealer.java
 * Description: Dealer class used to serve and handle cards during
 *  a game of Blackjack.
 * Author: Paulo Jorge.
 */
public class BlackjackDealer implements Serializable, Dealer {
    // Store deck
    private final Deck deck = new Deck();
    // List of active players
    private List<Player> players;
    // Dealers hand
    private Hand hand = new Hand();
    // Each players bet
    private final List<Integer> betsPlaced = new ArrayList();
    // Total amount of cards when deck is full
    private final int TOTAL_CARDS;
    // Bet size limits
    private final int MINIMUM_BET_SIZE;
    private final int MAXIMUM_BET_SIZE;
    // Round number
    private int round = 1;
    // Used to determine if average will be recorded
    private final boolean recordAverage;
    private int sum = 0;
    private int average = 0;
    
    
    /**
     * Constructor method used to initialise a new blackjack dealer
     *  object.
     * @param minimumBetSize Integer minimum bet size value.
     * @param maximumBetSize Integer maximum bet size value.
     * @param recordAverage Boolean true if dealer will save average
     *  profit/loss per deck or false otherwise. True will also remove
     *  all console output apart from current deck average message.
     */
    public BlackjackDealer(int minimumBetSize, int maximumBetSize, 
            boolean recordAverage) {
        // Set bet size upper and lower limits
        this.MINIMUM_BET_SIZE = minimumBetSize;
        this.MAXIMUM_BET_SIZE = maximumBetSize;
        // Shuffle deck
        this.deck.shuffle();
        // Initialise variables
        this.TOTAL_CARDS = this.deck.totalSize();
        this.recordAverage = recordAverage;
    }
    
    /**
     * Method to assign players to this instance of the game.
     * @param players List object of type Player containing players to 
     *  be assigned to the game.
     */
    @Override
    public void assignPlayers(List<Player> players) {
        this.players = players;
    }

    /**
     * Method used to take the bets of all the assigned players.
     */
    @Override
    public void takeBets() {
        // Print round number
        print("Round " + this.round + ":\n");

        // Clear previously recorded bets
        this.betsPlaced.clear();
        for (Iterator<Player> i = this.players.iterator(); 
                i.hasNext();) {
            // Get player        
            Player player = i.next();
            
            // Take bets if player is not out of balance
            if (!outOfBalance(player)) {
                // Prompt player to place a bet
                print("Player " + indexOfPlayer(player) 
                        + ": \nYou currently have £" 
                        + player.getBalance() 
                        + " balance. What bet would you like to "
                        + "place?");
                int bet = player.makeBet();                
                
                // If bet exeeds lower/upper bounds, error correct bet
                while ((bet < this.MINIMUM_BET_SIZE) 
                        || (bet > this.MAXIMUM_BET_SIZE)) {

                    String type = (bet < this.MINIMUM_BET_SIZE 
                            ? ("minimum (£" 
                            + this.MINIMUM_BET_SIZE + ")") 
                            : ("maximum (£" 
                            + this.MAXIMUM_BET_SIZE + ")"));
                    // Prompt player
                    print("Error: Bet placed exeeds " 
                            + type + " bet size limits. Please bet a "
                            + "different amount.");
                    bet = player.makeBet();
                }
                
                print(bet + ".\n");
                
                // Store bet placed
                this.betsPlaced.add(bet);
            }
            // Else, remove player if out of balance
            else {
                print("Player " + indexOfPlayer(player) + " cannot "
                        + "play round! The player is out of funds.\n");
                i.remove();
            }
        }
    }

    /**
     * Method to check and initialise a new deck if the current deck is
     *  less than 1/4 of the total size of deck.
     */
    private void newDeck() {
        if (this.deck.size() < (this.TOTAL_CARDS / 4)) {
            // Generate new shuffled deck
            this.deck.newDeck();
            this.deck.shuffle();
            
            // Save averages to file
            if (this.recordAverage) {
                String fileName = "average.txt";
                
                // Default average to total if first average
                if (this.average == 0)
                    this.average = this.sum;
                
                // Calculate average
                this.average = (this.average + this.sum) / 2;
                // Reset sum
                sum = 0;
                System.out.println("Round " + this.round + ": £" 
                        + this.average + ".");

                // Write new average to file
                try {
                    writeToFile(fileName, this.average);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
            
            // Tell each plyer than a new deck is in play
            for (Player player : this.players)
                player.newDeck();
        }
    }

    /**
     * Method to write a integer to a file
     * @param fileName File to write value to.
     * @param value Integer value to write to file with.
     * @throws IOException
     */
    private void writeToFile(String fileName, int value) 
            throws IOException {
        try(PrintWriter file = new PrintWriter(fileName)) {
            file.print(value);
        }
    }
    
    /**
     * Method to deal the first two cards to each player, and one
     *  card to the dealer.
     */
    @Override
    public void dealFirstCards() {
        // Check if a new deck is needed
        newDeck();
        
        // Deal single card to dealer
        Card card = this.deck.deal();
        this.hand.add(card);
        
        for (Iterator<Player> i = this.players.iterator(); 
                i.hasNext();) {
            Player player = i.next();
            
            // Show player dealers first card
            player.viewDealerCard(card);
            
            // Deal 2 cards to each player
            for (int x = 0; x < 2; x++)
                player.takeCard(this.deck.deal());
        }
    }

    /**
     * Method to play the hand of a player. Keep asking if the player 
     *  wants a card until they stick or they are bust.
     * @param player Player object to play the hand with.
     * @return Integer value of the players final score of the hand.
     **/    
    @Override
    public int play(Player player) {
        // Check if a new deck is needed
        newDeck();            

        // Position of player
        print("Player " + indexOfPlayer(player) + ": ");

        // Loop until player sticks
        boolean playing = true;
        // Loop unless player has 21 or more in hand value
        while(playing && !player.getHand().isOver(20)) {
            print("Do you want to hit or stick?");
            playing = player.hit();
            
            print((playing ? "Player Hits" : "Player Sticks") + "!");

            // If player hit deal card
            if (playing)
               player.takeCard(this.deck.deal());
        }
        
        // Print new line for aesthetic purposes
        print("");
        
        return scoreHand(player.getHand());
    }
    
    /** 
     * Method to play the dealers hand. The dealer take cards 
     *  until their total is 17 or higher. 
     * @return Integer value of the dealers score.
     */       
    @Override
    public int playDealer() {
        // Check if a new deck is needed
        newDeck();
        
        print("Dealer:");
        // Deal cards to dealer until at 17 or higher
        while (!this.hand.isOver(16)) {
            this.hand.add(this.deck.deal());
            
            print("Dealer Hits!");
        }
        
        print("Dealer Sticks!\n");
        return scoreHand(this.hand);
    }

    /**
     * Method to score hand of a player. Without relying on the player
     *  scoring on their own hand. 
     * @param hand Players hand to score.
     * @return Integer score. Although if there are aces, returned is
     *  the highest possible value that is less than or equal to 21. 
     *  For example:
     *   - ACE, THREE           returns 14.
     *   - ACE, THREE, TEN      returns 14.
     *   - ACE, ACE, TWO, THREE returns 17.
     *   - ACE, ACE, TEN        returns 12.
     *  Whereas: 
     *   - TWO, QUEEN, KING     returns 22.
     */    
    @Override
    public int scoreHand(Hand hand) {
        return hand.getHighestBelowOrEqual(21);
    }

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
    @Override
    public void settleBets() {
        // Print dealer information
        print("Dealer: \n" + toString());

        // Dealer hand info
        boolean dealerOver21 = this.hand.isOver(21);
        int dealerScore = scoreHand(this.hand);
        boolean dealerBlackjack = this.hand.isBlackjack();            
        
        // Track cards played
        List<Card> cardsPlayed = new ArrayList();
        cardsPlayed = getCardsPlayed(cardsPlayed, this.hand);

        // Reset dealers hand
        this.hand = new Hand(); 
        
        // Number of players that have lost this round
        int lost = 0;
        
        for (Iterator<Player> i = this.players.iterator(); 
                i.hasNext();) {
            // Get player
            Player player = i.next();
            String playerInfo = player.toString();
            Hand playerHand = player.newHand();         
            
            int bet = this.betsPlaced.get(this.players.indexOf(
                    player));
            // Store bet to settle
            int betToSettle = bet;
                        
            // Player Blackjack state
            boolean playerBlackjack = playerHand.isBlackjack();
                    
            // Determine bet amount to settle
            if (playerHand.isOver(21))
                betToSettle = -betToSettle;
            else if (dealerOver21 
                    || (playerBlackjack && !dealerBlackjack))
                betToSettle = (betToSettle * 2);
            else if ((dealerBlackjack && !playerBlackjack)
                    || (scoreHand(playerHand) < dealerScore))
                betToSettle = -betToSettle;
            
            // Settle bet
            this.sum += betToSettle;
            player.settleBet(betToSettle);
            
            // Player information
            StringBuilder str = new StringBuilder();
            str.append("Player ").append((indexOfPlayer(player) 
                    + lost));
            str.append(":").append(playerInfo);
            // Round outcome
            String outcome = (betToSettle < 0 ? "Lost" 
                    : (betToSettle == bet ? "Retained" : "Won"));
            str.append(String.format("\n%15s", "Bet " + outcome));
            str.append(": £").append(betToSettle);
            str.append(String.format(".\n%18s", "New Balance: £"));
            str.append(player.getBalance()).append(".\n");
            
            // Print player end of round information
            print(str.toString());

            // Add players cards played to list
            cardsPlayed = getCardsPlayed(cardsPlayed, playerHand);
            
            // Check if player is out of balance
            if (outOfBalance(player)) {
                print("Player " + (indexOfPlayer(player) + lost)
                        + " has lost! The player is out of funds.\n");
                i.remove();
                lost++;
            }
        }

        // Allow each player to view every card played
        for (Iterator<Player> i = this.players.iterator(); 
                i.hasNext();)
            i.next().viewCards(cardsPlayed);

        // Increment round count
        this.round++;
    }
    
    /**
     * Method to get and store cards played.
     * @param cardsPlayed List of type card containing cards list to 
     *  add to.
     * @param hand Object containing cards to add to list.
     * @return List object of type card containing parameter cards
     *  and new cards from hand object.
     */
    private List<Card> getCardsPlayed(List<Card> cardsPlayed, 
            Hand hand) {
        for (Iterator<Card> i = hand.iterator(); 
            i.hasNext();)
            cardsPlayed.add(i.next());
        return cardsPlayed;
    }
    
    /**
     * Method to check and remove a player if they have run out of 
     *  funds.
     * @param position Integer position of player object.
     * @return New integer index position after removing player.
     */
    private boolean outOfBalance(Player player) {
        return ((player.getBalance() - this.MINIMUM_BET_SIZE) < 0);
    }
    
    /**
     * Method to get and return the index of a players position
     *  defaulting to 1 instead of 0.
     * @param player Object of player to check.
     * @return Integer index position of player plus 1.
     */
    private int indexOfPlayer(Player player) {
        return (this.players.indexOf(player) + 1);
    }
    
    /**
     * Method used to print parameter string to console if print 
     *  to console state is true, otherwise false.
     * @param message String containing message to print.
     */
    private void print(String message) {
        if (!this.recordAverage)
            System.out.println(message);
    }
    
    /**
     * Method to get and return dealer string containing dealer
     *  information.
     * @return Dealer type string.
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        
        str.append(this.hand.toString());
        str.append(String.format("%17s","Value of Hand: "));
        str.append(scoreHand(this.hand)).append(".\n");
        
        return str.toString();
    }
}