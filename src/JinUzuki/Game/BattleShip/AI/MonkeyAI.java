/**
 * 
 */
package JinUzuki.Game.BattleShip.AI;

import java.util.Random;

import android.util.Log;

import JinUzuki.Game.BattleShip.Data.Position;

/**
 * @author v-alajin
 *	this AI is no brain , total random
 */
public final class MonkeyAI extends BaseAI {

	/**
	 * 
	 */
	public MonkeyAI() {
		super();
	}
	
	@Override
	public Position askForTarget(){
		Random rd = new Random();
		
		int idx = rd.nextInt(this.candidate.size()-1);
		int rdmX = this.candidate.get(idx).x;
		int rdmY = this.candidate.get(idx).y;
		
		//Log.d("MonkeyAI",Integer.toString(rdmX) + "," + Integer.toString(rdmY));
		
		this.removeCandidate(rdmX,rdmY);
		return new Position(rdmX,rdmY);
	}

}
