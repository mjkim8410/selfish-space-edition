package selfish;
import java.io.Serializable;
import java.util.*;
import selfish.deck.*;


/**
 * Class Astronaut
 * @author Minjun Kim
 * @version 1.0
 */
public class Astronaut implements Serializable {
    private static final long serialVersionUID = 422L;
    private GameEngine game;
    private String name;
    private List<Card> actions = new ArrayList<Card>();
    private List<Oxygen> oxygens = new ArrayList<Oxygen>();
    private Collection<Card> track = new ArrayList<Card>();


    /**
     * Astronaut constructor
     * @param name name of the astronaut
     * @param game game which the astronaut is in
     */
    public Astronaut(String name, GameEngine game) {
        this.name = name;
        this.game = game;
    }


    /**
     * adds passed card to the astronaut's hand
     * @param card card to add
     */
    public void addToHand(Card card) {
        if (card instanceof Oxygen) {
            oxygens.add((Oxygen) card);
        }
        else {actions.add(card);}
    }


    /**
     * adds passed card to the astronaut's track
     * @param card card to add
     */
    public void addToTrack(Card card) {
        track.add(card);
    }


    /**
     * the astronauts breathes and discards two oxygens from hand
     * @return oxygen remaining
     */
    public int breathe() {
        if (!isAlive()) {
            throw new IllegalStateException();
        }
        int i=0;
        Oxygen oxy = new Oxygen(1);
        for (Oxygen element : oxygens) {
            if (element instanceof Oxygen) {
                if (element.compareTo(oxy) == 0) {
                    i++;
                }
            }
        }

        if (i==0) {
            for (Oxygen element : oxygens) {
                if (element.getValue() == 2) {
                    Oxygen[] oxyPair = game.splitOxygen(element);
                    oxygens.remove(element);
                    addToHand(oxyPair[0]);
                    addToHand(oxyPair[1]);
                    break;
                }
            }
        }

        for (Oxygen element : oxygens) {
            if (element.getValue() == 1) {
                oxygens.remove(element);
                (game.getGameDiscard()).add(element);
                break;
            }
        }

        int j = 0;
        for (Oxygen element: oxygens) {
            j += element.getValue();
        }
        if (oxygenRemaining() == 0) {
            game.killPlayer(this);
        }
        return j;
    }


    /**
     * returns the astronaut's distance from ship
     * @return distance
     */
    public int distanceFromShip() {
        return 6 - getTrack().size();
    }


    /**
     * returns all the action cards in hand
     * @return action cards
     */
    public List<Card> getActions() {
        Collections.sort(actions);
        return actions;
    }


    /**
     * lists all the action cards in hand
     * @param enumerated enumerates the cards
     * @param excludeShields excludes shields
     * @return actions cards
     */
    public String getActionsStr(boolean enumerated, boolean excludeShields) {

        String str = "";
        ArrayList<String> actions = new ArrayList<String>();
        actions.add("Hack suit");
        actions.add("Hole in suit");
        actions.add("Laser blast");
        actions.add("Oxygen siphon");
        actions.add("Rocket booster");
        if (!excludeShields) {
            actions.add("Shield");
        }
        actions.add("Tether");
        actions.add("Tractor beam");

        ArrayList<String> letters = new ArrayList<String>();
        letters.add("[A] ");
        letters.add("[B] ");
        letters.add("[C] ");
        letters.add("[D] ");
        letters.add("[E] ");
        letters.add("[F] ");
        letters.add("[G] ");
        letters.add("[H] ");

        if (enumerated) {
            int i = 0;
            for (String element : actions) {
                if (hasCard(element) != 0) {
                    str = str + letters.get(i) + element + ", ";
                    i++;
                }
            }
        }
        else {
            for (String element : actions) {
                if (hasCard(element) == 1) {
                    str = str + element + ", ";
                }
                else if (hasCard(element) > 1) {
                    str = str + Integer.toString(hasCard(element)) + "x " + element + ", ";
                }
            }
        }
        if (str.length() == 0) {return str;}
        else {return str.substring(0, str.length()-2);}
    }


    /**
     * returns all the cards in hand
     * @return cards
     */
    public List<Card> getHand() {
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.addAll(oxygens);
        hand.addAll(actions);
        Collections.sort(hand);
        return hand;
    }


    /**
     * lists all the cards in hand
     * @return cards
     */
    public String getHandStr() {
        String str = "";
        if (hasCard("Oxygen(2)") > 1) {
            str = str + Integer.toString(hasCard("Oxygen(2)")) + "x " + "Oxygen(2), ";
        }
        else if (hasCard("Oxygen(2)") == 1) {
            str = str + "Oxygen(2), ";
        }
        if (hasCard("Oxygen(1)") > 1) {
            str = str + Integer.toString(hasCard("Oxygen(1)")) + "x " + "Oxygen(1), ";
        }
        else if (hasCard("Oxygen(1)") == 1) {
            str = str + "Oxygen(1), ";
        }
        if (str.length() != 0) {
            str = str.substring(0, str.length()-2);
            str = str + "; ";
        }
        str = str + getActionsStr(false, false);
        return str;
    }


    /**
     * returns the track of the astrunaut
     * @return track
     */
    public Collection<Card> getTrack() {
        return track;
    }


