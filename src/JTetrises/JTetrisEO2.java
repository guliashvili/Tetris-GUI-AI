package JTetrises;

import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import core.Piece;
import Boards.BoardI;
import Brains.Brain;



@SuppressWarnings("serial")
public class JTetrisEO2 extends JTetrisEO1 {

	
	protected JCheckBox brainMode;
	protected Brain AI;
	private JPanel little;
	private JSlider adversary;
	protected JLabel adversaryOk;
	
	public JTetrisEO2(int pixels,BoardI board,Brain brain) {
		super(pixels,board);
		AI = brain;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public JComponent createControlPanel(){
		JComponent panel = super.createControlPanel();
		panel.add(new JLabel("Brain:")); 
		brainMode = new JCheckBox("Brain active"); 
		panel.add(brainMode);
		
		little = new JPanel(); 
		little.add(new JLabel("Adversary:"));
		adversary = new JSlider(0, 100, 0); // min, max, current
		adversary.setPreferredSize(new Dimension(100,15));
		little.add(adversary);
		
		adversaryOk = new JLabel();
		little.add(adversaryOk);
		
		panel.add(little);
		
		return panel;
	}
	
	@Override 
	public void tick(int verb) {
		if(brainMode.isSelected() && verb == DOWN){
			playAi();
		}
		super.tick(verb);
	}
	@Override 
	public Piece pickNextPiece() {
		Piece bs = super.pickNextPiece();
		if( (int) (100 * random.nextDouble()) < adversary.getValue()){
			adversaryOk.setText("*ok*");
			
			
			Brain.Move bst = null;
			Brain.Move cur = null;
			for(Piece p : Piece.getPieces()){
				do{
					cur = AI.bestMove(board, p, HEIGHT, cur);
					if(cur != null){
						if(bst == null) 
							bst = cur;
						else if(bst.score < cur.score){
							bst = cur;
							bs = p;
						}
					}
					
					p = p.fastRotation();
				}while(!p.equals(p));
			}
			
			
		}else
			adversaryOk.setText("ok");
		
		
		return bs;
	}
	
	
	private int lastCount = -1;
	private Brain.Move  target = null;
	private void playAi(){
		if(currentPiece == null) return;
		
		board.undo();
		if(lastCount != count){
			lastCount = count  ;
			target = AI.bestMove(board, currentPiece, HEIGHT, target);
		}
		if(target == null) return;
		
		if(!target.piece.equals(currentPiece)){
			currentPiece = currentPiece.fastRotation();
		}else if(target.x < currentX){
			currentX--;
		}else if(target.x > currentX)
			currentX++;
		
	}
}
