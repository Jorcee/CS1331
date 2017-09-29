import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PgnReader {

    /**
     * Find the tagName tag pair in a PGN game and return its value.
     *
     * @see http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm
     *
     * @param tagName the name of the tag whose value you want
     * @param game a `String` containing the PGN text of a chess game
     * @return the value in the named tag pair
     */
    public static String tagValue(String tagName, String game) {
        String toReturn = "NOT GIVEN";
        if (game.contains(tagName)) {
            int startPoint = game.indexOf(tagName);
            int endPoint = game.indexOf("]", startPoint);
            toReturn = game.substring(startPoint, endPoint);
            startPoint = toReturn.indexOf("\"");
            endPoint = toReturn.lastIndexOf("\"");
            toReturn = toReturn.substring(++startPoint, endPoint);
        }
        return toReturn;
    }

    /**
     * Play out the moves in game and return a String with the game's
     * final position in Forsyth-Edwards Notation (FEN).
     *
     * @see http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm#c16.1
     *
     * @param game a `Strring` containing a PGN-formatted chess game or opening
     * @return the game's final position in FEN.
     */
    public static String finalPosition(String game) {
        char[][] board = getBoard();
        String moveList = getMoveList(game);
        String currentMove = "";
        int startIndex = 0;
        int endIndex = 0;
        do {
            startIndex = moveList.indexOf('.', startIndex) + 1;
            startIndex = (startIndex == -1) ? moveList.length() : startIndex;
            endIndex = moveList.indexOf('.', startIndex) - 1;
            endIndex = (endIndex <=  -1) ? moveList.length() : endIndex;
            currentMove = getCurrentMove(moveList, startIndex, endIndex);
            board = makeMove(board, currentMove);
	    System.out.println(currentMove);
        } while (endIndex < moveList.length());
        for (char[] meh : board) {
            for (char j : meh) {
                System.out.print(" " + j);
            }
            System.out.print("\n");
        }
        return currentMove;
    }
    /**
     *Create the character array of the board
     *load in the starting piece positions
     */
    public static char[][] getBoard() {
        char[][] board = {{'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'},
                          {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
                          {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                          {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                          {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                          {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                          {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
                          {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'}};
        return board;
    }
    /**
     *Return a String with just the moves
     */
    public static String getMoveList(String game) {
        int startPoint = game.lastIndexOf(']');
        String toReturn = game.substring(++startPoint, game.length());
        toReturn = toReturn.trim();
        return toReturn;
    }


    /**
     *Get the Current Move
     */
    public static String getCurrentMove(String moveList, int start, int end) {
        String currentMove;
        if (end <= moveList.length()) {
            currentMove = moveList.substring(start, end);
            currentMove = currentMove.trim();
        } else {
            currentMove = "";
        }
        return currentMove;
    }

    /**
     *Make Changes to the board
     */
    public static char[][] makeMove(char[][] oldBoard, String move) {
        String[] moves  = move.split(" ");
        int row = getRow(moves[0]);
        int column = getColumn(moves[0]);
        char piece = getPiece(moves[0], true);
        char[][] newBoard = movePiece(oldBoard, row, column, piece);
        if (moves.length > 1) {
            row = getRow(moves[1]);
            column = getColumn(moves[1]);
            piece = getPiece(moves[1], false);
	    piece += 32;
            newBoard = movePiece(oldBoard, row, column, piece);
        }
        return newBoard;
    }

    /**
     *Copy moving piece into new spot
     */
    public static char[][] movePiece(char[][] oldBoard, int r, int c, char p) {
        char[][] newBoard = oldBoard;
        if (p == 'P' || p == 'p') {
            newBoard = movePawn(oldBoard, r, c, p);
        } else if (p == 'R' || p == 'r') {
            newBoard = moveRook(oldBoard, r, c, p);
        } else if(p == 'N' || p == 'n') {
	    newBoard = moveKnight(oldBoard, r, c, p);
	}
        return newBoard;
    }

    /**
     *Move a Pawn
     */
    public static char[][] movePawn(char[][] board, int r, int c, char p) {
        char[][] newBoard = board;
        newBoard[r][c] = p;
        if (r == 4  && p == 'P') {
            newBoard[6][c] = ' ';
        } else if (r == 3 && p == 'p') {
            newBoard[1][c] = ' ';
        } else if (p == 'P') {
            newBoard[r + 1][c] = ' ';
        } else {
            newBoard[r - 1][c] = ' ';
        }
        return newBoard;
    }
    /**
     *Move a Rook
     */
    public static char[][] moveRook(char[][] board, int r, int c, char p) {
	char[][] newBoard = board;
	boolean done = false;
	for (int i = r; i < 8; i++) {
	    if (newBoard[i][c] != ' ' || done) {
		if (newBoard[i][c] == p) {
		    newBoard[i][c] = ' ';
		    done = true;
		}
		break;
	    }
	}
       	for (int i = r; i > 0; i--) {
	    if (newBoard[i][c] != ' ' || done) {
		if (newBoard[i][c] == p) {
		    newBoard[i][c] = ' ';
		    done = true;
		}
		break;
	    }
	}
       	for (int i = c; i < 8; i++) {
	    if (newBoard[r][i] != ' ' || done) {
		if (newBoard[r][i] == p) {
		    newBoard[r][i] = ' ';
		    done = true;
		}
		break;
	    }
	}
	for (int i = c; i > 0; i--) {
	    if (newBoard[r][i] != ' ') {
		if (newBoard[r][i] == p || done) {
		    newBoard[r][i] = ' ';
		    done = true;
		}
		break;
	    }
	}
	newBoard[r][c] = p;
	return newBoard;
    }
    /**
     * Move a Knight
     */
    public static char[][] moveKnight(char[][] b, int r, int c, char p) {
	char[][] newBoard = b;
	if (r - 2 >= 0 && c - 1 >= 0 && newBoard[r - 2][c - 1] == p) {
	    newBoard[r - 2][c - 1] = ' ';
	} else if (r - 1 >= 0 && c - 2 >= 0 && newBoard[r - 1][c - 2] == p) {
	    newBoard[r - 1][c - 2] = ' ';
	} else if (r - 2 >= 0 && c + 1 <= 7 && newBoard[r - 2][c + 1] == p) {
	    newBoard[r - 2][c + 1] = ' ';
	} else if (r - 1 >= 0 && c + 2 <= 7 && newBoard[r - 1][c + 2] == p) {
	    newBoard[r - 1][c + 2] = ' ';
	} else if (r + 1 <= 7 && c + 2 <= 7 && newBoard[r + 1][c + 2] == p) {
	    newBoard[r +1 ][c + 2] = ' ';
	} else if (r + 2 <= 7 && c + 1 <= 7 && newBoard[r + 2][c + 1] == p) {
	    newBoard[r + 2][c + 1] = ' ';
	} else if (r + 1 <= 7 && c - 2 >= 0 && newBoard[r + 1][c + 2] == p) {
	    newBoard[r + 1][c + 2] = ' ';
	} else if (r + 2 <= 7 && c - 1 >= 0 && newBoard[r + 2][c - 1] == p) {
	    newBoard[r + 2][c - 1] = ' ';
	}
	newBoard[r][c] = p;
	return newBoard;
    }
    
     /**
     *Get the Column of the move
     */
    public static int getColumn(String move) {
        char[] elements = move.toCharArray();
        int column = 0;
        for (char element : elements) {
            if (element > 96 && element <= 104) {
                column = element - 97;
            }
        }
        return column;
    }

    /**
     *Get the Piece of the move
     */
    public static char getPiece(String move, boolean whiteTurn) {
        char[] elements = move.toCharArray();
        char piece = ' ';
        for (char element : elements) {
            if (element > 65 && element < 97) {
                piece = element;
            } else if (element > 104 && element < 122) {
                piece = element;
            }
        }
        if (piece == ' ') {
            if (whiteTurn) {
                piece = 'P';
            } else {
                piece = 'p';
            }
        }
        return piece;
    }
    /**
     * Get the Row of the Move
     */
    public static int getRow(String move) {
        char[] elements = move.toCharArray();
        int row = 0;
        for (char element : elements) {
            if (element > 48 && element <= 56) {
                row = element - 48;
            }
        }
        return row;
    }
    /**
     * Reads the file named by path and returns its content as a String.
     *
     * @param path the relative or abolute path of the file to read
     * @return a String containing the content of the file
     */
    public static String fileContent(String path) {
        Path file = Paths.get(path);
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                // Add the \n that's removed by readline()
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
            System.exit(1);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String game = fileContent(args[0]);
        System.out.format("Event: %s%n", tagValue("Event", game));
        System.out.format("Site: %s%n", tagValue("Site", game));
        System.out.format("Date: %s%n", tagValue("Date", game));
        System.out.format("Round: %s%n", tagValue("Round", game));
        System.out.format("White: %s%n", tagValue("White", game));
        System.out.format("Black: %s%n", tagValue("Black", game));
        System.out.format("Result: %s%n", tagValue("Result", game));
        System.out.println("Final Position:");
        System.out.println(finalPosition(game));

    }
}
