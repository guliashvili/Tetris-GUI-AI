package Brains;

import Boards.BoardI;
/*
 * idea Mariam Akhvlediani (Student of Martva :-D )
 * Written by Me
 * 
 */
public class MariBrain extends Brain {


/*	@Override
	public Brain getInstance() {
		return new MariBrain();
	}*/

	@Override
	public double rateBoard(BoardI board) {
		double ret = 0;
		double y = 0;
		for(int i = 0; i < board.getWidth();i++)
			for(int j = 0; j < board.getColumnHeight(i);j++)
				if(board.getGrid(i, j) == false) ret++;
				else y += j;
		if(board.getMaxHeight() > board.getHeight()/2){ 
			return ret * 100 + y + board.getMaxHeight()*10000;
		}else 
			return ret*10000 + y;
	}

}
