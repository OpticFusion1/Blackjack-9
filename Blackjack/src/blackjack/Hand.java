package blackjack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Class: Hand.java
 * Description: Hand object used to handle a hand of cards.
 * Author: Paulo Jorge.
 */
public class Hand implements Serializable, Iterable<Card> {
    // Version control
    private static final long serialVersionUID = 102;
    
    // Used to store collection of cards in hand    
    private ArrayList<Card> cards;
    // Card rank counts
    private int[] rankCount;
    // Total value(s) in hand
    private List<Integer> totalValue;
    
    /**
     * Constructor to initialise a new empty hand.
     */
    public Hand() {
        // Initialise new hand
        newHand();
    }
    
    /**
     * Constructor to add a array of cards to a new initialised hand.
     * @param cards Card array to be added to new hand.
     */
    public Hand(Card[] cards) {
        // Initialise new hand
        newHand();
        
        // Add cards to hand
        for (Card card : cards)
            add(card);
    }
    
    /**
     * Constructor to take a different hand and add all the cards to 
     *  a new initialised hand.
     * @param hand Hand object to get cards from and add to new hand.
     */
    public Hand(Hand hand) {
        // Initialise new hand
        newHand();

        // Add cards to hand
        for (Card card : this.cards)
            add(card);
    }
    
    /**
     * Method to initialise a new hand.
     */
    private void newHand() {
        this.cards = new ArrayList();
        this.rankCount = new int[Card.Rank.values().length];
        this.totalValue = new ArrayList();
    }
    
    /**
     * Method to get and return the size of this hand.
     * @return Integer size value of this hand.
     */
    public int size() {
        return this.cards.size();
    }

    /**
     * Method to add card to hand. Then increase the rank count of 
     *  cards in hand and update the total value(s) of the current hand 
     *  based on new added card.
     * @param card Card object to add to hand. 
     */
    private void addCard(Card card) {
        // Add card to hand
        this.cards.add(card);
        
        // Increase rank count based on rank enumerator position
        this.rankCount[card.getRank().ordinal()]++;

        // Get value of card
        int value = card.getRank().getValue();
        // Card type
        boolean isAce = (value == 11);
        
        // If list is empty add value to list
        if (this.totalValue.isEmpty()) {
            this.totalValue.add(isAce ? 1 : value);
            // If this is an ace, also add 11 to end of list
            if (isAce)
                this.totalValue.add(11);
        }
        else {
            // Store size of collection, to prevent loop from adding 1 
            // ontop of 11 if ace is added to total value
            int size = this.totalValue.size();
            
            // Add 11 to end of list
            if (isAce)
                this.totalValue.add((this.totalValue.get(size - 1) 
                        + 11));
            
            // Increase value(s) by this cards value
            for (int i = 0; i < size; i++)
                this.totalValue.set(i, this.totalValue.get(i) 
                        + (isAce ? 1 : value));
        }
    }
    
    /**
     * Method to remove a card. Then decrease the rank count of cards 
     *  in hand and update the total value(s) of the current hand based 
     *  on the newly removed card.
     * @param card Card object to remove from hand. 
     * @return Boolean true if card was removed or false if not. 
     */
    private boolean removeCard(Card card) {
        // Remove card
        boolean removed = this.cards.remove(card);
        
        // If card was removed update counts
        if (removed) {
            // Increase rank count based on rank enumerator position
            this.rankCount[card.getRank().ordinal()]--;

            // Get value of card
            int value = card.getRank().getValue();
            // Card type
            boolean isAce = (value == 11);

            // Remove last element from collection if this is an ace
            if (isAce)
                this.totalValue.remove(this.totalValue.get(
                        this.totalValue.size() - 1));

            // Decrease value(s) by this cards value
            for (int i = 0; i < this.totalValue.size(); i++)
                this.totalValue.set(i, (this.totalValue.get(i) 
                        - (isAce ? 1 : value)));
        }
        return removed;
    }
 
