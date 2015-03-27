package Boards;

import core.Piece;
import core.TPoint;





public class BoardOptimized extends Boards.BoardI{

	
	// Some ivars are stubbed out for you:
	
	protected database db;
	protected boolean committed;
	protected boolean DEBUG = false;
	

	
	
	private class database{
		private boolean[][] g;
		private int[] rW;
		private int[] cH;
		private int mH;
		private TPoint[] stk;
		private int stki = 0;

	
		boolean saved = false;
		
		public database(int width,int height){
			g = new boolean[height][width];
			rW = new int[height];
			cH = new int[width];
			stk = new TPoint[width*height];
		}
		
		private void save(){
			
			System.arraycopy(rowWidth, 0, rW, 0, rowWidth.length);
			System.arraycopy(columnHeight, 0, cH, 0, columnHeight.length);
			
			System.arraycopy(grid, 0, g, 0, g.length);
			
			mH = maxHeight;	

			saved = true;
		}
		
		
		public void undo(){
			

			if(saved){
				System.arraycopy(rW, 0, rowWidth, 0, rowWidth.length);
				System.arraycopy(cH, 0, columnHeight, 0, columnHeight.length);
				
				System.arraycopy(g, 0, grid, 0, g.length);
				
				maxHeight = mH;
			}
			while(stki > 0){
					stki--;
					TPoint p = stk[stki];
					BoardOptimized.this.grid[p.y][p.x] = false;
				}
			
			saved = false;
			stki = 0;
		}
		
		public void commit(){
			stki = 0;
			saved = false;
		}
		
		public void setGrid(int x, int y, boolean val){
			stk[stki++] = new TPoint(x,y);
		}
	
		
		public void place(Piece p,int x,int y){
			/*if(saved)
				throw new RuntimeException("ax");*/
			
			 save();
		}
		public void clearRows(){
			if(!saved) 
				save();
		
		}
		
		
	}
	
	
	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public BoardOptimized(int width, int height) {
		
		
		this.width = width;
		this.height = height;
		maxHeight = 0;
		
		grid = new boolean[height][width];
		committed = true;
		rowWidth = new int[height];
		columnHeight = new int[width];
		for(int i = 0; i < columnHeight.length; i++) columnHeight[i] = 0;
		
		db = new database(width,height);
		db.commit();
		
	}
	
	
	
	
	public void setMaxHeight(int val){
		maxHeight = val;
	}
	
	
	public void setRowWidth(int y,int val) {
		this.rowWidth[y] = val;
	}


	public void setColumnHeight(int x, int val) {		
		
		this.columnHeight[x] = val;
	}

	
	

	public void setGrid(int x, int y, boolean val) {
		db.setGrid(x,y,val);
		
		this.grid[y][x] = val;
	}


	
	/*
	 * copies b  to row  number a of grid
	 */
	private void copyRow(int a,boolean[] b){
	
		grid[a] = b;
		
	}


	

	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		int ret = 0;
		for(int i = 0; i < piece.getWidth(); i++){
			ret = Math.max(ret, getColumnHeight(x+i) - piece.getSkirt()[i] );
		}
		return ret; 
	}
	
	
	
	

	
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
	public int place(Piece piece, int x, int y) {
		
		if (!committed) throw new RuntimeException("place commit problem");
		committed = false;
		db.place(piece,x,y);
		
		int result = PLACE_OK;
		if(x<0 || y<0 || x + piece.getWidth() > this.getWidth() || y + piece.getHeight() > this.getHeight()) 
			result = PLACE_OUT_BOUNDS;
		else 
			for(TPoint pp : piece.getBody()){
				TPoint p = new TPoint(pp);
				p.x += x;
				p.y += y;
				if(getGridUnsafe(p.x, p.y)){
					result = PLACE_BAD;
					break;
				}else {
					setGrid(p.x, p.y, true);
					setRowWidth(p.y, getRowWidth(p.y) + 1);
					setColumnHeight(p.x,Math.max(getColumnHeight(p.x),p.y + 1));
					setMaxHeight(Math.max(getMaxHeight(), getColumnHeight(p.x)));
					
				}
				
			}
		
		if(result == PLACE_OK)
			sanityCheck();
		
		
		return result;
	}
	
	

	
	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		db.clearRows();
		committed = false;
		
		int rowsCleared = 0;
		int k = 0;
		for(int i = 0; i < getHeight();i++){
			if(getRowWidth(i) == 0) break;
			if(getRowWidth(i) != getWidth()){
				if(i != k){
					copyRow(k,grid[i]);
					setRowWidth(k, getRowWidth(i));
				}
				k++;
				
			}else 
				rowsCleared ++;
		}
		if(rowsCleared > 0){
			for(int i = k; i < getHeight(); i++) {
				if(getRowWidth(i) == 0) break;
				for(int j = 0; j < getWidth(); j++) copyRow(i , new boolean[getWidth()]);
				setRowWidth(i, 0);
			}
		
			setMaxHeight(0);
		
			for(int i = 0; i < getWidth(); i++){
				setColumnHeight(i, getColumnHeight(i) - rowsCleared);
				while(getColumnHeight(i) > 0 && !getGrid(i,getColumnHeight(i)-1) ){
					setColumnHeight(i, getColumnHeight(i) - 1);
				}
				setMaxHeight(Math.max(getMaxHeight(), getColumnHeight(i)));
			
			}
		}
		
		sanityCheck();
		return rowsCleared;
	}



	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if(committed) return;
		committed = true;
		
		db.undo();
		
		sanityCheck();
	
	}
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
		
		db.commit();
		sanityCheck();
	}


	
	
	
	

	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	 * @throws Exception 
	*/
	public void sanityCheck() {
		if (DEBUG) {
			//test heights
			
			int mxH = 0;
			for(int i = 0; i < getWidth(); i++)
				for(int j = getHeight()-1; j >= 0 ; j--) if(getGridUnsafe(i, j)){
					if(j + 1 != getColumnHeight(i)){
						throw new RuntimeException("wrong getColumnHeight column is " + i +" found " + (j+1) );
					}
					mxH = Math.max(mxH, j + 1);
					break;
				}
			if(getMaxHeight() != mxH)
				throw new RuntimeException("wrong getMaxHeight " + getMaxHeight() + " but real is " + mxH );
				
			//test width
			for(int j = 0; j < getHeight(); j++){
				int sum = 0;
				for(int i = 0; i< getWidth(); i++) if(getGridUnsafe(i, j)) sum ++;
				if(sum != getRowWidth(j))
					throw new RuntimeException("wrong getRowWidth");
			}
			
		}
	}




	@Override
	public BoardI getInstance(int width, int height) {
		return new BoardOptimized(width, height);
	}
	


	

}
