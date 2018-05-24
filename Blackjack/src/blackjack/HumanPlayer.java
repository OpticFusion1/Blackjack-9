package blackjack;

import java.util.Scanner;

/**
 * Class: HumanPlayer.java
 * Description: Human player class used to allow a user to play a
 *  game of Blackjack.
 * Author: Paulo Jorge.
 */
public class HumanPlayer extends BasicPlayer {
    // Scanner object to read from.
    private final Scanner scanner;
    
    /**
     * Constructor method used to initialise a new human player object.
     * @param scanner Scanner object to read from.
     */
    public HumanPlayer (Scanner scanner) {
        // Initialise new hand
        newHand();
        
        // Pass in scaner
        this.scanner = scanner;
        // Player type
        this.playerType = "Human"; 
    }
    
    /**
     * Method used to place a bet. The bet can not be greater the 
     *  players balance and should be called prior to any cards 
     *  being dealt. 
     * @return Integer bet placed.
     */
    @Override
    public int makeBet() {
        return (this.betPlaced 
                = BlackjackTable.getNextInt(this.scanner));
    }

        /**
     * Method used to determine whether the player wants to take a card
     *  or not. 
     * @return Boolean true if a card is required, false otherwise. 
     */    
    @Override
    public boolean hit() {
        String input;
        System.out.println(this.hand.toString() 
                + "Enter 'Y' to hit or 'N' to stick.");
        input = getNext();
        
        // Print new line for aesthetic purposes
        System.out.println("");

        // If player wants to hit return true, otherwise false
        if (input.equals("Y"))
            return true;
        return false;
    }
    
    /**
     * Method to get the next input from scanner.
     * @return String containing uppercase input.
     */
    private String getNext() {
        // Prompt user
        System.out.print("Input: ");

        // Get, convert string to uppercase and return
        String input = this.scanner.next();
        return validNext(input.toUpperCase());
    }
    
    /**
     * Method to make sure the input entered was a valid 'Y' or 'N', 
     *  else error prompt user until correct.
     * @param input String input to check and correct.
     * @return String 'Y' or 'N'.
     */
    private String validNext(String input) {
        // Display error message if input is invalid
        while (!(input.equals("Y") || input.equals("N"))) {
            System.out.println("Error: Option entered is "
                    + "invalid.\n");        
            input = getNext();
        }
        return input;
    }
}