    /**
     * Method to add a single card to hand.
     * @param card Card object to add to hand.
     */
    public final void add(Card card) {
        addCard(card);
    }
    
    /**
     * Method to add a collection type of cards to hand.
     * @param cards Collection object to add to hand.
     */
    public final void add(Collection cards) {
        for (Object card : cards)
            addCard((Card) card);
    }

    /**
     * Method to add a hand of cards to hand.
     * @param hand Hand object containing cards to add to hand.
     */
    public final void add(Hand hand) {
        for (Card card : hand.cards)
            addCard(card);
    }    
    
    /**
     * Method to remove a single card from hand if present.
     * @param card Card object to remove.
     * @return Boolean true if card was removed or false if not. 
     */
    public final boolean remove(Card card) {
        return removeCard(card);
    }
    
    /**
     * Method to remove all cards from a different hand if present.
     * @param hand Hand object containing cards to remove.
     * @return Boolean true if all cards were removed or false if not. 
     */
    public final boolean remove(Hand hand) {
        boolean removed = false;
        // Remove all occurrences of each card from collection
        for (Card card : hand.cards) {
            while (this.cards.contains(card)) {
                removed = removeCard(card);
            }
        }
        return removed;
    }    
    
    /**
     * Method to remove a single card from hand based on a index 
     *  position if present.
     * @param position Position integer to remove.
     * @return Card object removed from hand or null if position out
     *  of bounds.
     */
    public final Card remove(int position) {
        // Error check position bounds
        if ((position >= 0) && (position < size())) {
            Card card = this.cards.get(position);
            
            // Remove card and return card removed
            return (removeCard(card) ? card : null);
        }
        return null;
    }
    
    /**
     * Method to count the number of cards in hand based on passed 
     *  in argument suit.
     * @param suit Suit object type to check.
     * @return Integer count of number of cards in hand with the 
     *  type of argument suit.
     */
    public int countSuit(Card.Suit suit) {
        int count = 0;
        for (Card card : this.cards)
            // Increment count if card suit is same as passed in suit
            if (card.getSuit().equals(suit))
                count++;
        return count;
    }
    
    /**
     * Method to count the number of cards in hand based on passed 
     *  in argument rank.
     * @param rank Rank object type to check.
     * @return Integer count of number of cards in hand with the 
     *  type of argument rank.
     */
    public int countRank(Card.Rank rank) {
        return this.rankCount[rank.ordinal()];
    }
    
    /**
     * Method that takes a value argument and returns true
     *  if the lowest possible hand value is greater than the value 
     *  passed in.
     * @param value Integer value to check against.
     * @return True if the lowest hand value is greater than the 
     *  value passed in, else false.
     */
    public boolean isOver(int value) {
        // Check if total value(s) contains atleast 1 value
        if (this.totalValue.size() > 0)
            return (Collections.min(this.totalValue) > value);
        return false;
    }
    
    /**
     * Method to get and return the highest value below or equal to the 
     *  passed in value.
     * @param threshold Integer value to check against.
     * @return Integer highest value below parameter value. If
     *  no value exists below; the next lowest value. Or, if 
     *  hand is empty -1.
     */
    public int getHighestBelowOrEqual(int threshold) {
        // Check if total value(s) contains atleast 1 value
        if (this.totalValue.size() > 0) {
            // Size order sort
            Collections.sort(this.totalValue);
            
            int value = this.totalValue.get(0);
            int previous = value;
            // Begin iterator at position 1, as value starts at 0
            int i = 1;
            // Loop until i greater than list size or value is greater
            // than threshold value
            while ((i < this.totalValue.size()) 
                    && (value <= threshold)) {
                previous = value;
                value = this.totalValue.get(i);
                i++;
            }
            
            // If value is greater than threshold return previous value
            // that was less than threshold
            return ((value > threshold) ? previous : value);
        }
        
        // If hand is empty
        return -1;
    }
    
