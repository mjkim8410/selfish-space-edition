package selfish.deck;
import java.util.*;
import selfish.GameException;


/**
 * Class SpaceDeck
 * @author Minjun Kim
 * @version 1.0
 */
public class SpaceDeck extends Deck {
    /** Serial Version UID */
    private static final long serialVersionUID = 422L;
    /** Asteroid field */
    public static final String ASTEROID_FIELD = "Asteroid field";
    /** Blank space */
    public static final String BLANK_SPACE = "Blank space";
    /** Cosmic radiation */
    public static final String COSMIC_RADIATION = "Cosmic radiation";
    /** Gravitational anomaly */
    public static final String GRAVITATIONAL_ANOMALY = "Gravitational anomaly";
    /** Hyperspace */
    public static final String HYPERSPACE = "Hyperspace";
    /** Meteoroid */
    public static final String METEOROID = "Meteoroid";
    /** Mysterious nebula */
    public static final String MYSTERIOUS_NEBULA = "Mysterious nebula";
    /** Solar flare */
    public static final String SOLAR_FLARE = "Solar flare";
    /** Useful junk */
    public static final String USEFUL_JUNK = "Useful junk";
    /** Wormhole */
    public static final String WORMHOLE = "Wormhole";


    /**
     * creates an empty SpaceDeck
     */
    public SpaceDeck() {
        add(new ArrayList<Card>());
    }


    /**
     * creates a fully loaded SpaceDeck
     * @param path path of space cards
     * @throws GameException file not found
     */
    public SpaceDeck(String path) throws GameException {
        add(SpaceDeck.loadCards(path));
    }
}

