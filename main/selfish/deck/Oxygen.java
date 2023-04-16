package selfish.deck;

/**
 * Class Oxygen
 * 
 * @author Minjun Kim
 * @version 1.0
 */
public class Oxygen extends Card {
    private static final long serialVersionUID = 422L;
    private int value;

    /**
     * Oxygen constructor
     * 
     * @param value value of oxygen
     */
    public Oxygen(int value) {
        super("Oxygen", "description");
        this.value = value;
    }

    /**
     * returns the value of oxygen
     * 
     * @return valule
     */
    public int getValue() {
        return value;
    }

    /**
     * returns the name of oxygen in the form of "Oxygen(value)"
     * 
     * @return name
     */
    public String toString() {
        return "Oxygen(" + value + ")";
    }
}
