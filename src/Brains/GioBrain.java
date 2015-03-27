package Brains;

import core.BoardI;


public class GioBrain extends Brain implements Evolutable{
	double[] c;
	
	public GioBrain(){
		c = new double[]{0,1,100,10000,0};
	}
	
@Override

	public double rateBoard(BoardI board){
/*
 * 0 - onEmpties
 * 1 - squareDif
 * 2 - empty
 * 3 - getMaxHeight
 * 4 - dif
 */
		double onEmpties = 0;
		double squareDif = 0;
		double empty = 0;
		double dif = 0;
		for(int i = 0; i < board.getWidth(); i++){
			boolean wasEmpty= false;
			for(int j = 0; j < board.getColumnHeight(i); j++){
				if(board.getGrid(i, j) && wasEmpty)
					onEmpties++;
				else if(!board.getGrid(i, j))
					wasEmpty = true;
				if(!board.getGrid(i, j)) empty++;
			}
			
			if(i > 0){
				squareDif += Math.pow(board.getColumnHeight(i) - board.getColumnHeight(i-1), 2);
				dif += board.getColumnHeight(i) - board.getColumnHeight(i-1);
			}
			
		}
		
	
		return onEmpties * c[0] + squareDif * c[1] + empty * c[2] + board.getMaxHeight() * c[3] + dif * c[4];
	}
	

@Override
	public Brain getInstance() {
		return new GioBrain();
	}


@Override
public void setCoefficients(double[] c) {
	this.c = c.clone();
}


@Override
public int getNCoefficient() {
	return 5;
}
	

}
