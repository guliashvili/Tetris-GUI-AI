// Brain.java -- the interface for Tetris brains

package Brains;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import Boards.BoardI;
import core.Piece;

public abstract class Brain{
    // Move is used as a struct to store a single Move
    // ("static" here means it does not have a pointer to an
    // enclosing Brain object, it's just in the Brain namespace.)
    public static class Move {
        public int x;
        public int y;
        public Piece piece;
        public double score;    // lower scores are better
        
        public LinkedList<Integer> list;
    }
    
    /**
     Given a piece and a board, returns a move object that represents
     the best play for that piece, or returns null if no play is possible.
     The board should be in the committed state when this is called.
     
     limitHeight is the height of the lower part of the board that pieces
     must be inside when they land for the game to keep going
      -- typically 20 (i.e. board.getHeight() - 4)
     If the passed in move is non-null, it is used to hold the result
     (just to save the memory allocation).
    */
    
    public  Brain.Move bestMove(BoardI board, Piece piece, int limitHeight, Brain.Move move,int currentX,int currentY){
        // Allocate a move object if necessary
        if (move==null) move = new Brain.Move();
        
        double bestScore = 1e20;
        State bestState = null;
        board.commit();
        
        Map<State, StateTrans> hm = new HashMap<State, StateTrans>();
        Queue<State> que = new LinkedList<State>();
        
        que.add(new State(piece,currentX,currentY,true));
        hm.put(new State(piece,currentX,currentY,true), new StateTrans(null, -1));
        
        que.add(new State(piece,currentX,currentY,false));
        hm.put(new State(piece,currentX,currentY,false), new StateTrans(null, -1));
        
        
        while(!que.isEmpty()){
        	State top = new State(que.peek());que.remove();
        	
        	
    		for(int i = DOWN; i <= NOTHING; i++){
    			if((i == DOWN) != (!top.myMove)) continue;
    			
        		State nxt = top.check(i, board);
        		if(nxt == null){
        			if(i == DOWN){
        				if(board.place(top.cur, top.X, top.Y) == BoardI.PLACE_ROW_FILLED) 
        					board.clearRows();
        				double score = rateBoard(board);
        				board.undo();
        			
        				if(score < bestScore){
        					bestScore = score;
        					bestState = top;
        				}
        			}
        					
        		}else{
        			if(!hm.containsKey(nxt)){
        				hm.put(nxt, new StateTrans(top, i));
        				que.add(nxt);
        			}
        		}
    		}
        	
        	
        }
        
        if(bestState == null) return null;
        
        
        move.x = bestState.X;
        move.y = bestState.Y;
        move.piece = bestState.cur;
        move.list = new LinkedList<Integer>();
        
        State cur = bestState;
        while(true){
        	StateTrans tmp = hm.get(cur);
        	if(tmp.s == null) break;
        	if(tmp.s.myMove) move.list.addFirst(tmp.trans);
        	else move.list.addFirst(-1);
        	
        	cur = tmp.s;
        }
        
        return move;
             
    }
    public  ArrayList<BoardI> getAllMoves(BoardI board, Piece piece, int currentX,int currentY){
        // Allocate a move object if necessary
        ArrayList<BoardI> ret = new ArrayList<BoardI>();
        board.commit();
        
        
        Set<Integer> hm = new HashSet<Integer>();
        Queue<State> que = new LinkedList<State>();
        
        que.add(new State(piece,currentX,currentY,true));
        hm.add(new State(piece,currentX,currentY,true).hashCode());
        
        que.add(new State(piece,currentX,currentY,false));
        hm.add(new State(piece,currentX,currentY,false).hashCode());
        
        
        while(!que.isEmpty()){
        	State top = new State(que.peek());que.remove();
        	
        	
    		for(int i = DOWN; i <= NOTHING; i++){
    			if((i == DOWN) != (!top.myMove)) continue;
    			
        		State nxt = top.check(i, board);
        		if(nxt == null){
        			if(i == DOWN){
        				if(board.place(top.cur, top.X, top.Y) == BoardI.PLACE_ROW_FILLED) 
        					board.clearRows();
        				
        				ret.add(board.getInstance(board));
        				
        				board.undo();
        			
        				
        			}
        					
        		}else{
        			if(!hm.contains(nxt.hashCode())){
        				hm.add(nxt.hashCode());
        				que.add(nxt);
        			}
        		}
    		}
        	
        	
        }
        
        return ret;
             
    }
  
    
    private class State{
    	public Piece cur;
    	public int X,Y;
    	public boolean myMove = true;
    	
    	@Override
    	public int hashCode(){
    		int hash = cur.hashCode() * 100000 + X * 343243 + Y ;
    		if(myMove) hash += 25329953;
    		return hash;
    	}
    	@Override
    	public boolean equals(Object o){
    		if(this == o) return true;
    		if(!(o instanceof State)) return false;
    		State other = (State) o;
    		
    		return (other.cur.equals(cur) && other.X == X && other.Y == Y && myMove == other.myMove);
    	}
    	public State(Piece a,int x,int y,boolean myMove){
    		cur = a;
    		X = x;
    		Y = y;
    		this.myMove = myMove;
    	}
    	public State(State a){
    		cur = a.cur;
    		X = a.X;
    		Y = a.Y;
    		myMove = a.myMove;
    	}
    	
        public State check(int move,BoardI board){
        	
    		State tmp = new State(this);
    		tmp.myMove = !tmp.myMove;
    		if(move == NOTHING)
    			return tmp;
    		
    		switch(move){
    		case DOWN:
    			tmp.Y--;
    			break;
    		case LEFT:
    			tmp.X--;
    			break;
    		case RIGHT:
    			tmp.X++;
    			break;
    		case ROTATE:
    			tmp.cur = tmp.cur.fastRotation();
    			break;
    		default:
    			throw new RuntimeException("illefal mmove");
    		}
    		
    		boolean chk = board.place(tmp.cur, tmp.X, tmp.Y) <= BoardI.PLACE_ROW_FILLED;
    		board.undo();
    		
    		if(chk)			
    			return tmp;
    		else
    			return null;
    		
        }
    }
    
    private class StateTrans{
    	public State s;
    	public int trans;
    	
    	public StateTrans(State s,int trans){
    		this.trans = trans;
    		this.s = s;
    	}
    };
    
    
    public static final int DOWN = 0;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int ROTATE = 3;
	public static final int NOTHING = 4;
    
    


    
    
   // public  abstract Brain getInstance();
    
    /*
    A simple brain function.
    Given a board, produce a number that rates
    that board position -- larger numbers for worse boards.
    This version just counts the height
    and the number of "holes" in the board.
   */
   public abstract double rateBoard(BoardI board);
}
