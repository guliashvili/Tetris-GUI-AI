package JTetrises;

import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
	protected Brain AI,sec=null;
	private JPanel little;
	private JSlider adversary;
	protected JLabel adversaryOk;
	
	
	public JTetrisEO2(int pixels,BoardI board,Brain brain) {
		super(pixels,board);
		AI = brain;
	}
	
	@Override
	protected void enableButtons() {
		super.enableButtons();

	};
	
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
			int ret = playAi();
			if(ret != -1) super.tick(ret);
			try {
				Thread.sleep(timer.getDelay());
			} catch (InterruptedException e) {
			}
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
					Brain use = AI;
					if(use == null){
						try{
							//System.out.println(combo.getSelectedItem());
							use = (Brain) Class.forName("Brains."+(String)combo.getSelectedItem()).newInstance();
						}catch(Exception e){}
					}
					cur = use.bestMove(board, p, HEIGHT, cur, currentX,currentY);
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
	private int playAi(){
		if(currentPiece == null) return -1;
		
		board.undo();
		if(lastCount != count){
			lastCount = count;
			
			Brain use = AI;
			if(use == null){
				try{
					//System.out.println(combo.getSelectedItem());
					use = (Brain) Class.forName("Brains."+(String)combo.getSelectedItem()).newInstance();
				}catch(Exception e){}
			}
			target = use.bestMove(board, currentPiece, HEIGHT, target,currentX,currentY);
		}
		if(target == null) return -1;
			
		if(target.list.isEmpty()) return -1;
		
		if(target.list.getFirst() == -1){
			target.list.removeFirst();
			return -1;
			
		}else{
			int ret;
			switch(target.list.getFirst()){
			case Brain.DOWN:
				ret = JTetrisE.DOWN;
				break;
			case Brain.LEFT:
				ret = JTetrisE.LEFT;
				break;
			case Brain.RIGHT:
				ret = JTetrisE.RIGHT;
				break;
			case Brain.ROTATE:
				ret = JTetrisE.ROTATE;
				break;
			case Brain.NOTHING:
				ret = -1;
				break;
			default:
				throw new RuntimeException("invalid move");
			}
			target.list.removeFirst();
			if(!target.list.isEmpty()){
				if(target.list.getFirst() != -1) throw new RuntimeException("something wrong");
				target.list.removeFirst();
			}
			return ret;
		}
		
	
		
	}

	
}
