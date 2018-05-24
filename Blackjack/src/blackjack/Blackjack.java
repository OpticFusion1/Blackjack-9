package blackjack;

import java.util.Scanner;

/**
 * Class: Blackjack.java
 * Description: Project main used to run blackjack game.
 * Author: Paulo Jorge.
 */
public class Blackjack {
      public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {    
            //testing(scanner);

            BlackjackTable.basicGame(scanner);
            BlackjackTable.intermediateGame(scanner);
            BlackjackTable.humanGame(scanner);
            BlackjackTable.advancedGame(scanner);
        }
    }
      
    /**
     * Method used to test each class in the game.
     */
    private static void testing(Scanner scanner) {
        // ------------------ BasicPlayer.java ------------------
        System.out.println("\n------- BASIC PLAYER TESTING -------\n");
        
        BasicPlayer basicPlayer = new BasicPlayer();
        
        System.out.println("Player Info:" 
                + "\nBalance: " + basicPlayer.getBalance()
                + "\nBet Placed: " + basicPlayer.getBet()
                + "\nWant To Hit? " + basicPlayer.hit());
        
        basicPlayer.makeBet();
        System.out.println("\nBet Placed (After Make Bet): "
                + basicPlayer.getBet());
        
        System.out.println("\nPlayer Hand: \n" 
                + basicPlayer.getHand().toString()
                + "Hand Total: " + basicPlayer.getHandTotal());
        basicPlayer.takeCard(new Card(Card.Rank.ACE, Card.Suit.clubs));
        System.out.println("Player Hand (After Taking Card): \n"
                + basicPlayer.getHand().toString()
                + "Hand Total: " + basicPlayer.getHandTotal());

                basicPlayer.settleBet(10);
        System.out.println("\nBalance (After Settle Bed): \n"
                + basicPlayer.getBalance());
        
        System.out.println("\nHas blackjack? "
                + basicPlayer.blackjack()
                + "\nIs Bust? " + basicPlayer.isBust());
        
        System.out.println("\nBasic Player toString(): "
                + basicPlayer.toString());
        
        // ------------------ HumanPlayer.java ------------------
        System.out.println("------- HUMAN PLAYER TESTING -------\n");

        HumanPlayer humanPlayer = null;

        humanPlayer = new HumanPlayer(scanner);

        System.out.println("Please place a bet: ");
        humanPlayer.makeBet();
        System.out.println("Bet Placed: "
                + humanPlayer.getBet());

        humanPlayer.hit();
        humanPlayer.takeCard(new Card(Card.Rank.EIGHT, 
                Card.Suit.clubs));

        System.out.println("Human Player toString(): "
                + humanPlayer.toString());
                
        // ------------------ BlackjackTable.java ------------------
        System.out.println("------ BLACKJACK TABLE TESTING ------\n");
        
        BlackjackTable table = new BlackjackTable(false);
        
        System.out.println("Table: \n" + table.toString());
        table.addPlayer(basicPlayer);
        table.addPlayer(basicPlayer);
        System.out.println("Table (After Assigning 2 Basic Players): " 
                + table.toString());
        
        table.removePlayer(basicPlayer);
        System.out.println("Table (After Removing a Basic Player): " 
                + table.toString());
                
        table.save();
        BlackjackTable tableLoaded = BlackjackTable.load();
        System.out.print("\nTable (After save and load): " 
                + tableLoaded.toString());
        
        // ------------------ BlackjackDealer.java ------------------
        System.out.println("\n----- BLACKJACK DEALER TESTING -----\n");
        
        table.addPlayer(new IntermediatePlayer());
        table.addPlayer(new AdvancedPlayer());

        BlackjackDealer dealer = new BlackjackDealer(1, 500, false);
        
        dealer.assignPlayers(table.getPlayers());
        
        dealer.takeBets();

        dealer.dealFirstCards();
        
        for (Player player : table.getPlayers())
            dealer.play(player);
        dealer.playDealer();

        dealer.settleBets();

        System.out.println("---------- END OF TESTING ----------\n");
    }
}