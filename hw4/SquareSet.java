import java.util.Set;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
/**
 *Represents a Set of Squares
 *@author Jordan Goldstein
 *@version 1.0.0
 */
public class SquareSet implements Set<Square> {
    private class SquareSetIterator implements Iterator<Square> {
        private int cursor = 0;
        public boolean hasNext() {
            return cursor < usedSpace;
        }
        public Square next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Square toReturn = get(cursor++);
            return toReturn;
        }
    }
    private Square[] squaresArray;
    private int usedSpace = 0;
    /**
     *Creates an empty SquareSet with 5 open spots
     */
    public SquareSet() {
        squaresArray = new Square[5];
    }
    /**
     *Creates a SquareSet filled with provided squares
     *@param squares the squares to be added
     */
    public SquareSet(Collection<Square> squares) {
        if (squares.size() == 0) {
            squaresArray = new Square[0];
        } else {
            Square[] temp = squares.toArray(new Square[squares.size()]);
            int size = squares.size();
            squaresArray = temp;
            for (int i = 0; i < size; i++) {
                for (int j = i + 1; j < size; j++) {
                    if (squaresArray[i] != null) {
                        if (squaresArray[i].equals(squaresArray[j])) {
                            squaresArray[j] = null;
                        }
                    }
                }
                squaresArray[i] = (Square) temp[i];
            }
        }
        for (int i = 0; i < squaresArray.length; i++) {
            if (squaresArray[i] != null) {
                usedSpace++;
            }
        }
        Square[] temp = new Square[usedSpace];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = squaresArray[i];
        }
        squaresArray = temp;
    }
    /**
     *Adds a square to SquareSet if it is valid
     *@param square is the square to be added
     *@return whether or not the square was added
     *@throws NullPointerException
     *@throws InvalidSquareException
     */
    public boolean add(Square square) {
        if (square == null) {
            throw new NullPointerException();
        }
        char rank = square.getRank();
        char file = square.getFile();
        if (rank > '8' || rank < '1') {
            throw new InvalidSquareException("" + file + rank);
        } else if (file > 'h' || file  < 'a') {
            throw new InvalidSquareException("" + file + rank);
        }
        if (this.contains(square)) {
            return false;
        }
        if (usedSpace >= squaresArray.length) {
            Square[] temp = new Square[usedSpace * 2 + 5];
            for (int i = 0; i < usedSpace; i++) {
                temp[i] = this.get(i);
            }
            for (int i = usedSpace; i < temp.length; i++) {
                temp[i] = null;
            }
            squaresArray = new Square[usedSpace * 2 + 5];
            for (int i = 0; i < this.size(); i++) {
                squaresArray[i] = temp[i];
            }
        }
        squaresArray[usedSpace++] = square;
        return true;
    }
    /**
     *Add a collection of squares to the Set if all of
     *them are valid
     *@param squares the collection of squares to be added
     *@return whether the squares were added
     */
    public boolean addAll(Collection<? extends Square> squares) {
        char rank;
        char file;
        boolean addStuff = true;
        if (squares == null) {
            throw new NullPointerException();
        }
        if (this.equals(squares)) {
            return false;
        }
        if (this.containsAll(squares)) {
            return false;
        }
        for (Square a : squares) {
            if (a == null) {
                throw new NullPointerException();
            }
            rank = a.getRank();
            file = a.getFile();
            if (rank > '8' || rank < '1') {
                throw new InvalidSquareException("" + file + rank);
            } else if (file > 'h' || file  < 'a') {
                throw new InvalidSquareException("" + file + rank);
            }
        }
        for (Square a : squares) {
            this.add(a);
        }
        return true;
    }
    /**
     *replace all Squares in the Set with null
     *Not implemented
     *@throws UnsupportedOperationException
     */
    public void clear() {
        throw new UnsupportedOperationException();
    }
    /**
     *Checks to see if the set already contains a Square
     *@param square the object to be checked
     *@return whether or not the set contains a specified object
     *@throws ClassCastException
     *@throws NullPoint
     */
    public boolean contains(Object square) {
        if (square == null) {
            throw new NullPointerException();
        }
        if (!(square instanceof Square)) {
            throw new ClassCastException();
        }
        for (int i = 0; i < usedSpace; i++) {
            if (((Square) square).equals(squaresArray[i])) {
                return true;
            }
        }
        return false;
    }
    /**
     *Checks whether the set contains all objects provided
     *and that all objects provided are squares
     *@param squares the collection of objects to be checked
     *@return if all objects are squares in the set
     *@throws ClassCastException
     *@throws NullPointerException
     */
    public boolean containsAll(Collection<?> squares) {
        boolean toReturn = true;
        for (Object square : squares) {
            if (!(square instanceof Square)) {
                return false;
                //throw new ClassCastException();
            }
            if (square == null) {
                throw new NullPointerException();
            }
            if (!this.contains(square)) {
                toReturn = false;
            }
        }
        return toReturn;
    }
    /**
     *Checks if this set equals another
     *must be a SquareSet, not null, contain all the
     *same squares, and have the same size
     *@param squareSet the set to be checked
     *@return whether the sets are equal
     */
    @SuppressWarnings("unchecked")
    public boolean equals(Object squareSet) {
        if (squareSet == null) {
            return false;
        }
        if (squareSet == this) {
            return true;
        }
        if (squareSet instanceof Set) {
            Set<Square> set = (Set) squareSet;
            if (set.size() == this.size()) {
                if (this.containsAll(set)) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     *Get the square at the specifed index
     *@param index the index of the desired square
     *@return the requested square
     *@throws IndexOutOfBoundsException
     */
    public Square get(int index) {
        if (index >= squaresArray.length) {
            throw new IndexOutOfBoundsException();
        }
        return squaresArray[index];
    }
    /**
     *Get the hashCode of the Set
     *@return the set's hash code
     */
    public int hashCode() {
        int theCode = 0;
        for (int i = 0; i < usedSpace; i++) {
            theCode += squaresArray[i].hashCode();
        }
        return theCode;
    }
    /**
     *Checks whether the set is empty
     *@return whether the set is empty
     */
    public boolean isEmpty() {
        boolean toReturn = true;
        for (int i = 0; i < squaresArray.length; i++) {
            if (squaresArray[i] != null) {
                toReturn = false;
            }
        }
        return toReturn;
    }
    /**
     *Returns an iterator over the set
     *@return an iterator over the set
     */
    public Iterator<Square> iterator() {
        return new SquareSetIterator();
    }
    /**
     *Removes an object from the set
     *@param square the object to be removed
     *@return whether the object was removed
     */
    public boolean remove(Object square) {
        if (!(square instanceof Square)) {
            return false;
        }
        if (!this.contains(square)) {
            return false;
        }
        for (int i = 0; i < usedSpace; i++) {
            if (this.get(i).equals(square)) {
                squaresArray[i] = null;
                for (int j = i; j < usedSpace; j++) {
                    squaresArray[j] = squaresArray[j + 1];
                }
                usedSpace--;
                return true;
            }
        }
        return false;
    }
    /**
     *Remove all objects given in a collection
     *from the set
     *@param squares the collection of objects to be removed
     *@return whether the objects were removed
     */
    public boolean removeAll(Collection<?> squares) {
        throw new UnsupportedOperationException();
    }
    /**
     *Remove all objects from the set except those
     *provided in a collection
     *@param squares a collection of objects not to be removed
     *@return whether the objects were retained
     */
    public boolean retainAll(Collection<?> squares) {
        throw new UnsupportedOperationException();
    }
    /**
     *Return the amount of elements in the set
     *@return the amount of elements in the set
     */
    public int size() {
        return usedSpace;
    }
    /**
     *Return an array of all the elements of the set
     *@return an array of the set's elements
     */
    public Object[] toArray() {
        Object[] toReturn = new Object[usedSpace];
        for (int i = 0; i < usedSpace; i++) {
            toReturn[i] = squaresArray[i];
        }
        return toReturn;
    }
    /**
     *Return an array of specified type containing
     *All the elements in the set
     *@param squares an array of type T to be filled if big enough
     otherwise a new array is made
     *@return an array containing all the set's elements
     *@throws ArrayStoreException
     *@throws NullPointerException
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] squares) {
        if (squares == null) {
            throw new NullPointerException();
        }
        if (squares instanceof Object[]) {
            if (squares.length >= usedSpace) {
                for (int i  = 0; i < usedSpace; i++) {
                    squares[i] = (T) this.get(i);
                }
                for (int i  = usedSpace; i < squares.length; i++) {
                    squares[i] = null;
                }
                System.out.println(squares[0]);
                return squares;
            } else {
                Square[] squaress = new Square[usedSpace];
                for (int i = 0; i < usedSpace; i++) {
                    squaress[i] = this.get(i);
                }
                return (T[]) squaress;
            }
        } else {
            throw new ArrayStoreException();
        }
    }
}
