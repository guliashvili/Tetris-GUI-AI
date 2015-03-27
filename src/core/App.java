package core;

import javax.swing.JFrame;
import javax.swing.UIManager;

import Boards.BoardOptimized;
import Brains.*;
import JTetrises.*;

public class App {

	
	private static void evol(){
		Evolution e = new Evolution(new GioBrain(), new BoardOptimized(1, 1));
		e.evoluate();
		
	}
	private static void play(){
		JTetrisEO2 tetris;
		JFrame frame;
		
		
		tetris = new JTetrisEO2(16,new BoardOptimized(1, 1),new GioBrain());
		frame = JTetrisE.createFrame(tetris);
		frame.setVisible(true);
	}
	private static void rat(){
		JTetrisEO2 tetris;
		JFrame frame;
		Brain b;
		
		b = new GioBrain(0);
		tetris = new JTetrisEO2(16,new BoardOptimized(1, 1),b);
		frame = JTetrisE.createFrame(tetris);
		frame.setVisible(true);
		System.out.println(BrainRater.getAverageScore(tetris) );
		
		b = new GioBrain(1);
		tetris = new JTetrisEO2(16,new BoardOptimized(1, 1),b);
		frame = JTetrisE.createFrame(tetris);
		frame.setVisible(true);
		System.out.println(BrainRater.getAverageScore(tetris) );

		b = new GioBrain(2);
		tetris = new JTetrisEO2(16,new BoardOptimized(1, 1),b);
		frame = JTetrisE.createFrame(tetris);
		frame.setVisible(true);
		System.out.println(BrainRater.getAverageScore(tetris) );
		
		b = new GioBrain(3);
		tetris = new JTetrisEO2(16,new BoardOptimized(1, 1),b);
		frame = JTetrisE.createFrame(tetris);
		frame.setVisible(true);
		System.out.println(BrainRater.getAverageScore(tetris) );
		
	
	}
	
	/**
	 Creates a frame with a JTetris.
	*/
	public static void main(String[] args) {
		// Set GUI Look And Feel Boilerplate.
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
	
		
		
		play();
		
		/*
		* ;
		 * 
		 */
		
	}

}
