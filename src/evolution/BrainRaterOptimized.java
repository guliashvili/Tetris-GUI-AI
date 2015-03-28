package evolution;




import core.*;

import java.awt.*;

import javax.swing.*;

import java.util.*;
import java.awt.event.*;

import javax.swing.event.*;

import core.Piece;
import Boards.BoardI;
import Boards.BoardOptimized;
import Brains.Brain;
import Brains.EvolutableBrain;
import JTetrises.JTetrisE;

import java.awt.Toolkit;


/**
CS108 Tetris Game.
JTetris presents a tetris game in a window.
It handles the GUI and the animation.
The Piece and Board classes handle the
lower-level computations.
This code is provided in finished, working form for the students.

Use Keys j-k-l to move, n to drop (or 4-5-6 0)
During animation, filled rows draw as green.
Clearing 1-4 rows scores 5, 10, 20, 40 points.
Clearing 4 rows at a time beeps!
*/

/*
Implementation notes:
-The "currentPiece" points to a piece that is
currently falling, or is null when there is no piece.
-tick() moves the current piece
-a timer object calls tick(DOWN) periodically
-keystrokes call tick() with LEFT, RIGHT, etc.
-Board.undo() is used to remove the piece from its
old position and then Board.place() is used to install
the piece in its new position.
*/

public class BrainRaterOptimized {
	
	public static final int N_TEST = 5;
	
	// size of the board in blocks
	public static final int WIDTH = 10;
	public static final int HEIGHT = 20;
	
	// Extra blocks at the top for pieces to start.
	// If a piece is sticking up into this area
	// when it has landed -- game over!
	public static final int TOP_SPACE = 4;


	
	// Board data structures
	protected BoardI board;
	protected Piece[] pieces;
	
	
	
	// The current piece in play or null
	protected Piece currentPiece;
	protected int currentX;
	protected int currentY;
	protected boolean moved;	// did the player move the piece
	
	
	// The piece we're thinking about playing
	// -- set by computeNewPosition
	// (storing this in ivars is slightly questionable style)
	protected Piece newPiece;
	protected int newX;
	protected int newY;
	
	// State of the game
	protected boolean gameOn;	// true if we are playing
	protected int count;		 // how many pieces played so far
	protected long startTime;	// used to measure elapsed time
	protected Random random;	 // the random generator for new pieces
	
	
	// Controls
	protected int score;

	protected EvolutableBrain AI;
	
	
	/**
	 * Creates a new JTetris where each tetris square
	 * is drawn with the given number of pixels.
	 */
	public BrainRaterOptimized(BoardI board, EvolutableBrain br) {
		AI = br;
		gameOn = false;
		
		pieces = Piece.getPieces();
		this.board =  board.getInstance(WIDTH, HEIGHT + TOP_SPACE);

	}
	
	private int lastCount = -1;
	private Brain.Move  target = null;
	
	private int playAi(){
		if(currentPiece == null) return -1;
		
		board.undo();
		if(lastCount != count){
			lastCount = count;
			
			target = AI.bestMove(board, currentPiece, HEIGHT, target,currentX,currentY);
		}
		if(target == null) return -1;
		
		if(target.list.isEmpty()) return -1;
		if(target.list.getFirst() == -1){
			target.list.removeFirst();
			return -1;
			
		}else{
			int ret;
			switch(target.list.getFirst()){
			case Brain.DOWN:
				ret = JTetrisE.DOWN;
				break;
			case Brain.LEFT:
				ret = JTetrisE.LEFT;
				break;
			case Brain.RIGHT:
				ret = JTetrisE.RIGHT;
				break;
			case Brain.ROTATE:
				ret = JTetrisE.ROTATE;
				break;
			case Brain.NOTHING:
				ret = -1;
				break;
			default:
				throw new RuntimeException("invalid move");
			}
			target.list.removeFirst();
			if(!target.list.isEmpty()){
				if(target.list.getFirst() != -1) throw new RuntimeException("something wrong");
				target.list.removeFirst();
			}
			return ret;
		}
		
	
		
	}


	/**
	 Sets the internal state and starts the timer
	 so the game is happening.
	*/
	public int startGame() {
		// cheap way to reset the board state
		board = new BoardOptimized(WIDTH, HEIGHT + TOP_SPACE);
		
		
		count = 0;
		score = 0;
		gameOn = true;
		
		
		random = new Random(); // diff seq each game
		
		addNewPiece();
		startTime = System.currentTimeMillis();
		
		while(gameOn){
			int r = playAi();
			if(r != -1) 
				tick(r);
			tick(DOWN);
			
			
		}
		
		return score;
	}
	
	

	
	
	/**
	 Given a piece, tries to install that piece
	 into the board and set it to be the current piece.
	 Does the necessary repaints.
	 If the placement is not possible, then the placement
	 is undone, and the board is not changed. The board
	 should be in the committed state when this is called.
	 Returns the same error code as Board.place().
	*/
	public int setCurrent(Piece piece, int x, int y) {
		int result = board.place(piece, x, y);
		
		if (result <= BoardI.PLACE_ROW_FILLED) { // SUCESS
			currentPiece = piece;
			currentX = x;
			currentY = y;
		}
		else {
			board.undo();
		}
		
		return(result);
	}