    /**
     * discards the passed card from hand
     * @param card card to discard
     */
    public void hack(Card card) {
        if (card == null) {
            throw new IllegalArgumentException();
        }
        boolean cardFound = false;
        for (Card element : getHand()) {
            if (element == card) {
                cardFound = true;
                if (card instanceof Oxygen) {
                    oxygens.remove(card);
                    if (oxygenRemaining() == 0) {
                        game.killPlayer(this);
                        }
                    }
                else {
                    actions.remove(card);
                }
            }
        }
        if (!cardFound) {throw new IllegalArgumentException();}
    }


    /**
     * discards the same card as the passed card from hand 
     * @param card card to discard
     * @return discarded card
     */
    public Card hack(String card) {
        if (card == null) {
            throw new IllegalArgumentException();
        }
        if (card.equals(GameDeck.OXYGEN_1)) {
            for (Oxygen element : oxygens) {
                if (element.getValue() == 1) {
                    oxygens.remove(element);
                    if (oxygenRemaining() == 0) {
                        game.killPlayer(this);
                    }
                    return (Card) element;
                }
            }
        }
        else if (card.equals(GameDeck.OXYGEN_2)) {
            for (Oxygen element : oxygens) {
                if (element.getValue() == 2) {
                    oxygens.remove(element);
                    if (oxygenRemaining() == 0) {
                        game.killPlayer(this);
                    }
                    return (Card) element;
                }
            }
        }
        else {
            for (Card element : actions) {
                if (element.toString().equals(card)) {
                    actions.remove(element);
                    return (Card) element;
                }
            }
        }
        throw new IllegalArgumentException();
    }


    /**
     * returns the number of passed card in hand
     * @param card card
     * @return number of cards
     */
    public int hasCard(String card) {
        int i = 0;
        for (Card element : getHand()) {
            if (element.toString().equals(card)) {
                i++;
            }
        }
        return i;
    }


    /**
     * returns true if Solar flare is directed behind the astronaut
     * @return boolean
     */
    public boolean hasMeltedEyeballs() {
        return peekAtTrack().toString().equals(SpaceDeck.SOLAR_FLARE);
    }


    /**
     * returns true if the astronaut has won
     * @return boolean
     */
    public boolean hasWon() {
        Boolean win = (distanceFromShip() == 0 && isAlive());
        return win;
    }


    /**
     * returns true of the astronaut is alive
     * @return oxygen remaining
     */
    public boolean isAlive() {
        return (oxygenRemaining() > 0);
    }


    /**
     * pushed the astronaut back by one space
     * @return the space card that was directly behind
     */
    public Card laserBlast() {
        if (this.track.size() > 0) {
            Card ob = ((List<Card>) track).get(track.size()-1);
            track.remove(ob);
            return ob;
        }
        else {throw new IllegalArgumentException();}
    }


    /**
     * returns the number of oxygen remaining
     * @return number of oxygen
     */
    public int oxygenRemaining() {
        int oxy = 0;
        for (Oxygen element : oxygens) {
            oxy += element.getValue();
        } 
        return oxy;
    }


    /**
     * returns the space card directly behind
     * @return space card
     */
    public Card peekAtTrack() {
        if (getTrack().isEmpty()) {
            return null;
        }
        else if (this.distanceFromShip() < 0) {
            return null;
        }
        else {
            Card peek = ((ArrayList<Card>)track).get(getTrack().size()-1);
        return peek;
        }
    }


    /**
     * discards one Oxygen(1) from hand
     * @return discarded oxygen
     */
    public Oxygen siphon() {
        for (Oxygen element : oxygens ) {
            if (element.getValue() == 1) {
                oxygens.remove(element);
                if (oxygenRemaining() == 0) {
                    game.killPlayer(this);
                }
                return element;
            }
        }
        for (Oxygen element : oxygens ) {
            if (element.getValue() == 2) {
                Card[] oxyPair = game.splitOxygen(element);
                oxygens.remove(element);
                addToHand(oxyPair[1]);
                Card ox = oxyPair[0];
                return (Oxygen)ox;
            }
        }
        return null;
    }


    /**
     * removes a random card in hand
     * @return removed card
     */
    public Card steal() {
        if (getHand().size() == 1) {
            Card stolenCard = getHand().get(0);
            if (stolenCard instanceof Oxygen) {
                oxygens.remove(stolenCard);
            }
            else {
                actions.remove(stolenCard);
            }
            game.killPlayer(this);
            return stolenCard;
        }
        else {
            Random rand = new Random();
            Card stolenCard = getHand().get(rand.nextInt(getHand().size()-1));
            if (stolenCard instanceof Oxygen) {
                oxygens.remove(stolenCard);
            }
            else {
                actions.remove(stolenCard);
            }
            return stolenCard;
        }
    }


    /**
     * swaps track with the passed astronaut
     * @param swapee astronaut to swap track with
     */
    public void swapTrack(Astronaut swapee) {
        Collection<Card> swapeeTrack = swapee.getTrack();
        Collection<Card> thisTrack = this.getTrack();
        this.track = swapeeTrack;
        swapee.track = thisTrack;
    }


    /**
     * returns the astronaut's name and whether they are dead
     * @return  astronaut
     */
    public String toString() {
        if (isAlive()) {
            return name;
        }
        else {return (name + " (is dead)");}
    }
}
