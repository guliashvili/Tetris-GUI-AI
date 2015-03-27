package JTetrises;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;

import Boards.BoardI;

@SuppressWarnings("serial")
public class JTetrisEO1 extends JTetrisE {
	protected JButton pauseButton;
	protected boolean paused = false;
	protected long pauseStart;
	

	@Override
	protected void enableButtons(){
		super.enableButtons();
		if(gameOn)
			pauseButton.setVisible(true);
		else 
			pauseButton.setVisible(false);
		if(paused){
			startButton.setEnabled(false);
			stopButton.setEnabled(false);
		}
	}
	
	@Override
	public void tick(int verb){
		if(paused) return;
		else super.tick(verb);
	}
	@Override
	public JComponent createControlPanel(){
		JComponent panel = super.createControlPanel();
		
		// PAUSE button
		panel.add(pauseButton);
		pauseButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				paused = !paused;
				if(paused){
					pauseButton.setText("Unpause");
					timer.stop();
					pauseStart =   System.currentTimeMillis();
					
					
				}else{
					startTime += System.currentTimeMillis() - pauseStart;
					timer.start();
					pauseButton.setText("Pause");
				}
				
				enableButtons();
			}
		});
		
		return panel;
	}
	
	

	JTetrisEO1(int pixels, BoardI board) {

		super(pixels, board);
		pauseButton = new JButton("Pause");
		
	}

}
