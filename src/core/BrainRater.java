package core;

public class BrainRater {
	private static final int N_TEST = 30;
	
	
	public static String getAverageScore(JTetrisEO2 tetris){
		tetris.brainMode.setSelected(true);
		String ret = new String();
		double score = 0;
		
		for(int i = 0; i < N_TEST; i++){
			tetris.stopGame();
			tetris.startGame();
			tetris.speed.setValue(tetris.speed.getMaximum());
			while(tetris.gameOn){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {}
			}
			score += tetris.score;
			
		}
		score /= N_TEST;
		
		ret = tetris.AI.getClass().getName() + "  score = " + score;  
		
		
		return ret;
	}

}
