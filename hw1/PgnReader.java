import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
public class PgnReader {
    private static char[][] board = {{'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'},
                                     {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
                                     {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                                     {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                                     {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                                     {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                                     {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
                                     {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'}};
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
        String[] moves = separateMoves(game);
        int[][] coordinatesOfPieces;
        int[] coordinatesOfPiece;
        int[] coordsOfMove = new int[2];
        char piece = ' ';
        String finalInFEN = "";
        int spaceCounter = 0;
        for (int i = 0; i < moves.length; i++) {
	    //Debug 3
	    System.out.println("Move: " + moves[i]);
	    if (isSpecialMove(moves[i])) {
		doSpecialMove(moves[i], i);
	    } else {
		piece = decidePiece(moves[i], i);
		coordsOfMove = moveCoords(moves[i]);
		coordinatesOfPieces = findPieces(piece);
		coordinatesOfPiece = choosePiece(piece, coordinatesOfPieces,
						 coordsOfMove, moves[i], i);
	    //Debug Code
            System.out.println(moves[i]);
            System.out.println(coordsOfMove[0] + ", " + coordsOfMove[1] +
            " Move");
            for (int j[] : coordinatesOfPieces) {
                System.out.println(j[0] + ", " + j[1]);
            }
            System.out.println(piece);
            System.out.println(coordinatesOfPiece[0] + " " +
            coordinatesOfPiece[1]);
            //Debug End
		board[coordsOfMove[0]][coordsOfMove[1]] = piece;
		board[coordinatesOfPiece[0]][coordinatesOfPiece[1]] = ' ';
		finalInFEN = "";
            //Debug Code pt2
            /*
            for (char[] k : board) {
                for(char j : k) {
                    if (j == ' ') {
                    spaceCounter++;
                    } else {
                        if ( spaceCounter > 0) {
                            finalInFEN += spaceCounter + Character.toString(j);
                            spaceCounter = 0;
                        } else {
                            finalInFEN += Character.toString(j);
                        }
                    }
                }
                finalInFEN += (spaceCounter > 0) ? spaceCounter + "/" : "/";
                spaceCounter = 0;
            }
            System.out.println(finalInFEN);*/
	    }
	    for (char[] a : board) {
		System.out.println();
		for(char b : a) {
		    System.out.print(b + " ");
		}
	    }
	    System.out.println("\n----------------");
        }
        finalInFEN = "";
        for (char[] i : board) {
            for (char j : i) {
                if (j == ' ') {
                    spaceCounter++;
                } else {
                    if (spaceCounter > 0) {
                        finalInFEN += spaceCounter + Character.toString(j);
                        spaceCounter = 0;
                    } else {
                        finalInFEN += Character.toString(j);
                    }
                }
            }
            finalInFEN += (spaceCounter > 0) ? spaceCounter + "/" : "/";
            spaceCounter = 0;
        }
        finalInFEN = finalInFEN.substring(0, (finalInFEN.length() - 1));
        return finalInFEN;
    }
    /**
     *Decide if the move is a special case
     */
    public static boolean isSpecialMove(String move) {
	if (move.equals("O-O-O")) {
	    return true;
	}
	if (move.contains("#") || move.contains("+")) {
	    return false;
	}
	if (move.length() >= 4) {
	    if (move.charAt(1) == 'x') {
		return false;
	    }
	    if (move.charAt(1) > 96 && move.charAt(1) < 105) {
		return true;
	    }
	}
	return false;
    }
    /**
     *Handle special moves
     */
    public static void doSpecialMove(String move, int turn) {
	char piece = ' ';
	if (move.equals("O-O-O")) {
	    if ( turn % 2 == 0) {
		board[7][2] = 'K';
		board[7][3] = 'R';
		board[7][0] = ' ';
		board[7][4] = ' ';
	    } else {
		board[0][2] = 'k';
		board[0][3] = 'r';
		board[0][0] = ' ';
		board[0][4] = ' ';
	    }
	} else if (move.equals("O-O")) {
	    	    if ( turn % 2 == 0) {
		board[7][6] = 'K';
		board[7][5] = 'R';
		board[7][7] = ' ';
		board[7][4] = ' ';
	    } else {
		board[0][6] = 'k';
		board[0][5] = 'r';
		board[0][7] = ' ';
		board[0][4] = ' ';
	    }
	} else if (move.length() >= 4)/*Handle rank ambiguities*/ {
	    if (move.charAt(1) > 96 && move.charAt(0) < 105) {
		piece = decidePiece(move, turn);
		int[] coordsOfMove = moveCoords(move);
		int[][] coordsOfPieces = findPieces(piece);
		char rank = move.charAt(1);
		rank -= 97;
		for (int i = 0; i < coordsOfPieces.length; i++) {
		    if (coordsOfPieces[i][1] == rank) {
			board[coordsOfMove[0]][coordsOfMove[1]] = piece;
			board[i][rank] = ' ';
		    }
		}
	    }
	}
    }
    /**
     *Find the coordinates of the one piece that
     *can legally make the move that is taking place
     *where piece is the type of piece to be moved
     *and coords is an array of all the corrdinates
     *of pieces of that type
     *and moveCoords is the coordinates to move to
     *and move is the String of the Move
     *and moveNum is the turn number
     */
    public static int[] choosePiece(char piece, int[][] coords,
                                    int[] moveCoords, String move,
                                    int moveNum) {
        int[] coordsOfAble = {6, 4};
        if (piece == 'P' || piece == 'p') {
            for (int[] i : coords) {
                coordsOfAble = (canPawnMove(i[0], i[1], moveCoords[0],
                                            moveCoords[1],
                                            moveNum,
                                            move.contains("x"))) ? i
                    : coordsOfAble;
            }
        } else if (piece == 'N' || piece == 'n') {
            for (int[] i : coords) {
                coordsOfAble = (canKnightMove(i[0], i[1],
                                              moveCoords[0],
                                              moveCoords[1])) ? i
                    : coordsOfAble;
            }
        } else if (piece == 'B' || piece == 'b') {
            for (int[] i : coords) {
                coordsOfAble = (canBishopMove(i[0], i[1],
                                              moveCoords[0],
                                              moveCoords[1])) ? i
                    : coordsOfAble;
            }
        } else if (piece == 'R' || piece == 'r') {
            for (int[] i : coords) {
                coordsOfAble = (canRookMove(i[0], i[1],
                                            moveCoords[0],
                                            moveCoords[1])) ? i
                    : coordsOfAble;
            }
        } else if (piece == 'Q' || piece == 'q') {
            for (int[] i : coords) {
                coordsOfAble = (canQueenMove(i[0], i[1],
                                             moveCoords[0],
                                             moveCoords[1])) ? i
                    :  coordsOfAble;
            }
        } else if (piece == 'K' || piece == 'k') {
            for (int[] i : coords) {
                coordsOfAble = (canKingMove(i[0], i[1],
                                            moveCoords[0],
                                            moveCoords[1])) ? i
                    :  coordsOfAble;
            }
        }
        return coordsOfAble;
    }
    /**
     *Parse the coordinates of the move to be made
     *where move is the string of the move
     */
    public static int[] moveCoords(String move) {
        int[] coords = {0, 0};
        char[] charsOfMove = move.toCharArray();
        for (char i : charsOfMove) {
            if (i > 96 && i < 105) {
                coords[1] = i - 97;
            } else if (i > 48 && i < 57) {
                coords[0] = i - 49;
                coords[0] = 7 - coords[0];
            }
        }
        return coords;
    }
    /**
     *find all pieces of the type to be moved
     *where piece is the type of piece to find
     *return the coordinates of the pieces
     */
    public static int[][] findPieces(char piece) {
        int[][] coordinates = new int[8][2];
        int coordCounter = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == piece) {
                    coordinates[coordCounter][0] = i;
                    coordinates[coordCounter][1] = j;
                    coordCounter++;
                }
            }
        }
        int[][] tempCoordinates = coordinates;
        coordinates = new int[coordCounter][2];
        for (int i = 0; i < coordCounter; i++) {
            coordinates[i] = tempCoordinates[i];
        }
        return coordinates;
    }
    /**
     *Decide which piece is being moved
     * where move is the move to be checked
     * and turn is the turn number(index in moves[])
     */
    public static char decidePiece(String move, int turn) {
        /*if (move.equals(" ")) {
            return ' ';
            }*/
        char pulledPiece = move.charAt(0);
	if (move.length() > 3 && pulledPiece > 96 && pulledPiece < 105) {
	    pulledPiece = move.charAt(1);
	}
        char piece;
        if (pulledPiece == 'N') {
            if (turn % 2 == 0) {
                piece = 'N';
            } else {
                piece = 'n';
            }
        } else if (pulledPiece == 'B') {
            if (turn % 2 == 0) {
                piece = 'B';
            } else {
                piece = 'b';
            }
        } else if (pulledPiece == 'Q') {
            if (turn % 2 == 0) {
                piece = 'Q';
            } else {
                piece = 'q';
            }
        } else if (pulledPiece == 'R') {
            if (turn % 2 == 0) {
                piece = 'R';
            } else {
                piece = 'r';
            }
        } else if (pulledPiece == 'K') {
            if (turn % 2 == 0) {
                piece = 'K';
            } else {
                piece = 'k';
            }
        } else {
            if (turn % 2 == 0) {
                piece = 'P';
            } else {
                piece = 'p';
            }
        }
        return piece;
    }

    /**
     *Break up the string of all moves into an array where
     *each spot is a separate move
     */
    public static String[] separateMoves(String game) {
        String listOfMoves;
        int startIndex = 0;
        int endIndex = 0;
        int numOfMoves;
        String tempMove;
	game = game.replace("\n"," ");
        startIndex = game.lastIndexOf(']');
        startIndex++;
        endIndex = game.length();
        listOfMoves = game.substring(startIndex, endIndex);
        listOfMoves = listOfMoves.trim();
        //listOfMoves now contains only the numbered list of moves
        endIndex = listOfMoves.lastIndexOf('.');
        startIndex = endIndex - 2;
        numOfMoves = Integer.parseInt(listOfMoves.substring(startIndex,
                                                            endIndex).trim());
        //numOfMoves reads the number of the last move in the file;
        //How many move sets there are
        String[] moves = new String[numOfMoves * 2];
        String[] tempMoves;
        startIndex = 0;
        endIndex = 0;
        for (int i = 0; i < moves.length; i += 2) {
            startIndex = listOfMoves.indexOf('.', startIndex) + 1;
            startIndex = (startIndex < 0) ? listOfMoves.length() : startIndex;
            endIndex = listOfMoves.indexOf('.', startIndex) - 1;
            endIndex = (endIndex < 0) ? listOfMoves.length() : endIndex;
            tempMove = listOfMoves.substring(startIndex, endIndex);
            tempMove = tempMove.trim();
            if (tempMove.contains(" ")) {
                moves[i] = tempMove.split(" ")[0];
                moves[i + 1] = tempMove.split(" ")[1];
            } else {
                moves[i] = tempMove;
            }
        }
        tempMoves = new String[moves.length - 1];
        if (moves[moves.length - 1] == null) {
            for (int a = 0; a < tempMoves.length; a++) {
                tempMoves[a] = moves[a];
            }
            moves = new String[tempMoves.length];
            moves = tempMoves;
        }
        //each cell of moves is now filled with a single move and is
        //ordered sequentially
        return moves;
    }
    /**
     *Check to see if a pawn can move to the provided spot
     * r is start row, c is start column
     *mr is move row, mc is move column, t is turn, a is attack
     */
    public static boolean canPawnMove(int r, int c, int mr, int mc,
                                      int t, boolean a) {
        int multiplier = (t % 2 == 0) ? 1 : -1;
        boolean canMove = false;
        if (a) {
            canMove = ((r - mr) * multiplier == 1) ? true : canMove;
            canMove = (Math.abs(c - mc) == 1) ? canMove : false;
        } else {
            canMove = ((r - mr) * multiplier == 2) ? true : canMove;
            canMove = ((r - mr) * multiplier == 1) ? true : canMove;
            canMove = (c == mc) ? canMove : false;
        }
        return canMove;
    }

    /**
     *Check to see if a Knight can move to the provided spot
     *r  is start row, c is start column
     *mr is move row, mc is move column
     */
    public static boolean canKnightMove(int r, int c, int mr, int mc) {
        boolean canMove = false;
        if (Math.abs(r - mr) == 2) {
            canMove = (Math.abs(c - mc) == 1) ? true : false;
        } else if (Math.abs(r - mr) == 1) {
            canMove = (Math.abs(c - mc) == 2) ? true : false;
        }
        return canMove;
    }

    /**
     *Check to see if a Queen can move to the provided spot
     *r is start row, c is start column
     *mr is move row, mc is move column
     */
    public static boolean canQueenMove(int r, int c, int mr, int mc) {
        boolean canMove = false;
        canMove = (canBishopMove(r, c, mr, mc));
        canMove = (canRookMove(r, c, mr, mc)) ? true : canMove;
        return canMove;
    }

    /**
     *Check to see if a Bishop can move to the provided spot
     * r is start row, c is start column
     *mr is move row, mc is move column
     */
    public static boolean canBishopMove(int r, int c, int mr, int mc) {
        boolean canMove = false;
        for (int i = 1; i < 8; i++) {
            canMove = (r + i == mr && c + i == mc) ? true : canMove;
            canMove = (r + i == mr && c - i == mc) ? true : canMove;
            canMove = (r - i == mr && c + i == mc) ? true : canMove;
            canMove = (r - i == mr && c - i == mc) ? true : canMove;
        }
        return canMove;
    }

    /**
     *Check to see if a Rook can move to the provided spot
     * r is start row, c is start column
     *mr is move row, mc is move column
     */
    public static boolean canRookMove(int r, int c, int mr, int mc) {
        boolean canMove = false;
        char[] rowArray;
	char[] colArray;
	if (r == mr) {
	    if(Math.abs(c - mc) == 1) {
		return true;
	    }
	    if (c > mc) {
		colArray = Arrays.copyOfRange(board[r], mc + 1, c);
	    } else {
		colArray = Arrays.copyOfRange(board[r], c + 1, mc);
	    }
	    canMove = true;
	    for (char i : colArray) {
		if (i != ' ') {
		    canMove = false;
		}
	    }
	}
	if (c == mc) {
	    if(Math.abs(r - mr) == 1) {
		return true;
	    }
	    if (r > mr) {
		rowArray = new char[r - mr - 1];
		int counter = 0;
		for(int i = mr + 1; i < r; i++) {
		    rowArray[counter++] = board[i][c];
		}
	    } else {
		rowArray = new char[mr - r - 1];
		int counter = 0;
		for(int i = r + 1; i < mr; i++) {
		    rowArray[counter++] = board[i][c];
		}
	    }
	    canMove = true;
	    for (char i : rowArray) {
		if (i != ' ') {
		    canMove = false;
		}
	    }
	}
        return canMove;
    }

    /**
     *Check to see if a King can move to the provided spot
     *r is start row, c is start column
     *mr is move row, mc is move column
     *Note: this method is extraneous as there is only one
     *King provided to each side, however I am including it
     *for reasons
     */
    public static boolean canKingMove(int r, int c, int mr, int mc) {
        boolean canMove = false;
        canMove = (Math.abs(r - mr) == 1) ? true : canMove;
        canMove = (Math.abs(c - mc) == 1) ? true : canMove;
        return canMove;
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
