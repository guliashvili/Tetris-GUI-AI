package Brains;

public interface Evolutable {
	/*
	 * c has getNCoefficient() length.
	 * c is coefficients for different variables
	 * so brain will return sum(fi(board)*c[i])
	 */
	public void setCoefficients(double[] c);
	public int getNCoefficient();
	
}
