package Brains;

import evolution.Chromosome;


public abstract class EvolutableBrain extends Brain {
	/*
	 * c has getNCoefficient() length.
	 * c is coefficients for different variables
	 * so brain will return sum(fi(board)*c[i])
	 */
	public abstract  void setChromosome(Chromosome chromosome);
	public  abstract int getChromosomeLength();
	
}
