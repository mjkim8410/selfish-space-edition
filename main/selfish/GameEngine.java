package selfish;
import java.io.*;
import java.util.*;
import selfish.deck.*;


/**
 * Class GameEngine
 * @author Minjun Kim
 * @version 1.0
 */
public class GameEngine implements Serializable {
    private Collection<Astronaut> activePlayers = new LinkedList<Astronaut>();
    private List<Astronaut> corpses = new ArrayList<Astronaut>();
    private Astronaut currentPlayer = null;
    private boolean hasStarted = false;
    private Random random = new Random();
    private static final long serialVersionUID = 422L;
    private GameDeck gameDeck = new GameDeck();
    private GameDeck gameDiscard = new GameDeck();
    private SpaceDeck spaceDeck = new SpaceDeck();
    private SpaceDeck spaceDiscard = new SpaceDeck();

    
    /**
     * an empty constructor
     */
    private GameEngine() {}


    /**
     * GameEngine constructor
     * @param seed seed for the random object
     * @param gameDeck path to the action cards text file
     * @param spaceDeck path to the space cards text file
     * @throws GameException file not found
     */
    public GameEngine(long seed, String gameDeck, String spaceDeck) throws GameException {
        

        random.setSeed(seed);

        try {
            this.gameDeck = new GameDeck(gameDeck);
        } catch (GameException e) {
            throw new GameException("GameDeck file not found", null);
        }

        try {
            this.spaceDeck = new SpaceDeck(spaceDeck);
        } catch (GameException e) {
            throw new GameException("SpaceDeck file not found", null);
        }

        this.gameDeck.shuffle(random);
        this.spaceDeck.shuffle(random);
    }


    /**
     * adds a player to the game
     * @param player player name
     * @return number of players added
     */
    public int addPlayer(String player) {
        if (hasStarted) {
            System.out.println("Game already started yet");
            throw new IllegalStateException();
        }
        if (getFullPlayerCount() == 5) {
            System.out.println("already five players");
            throw new IllegalStateException();
        }
        ((LinkedList<Astronaut>)activePlayers).add(new Astronaut(player, this));
        return activePlayers.size();
    }


    /**
     * returns true if game is over
     * @return boolean
     */
    public boolean gameOver() {
        for (Astronaut astronaut : getAllPlayers()) {
            if (astronaut.hasWon() && astronaut.isAlive()) {return true;}
        }
        int i = 0;
        for (Astronaut astronaut : getAllPlayers()) {
            if (!astronaut.isAlive()) {
                i++;
            }
        if (getFullPlayerCount() == i) {return true;}
        }
        return false;
    }


    /**
     * returns all the players in game
     * @return players
     */
    public List<Astronaut> getAllPlayers() {
        List<Astronaut> all = new ArrayList<Astronaut>(); 
        if ((!(currentPlayer == null)) && !(corpses.contains(currentPlayer))) {
            all.add(currentPlayer);
        }
        all.addAll(activePlayers);
        all.addAll(corpses);
        return all;
    }


    /**
     * returns the current player
     * @return player
     */
    public Astronaut getCurrentPlayer() {
        return currentPlayer;
    }


    /**
     * returns the number of players in game
     * @return number of players
     */
    public int getFullPlayerCount() {
        return getAllPlayers().size();
    }


    /**
     * returns the game deck
     * @return game deck
     */
    public GameDeck getGameDeck() {
        return this.gameDeck;
    }


    /**
     * returns the game discard deck
     * @return game discard deck
     */
    public GameDeck getGameDiscard() {
        return this.gameDiscard;
    }


    /**
     * returns the space deck
     * @return space deck
     */
    public SpaceDeck getSpaceDeck() {
        return this.spaceDeck;
    }


    /**
     * returns the space discard deck
     * @return space discard deck
     */
    public SpaceDeck getSpaceDiscard() {
        return this.spaceDiscard;
    }


    /**
     * returns the winner of the game
     * @return winner
     */
    public Astronaut getWinner() {
        for (Astronaut player : getAllPlayers())
            if (player.hasWon() && player.isAlive()) {
                return player;
            }
        return null;
    }


    /**
     * kills the passed player
     * @param corpse player to kill
     */
    public void killPlayer(Astronaut corpse) {
        for (int i=0; i < corpse.oxygenRemaining(); i++) {corpse.breathe();}
        corpse.getActions().clear();
        corpses.add(corpse);
        activePlayers.remove(corpse);
        if (corpse.equals(currentPlayer)) {
            currentPlayer = null;
        }
    }


    /**
     * refills the first deck with the cards from the second deck
     * @param deck1 deck to refill
     * @param deck2 deck to empty
     */
    public void mergeDecks(Deck deck1, Deck deck2) {
        System.out.println("Merging Decks");
        System.out.println("Size of deck1 is " + deck1.size());
        System.out.println("Size of deck2 is " + deck2.size());
        int size = deck2.size();
        for (int i=0; i < size; i++) {
            deck1.add(deck2.draw());
        }
        deck1.shuffle(random);
        System.out.println("Merging done");
    }


