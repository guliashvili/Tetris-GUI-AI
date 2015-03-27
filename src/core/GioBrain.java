package core;


public class GioBrain extends Brain {

@Override
	public double rateBoard(BoardI board){
		double onEmpties = 0;
		double squareDif = 0;
		double empty = 0;
		for(int i = 0; i < board.getWidth(); i++){
			boolean wasEmpty= false;
			for(int j = 0; j < board.getColumnHeight(i); j++){
				if(board.getGrid(i, j) && wasEmpty)
					onEmpties++;
				else if(!board.getGrid(i, j))
					wasEmpty = true;
				if(!board.getGrid(i, j)) empty++;
			}
			
			if((i > 1 && i + 1 < board.getWidth()) || (i > 0 && board.getMaxHeight() < board.getHeight()*3/2) ) squareDif += Math.pow(board.getColumnHeight(i) - board.getColumnHeight(i-1), 2);
			
		}
		
	
		return squareDif + 100 * empty + board.getMaxHeight()*10000;
	}
	

@Override
	public Brain getInstance() {
		return new GioBrain();
	}
	

}
