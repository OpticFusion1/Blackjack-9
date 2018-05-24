package blackjack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Class: BlackjackTable.java
 * Description: Class containing the methods required to run a 
 *  blackjack game of different type of player types.
 * Author: Paulo Jorge.
 */
public class BlackjackTable implements Serializable {
    // Game objects used to run game
    private final BlackjackDealer dealer;
    private final List<Player> players = new ArrayList();
    // Max number of players
    private final int MAX_PLAYERS = 8;
    // Bet size limits
    private final int MINIMUM_BET_SIZE = 1;
    private final int MAXIMUM_BET_SIZE = 500;

    /**
     * Constructor method used to initialise a new black jack game.
     * @param recordAverage Boolean true if dealer will save average
     *  profit/loss per deck or false otherwise. True will also remove
     *  all console output apart from current deck average message.
     */
    public BlackjackTable(Boolean recordAverage) {        
        // Initialise dealer
        this.dealer = new BlackjackDealer(MINIMUM_BET_SIZE, 
                MAXIMUM_BET_SIZE, recordAverage);
    }
    
    /**
     * Method to run a basic game with 4 basic players.
     * @param scanner Scanner object to read from.
     */
    public static void basicGame(Scanner scanner) {
        // New table
        BlackjackTable table = new BlackjackTable(false);
        
        // Assign players to table
        for (int i = 0; i < 4; i++)
            table.addPlayer(new BasicPlayer());
        
        // Run game
        runGame(table, "Basic", false, scanner);
    }
    
    /**
     * Method to run a intermediate game with 4 intermediate players.
     * @param scanner Scanner object to read from.
     */
    public static void intermediateGame(Scanner scanner) {
        // New table
        BlackjackTable table = new BlackjackTable(false);
        
        // Assign players to table
        for (int i = 0; i < 4; i++)
            table.addPlayer(new IntermediatePlayer());
        
        // Run game
        runGame(table, "Intermediate", false, scanner);
    }
    
    /**
     * Method to run a human game with a basic and human player.
     * @param scanner Scanner object to read from.
     */
    public static void humanGame(Scanner scanner) {
        // New table
        BlackjackTable table = new BlackjackTable(false);
        
        // Assign players to table
        table.addPlayer(new BasicPlayer());
        table.addPlayer(new HumanPlayer(scanner));
       
        // Run game
        runGame(table, "Human", false, scanner);
    }
        
    /**
     * Method to run a advanced game with a basic and human player.
     * @param scanner Scanner object to read from.
     */
    public static void advancedGame(Scanner scanner) {
        // New table
        BlackjackTable table = new BlackjackTable(true);
        
        // Assign players to table
        table.addPlayer(new BasicPlayer());
        table.addPlayer(new IntermediatePlayer());
        table.addPlayer(new AdvancedPlayer());
       
        // Run game
        runGame(table, "Advanced", true, scanner);
    }
    
    /**
     * Method to run a game of blackjack.
     * @param table BlackjackTable object to run.
     * @param type String type of game to print.
     * @param recordAverage Boolean true if dealer will save average
     *  profit/loss per deck or false otherwise. True will also remove
     *  all console output apart from current deck average message.
     * @param scanner Scanner object to read from. 
     */
    private static void runGame(BlackjackTable table,
            String type, Boolean recordAverage, Scanner scanner) {
        // Assign players
        table.assignPlayers();

        // Check if table contains atleast 1 human player
        boolean hasHumanPlayer = (numberOfHumanPlayers
                (table.getPlayers()) != 0);
        
        // Run game if table contains atleast 1 player
        boolean empty = table.players.isEmpty();
        if (!empty)
            System.out.println("Welcome to a new game of " + type 
                    + " Blackjack");
        else
            System.out.println("Table is empty. Game cannot start.\n");
        
        // Initialise new scanner
        boolean over = false;
        // If table is not empty loop until over
        while (!empty && !over) {
            // Prompt user to select an option
            System.out.println("\nPlease select one of the "
                    + "following options: "
                    + "\n1. Continue Game. "
                    + "\n2. Save Game "
                    + "\n3. Load Game. "
                    + "\n4. End Game.");
            int option = getNextInt(scanner);

            // Error check option entered until valid
            while (option < 1 || option > 4) {
                System.out.println("Error: Option selected was "
                        + "invalid. Please enter a valid option "
                        + "from the list (Such as; '1' to "
                        + "continue): ");
                option = getNextInt(scanner);
            }
            // Print new line for aesthetic purposes
            System.out.println("");
                    
            int iterations = 0;
            switch (option) {
                case 1:
                    // Get number of hands to simulate
                    System.out.println("How many hands would you "
                            + "like to simulate? Please enter a "
                            + "number greater than 0.");
                    iterations = getNextInt(scanner);

                    // Error check iterations entered until valid
                    while (iterations < 1) {
                        System.out.println("Error: Iterations "
                                + "entered were invalid. Please "
                                + "enter a number greater than 0.");
                        iterations = getNextInt(scanner);
                    }
                    // Print new line for aesthetic purposes
                    System.out.println("");
                    
                    if (recordAverage)
                        System.out.println("Average Profit/Loss Per "
                                + "Deck:");
                    break;
                case 2:
                    // Save game
                    table.save();
                    break;
                case 3:
                    // Load game
                    table = BlackjackTable.load();
                    break;
                default:
                    // End game
                    System.out.println("Thank you for playing "
                            + "Blackjack!\n");
                    over = true;
                    break;
            }

            // Continue game
            while (!empty && iterations > 0) {
                // Print round start seperater
                if (!recordAverage)
                    System.out.println("_____________________"
                            + "_____________________");

                // Allow players to place bets
                table.getDealer().takeBets();

                // Deal first cards
                table.getDealer().dealFirstCards();

                // Play hands
                for (Iterator<Player> i = table.getPlayers()
                        .iterator(); i.hasNext();)
                    table.getDealer().play(i.next());
                table.getDealer().playDealer();

                // Settle bets placed
                table.getDealer().settleBets();

                // Print round end seperater
                if (iterations > 1)
                    if (!recordAverage)
                        System.out.println("_____________________"
                                + "_____________________");
                
                // If table is empty end game
                empty = (table.getPlayers().isEmpty());
                if (empty)
                    System.out.println("\nGame Over! Every player "
                            + "is out of funds. Thank you for "
                            + "playing Blackjack!\n");
                
                // If game had human players, and all have lost end
                if (hasHumanPlayer 
                        && numberOfHumanPlayers(table.getPlayers()) 
                        == 0) {
                    empty = true;
                    System.out.println("\nGame Over! Every human "
                            + "player is out of funds. Thank you "
                            + "for playing Blackjack!\n");
                }

                // Decrement iterations
                iterations--;
            }
        }
    }
    
