package core;

import javax.swing.JFrame;
import javax.swing.UIManager;

import evolution.BrainRaterOptimized;
import evolution.Evolution;
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
		
		
		tetris = new JTetrisEO2(16,new BoardOptimized(1, 1),null);
		frame = JTetrisE.createFrame(tetris);
		frame.setVisible(true);
	}

	
	private static void bro(){
		BrainRaterOptimized b = new BrainRaterOptimized(new BoardOptimized(1, 1), new GioBrain());
		System.out.print(b.getAverageScore());
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
		
	}

}
