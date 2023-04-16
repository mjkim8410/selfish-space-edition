package selfish.deck;

import java.util.*;
import java.io.*;
import selfish.GameException;

/**
 * Astract class Deck
 * 
 * @author Minjun Kim
 * @version 1.0
 */
public abstract class Deck implements Serializable {
    private static final long serialVersionUID = 422L;
    private Collection<Card> cards = new ArrayList<Card>();

    /**
     * Empty constructor
     */
    protected Deck() {
    }

    /**
     * returns all the cards in the deck
     * 
     * @return cards
     */
    public Collection<Card> getCards() {
        return cards;
    }

    /**
     * reads text file and return cards
     * 
     * @param path text file path
     * @return created cards from the text file
     * @throws GameException file not found
     */
    protected static List<Card> loadCards(String path) throws GameException {

        List<Card> cardList = new ArrayList<Card>();
        try {
            File cardFile = new File(path);
            Scanner Scan = new Scanner(cardFile);

            while (Scan.hasNextLine()) {
                String cardLine = Scan.nextLine();
                String lineSplit[] = cardLine.split("; ");
                if (lineSplit[0].equals("NAME") == true) {
                    continue;
                }

                for (int i = 0; i < stringToCards(cardLine).length; i++) {
                    ((ArrayList<Card>) cardList).add(stringToCards(cardLine)[i]);
                }
            }
            Scan.close();
        } catch (FileNotFoundException e) {
            throw new GameException("FileNotFoundError", e);
        }
        return cardList;
    }

    /**
     * takes a string and create cards
     * 
     * @param str string to create cards with
     * @return cards created
     */
    protected static Card[] stringToCards(String str) {
        String lineSplit[] = str.split("; ");
        int numOfCards = Integer.parseInt(lineSplit[2]);
        Card[] cards = new Card[numOfCards];
        for (int i = 0; i < numOfCards; i++) {
            cards[i] = new Card(lineSplit[0], lineSplit[1]);
        }
        return cards;
    }

    /**
     * adds a passed card to deck
     * 
     * @param card card to add
     * @return number of cards in deck
     */
    public int add(Card card) {
        ((ArrayList<Card>) this.cards).add(card);
        return cards.size();
    }

    /**
     * adds passed cards to deck
     * 
     * @param cards list of cards to add
     * @return number of cards in deck
     */
    protected int add(List<Card> cards) {
        this.cards.addAll(cards);
        return cards.size();
    }

    /**
     * draws the passed card from deck
     * 
     * @return card drawn
     */
    public Card draw() {
        int lastIndex = cards.size() - 1;
        if (cards.isEmpty()) {
            throw new IllegalStateException();
        }
        Card card = ((ArrayList<Card>) cards).get(lastIndex);
        ((ArrayList<Card>) cards).remove(lastIndex);
        return card;
    }

    /**
     * removed passed card from deck
     * 
     * @param card removed card
     */
    public void remove(Card card) {
        for (Card element : cards) {
            boolean match = element.compareTo(card) == 0;
            if (!match) {
                continue;
            }
            cards.remove(element);
            break;
        }
    }

    /**
     * shuffles the deck
     * 
     * @param random random
     */
    public void shuffle(Random random) {
        Collections.shuffle((ArrayList<Card>) cards, random);
    }

    /**
     * returns the number of cards in deck
     * 
     * @return number of cards
     */
    public int size() {
        return this.cards.size();
    }
}
