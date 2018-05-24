package blackjack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Class: Card.java
 * Description: Card class used to handle a single card type within a 
 * deck.
 * Author: Paulo Jorge .
 */
public class Card implements Serializable, Comparable<Card> {
    // Version control
    private static final long serialVersionUID = 111;

    /**
     * Rank enumerator used to define every type of card from 
     *  TWO to ACE. With further methods used to get and return
     *  the value of the card or get the previous card in the rank.
     */
    public enum Rank { 
        // Type of ranks
        TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), 
            SEVEN(7), EIGHT(8), NINE(9), TEN(10), 
            JACK(10), QUEEN(10), KING(10), ACE(11);

        private final int value;
        /**
         * Method to define a new rank enumerator type.
         * @param value Integer value of card.
         */
        private Rank(int value) { 
            this.value = value; 
        }

        /**
         * Method to get and return the previous rank of a card, such
         *  as ACE if method is called on TWO.
         * @return Previous rank object.
         */
        public Rank getPrevious(){
            return values()
                    [this.ordinal() > 0 ? this.ordinal() - 1 : 12];
        }

        /**
         * Get method to get value of card rank.
         * @return Integer value of card.
         */
        public int getValue() { 
            return this.value; 
        }
    }
    
    /**
     * Suit enumerator used to define all 4 types of card suits 
     *  (clubs, diamonds, hearts and spades).
     */
    public enum Suit { clubs, diamonds, hearts, spades; }

    // Used to store card type information
    private final Rank rank;
    private final Suit suit;

    /**
     * Card constructor used to define a single card within a deck.
     * @param rank Rank enumerator object type of this card.
     * @param suit Suit enumerator object type of this card. 
     */
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }
    
    /**
     * Get method to get and return rank of card.
     * @return Rank object of card.
     */
    public Rank getRank() { return this.rank; }

    /**
     * Get method to get and return suit of card.
     * @return Suit object of card.
     */
    public Suit getSuit() { return this.suit; }    

    /**
     * Static method to get 2 cards and output the sum value of both
     *  combined.
     * @param a First card object.
     * @param b Second card object.
     * @return Integer value of both cards summed.
     */
    public static int sum(Card a, Card b) {
        return (a.getRank().getValue() + b.getRank().getValue());
    }

    /**
     * Method to check if 2 card values summed are a blackjack.
     * @param a First card object.
     * @param b Second card object.
     * @return Boolean true if both cards summed equal 21 or false if
     *          not.
     */
    public static boolean isBlackjack(Card a, Card b) {
        return (sum(a, b) == 21);
    }

    /** 
     * Method to compare this object to a second object and return
     *  the descending order of rank then ascending order of suit.
     * @param b Card object to compare to.
     * @return Integer value of compare result: 
     *         0       = object is equal to b.
     *         -Number = object is greater than b.
     *         +Number = object is less than b.
     */
    @Override
    public int compareTo(Card b) {
        int rankComp = getRank().compareTo(b.getRank());
        return (rankComp == 0 ? getSuit().compareTo(b.getSuit()) 
                : -rankComp);
    }        
    
    /**
     * Comparator class used to apply a different compare to method.
     *  Comparing by ascending rank then suit order instead of
     *  descending rank then ascending suit order.
     */
    public static class CompareAscending implements Comparator<Card> {
        /** 
         * Method to compare a object to a second object and return
         *  the ascending order of rank then suit.
         * @param a First card object to compare
         * @param b Second card object to compare against.
         * @return Integer value of compare result: 
         *         0       = object is equal to b.
         *         -Number = object is less than b.
         *         +Number = object is greater than b.
         */
        @Override
        public int compare(Card a, Card b) {
            int rankComp = a.getRank().compareTo(b.getRank());
            return (rankComp == 0 ? a.getSuit().compareTo(b.getSuit()) 
                    : rankComp);
        }      
    }
    
    /**
     * Comparator class used to apply a different compare to method.
     *  Comparing by ascending suit then rank order instead of
     *  descending rank then ascending suit order.
     */
    public static class CompareSuit implements Comparator<Card> {
        /** 
         * Method to compare a object to a second object and return
         *  the ascending order of suit then rank.
         * @param a First card object to compare
         * @param b Second card object to compare against.
         * @return Integer value of compare result: 
         *         0       = object is equal to b.
         *         -Number = object is less than b.
         *         +Number = object is greater than b.
         */
        @Override
        public int compare(Card a, Card b) {
            int suitComp = a.getSuit().compareTo(b.getSuit());
            return (suitComp == 0 ? a.getRank().compareTo(b.getRank()) 
                    : suitComp);
        }      
    }
        
    /**
     * Method to serialise card object to file.
     */
    public void save() {
        Serialization.writeToFile(this, "card.ser");
    }
    
    /**
     * Method to load and return card object from file.
     * @return Card object loaded or null if card was unable to be 
     *  loaded.
     */
    public static Card load() {
        return (Card)Serialization.readFromFile("card.ser");
    }
    
    /**
     * Method to get and return formatted string containing card
     *  information.
     * @return String with card information.
     */
    @Override
    public String toString() { 
        // Format card information and return
        StringBuilder str = new StringBuilder();        
        str.append(getRank()).append(" of ");
        str.append(getSuit()).append(".\n");
        return str.toString();
    }
    
    /**
     * Method used to test every part of Card.java.
     */
    public static void main() {
        System.out.println("\n----------- CARD TESTING -----------\n");
        
        // Test cards
        Card a = new Card(Card.Rank.KING, Card.Suit.hearts);
        Card b = new Card(Card.Rank.TWO, Card.Suit.clubs);
        Card c = new Card(Card.Rank.KING, Card.Suit.hearts);
        Card d = new Card(Card.Rank.ACE, Card.Suit.clubs);
        Card e = new Card(Card.Rank.KING, Card.Suit.clubs);
        
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(a);
        cards.add(b);
        cards.add(c);
        cards.add(d);        
        cards.add(e);
        
        System.out.println("Card Type: " + a.getRank()
                + "\nCard Value: " + a.getRank().getValue()
                + "\nPrevious Card Type: " + a.getRank().getPrevious()
                + "\nPrevious Card Value: " 
                + a.getRank().getPrevious().getValue() + "\n");
        System.out.println("Card Type: " + b.getRank()
                + "\nCard Value: " + b.getRank().getValue()
                + "\nPrevious Card Type: " + b.getRank().getPrevious()
                + "\nPrevious Card Value: " 
                + b.getRank().getPrevious().getValue() + "\n");
        
        System.out.println("Sum of " + a.getRank() + " and "
                + b.getRank() + " equals " + Card.sum(a, b) + "\n");
        
        System.out.println("Are " + a.getRank() + " and "
                + b.getRank() + " a blackjack? " 
                + Card.isBlackjack(a, b));        
        System.out.println("Are " + c.getRank() + " and "
                + d.getRank() + " a blackjack? " 
                + Card.isBlackjack(c, d));
        
        System.out.println("\nCard " + a.getRank() + " of " 
                + a.getSuit() + " compared to " + b.getRank() 
                + " of " + b.getSuit() + " result: " 
                + a.compareTo(b));
        System.out.println("Card " + a.getRank() + " of "
                + a.getSuit() + " compared to " + c.getRank()
                + " of " + c.getSuit() + " result: "
                + a.compareTo(c) + "\n");
               
        System.out.println("Before Sort:");
        for (Card card : cards)
            System.out.print(card.toString());
        
        Collections.sort(cards);  
        System.out.println("\nRank Descending Sort:");
        for (Card card : cards)
            System.out.print(card.toString());

        Collections.sort(cards, new Card.CompareAscending());  
        System.out.println("\nRank Ascending Sort:");
        for (Card card : cards)
            System.out.print(card.toString());
        
        Collections.sort(cards, new Card.CompareSuit());  
        System.out.println("\nSuit Ascending Sort:");
        for (Card card : cards)
            System.out.print(card.toString());

        System.out.println("");
        a.save();
        Card aLoaded = Card.load();
        System.out.print("\nCard Loaded (After Save): \n" 
                + aLoaded.toString());    
    }
}