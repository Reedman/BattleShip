/**
 * 
 */
package JinUzuki.Game.BattleShip.Utility;

import java.util.ArrayList;

import android.util.Log;

import JinUzuki.Game.BattleShip.Data.Position;
import JinUzuki.Game.BattleShip.Data.shipdir;
import JinUzuki.Game.BattleShip.shape.Ship;

/**
 * @author v-alajin
 *
 */
public class Utility {

	/**
	 * 
	 */
	public static boolean allDestoryed(ArrayList<Ship> ships){
		
		boolean flag = true;
		
		for(Ship ship:ships){
			if(!ship.destoryed) flag = false;
		}
		
		return flag;
	}
	
	public static void prepareFleet(ArrayList<Ship> ships){
		randomShip(ships);
		//boolean inter = isIntersect(ships);
		//boolean touch = isTouch(ships);
		//while(inter == true || touch == true){
		//	randomShip(ships);
		//	inter = isIntersect(ships);
		//	touch = isTouch(ships);
		//}
	}
	
	public static void randomShip(ArrayList<Ship> ships){

		int[][] tGrid = new int[10][10];
		for(int i=0;i<10;i++)	for(int j=0;j<10;j++)	tGrid[i][j] = 0; 
			
		for(Ship ship:ships){
			ship.randomPos(tGrid);
			while(verifyPos(tGrid,ship.getHolder()) == true){
				ship.randomPos(tGrid);
			}
			
			for(Position pos:ship.getBuffer()){
				tGrid[pos.x][pos.y] = 1;
			}
		}
	}
	
	public static boolean verifyPos(int[][] tGrid,ArrayList<Position> posArray){
		for(Position pos:posArray){
			if(tGrid[pos.x][pos.y] == 1) return true;
		}
		return false;
	}
	
	public static boolean fleetReady(ArrayList<Ship> ships){
		int[][] tGrid = new int[10][10];
		for(int i=0;i<10;i++)	for(int j=0;j<10;j++)	tGrid[i][j] = 0; 
		
		for(Ship ship:ships){
			
			if(verifyPos(tGrid,ship.getHolder()) == true){
				return false;
			}
			
			for(Position pos:ship.getBuffer()){
				tGrid[pos.x][pos.y] = 1;
			}
		}
		return true;
	}
	
	public static boolean isIntersect(ArrayList<Ship> ships){
		int[][] tGrid = new int[10][10];
		for(int i=0;i <10;i++)	for(int j=0;j <10;j++)	tGrid[i][j] = 0; 
		  
		for(Ship ship:ships){
			FillGrid(ship,tGrid);
		}
		
		for(int i=0;i <10;i++)	for(int j=0;j <10;j++)	if(tGrid[i][j] > 1){
			return true;
		}
		
		return false;
	}
	
	public static boolean isTouch(ArrayList<Ship> ships){
		String[][] tGrid = new String[10][10];
		for(int i=0;i <10;i++)	for(int j=0;j <10;j++)	tGrid[i][j] = ""; 
		
		for(Ship ship:ships){
			FillGrid(ship,tGrid);
		}
		
		String mark = "";
		for(int i=0;i<10;i++){	
			mark = "";
			for(int j=0;j <10;j++){	
				if(mark != tGrid[i][j] && tGrid[i][j] != "" && mark != ""){
					//Log.e("isTouch", "true");
					return true;
				}
				mark = tGrid[i][j];
			}
		}
		
		for(int i=0;i<10;i++){	
			mark = "";
			for(int j=0;j <10;j++){	
				if(mark != tGrid[j][i] && tGrid[j][i] != "" && mark != ""){
					//Log.e("isTouch", "true");
					return true;
				}
				mark = tGrid[j][i];
			}
		}
		
		//Log.e("isTouch", "false");
		return false;
	}
	
	public static void FillGrid(Ship ship, String[][] grid){
		if(ship.direct == shipdir.Horizon){
			for(int i= ship.pos.x ; i< ship.pos.x + ship.size; i++){
				grid[i][ship.pos.y] = ship.id;
			}
		}
		else if(ship.direct == shipdir.Vertical){
			for(int i= ship.pos.y ; i< ship.pos.y + ship.size; i++){
				grid[ship.pos.x][i] = ship.id;
			}
		}
	}
	
	public static void FillGrid(Ship ship, int[][] grid){
		if(ship.direct == shipdir.Horizon){
			for(int i= ship.pos.x ; i< ship.pos.x + ship.size; i++){
				grid[i][ship.pos.y] += 1;
			}
		}
		else if(ship.direct == shipdir.Vertical){
			for(int i= ship.pos.y ; i< ship.pos.y + ship.size; i++){
				grid[ship.pos.x][i] += 1;
			}
		}
	}

}
