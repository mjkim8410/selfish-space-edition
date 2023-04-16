package selfish;

/**
 * Class GameException custom exception
 * 
 * @author Minjun Kim
 * @version 1.0
 */
public class GameException extends Exception {

    /**
     * constructor of GameException
     * 
     * @param msg message to display
     * @param e   e
     */
    public GameException(String msg, Throwable e) {
        super(msg, e);
        System.out.println("Game exception was thrown.");
    }
}
