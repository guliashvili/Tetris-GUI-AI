package JTetrises;


public class BrainRater {
	private static final int N_TEST = 20;
	
	
	public static double getAverageScore(JTetrisEO2 tetris){
		tetris.brainMode.setSelected(true);
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
		
		
		
		return score;
	}

}
