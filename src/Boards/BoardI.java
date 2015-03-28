package Boards;

import core.Piece;


public abstract class BoardI {
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	protected int width;
	protected int height;
	protected boolean[][] grid;
	protected int[] rowWidth;
	protected int[] columnHeight;
	protected int maxHeight;
	
	

	
	
	public abstract  BoardI getInstance(int width,int height);
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {	 
		return maxHeight;
	}
	public abstract void setMaxHeight(int val);
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		 return rowWidth[y]; 
	}
	
	public abstract void setRowWidth(int y,int val) ;
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return columnHeight[x] ; 
	}


	public abstract void setColumnHeight(int x, int val);

	protected boolean outOfBound(int x,int y){
		return x < 0 || x >= getWidth() || y < 0 || y >= getHeight();
	}
	/*
	 * returns true if grid square is true. 
	 * will not check for out of bounds for optimization
	 */
	protected boolean getGridUnsafe(int x,int y){
		return grid[y][x];
	}
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public  boolean getGrid(int x, int y) {
		return (outOfBound(x, y) || getGridUnsafe(x, y));
	}
	

	public abstract void setGrid(int x, int y, boolean val) ;

	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public abstract int dropHeight(Piece piece, int x);
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public abstract int place(Piece piece, int x, int y);
	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public abstract int clearRows();
	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public abstract void undo();
	/**
	 Puts the board in the committed state.
	*/
	public abstract  void commit();
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	 * @throws Exception 
	*/
	public abstract void sanityCheck();
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = getHeight()-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<getWidth(); x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<getWidth()+2; x++) buff.append('-');
		return(buff.toString());
	}
}
