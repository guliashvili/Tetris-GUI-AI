package evolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import javax.swing.JFrame;

import Boards.*;
import Brains.*;
import JTetrises.BrainRater;
import JTetrises.JTetrisE;
import JTetrises.JTetrisEO2;


public class Evolution{
	 public static final long TOTAL_TIME = 600;
	 public static final boolean DEBUG  = true;
	 public static final int LIM = 10;
	 public static boolean evolutionIsActiveSuckers = false;
	 private EvolutableBrain brain;
	 private BoardI bord;
	 
	 
	 public Evolution(EvolutableBrain brn,BoardI bo) {
		brain = brn;
		bord = bo;
	}
	 
	private class Container implements Comparator<Container>,Comparable<Container>{
		public double ar[];
		public double rate;
		
		public Container(double[] a,double r){
			ar = a;
			rate = r;
		}

		@Override
		public int compareTo(Container arg0) {
			return compare(this,arg0);
		}

		@Override
		public int compare(Container arg0, Container arg1) {
			if(arg0.rate < arg1.rate) return -1;
			if(arg0.rate > arg1.rate) return 1;
			return 0;
		}
		
	}
	
	
	
	public double[] evoluate(){
		evolutionIsActiveSuckers = true;
		int n;
		n = brain.getNCoefficient();
		Container ret = null;
		
		BrainRaterOptimized br;
		
		LinkedList<Container> ar = new LinkedList<Evolution.Container>();
		
		long start = System.currentTimeMillis();
		
		double step = 1000;
		double[] coef;
		
		
		for(int msk = 0; msk < (1<<n); msk++){
			coef = new double[n];
			for(int i = 0; i < coef.length; i++)  if((msk & (1<<i)) > 0) coef[i] = step;
			brain.setCoefficients(coef);
			
			br = new BrainRaterOptimized(bord, brain);
			ret = new Container(coef,br.getAverageScore() );
			ar.add(ret);
				
		}
		
		
		
		
		while(System.currentTimeMillis() - start > 0){
			System.out.println("current step - " + step);
			step/=2;
			Collections.sort(ar,Collections.reverseOrder());
			while(ar.size() > LIM) ar.removeLast();
			
			if(DEBUG){
				System.out.println(ar.getFirst().rate + "  " + ar.getLast().rate);
				
				System.out.println(ret.rate);
				for(double elem : ret.ar) System.out.print(elem + " ");
			}
			
			int sz = ar.size();
			for(int i = 0;i < sz; i ++){
				if(ar.getFirst().rate > ret.rate) ret = ar.getFirst();
				for(int j = 0; j < ar.getFirst().ar.length; j++){
					
				
					for(int k = 0; k < 2; k++){
						coef = new double[n];
						System.arraycopy(ar.getFirst().ar, 0, coef, 0, coef.length);
						if(k==0)
							coef[j] -= step;
						else 
							coef[j] += step;
						
						
						
						brain.setCoefficients(coef);
						
						br = new BrainRaterOptimized(bord, brain);
						Container tmp = new Container(coef,br.getAverageScore() );
						ar.add(tmp);
					}					
					ar.removeFirst();
					
				}
				
			}
			
		}
		

		
		evolutionIsActiveSuckers = false;
		
		return ret.ar;
	}
	
	
	
}