	/**
	 Selects the next piece to use using the random generator
	 set in startGame().
	*/
	public Piece pickNextPiece() {
		int pieceNum;
		
		pieceNum = (int) (pieces.length * random.nextDouble());
		
		Piece piece	 = pieces[pieceNum];
		
		return(piece);
	}
	
			
	/**
	 Tries to add a new random piece at the top of the board.
	 Ends the game if it's not possible.
	*/
	public void addNewPiece() {
		count++;
		score++;
	

		// commit things the way they are
		board.commit();
		currentPiece = null;

		Piece piece = pickNextPiece();
		
		// Center it up at the top
		int px = (board.getWidth() - piece.getWidth())/2;
		int py = board.getHeight() - piece.getHeight();
		
		// add the new piece to be in play
		int result = setCurrent(piece, px, py);
		
		// This probably never happens, since
		// the blocks at the top allow space
		// for new pieces to at least be added.
		if (result>BoardOptimized.PLACE_ROW_FILLED) {
			stopGame();
		}
	}

	
	
	/**
	 Figures a new position for the current piece
	 based on the given verb (LEFT, RIGHT, ...).
	 The board should be in the committed state --
	 i.e. the piece should not be in the board at the moment.
	 This is necessary so dropHeight() may be called without
	 the piece "hitting itself" on the way down.

	 Sets the ivars newX, newY, and newPiece to hold
	 what it thinks the new piece position should be.
	 (Storing an intermediate result like that in
	 ivars is a little tacky.)
	*/
	public void computeNewPosition(int verb) {
		// As a starting point, the new position is the same as the old
		newPiece = currentPiece;
		newX = currentX;
		newY = currentY;
		
		// Make changes based on the verb
		switch (verb) {
			case LEFT: newX--; break;
			
			case RIGHT: newX++; break;
			
			case ROTATE:
				newPiece = newPiece.fastRotation();
				
				// tricky: make the piece appear to rotate about its center
				// can't just leave it at the same lower-left origin as the
				// previous piece.
				newX = newX + (currentPiece.getWidth() - newPiece.getWidth())/2;
				newY = newY + (currentPiece.getHeight() - newPiece.getHeight())/2;
				break;
				
			case DOWN: newY--; break;
			
			case DROP:
			 newY = board.dropHeight(newPiece, newX);
			 
			 // trick: avoid the case where the drop would cause
			 // the piece to appear to move up
			 if (newY > currentY) {
				 newY = currentY;
			 }
			 break;
			 
			default:
				 throw new RuntimeException("Bad verb");
		}
	
	}


	public void stopGame(){
		gameOn = false;
	}
		
	public static final int ROTATE = 0;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int DROP = 3;
	public static final int DOWN = 4;
	
	/**
	 Called to change the position of the current piece.
	 Each key press calls this once with the verbs
	 LEFT RIGHT ROTATE DROP for the user moves,
	 and the timer calls it with the verb DOWN to move
	 the piece down one square.

	 Before this is called, the piece is at some location in the board.
	 This advances the piece to be at its next location.
	 
	 Overriden by the brain when it plays.
	*/
	public void tick(int verb) {
		if (!gameOn) return;
	
		
		if (currentPiece != null) {
			board.undo();	// remove the piece from its old position
		}
		
		// Sets the newXXX ivars
		computeNewPosition(verb);
		
		// try out the new position (rolls back if it doesn't work)
		int result = setCurrent(newPiece, newX, newY);
		
		
		

		boolean failed = (result >= BoardOptimized.PLACE_OUT_BOUNDS);
		
		// if it didn't work, put it back the way it was
		if (failed) {
			if (currentPiece != null) board.place(currentPiece, currentX, currentY);
		}
		
		/*
		 How to detect when a piece has landed:
		 if this move hits something on its DOWN verb,
		 and the previous verb was also DOWN (i.e. the player was not
		 still moving it),	then the previous position must be the correct
		 "landed" position, so we're done with the falling of this piece.
		*/
		if (failed && verb==DOWN && !moved) {	// it's landed
		
			int cleared = board.clearRows();
			if (cleared > 0) {
				// score goes up by 5, 10, 20, 40 for row clearing
				// clearing 4 gets you a beep!
				switch (cleared) {
					case 1: score += 5;	 break;
					case 2: score += 10;  break;
					case 3: score += 20;  break;
					case 4: score += 40; Toolkit.getDefaultToolkit().beep(); break;
					default: score += 50;  // could happen with non-standard pieces
				}
				
			}
			
			
			// if the board is too tall, we've lost
			if (board.getMaxHeight() > board.getHeight() - TOP_SPACE) {
				stopGame();
			}
			// Otherwise add a new piece and keep playing
			else {
				addNewPiece();
			}
		}
		
		// Note if the player made a successful non-DOWN move --
		// used to detect if the piece has landed on the next tick()
		moved = (!failed && verb!=DOWN);
	}
	public double getAverageScore(){
		double ret = 0;
		for(int i = 0; i < N_TEST; i++) ret += startGame();
		ret /= N_TEST;
		
		return ret;
		
	}

	

	

}