    /**
     * Method to determine if the current hand is a blackjack.
     * @return Boolean true if the current hand is a blackjack. For
     *  example a ACE + TEN (Or a picture card).
     */ 
    public boolean isBlackjack() {
        if (size() == 2)
            return Card.isBlackjack(this.cards.get(0), 
                    this.cards.get(1));
        return false;
    }
    
    /**
     * Method to sort a hand into descending rank order.
     * @param hand Hand object to sort.
     * @return Sorted hand object.
     */
    public static Hand sortDescending(Hand hand) {
        // Sort and return hand
        Collections.sort(hand.cards);
        return hand;
    }

    /**
     * Method to sort a hand into ascending rank order.
     * @param hand Hand object to sort.
     * @return Sorted hand object.
     */
    public static Hand sortAscending(Hand hand) {
        // Sort and return hand
        Collections.sort(hand.cards, new Card.CompareAscending());
        return hand;
    }    

    /**
     * Method to sort a hand into ascending suit order.
     * @param hand Hand object to sort.
     * @return Sorted hand object.
     */
    public static Hand sortSuit(Hand hand) {
        // Sort and return hand
        Collections.sort(hand.cards, new Card.CompareSuit());
        return hand;
    }    
    
    /**
     * Method to return a new hand in reverse card order.
     * @return Hand object in reverse card order.
     */
    public Hand reverseHand() {
        Card[] temp = new Card[size()];
        // Loop hand and add each card to array in reverse order
        for (int i = 0, x = size(); i < x; i++)
            temp[i] = this.cards.get((x - 1) - i);

        // Return new hand object containing reversed array
        return (new Hand(temp));
    }
    
    /**
     * Method to initialise a new iterator used to iterate over 
     *  all cards in the hand.
     * @return Iterator object that can be used to iterate hand.
     */
    @Override
    public Iterator<Card> iterator() {
        return new HandIterator();
    }
    
    /**
     * Private iterator used to iterate each card in a hand. Class 
     * is not accessible directly by user, but instead through the 
     * use of the iterator() method of the hand class.
     */
    private class HandIterator implements Iterator<Card> {       
        // Iterator position
        private int nextCard;
        
        /**
         * Constructor to initialise a new hand iterator object. 
         */
        private HandIterator() {
            this.nextCard = 0;
        }
        
        /**
         * Method to check if the iterator has reached the end of the 
         *  hand.
         * @return True if the iterator is not at end of hand else 
         *  false. 
         */
        @Override
        public boolean hasNext() {
            return (this.nextCard < Hand.this.size());
        }

        /**
         * Method to iterate each card in hand and return card object.
         * @return Card object in hand.
         */
        @Override
        public Card next() {
            if (hasNext())
                return Hand.this.cards.get(this.nextCard++);
            else
                throw new NoSuchElementException();
        }

        /**
         * Method to remove the last element called by the iterator.
         */
        @Override
        public void remove() {
            Hand.this.remove(this.nextCard -= 1);
        }    
    }
    
    /**
     * Method to serialise hand object to file.
     */
    public void save() {
        Serialization.writeToFile(this, "hand.ser");
    }
    
    /**
     * Method to load and return hand object from file.
     * @return Hand object loaded or null if hand was unable to be 
     *  loaded.
     */
    public static Hand load() {
        return (Hand)Serialization.readFromFile("hand.ser");
    }
    
    
    /**
     * Method to get and return formatted string containing hand
     *  information.
     * @return String with hand information.
     */
    @Override
    public String toString() { 
        // Format hand information and return
        StringBuilder str = new StringBuilder();  
        
        // Only display hand if hand contains at least 1 card
        if (size() > 0) {
            str.append("Card(s) in hand: \n");
            for (Iterator<Card> i = this.cards.iterator(); 
                    i.hasNext();) {
                str.append(" - ").append(i.next().toString());
            }
        } else
            str.append("Hand is currently empty.\n");
        
        return str.toString();
    }
    
