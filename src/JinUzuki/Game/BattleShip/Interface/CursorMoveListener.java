/**
 * 
 */
package JinUzuki.Game.BattleShip.Interface;

import JinUzuki.Game.BattleShip.Data.Position;

/**
 * @author v-alajin
 *
 */
public interface CursorMoveListener {
	public abstract void onCursorMoved(Position tgt);
}
