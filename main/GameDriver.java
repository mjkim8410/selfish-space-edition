import java.io.IOException;
import java.util.Scanner;
import selfish.Astronaut;
import selfish.GameEngine;
import selfish.GameException;


/**
 * Class GameDriver
 * @author Minjun Kim
 * @version 1.0
 */
public class GameDriver {

    /**
     * A helper function to centre text in a longer String.
     * @param width The length of the return String.
     * @param s The text to centre.
     * @return A longer string with the specified text centred.
     */
    public static String centreString (int width, String s) {
        return String.format("%-" + width  + "s", String.format("%" + (s.length() + (width - s.length()) / 2) + "s", s));
    }


    /**
     * an empty constructor
     */
    public GameDriver() {}

    public static void main(String[] args) throws GameException, IOException, ClassNotFoundException  {

        Scanner scanner = new Scanner(System.in);

        // Instansiating the game engine.
        String gPath = "/home/minjun/git-repository/comp16412-coursework-2__p02428mk/io/ActionCards.txt";
        String sPath = "/home/minjun/git-repository/comp16412-coursework-2__p02428mk/io/SpaceCards.txt";

        System.out.print("Would you like to load previously saved game? [Y]es or [N]o: ");
        if (scanner.nextLine().equalsIgnoreCase("y")) {
            GameEngine.loadState("gameDeckSave.ser");
        } 
        else {
            GameEngine gameEngine = new selfish.GameEngine(16412, gPath, sPath);

            // This loop generates from 2 to 5 players.
            for (int i=0; i<9; i++) {
                if (gameEngine.getAllPlayers().size() >= 2) {
                    System.out.print("Add another player? [Y]es or [N]o: ");
                    if (!(scanner.nextLine().equalsIgnoreCase("y"))) {break;}
                    else {;}
                }
                System.out.print("Enter player name: ");
                String name = scanner.nextLine(); 
                gameEngine.addPlayer(name);
                System.out.println(gameEngine.getAllPlayers().size() + " player(s) added.");
            }

            String gameOpening = "After a dazzling (but doomed) space mission, the astronauts are floating in space and their Oxygen supplies are running low.\nOnly the first back to the ship will survive!";
            System.out.println(gameOpening);
            System.out.println("Their names are:");
            for (Astronaut element : gameEngine.getAllPlayers()) {
                System.out.println(element.toString());
            }
            
            System.out.println("Total of " + gameEngine.getAllPlayers().size() + " players added.");

            gameEngine.startGame();

            while (!gameEngine.gameOver()) {
                gameEngine.startTurn();
                gameEngine.endTurn();
            }
            
        }
        scanner.close();
    }
}  
   

        