    /**
     * saves game
     * @param path name of the save file
     * @throws GameException file not found
     */
    public void saveState(String path) throws GameException {
        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream(path);
        } catch (FileNotFoundException e) {
            throw new GameException("FileNotFoundError", e);
        }

        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close(); 
        } catch (IOException e) {
            throw new GameException("IOException", e);
        }
    }


    /**
     * loads the saved game
     * @param path name of the save file
     * @return game
     * @throws GameException file not found
     * @throws ClassNotFoundException IOException
     */
    public static GameEngine loadState(String path) throws GameException, ClassNotFoundException {
        selfish.GameEngine gameEngine;
        FileInputStream fileIn;
        try {
            fileIn = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            throw new GameException("FileNotFoundError", e);
        }

        ObjectInputStream in;
        try {
            in = new ObjectInputStream(fileIn);
            gameEngine = (GameEngine) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException e) {
            throw new GameException("IOException", e);
        }
        return gameEngine;
    }
    

    /**
     * takes one Oxygen(2) and returns a pair of Oxygen(1)
     * @param dbl Oxygen(2) to take
     * @return pair of Oxygen(1)
     */
    public Oxygen[] splitOxygen(Oxygen dbl) {
        if (dbl.getValue() == 1) {
            throw new IllegalArgumentException();
        }
        int i = 0;
        for (Card element : gameDeck.getCards()) {
            if (element instanceof Oxygen) {
                if (((Oxygen)element).getValue() == 1) {i++;}
            }
        }
        if (i > 1) {
            gameDeck.add(dbl);
            return new Oxygen[] {gameDeck.drawOxygen(1), gameDeck.drawOxygen(1)};
        }
        else if (i == 1) {
            for (Card card : gameDiscard.getCards()) {
                if (card instanceof Oxygen) {
                    if (((Oxygen)card).getValue() == 1) {i++;}
                }
            }
            if (i > 1) {
                gameDeck.add(dbl);
                return new Oxygen[] {gameDeck.drawOxygen(1), gameDiscard.drawOxygen(1)};
            }
        }
        else {
            for (Card card : gameDiscard.getCards()) {
                if (card instanceof Oxygen) {
                    if (((Oxygen)card).getValue() == 1) {i++;}
                }
            }
            if (i > 1) {
                gameDeck.add(dbl);
                return new Oxygen[] {gameDiscard.drawOxygen(1), gameDiscard.drawOxygen(1)};
            }
        }
        throw new IllegalStateException();
    }


    /**
     * starts the game
     */
    public void startGame() {
        if (this.getFullPlayerCount() == 1) {
            throw new IllegalStateException();
        }
        if (this.getFullPlayerCount() == 6) {
            throw new IllegalStateException();
        }
        if (hasStarted) {
            System.out.println("The game has started");
            throw new IllegalStateException();
        }
        for (Astronaut element : activePlayers) {
            element.addToHand(this.gameDeck.drawOxygen(2));
            for (int i=0; i<4; i++) {
                element.addToHand(this.gameDeck.drawOxygen(1));
            }
        }

        for (int i=0; i<4; i++) {
            for (Astronaut element : activePlayers) {
                element.addToHand(this.gameDeck.draw());
            }    
        }
        hasStarted = true;
    }


    /**
     * starts the turn
     */
    public void startTurn() {
        Scanner scanner = new Scanner(System.in);
        if (!hasStarted || gameOver()) {
            scanner.close();
            throw new IllegalStateException();
        }
        if (!(currentPlayer==null)) {
            scanner.close();
            throw new IllegalStateException();
        }
        currentPlayer = ((LinkedList<Astronaut>)activePlayers).poll();
        String playerName = currentPlayer.toString();
        System.out.println(playerName + "'s turn has started");
        if (gameDeck.size() == 0) {
            mergeDecks(gameDeck, gameDiscard);
        }
        currentPlayer.addToHand(gameDeck.draw());
        System.out.println("Your actions cards are: " + currentPlayer.getActionsStr(false, false));
        System.out.print("Do you wish to play a game card? [Y]es or [N]o: ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            System.out.println("Which card would you like to play?");
        }
        scanner.close();
    }


    /**
     * ends the turn
     * @return number of players alive
     */
    public int endTurn() {
        if (currentPlayer != null) {
            String playerName = currentPlayer.toString();
            if (currentPlayer.isAlive()) {
                activePlayers.add(currentPlayer);
                currentPlayer = null;
            }
            else {
                killPlayer(currentPlayer);
            }
            System.out.println(playerName + "'s turn has ended");
        }
        return getFullPlayerCount() - corpses.size();
    }


    /**
     * moves the astronaut forward by one space
     * @param traveller astronaut to move
     * @return space card drawn
     */
    public Card travel(Astronaut traveller) {
        if (traveller.oxygenRemaining() < 2) {
            throw new IllegalStateException();
        }
        traveller.breathe();
        traveller.breathe();
        Card card = spaceDeck.draw();
        if (card.toString().equalsIgnoreCase("GRAVITATIONAL ANOMALY")) {
            spaceDiscard.add(card);
        }
        else {
            traveller.addToTrack(card);
        }
        return card;
    }
}

