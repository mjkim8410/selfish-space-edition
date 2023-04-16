package selfish.deck;
import java.util.*;
import selfish.GameException;


/**
 * GameDeck constructor
 * @author Minjun Kim
 * @version 1.0
 */
public class GameDeck extends Deck {
    /** Serial Version UID */
    private static final long serialVersionUID = 422L;
    /** Hack suit */
    public static final String HACK_SUIT = "Hack suit";
    /** Hole in suit */
    public static final String HOLE_IN_SUIT = "Hole in suit";
    /** Laser blast */
    public static final String LASER_BLAST = "Laser blast";
    /** Oxygen */
    public static final String OXYGEN = "Oxygen";
    /** Oxygen(1) */
    public static final String OXYGEN_1 = "Oxygen(1)";
    /** Oxygen(2) */
    public static final String OXYGEN_2 = "Oxygen(2)";
    /** Oxygen siphon */
    public static final String OXYGEN_SIPHON = "Oxygen siphon";
    /** Rocket booster */
    public static final String ROCKET_BOOSTER = "Rocket booster";
    /** Shield */
    public static final String SHIELD = "Shield";
    /** Tether */
    public static final String TETHER = "Tether";
    /** Tractor beam */
    public static final String TRACTOR_BEAM = "Tractor beam";
    

    /**
     * creates an empty GameDeck
     */
    public GameDeck() {
        add(new ArrayList<Card>());
    }


    /**
     * creates a fully loaded GameDeck
     * @param path path of action cards
     * @throws GameException path not found
     */
    public GameDeck(String path) throws GameException {
        add(GameDeck.loadCards(path));
        int numberOfOxygenTwo = 10;
        int numberOfOxygenOne = 38;
        for (int i=0; i<numberOfOxygenTwo; i++) {
            add(new Oxygen(2));
        }
        for (int i=0; i<numberOfOxygenOne; i++) {
            add(new Oxygen(1));
        }
    }


    /**
     * draws oxygen of the passed value from GameDeck
     * @param value value of oxygen to draw
     * @return drawn oxygen
     */
    public Oxygen drawOxygen(int value) {
        Oxygen sampleOxygen = new Oxygen(value);
        for (Card element : this.getCards()) {
            boolean notOxygen = !(element instanceof Oxygen);
            boolean wrongOxygen = sampleOxygen.compareTo(element) != 0;
            if (notOxygen || wrongOxygen) {continue;}
            this.remove(element);
            return ((Oxygen) element);
        }
        throw new IllegalStateException();
    }


    /**
     * takes an Oxygen(2) and returns a pair of Oxygen(1)
     * @param dbl Oxygen(2) to take
     * @return a pair of Oxygen(1)
     */
    public Oxygen[] splitOxygen(Oxygen dbl) {
        if (dbl.getValue() == 1) {
            throw new IllegalArgumentException();
            }

        Oxygen firstOxygenOne = this.drawOxygen(1);
        Oxygen secondOxygenOne = this.drawOxygen(1);
        Oxygen[] oxygenPair = {firstOxygenOne, secondOxygenOne};
        
        this.add(dbl);
        return oxygenPair;
    }
}
