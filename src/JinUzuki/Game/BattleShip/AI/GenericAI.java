/**
 * 
 */
package JinUzuki.Game.BattleShip.AI;

import java.util.Random;

import android.util.Log;
import JinUzuki.Game.BattleShip.Data.Position;
import JinUzuki.Game.BattleShip.shape.Ship;

/**
 * @author v-alajin
 *
 */
public class GenericAI extends BaseAI {

	/**
	 * 
	 */
	public GenericAI() {
		super();
	}
	
	//We haven¡¯t hit anything yet,
	//we¡¯ve hit once,
	//we¡¯ve hit more than once

	
	@Override
	public Position askForTarget(){

		int rdmX = 0;
		int rdmY = 0;
		
		Position pos = findDistinctTwo();
		if(pos != null){
			rdmX = pos.x;
			rdmY = pos.y;
		}
		else{
			Random rd = new Random();
			int idx = rd.nextInt(this.candidate.size()-1);
			rdmX = this.candidate.get(idx).x;
			rdmY = this.candidate.get(idx).y;
			
			int maxLoop = 0;
			while(!checkPosibility(rdmX,rdmY)){
				idx = rd.nextInt(this.candidate.size()-1);
				rdmX = this.candidate.get(idx).x;
				rdmY = this.candidate.get(idx).y;
				
				//Log.d("GenericAI","NotPossible:" + Integer.toString(rdmX) + "," + Integer.toString(rdmY));
				
				maxLoop++;
				if(maxLoop >= 100)
					break;
			}

		}
		
		//Log.d("GenericAI",Integer.toString(rdmX) + "," + Integer.toString(rdmY));
		
		this.removeCandidate(rdmX,rdmY);
		return new Position(rdmX,rdmY);
	}
	
	public Boolean checkPosibility(int x, int y){
		int minSize = 5;
		for(Ship ship:fleet){
			if(ship.destoryed == false && ship.size < minSize)
				minSize = ship.size;
		}
		
		//Log.d("GenericAI","MinShipSize:" + Integer.toString(minSize));
		
		return nb.checkPosibility(minSize,x,y);
	}

}
