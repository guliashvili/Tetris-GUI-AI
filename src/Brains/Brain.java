// Brain.java -- the interface for Tetris brains

package Brains;

import core.BoardI;
import core.Piece;

public abstract class Brain{
    // Move is used as a struct to store a single Move
    // ("static" here means it does not have a pointer to an
    // enclosing Brain object, it's just in the Brain namespace.)
    public static class Move {
        public int x;
        public int y;
        public Piece piece;
        public double score;    // lower scores are better
    }
    
    /**
     Given a piece and a board, returns a move object that represents
     the best play for that piece, or returns null if no play is possible.
     The board should be in the committed state when this is called.
     
     limitHeight is the height of the lower part of the board that pieces
     must be inside when they land for the game to keep going
      -- typically 20 (i.e. board.getHeight() - 4)
     If the passed in move is non-null, it is used to hold the result
     (just to save the memory allocation).
    */
    public    Brain.Move bestMove(BoardI board, Piece piece, int limitHeight, Brain.Move move){
        // Allocate a move object if necessary
        if (move==null) move = new Brain.Move();
        
        double bestScore = 1e20;
        int bestX = 0;
        int bestY = 0;
        Piece bestPiece = null;
        Piece current = piece;
        
        board.commit();
        
        // loop through all the rotations
        while (true) {
            final int yBound = limitHeight - current.getHeight() + 1;
            final int xBound = board.getWidth() - current.getWidth()+1;
            
            // For current rotation, try all the possible columns
            for (int x = 0; x<xBound; x++) {
                int y = board.dropHeight(current, x);
                if (y<yBound) {    // piece does not stick up too far
                    int result = board.place(current, x, y);
                    if (result <= BoardI.PLACE_ROW_FILLED) {
                        if (result == BoardI.PLACE_ROW_FILLED) board.clearRows();
                        
                        double score = rateBoard(board);
                        
                        
                        if (score<bestScore) {
                            bestScore = score;
                            bestX = x;
                            bestY = y;
                            bestPiece = current;
                        }
                    }
                    
                    board.undo();    // back out that play, loop around for the next
                }
            }
            
            current = current.fastRotation();
            if (current == piece) break;    // break if back to original rotation
        }
        
        if (bestPiece == null) return(null);    // could not find a play at all!
        else {
            move.x = bestX;
            move.y = bestY;
            move.piece = bestPiece;
            move.score = bestScore;
            return(move);
        }
    }
    
    public  abstract Brain getInstance();
    
    /*
    A simple brain function.
    Given a board, produce a number that rates
    that board position -- larger numbers for worse boards.
    This version just counts the height
    and the number of "holes" in the board.
   */
   public abstract double rateBoard(BoardI board);
}
