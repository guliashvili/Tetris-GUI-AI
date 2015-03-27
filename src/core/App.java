package core;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class App {

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
		
		JTetrisEO2 tetris;
		JFrame frame;
		
		/*
		tetris = new JTetrisEO2(16,new BoardOptimized(1, 1),new DefaultBrain());
		frame = JTetrisE.createFrame(tetris);
		//frame.setVisible(true);
		
		System.out.println(BrainRater.getAverageScore(tetris) );
		*/
		tetris = new JTetrisEO2(16,new BoardOptimized(1, 1),new MariBrain());
		frame = JTetrisE.createFrame(tetris);
		frame.setVisible(true);
		
		//System.out.println(BrainRater.getAverageScore(tetris) );
		
		
	}

}
