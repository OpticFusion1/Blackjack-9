package blackjack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Class: Deck.java
 * Description: Deck class used to generate and handle a deck 
 *  of 52 cards.
 * Author: Paulo Jorge.
 */
public class Deck implements Serializable, Iterable<Card> {
    // Version control
    private static final long serialVersionUID = 112;
    
    // Deck list containing cards
    private List<Card> deck = new ArrayList();;
    // Total amount of cards when deck is full
    private final int TOTAL_SIZE = 52;
    
    /**
     * Deck constructor method used to initialise a new array list
     *  containing all 52 different card types.
     */
    public Deck() { 
        // Create new deck
        newDeck();
    }
    
    /**
     * Method to shuffle the deck into a random order.
     */
    public void shuffle() {
        // New instance of random
        Random random = new Random();
        
        // Only shuffle if deck contains more than 1 card
        if (size() > 1) {
            for (int i = (size() - 1); i > 0; i--) {
                // Swap position of index card with random card in deck
                this.deck.set(i, this.deck.set(random.nextInt(i), 
                        this.deck.get(i)));
            }
        }
    }
    
    /**
     * Method to remove the top card from the deck and return it.
     * @return Card object of the card dealt, otherwise null if deck
     *  is empty.
     */
    public Card deal() {
        // Position to deal
        int position = this.deck.size() - 1;
        // Deal card if available, else null if deck is empty.
        return (position > 0 ? this.deck.remove(position) : null);
    }
    
    /**
     * Method to get and return the size of this deck.
     * @return Integer size value of this deck.
     */
    public int size() {
        return this.deck.size();
    }
    
    /**
     * Method to get and return the size of this deck when completely
     *  full.
     * @return Integer value of total size. 
     */
    public int totalSize() {
        return this.TOTAL_SIZE;
    }
    
    /**
     * Method to create a new deck with all 52 different possible 
     *  cards.
     */
    public final void newDeck() {
        // Clear deck
        this.deck.clear();
        
        // Create new deck containing all 52 different cards
        for(Card.Suit suit : Card.Suit.values())
            for(Card.Rank rank : Card.Rank.values())
                this.deck.add(new Card(rank, suit));
    }
    
    /**
     * Method to initialise a new iterator used to iterate over 
     *  all cards in the deck.
     * @return Iterator object that can be used to iterate deck.
     */
    @Override
    public Iterator<Card> iterator() {
        return new SecondCardIterator();
    }
    
    /**
     * Iterator nested class used to iterate every other card in
     *  deck from back to front in descending order.
     */
    private class SecondCardIterator implements Iterator<Card> {
        // Iterator position
        private int nextCard;
    
        /**
         * Constructor to initialise a new second card iterator object. 
         */
        private SecondCardIterator() {
            // + 1 used to iterate from the card at top of the deck.
            // For example, if deck size was 52, then nextCard = 53,
            // this then means 53 - 2 = 51. Which is the top of deck.
            this.nextCard = Deck.this.size() + 1;
        }
        
        /**
         * Method to check if the iterator can still iterate by 2 
         *  cards, else identify that it has reached the end of the 
         *  deck.
         * @return True if the iterator is not at end of deck else 
         *  false. 
         */
        @Override
        public boolean hasNext() {
            return (this.nextCard > 1);
        }
        
        /**
         * Method to iterate 2 cards in iterator and return card 
         * object.
         * @return Card object in deck.
         */
        @Override
        public Card next() {
            if (hasNext())
                return Deck.this.deck.get(this.nextCard -= 2);
            else
                throw new NoSuchElementException();
        }
        
        /**
         * Method to remove the last element called by the iterator.
         */
        @Override
        public void remove() {
            // Error check position bounds
            if ((this.nextCard >= 0) 
                    && (this.nextCard < (Deck.this.deck.size() - 1)))
                Deck.this.deck.remove(this.nextCard += 2);
        }
    }
    
    /**
     * Method to serialise deck object to file in second card iterator
     *  order.
     */
    public void save() {
        // Create temp list in second card order
        List<Card> secondCardOrder = new ArrayList();
        Iterator<Card> iterator = this.iterator();
        while (iterator.hasNext())
            secondCardOrder.add(iterator.next());
        
        // Replace deck with second card order deck
        List<Card> temp = this.deck;
        this.deck = secondCardOrder;
        
        // Save
        Serialization.writeToFile(this, "deck.ser");
        
        // Restore deck after save
        this.deck = temp; 
    }
    
    /**
     * Method to load and return deck object from file.
     * @return Deck object loaded  or null if deck was unable to be 
     *  loaded.
     */
    public static Deck load() {
        return (Deck)Serialization.readFromFile("deck.ser");
    }    
    
    /**
     * Method used to test every part of Deck.java.
     */
    public static void main() { 
        System.out.println("\n----------- DECK TESTING -----------\n");
       
        Deck deck = new Deck();

        System.out.println("Deck Size: " + deck.size() + "\n");
        System.out.println("Card Dealt: \n" + deck.deal());
        System.out.println("Deck Size: " + deck.size() + "\n");
      
        System.out.println("Cards in deck: ");
        Iterator<Card> deckIterator = deck.iterator();
        while (deckIterator.hasNext())
            System.out.print(deckIterator.next());
        
        deckIterator.remove();
        System.out.println("\nDeck Size (After Remove): " 
                + deck.size() + "\n");
        
        deck.newDeck();
        System.out.println("New Deck Size: " + deck.size() + "\n");

        deck.save();
        Deck deckLoaded = Deck.load();
        System.out.println("\nDeck Loaded Size: " + deckLoaded.size());        
    }
}