public class Square {
    private char rank;
    private char file;
    public Square(char f, char r) {
        this.rank = r;
        this.file = f;
        if (r > '8' || r < '1') {
            throw new InvalidSquareException("" + file + rank);
        } else if (f > 'h' || f  < 'a') {
            throw new InvalidSquareException("" + file + rank);
        }
    }
    public Square(String name) {
        this(name.toCharArray()[0], name.toCharArray()[1]);
        if (name.length() != 2) {
            throw new InvalidSquareException(name);
        }
    }
    public String toString() {
        return "" + getFile() + getRank();
    }
    //Return the rank of the square
    public char getRank() {
        return this.rank;
    }
    //Return the file of the square
    public char getFile() {
        return this.file;
    }
    @Override
    public boolean equals(Object otherSquare) {
	if (otherSquare == null) {
	    return false;
	}
	if (!(otherSquare instanceof Square)) {
	    return false;
	}
	if (otherSquare == this) {
	    return true;
	}
	if (otherSquare.toString().equals(this.toString())) {
	    return true;
	}
	if (otherSquare.hashCode() == this.hashCode()) {
	    return true;
	}
	return false;
    }
    @Override
    public int hashCode() {
	String code = "" + rank + (int) file;
	return Integer.parseInt(code);
    }
}
