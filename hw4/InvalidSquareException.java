/**
 *Is thrown when a square does not exist on a board
 *Un-checked exception:
 *At runtime we want to make sure the squares inputted by the user,
 *or returned by MovesFrom are valid squares
 * and allow for re entry due to mistypes
 *@author Jordan Goldstein
 */
public class InvalidSquareException extends RuntimeException {
    public InvalidSquareException(String msg) {
        super(msg);
    }
}
