package core;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.UIManager;

import evolution.BrainRaterOptimized;
import evolution.Evolution;
import Boards.BoardI;
import Boards.BoardOptimized;
import Brains.*;
import JTetrises.*;

public class App {

	

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
	private static void evol2(){
		Evolution e = new Evolution(new GioBrain(), new BoardOptimized(1, 1));
		e.evoluate();
	}
	public static void test() {
		BoardOptimized b = new BoardOptimized(20,20);
		b.place(Piece.getPieces()[0], 0, 0);
		GioBrain br = new GioBrain();
		ArrayList<BoardI> ar = br.getAllMoves(b, Piece.getPieces()[1],7, 7);
		for(BoardI b1 : ar){
			b1.sanityCheck();
			System.out.println(b1);
			b1.place(Piece.getPieces()[3], 6, 6);
			System.out.println(b1);
			b1.undo();
			System.out.println(b1);
			System.out.println("enddd");
		}
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
