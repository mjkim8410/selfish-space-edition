package selfish.deck;
import java.io.Serializable;


/**
 * Class Card
 * @author Minjun Kim
 * @version 1.0
 */
public class Card implements Serializable, Comparable<Card> {

    private static final long serialVersionUID = 422L;

    private String name;

    private String description;


    /**
     * Card constructor
     * @param name name
     * @param description description
     */
    public Card(String name, String description) {   

        this.name = name;

        this.description = description; 
    }


    /**
     * returns card description
     * @return description
     */
    public String getDescription() {return description;}


    /**
     * returns card name
     * @return  name
     */
    public String toString() {return name;}


    /**
     * implements comparisons between cards
     */
    @Override public int compareTo(Card a) {
        if (this instanceof Oxygen && a instanceof Oxygen) {

            Oxygen firstOxygen = (Oxygen) this;

            Oxygen secondOxygen = (Oxygen) a;

            if (firstOxygen.getValue() == secondOxygen.getValue()) {
                return 0;
            } 
            else if (firstOxygen.getValue() < secondOxygen.getValue()) {
                return -1;
            }
            else {return 1;}
        }
        else
            {
                String firstCardName = this.toString();

                String secondCardName = a.toString();

                return firstCardName.compareTo(secondCardName);
            }
    }   
}
