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
        boolean playerIsDead = !this.isAlive();
        if (playerIsDead) {
            throw new IllegalStateException();
        }
        int numberOfOxygenOneFound = 0;
        Oxygen sampleOxygenOne = new Oxygen(1);
        for (Oxygen element : oxygens) {
            if (element instanceof Oxygen) {
                if (element.compareTo(sampleOxygenOne) == 0) {
                    numberOfOxygenOneFound++;
                }
            }
        }

        if (numberOfOxygenOneFound == 0) {
            for (Oxygen element : oxygens) {
                if (element.getValue() == 2) {
                    Oxygen[] oxygenOnePair = game.splitOxygen(element);
                    oxygens.remove(element);
                    addToHand(oxygenOnePair[0]);
                    addToHand(oxygenOnePair[1]);
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

        int totalOxygenLeft = 0;
        for (Oxygen element: oxygens) {
            totalOxygenLeft += element.getValue();
        }
        if (oxygenRemaining() == 0) {
            game.killPlayer(this);
        }
        return totalOxygenLeft;
    }


    /**
     * returns the astronaut's distance from ship
     * @return distance
     */
    public int distanceFromShip() {
        int startingDistanceFromShip = 6;
        int distanceTrvelled = this.getTrack().size();
        return startingDistanceFromShip - distanceTrvelled;
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

        String actionCardsListed = "";
        ArrayList<String> actions = new ArrayList<String>();
        actions.add("Hack suit");
        actions.add("Hole in suit");
        actions.add("Laser blast");
        actions.add("Oxygen siphon");
        actions.add("Rocket booster");
        if (!excludeShields) 
        {actions.add("Shield");}
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
                    actionCardsListed = actionCardsListed + letters.get(i) + element + ", ";
                    i++;
                }
            }
        }
        else {
            for (String element : actions) {
                if (hasCard(element) == 1) {
                    actionCardsListed = actionCardsListed + element + ", ";
                }
                else if (hasCard(element) > 1) {
                    actionCardsListed = actionCardsListed + Integer.toString(hasCard(element)) + "x " + element + ", ";
                }
            }
        }
        boolean actionCardsListedIsEmpty = actionCardsListed.length() == 0;
        String truncatedActionCardsListed = actionCardsListed.substring(0, actionCardsListed.length()-2);
        if (actionCardsListedIsEmpty) {return actionCardsListed;}
        else {return truncatedActionCardsListed;}
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
        String oxygensInHandListed = "";
        int numberOfOxygenTwoInHand = hasCard("Oxygen(2)");
        int numberOfOxygenOneInHand = hasCard("Oxygen(1)");
        String numberOfOxygenTwoInHandStr = Integer.toString(numberOfOxygenTwoInHand);
        String numberOfOxygenOneInHandStr = Integer.toString(numberOfOxygenOneInHand);

        if (numberOfOxygenTwoInHand > 1) {
            oxygensInHandListed = oxygensInHandListed + numberOfOxygenTwoInHandStr + "x " + "Oxygen(2), ";
        }
        else if (numberOfOxygenTwoInHand == 1) {
            oxygensInHandListed = oxygensInHandListed + "Oxygen(2), ";
        }
        if (numberOfOxygenOneInHand > 1) {
            oxygensInHandListed = oxygensInHandListed + numberOfOxygenOneInHandStr + "x " + "Oxygen(1), ";
        }
        else if (numberOfOxygenOneInHand == 1) {
            oxygensInHandListed = oxygensInHandListed + "Oxygen(1), ";
        }
        if (oxygensInHandListed.length() != 0) {
            oxygensInHandListed = oxygensInHandListed.substring(0, oxygensInHandListed.length()-2);
            oxygensInHandListed = oxygensInHandListed + "; ";
        }
        String actionCardsListed = getActionsStr(false, false);
        String cardsInHandListed = oxygensInHandListed + actionCardsListed;
        return cardsInHandListed;
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
        boolean cardNotFound = true;
        for (Card element : getHand()) {
            if (element == card) {
                cardNotFound = false;
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
        if (cardNotFound) {throw new IllegalArgumentException();}
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
        int numberOfCardsInHand = 0;
        for (Card element : getHand()) {
            if (element.toString().equals(card)) {
                numberOfCardsInHand++;
            }
        }
        return numberOfCardsInHand;
    }


    /**
     * returns true if Solar flare is directed behind the astronaut
     * @return boolean
     */
    public boolean hasMeltedEyeballs() {
        String spaceCardBehind = peekAtTrack().toString();
        String solarFlare = SpaceDeck.SOLAR_FLARE;
        return spaceCardBehind.equals(solarFlare);
    }


    /**
     * returns true if the astronaut has won
     * @return boolean
     */
    public boolean hasWon() {
        boolean isAtShip = distanceFromShip() == 0;
        Boolean hasWon = (isAtShip && isAlive());
        return hasWon;
    }


    /**
     * returns true if the astronaut is alive
     * @return oxygenRemaining() > 0
     */
    public boolean isAlive() {
        boolean isAlive = oxygenRemaining() > 0;
        return (isAlive);
    }


    /**
     * pushed the astronaut back by one space
     * @return the space card that was directly behind
     */
    public Card laserBlast() {
        boolean isNotAtStartingPosition = this.track.size() > 0;
        if (isNotAtStartingPosition) {
            Card spaceCardBehind = ((List<Card>) track).get(track.size()-1);
            track.remove(spaceCardBehind);
            return spaceCardBehind;
        }
        else {throw new IllegalArgumentException();}
    }


    /**
     * returns the number of oxygen remaining
     * @return number of oxygen
     */
    public int oxygenRemaining() {
        int oxygenRemaining = 0;
        for (Oxygen element : oxygens) {
            oxygenRemaining += element.getValue();
        } 
        return oxygenRemaining;
    }


    /**
     * returns the space card directly behind
     * @return space card
     */
    public Card peekAtTrack() {
        boolean trackIsEmpty = getTrack().isEmpty();
        boolean isAtStartingPosition = this.distanceFromShip() < 0;
        if (trackIsEmpty || isAtStartingPosition) {
            return null;
        }
        else {
            Card spaceCardBehind = ((ArrayList<Card>)track).get(getTrack().size()-1);
        return spaceCardBehind;
        }
    }


    /**
     * discards one Oxygen(1) from hand
     * @return discarded oxygen
     */
    public Oxygen siphon() {
        for (Oxygen element : oxygens ) {
            boolean elementIsOxygenOne = element.getValue() == 1;
            if (elementIsOxygenOne) {
                oxygens.remove(element);
                if (oxygenRemaining() == 0) {
                    game.killPlayer(this);
                }
                return element;
            }
        }
        for (Oxygen element : oxygens ) {
            boolean elementIsOxygenTwo = element.getValue() == 2;
            if (elementIsOxygenTwo) {
                Card[] pairOfOxygenOne = game.splitOxygen(element);
                oxygens.remove(element);
                addToHand(pairOfOxygenOne[1]);
                Card oxygenOne = pairOfOxygenOne[0];
                return (Oxygen)oxygenOne;
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
        Collection<Card> myTrack = this.getTrack();
        this.track = swapeeTrack;
        swapee.track = myTrack;
    }


    /**
     * returns the astronaut's name and whether they are dead
     * @return  astronaut
     */
    public String toString() {
        boolean gameNotStarted = !game.hasStarted();
        if (isAlive() && gameNotStarted) {
            return name;
        }
        else {return (name + " (is dead)");}
    }
}
