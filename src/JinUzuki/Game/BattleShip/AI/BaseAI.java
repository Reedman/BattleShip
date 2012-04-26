/**
 * 
 */
package JinUzuki.Game.BattleShip.AI;

import java.util.ArrayList;

import android.util.Log;

import JinUzuki.Game.BattleShip.Data.Position;
import JinUzuki.Game.BattleShip.shape.Board;
import JinUzuki.Game.BattleShip.shape.Ship;

/**
 * @author v-alajin
 * ship is 5 4 3 3 2
 */
public class BaseAI {
	
	protected Board nb = null;
	protected ArrayList<Ship> fleet = null;
	protected ArrayList<Position> candidate = null;
	protected int[][] tGrid = new int[10][10];
	protected int[][] pGrid = new int[10][10];
	protected Position prePos = null;
	  
	
	public BaseAI(){
		candidate = new ArrayList<Position>();
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				Position pos = new Position(i,j);
				candidate.add(pos);
			}
		}
		//at the beginning , all cell proier is 1 , if 0 its a dead cell
		for(int i=0;i <10;i++)	for(int j=0;j <10;j++)	tGrid[i][j] = 1; 
		
		prePos = new Position(-1,-1);
	}
	
	public void removeCandidate(int x,int y){
		for(Position pos:candidate){
			if(pos.x == x && pos.y == y){
				candidate.remove(pos);
				return;
			}
		}
	}
	
	public void removeCandidate(Ship ship){
		ArrayList<Position> nPos = ship.getBuffer();
		for(Position pos:nPos){
			this.removeCandidate(pos.x, pos.y);
			tGrid[pos.x][pos.y] = 0 ;
		}
	}
	
	public boolean existCandidate(int x,int y){
		for(Position pos:candidate){
			if(pos.x == x && pos.y == y){
				return true;
			}
		}
		return false;
	}
	
	//mark a Hit
	public void markHit(int x,int y){
		if(VerifyHit(x-1,y)) tGrid[x-1][y] = 2;
		if(VerifyHit(x+1,y)) tGrid[x+1][y] = 2;
		if(VerifyHit(x,y-1)) tGrid[x][y-1] = 2;
		if(VerifyHit(x,y+1)) tGrid[x][y+1] = 2;
		tGrid[x][y] = 0 ;
		
		setPreHit(x,y);
	}
	
	public Position findDistinctTwo(){
		for(int i=0;i <10;i++){
			for(int j=0;j <10;j++){
				if(tGrid[i][j] == 2 && existCandidate(i,j)){
					if(VerifyNearby(i,j))
						return new Position(i,j);
				}
			}
		}
		return null;
	}

	/**查找四周优先级是否包含为2
	 * @param x
	 * @param y
	 * @return true 是独立点， false 非独立点
	 */
	public boolean VerifyNearby(int x,int y){
		if(VerifyDir(x-1,y)){
			if(tGrid[x-1][y] == 2)	return false;
		}
		if(VerifyDir(x+1,y)){
			if(tGrid[x+1][y] == 2)	return false;
		}
		if(VerifyDir(x,y-1)){
			if(tGrid[x][y-1] == 2)	return false;
		}
		if(VerifyDir(x,y+1)){
			if(tGrid[x][y+1] == 2)	return false;
		}
		return true;
	}
	
	public boolean VerifyDir(int x,int y){
		if(x >=0 && x <10 && y >=0 && y < 10) return true;
		else return false;
	}
	
	public boolean VerifyHit(int x,int y){
		//if(x >=0 && x <10 && y >=0 && y < 10 && tGrid[x][y] != 0) return true;
		if(x >=0 && x <10 && y >=0 && y < 10 && (prePos.x != x || prePos.y != y)) return true;
		else return false;
	}
	
	public void clearMark(){
		for(int i=0;i <10;i++)	for(int j=0;j <10;j++) if(tGrid[i][j] == 2)	tGrid[i][j] = 1; 
	}
	
	public void updateBattleField(Board cb,ArrayList<Ship> ships){
		this.fleet = ships;
		this.nb = cb;
	}
	
	public void setPreHit(int x, int y){
		prePos.x = x;
		prePos.y = y;
	}
	
	public Position askForTarget(){
		
		this.removeCandidate(0,3);
		return new Position(0,3);
	}
	
	public void recycle(){
		nb = null;
		fleet = null;
		candidate = null;
		tGrid = null;
		pGrid = null;
		prePos = null;
	}
}
