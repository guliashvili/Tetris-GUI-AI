package Brains;

import Boards.BoardI;

public abstract class EvolutableBrain extends Brain {
	/*
	 * c has getNCoefficient() length.
	 * c is coefficients for different variables
	 * so brain will return sum(fi(board)*c[i])
	 */
	public abstract  void setCoefficients(double[] c);
	public  abstract int getNCoefficient();
	
	//@Override
	//public abstract EvolutableBrain getInstance(); 
}
