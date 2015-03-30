package evolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import java.util.Random;

import Boards.*;
import Brains.*;


public class Evolution{
	 public static final long TOTAL_TIME = 600;
	 public static final boolean DEBUG  = true;
	 public static final int LIM = 5;
	 public static boolean evolutionIsActiveSuckers = false;
	 private GioBrain brain;
	 private BoardI bord;
	 private static Random random;
	 static{
		 random = new Random();
	 }
	 private final static double ABNORMALITY = 0.01;
	 
	 
	 
	 public Evolution(GioBrain brn,BoardI bo) {
		brain = brn;
		bord = bo;
	}
	 
	private class Container implements Comparator<Container>,Comparable<Container>{
		public Chromosome cro;
		public double rate;
		
		public Container(Chromosome cro,double r){
			this.cro = cro;
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
	
	
	private Container addAgent(ArrayList<Container> ar,Chromosome cro){
		//System.out.println(cro);
		brain.setChromosome(cro);
		BrainRaterOptimized br = new BrainRaterOptimized(bord, brain);
		Container ret = new Container(cro, br.getAverageScore()); 
		ar.add( ret );
		return ret;
	}
	
	
	public Chromosome evoluate(){
		evolutionIsActiveSuckers = true;
		int n;
		n = brain.getChromosomeLength();
		Container ret = null;
		
		
		ArrayList<Container> currentGeneration = new ArrayList<Evolution.Container>() ;
		ArrayList<Container> nextGeneration = new ArrayList<Evolution.Container>() ;
		
		
		
		//double step = 1000;
		double[] coef = new double[n];
		ret = addAgent(currentGeneration, new Chromosome(coef));
		
		
		
		
		
		double step = 2;
		for(int generation = 0; generation < 100000; generation++){
			step /=2;
			//System.out.println("current step - " + step);
			//step/=2;
			Collections.sort(currentGeneration,Collections.reverseOrder());
			

			
			
			if(currentGeneration.size() > 2 * LIM){
				double prob = LIM / (double)(currentGeneration.size() - LIM);
				for(int i = LIM; i < currentGeneration.size(); i ++){
					if(random.nextDouble() > prob){
						currentGeneration.remove(i); 
						i--;
					}
				}
			}
			
			if(DEBUG){
				System.out.println("generation size = "+currentGeneration.size());
				
				System.out.println(currentGeneration.get(0).rate + "  " + currentGeneration.get(currentGeneration.size()-1).rate);
				
				System.out.println(ret.rate);
				for(double elem : ret.cro.ar) System.out.print(elem + " ");System.out.println("");
			}
			
			Container cur;
			for(int i = 0 ; i < currentGeneration.size(); i++){
				
				
				for(int j = 0; j < 2; j++){
					Chromosome elem = currentGeneration.get(i).cro;
					for(int k = 0; k < elem.ar.length; k++){
						Chromosome deterministic = new Chromosome(elem);
						if(random.nextDouble()<0.1){
							if(j == 0)
								deterministic.ar[k] += step;
							else 
								deterministic.ar[k] -= step;
							
							cur =  addAgent(nextGeneration, deterministic );
							if(cur.rate > ret.rate) ret = cur;
						}
					}
					
				}
				
				
				if(i < LIM || random.nextDouble() < ABNORMALITY){
					cur =  addAgent(nextGeneration, currentGeneration.get(i).cro.radiation() );
					if(cur.rate > ret.rate) ret = cur;
				}
				if(i < LIM || random.nextDouble() < ABNORMALITY){
					cur =  addAgent(nextGeneration, currentGeneration.get(i).cro.mutate() );
					if(cur.rate > ret.rate) ret = cur;
				}
				if(i < LIM || random.nextDouble() < ABNORMALITY){
					cur =  addAgent(nextGeneration, currentGeneration.get(i).cro);
					if(cur.rate > ret.rate) ret = cur;
				}
			
				
				for(int j = i; j < currentGeneration.size(); j++){
					if((i < LIM && j < LIM) || random.nextDouble() < ABNORMALITY){
						cur =  addAgent(nextGeneration,  currentGeneration.get(i).cro.merge(currentGeneration.get(j).cro));
						if(cur.rate > ret.rate) ret = cur;
					}
					if((i < LIM && j < LIM) || random.nextDouble() < ABNORMALITY){
						cur =  addAgent(nextGeneration,  currentGeneration.get(i).cro.crossOver(currentGeneration.get(j).cro));
						if(cur.rate > ret.rate) ret = cur;
					}
				}
			}
			
			
		
			
		
			currentGeneration = nextGeneration;
			nextGeneration =  new ArrayList<Evolution.Container>() ;;
			
		}
		

		
		evolutionIsActiveSuckers = false;
		
		return ret.cro;
	}
	
	
	
}
