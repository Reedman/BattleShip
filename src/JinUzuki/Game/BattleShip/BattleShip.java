/**
 * 
 */
package JinUzuki.Game.BattleShip;

import android.app.Application;
import JinUzuki.Game.BattleShip.Data.gameSetting;

/**
 * @author v-alajin
 *
 */
public class BattleShip extends Application {

	gameSetting gameSet = null;
	
	/**
	 * 
	 */
	public BattleShip() {
		gameSet = new gameSetting();
		gameSet.bPlaySound = true;
		gameSet.bVibrate = true;
	}
	
	public void setGameSetting(gameSetting gs){
		gameSet = gs;
	}
	
	public gameSetting getGameSetting(){
		return gameSet;
	}

}
