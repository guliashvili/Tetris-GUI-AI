package Brains;

import Boards.BoardI;


public class GioBrain extends EvolutableBrain {
	double[] c;
	
	public GioBrain(int i){
		if(i == 0)
			c= new double[]{500.0 ,500.0, 1250.0, 500.0, 1000.0} ;
		else if(i== 1)
			c=new double[]{187.5,375.0,1000.0,250.0,1062.5} ;
		else if(i==2)
			c = new double[]{500.0,375.0,1375.0,1250.0,875.0} ;
		else
			c = new double[]{640.625,281.25,1656.25,750.0,1000.0};
	}
	public GioBrain(){
		c= new double[]{500.0 ,500.0, 1250.0, 500.0, 1000.0} ;
	}
	
@Override

	public double rateBoard(BoardI board){
/*
 * 0 - onEmpties
 * 1 - squareDif
 * 2 - empty
 * 3 - getMaxHeight
 * 4 - ysum
 */
		double onEmpties = 0;
		double squareDif = 0;
		double empty = 0;
		double ysum = 0;
		for(int i = 0; i < board.getWidth(); i++){
			boolean wasEmpty= false;
			for(int j = 0; j < board.getColumnHeight(i); j++){
				if(board.getGrid(i, j) && wasEmpty)
					onEmpties++;
				else if(!board.getGrid(i, j))
					wasEmpty = true;
				if(!board.getGrid(i, j)) empty++;
				ysum += j;
			}
			
			
			if(i > 0)
				squareDif += Math.pow(board.getColumnHeight(i) - board.getColumnHeight(i-1), 2);
			
			
		}
		
	
		return onEmpties * c[0] + squareDif * c[1] + empty * c[2] + board.getMaxHeight() * c[3] + ysum * c[4];
	}
	

/*@Override
	public EvolutableBrain getInstance() {
		return new GioBrain();
	}
*/

@Override
public void setCoefficients(double[] c) {
	this.c = c.clone();
}


@Override
public int getNCoefficient() {
	return 5;
}
	

}