    /**
     * Method to get and count number of human players active in a 
     *  list of players.
     * @param players List object of type Player to check
     * @return Integer number of active human players.
     */
    private static int numberOfHumanPlayers(List<Player> players) {
        int humanPlayerCount = 0;
        // Count human players
        for (Iterator<Player> i = players.iterator(); i.hasNext();)
            if (i.next().getPlayerType().equals("Human"))
                humanPlayerCount++;
        return humanPlayerCount;
    }
      
    /**
     * Method to assign all players at the table to a game of
     *  blackjack.
     */
    public void assignPlayers() {
        this.dealer.assignPlayers(this.players);
    }
    
    /**
     * Method to assign a player table.
     * @param player Player object to assign to game. 
     * @return Boolean true if player was assigned, false otherwise 
     *  if player would exceed the tables max player count of 8 
     *  or if the player does not have enough funds for at least 1
     *  round.
     */
    public boolean addPlayer(Player player) {
        if((this.players.size() < this.MAX_PLAYERS)
                && (player.getBalance() - MINIMUM_BET_SIZE) > 0)
            return this.players.add(player);
        return false;
    }
    
    /**
     * Method to remove a player from table.
     * @param player Player object to remove from game.
     * @return Boolean true if player was removed, false otherwise.
     */
    public boolean removePlayer(Player player) {
        return this.players.remove(player);
    }
    
    /**
     * Method to get and return this tables dealer.
     * @return Dealer object of table.
     */
    public Dealer getDealer() {
        return this.dealer;
    }
    
    /**
     * Method to get and return all players currently assigned to 
     *  this table.
     * @return List object of type Player containing all the players
     *  currently assigned.
     */
    public List<Player> getPlayers() {
        return players;
    }
    
    /**
     * Method to check and return if the next value that user inputs
     *  is a integer.
     * @param scanner Scanner object to read from.
     * @return Value of next integer, or -1 if input is not an integer.
     */
    public static int getNextInt(Scanner scanner) {
        // Prompt user
        System.out.print("Input: ");

        int value = -1;
        // Error check scanner that the next input is an integer
        if (scanner.hasNextInt())
            // Instead of using scanner.nextInt(), convert string to
            // int. This, therefore supports input even if it 
            // uses spaces, of which get removed
            value = Integer.valueOf(scanner.next());
        else
            // Clear value
            scanner.next();

        return value;
    }
    
    /**
     * Method to serialise blackjack table object to file.
     */
    public void save() {
        Serialization.writeToFile(this, "blackjackTable.ser");
    }
    
    /**
     * Method to load and return blackjack table object from file.
     * @return Hand object loaded.
     */    
    public static BlackjackTable load() {
        return (BlackjackTable)Serialization.readFromFile
                ("blackjackTable.ser");
    }
    
    /**
     * Method to get and return formatted string containing table
     *  information.
     * @return String with table information.
     */
    @Override
    public String toString() { 
        StringBuilder str = new StringBuilder();  
        
        // Check if table contains atleast 1 player, else print empty
        // message to console
        if (this.players.size() > 0) {
            str.append("\nPlayer(s) Assigned to Table: \n");
            for (int i = 0; i < this.players.size(); i++) {
                // Get player
                Player player = this.players.get(i);
                
                // Append player information
                str.append("Player ").append(i + 1).append(": ");
                str.append(player.toString());
            }
        } else
            str.append("No player is currently assigned to table.\n");
        
        return str.toString();
    }
}