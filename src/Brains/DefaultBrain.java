// DefaultBrain.java

package Brains;

import Boards.BoardI;

/**
 Provided code.
 A simple Brain implementation.
 bestMove() iterates through all the possible x values
 and rotations to play a particular piece (there are only
 around 10-30 ways to play a piece).
 
 For each play, it uses the rateBoard() message to rate how
 good the resulting board is and it just remembers the
 play with the lowest score. Undo() is used to back-out
 each play before trying the next. To experiment with writing your own
 brain -- just subclass off DefaultBrain and override rateBoard().
*/

public class DefaultBrain extends Brain {

    
 
    /*
     A simple brain function.
     Given a board, produce a number that rates
     that board position -- larger numbers for worse boards.
     This version just counts the height
     and the number of "holes" in the board.
    */
    public double rateBoard(BoardI board) {
        final int width = board.getWidth();
        final int maxHeight = board.getMaxHeight();
        
        int sumHeight = 0;
        int holes = 0;
        
        // Count the holes, and sum up the heights
        for (int x=0; x<width; x++) {
            final int colHeight = board.getColumnHeight(x);
            sumHeight += colHeight;
            
            int y = colHeight - 2;    // addr of first possible hole
            
            while (y>=0) {
                if  (!board.getGrid(x,y)) {
                    holes++;
                }
                y--;
            }
        }
        
        double avgHeight = ((double)sumHeight)/width;
        
        // Add up the counts to make an overall score
        // The weights, 8, 40, etc., are just made up numbers that appear to work
        return (8*maxHeight + 40*avgHeight + 1.25*holes);    
    }


/*	@Override
	public Brain getInstance() {
		return new DefaultBrain();
	}
*/
}

