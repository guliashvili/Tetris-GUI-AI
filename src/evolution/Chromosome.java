package evolution;

import java.util.Random;

public class Chromosome {
	public static final double MAX = 1;
	
	public double ar[];
	private static  Random random;
	
	
	static{
		random = new Random();
	}
	
	public Chromosome(int n) {
		if(n == 0 ) throw new RuntimeException("gene should not be 0 length");
		ar = new double[n];
	}
	public Chromosome(double a[]){
		if(a == null || a.length == 0) throw new RuntimeException("gene should not be 0 length or null");
		ar = new double[a.length];
		System.arraycopy(a, 0, ar, 0, a.length);
	}
	public Chromosome(Chromosome a){
		this(a.ar);
	}
	

	
	public Chromosome mutate(){
		Chromosome ret = new Chromosome(this);
		ret.ar[random.nextInt(ar.length)] = random.nextDouble();
		
		
		return ret;
	}
	
	public Chromosome merge(Chromosome mom){
		Chromosome ret = new Chromosome(this.ar.length);
		for(int i = 0; i < ret.ar.length; i++) ret.ar[i] = (this.ar[i] + mom.ar[i])/2;
	
		
		return ret;
	}
	public Chromosome crossOver(Chromosome mom){
		Chromosome ret = new Chromosome(this.ar.length);
		for(int i = 0; i < ret.ar.length; i++)
			if(random.nextBoolean()) ret.ar[i] = this.ar[i];
			else ret.ar[i]= mom.ar[i];
		
		
		return ret;
	}
	public Chromosome radiation(){
		Chromosome ret = new Chromosome(this);
		for(int i = 0; i < ar.length; i++){
			ret.ar[i] += random.nextDouble() - 0.5;
		}
		
		
		return ret;
	}
	
	@Override
	public String toString(){
		StringBuffer b = new StringBuffer();
		b.append("\n");
		for(int i = 0; i < ar.length; i++) b.append(ar[i] + " ");
	
	b.append("\n");
	return b.toString();
	}
}
