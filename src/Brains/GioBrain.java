package Brains;

import java.util.ArrayList;
import java.util.Random;

import core.Piece;
import evolution.Chromosome;
import Boards.BoardI;

public class GioBrain extends EvolutableBrain {

	private Chromosome cro;
	private static final Random rnd;
	static {
		rnd = new Random();
	}
	/*
	public GioBrain(int i){
		if(i == 0)
			c= new double[]{500.0 ,500.0, 1250.0, 500.0, 1000.0} ;
		else if(i== 1)
			c=new double[]{187.5,375.0,1000.0,250.0,1062.5} ;
		else if(i==2)
			c = new double[]{500.0,375.0,1375.0,1250.0,875.0} ;
		else
			c = new double[]{640.625,281.25,1656.25,750.0,1000.0};
		c= new double[]{250.0,218.75,1093.75,-1093.75,937.5 } ;
	}*/
	public GioBrain(){
		//c= new double[]{250.0,218.75,1093.75,-1093.75,937.5 } ;
		cro = new Chromosome( new double[]{0.0, 0.0, 0.0, 1.0});
	}
	
	
	private double rate(BoardI board){
		/*
		 * 1 - squareDif
		 * 2 - empty
		 * 3 - getMaxHeight
		 * 4 - ysum
		 */
				double squareDif = 0;
				double empty = 0;
				double ysum = 0;
				for(int i = 0; i < board.getWidth(); i++){
					for(int j = 0; j < board.getColumnHeight(i); j++){
						
						if(!board.getGrid(i, j))
							 empty++;
						ysum += j;
					}
					
					
					if(i > 0)
						squareDif += board.getColumnHeight(i) - board.getColumnHeight(i-1);
					
					
				}
				
			return  squareDif * cro.ar[0] + empty * cro.ar[1] + board.getMaxHeight() * cro.ar[2] + ysum * cro.ar[3];
		
	}
	
@Override
	public double rateBoard(BoardI board){
		double mathMean = 0;
		for(int i = 0 ; i < Piece.getPieces().length/3; i++){
			double mn = 1010101000;
			Piece p = Piece.getPieces()[rnd.nextInt(Piece.getPieces().length)];
			ArrayList<BoardI> ar = getAllMoves(board.getInstance(board), p,
												(board.getWidth() - p.getWidth())/2,
												board.getHeight() - p.getHeight());
			
			for(BoardI b : ar) mn = Math.min(mn, rate(b));
			
			mathMean += mn * 1.0 / Piece.getPieces().length;
			
		}
		
		return mathMean;
	}
	
	public int getChromosomeLength(){
		return 4;
	}
	public void setChromosome(Chromosome chromosom){
		cro = chromosom;
	}
	

}