    /**
     * Method used to test every part of Hand.java.
     */
    public static void main() {
        System.out.println("\n----------- HAND TESTING -----------\n");
        
        // Test Cards
        Card a = new Card(Card.Rank.KING, Card.Suit.hearts);
        Card b = new Card(Card.Rank.TWO, Card.Suit.clubs);
        Card c = new Card(Card.Rank.KING, Card.Suit.hearts);
        Card d = new Card(Card.Rank.ACE, Card.Suit.clubs);
        Card e = new Card(Card.Rank.KING, Card.Suit.clubs);
        Card f = new Card(Card.Rank.ACE, Card.Suit.diamonds);
        
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(a);
        cards.add(b);
        cards.add(c);
        cards.add(d);        
        cards.add(e);
        
        Hand handA = new Hand();
        System.out.println("Hand A: \n" + handA.toString());

        Card[] cardsArray = {a, b};
        Hand handB = new Hand(cardsArray);
        System.out.println("Hand B: \n" + handB.toString());
        
        Hand handC = new Hand(handB);
        handC.add(d);
        System.out.println("Hand C (After Add Card): \n" 
                + handC.toString());  

        System.out.println("Remove Card (d) Result: " 
                + handC.remove(d));        
        System.out.println("Remove Card (d) Result: " 
                + handC.remove(d) + "\n");
        System.out.println("Hand C (After Remove Card): \n" 
                + handC.toString());
        
        handC.add(cards);
        System.out.println("Hand C (After Add Collection): \n" 
                + handC.toString());        
        handC.add(handB);
        System.out.println("Hand C (After Add Hand): \n" 
                + handC.toString());      

        System.out.println("Hand C sorted into descending order: \n"
                + Hand.sortDescending(handC).toString());
        System.out.println("Hand C sorted into ascending order: \n"
                + Hand.sortAscending(handC).toString());
        System.out.println("Hand C sorted into suit order: \n"
                + Hand.sortSuit(handC).toString());
        
        System.out.println("Remove Hand Result: " 
                + handC.remove(handB));
        System.out.println("Remove Hand Result: " 
                + handC.remove(handB) + "\n");
        System.out.println("Hand C (After Remove Hand): \n" 
                + handC.toString());

        System.out.println("Remove Position 3 Result: " 
                + handC.remove(3));
        System.out.println("Remove Position 0 Result: " 
                + handC.remove(0));
        System.out.println("Hand C (After Remove Position): \n" 
                + handC.toString());
        
        handC.add(f);
        System.out.println("Hand C (After Add Second Ace): \n" 
                + handC.toString());

        System.out.println("Suit Count: ");
        for (Card.Suit suit : Card.Suit.values())
            System.out.println(suit + ": " + handC.countSuit(suit));
        System.out.println("\nRank Count: ");
        for (Card.Rank rank : Card.Rank.values())
            System.out.println(rank + ": " + handC.countRank(rank));    
        
        System.out.println("\nIs the hands lowest value over 12)? " 
                + handC.isOver(12));
        System.out.println("Is the hands lowest value over 11)? " 
                + handC.isOver(11));
        
        System.out.println("\nHighest value below or equal to 22: \n"
                + handC.getHighestBelowOrEqual(22));
        System.out.println("Highest value below or equal to 11: \n"
                + handC.getHighestBelowOrEqual(11));
        
        System.out.println("\nHand Reversed: \n"
                + handC.reverseHand().toString());
        
        System.out.println("Hand C Cards Iterated: ");
        Iterator<Card> handIterator = handC.iterator();
        while (handIterator.hasNext())
            System.out.print(handIterator.next());
        handIterator.remove();
        System.out.println("\nHand C (After Iterator Remove): \n" 
                + handC.toString());
        
        handC.save();
        Hand handCLoaded = Hand.load();
        System.out.print("\nHand C (After save and load): \n" 
                + handCLoaded.toString());
    }
}