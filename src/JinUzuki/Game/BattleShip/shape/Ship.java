/**
 * 
 */
package JinUzuki.Game.BattleShip.shape;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import JinUzuki.Game.BattleShip.Data.Position;
import JinUzuki.Game.BattleShip.Data.shipdir;
import at.bartinger.candroid.renderable.Sprite;
import at.bartinger.candroid.texture.Texture;

/**
 * @author v-alajin
 *
 */

public class Ship extends Sprite {
	
	public shipdir direct;
	public int size;	//
	public int type;
	public Position pos;
	public int cellSize;
	
	private int dmg;
	public boolean destoryed = false;

	
	
	/**
	 * @param tex
	 * @param x
	 * @param y
	 */
	public Ship(Texture tex, int x, int y, int shipSize, int cSize, shipdir dir, String sid) {
		super(tex, x, y);
		
		pos = new Position(0,0);
		
		this.size = shipSize;
		this.direct = dir;
		this.cellSize = cSize;
		this.id = sid;
		this.dmg = 0;
		
		if(direct == dir.Vertical){
			this.rotate(this.width, 0, 270);
		}
		
		this.Snaping(x, y);
		this.getBuffer();
	}

	/**
	 * @param tex
	 * @param x
	 * @param y
	 * @param id
	 */
	public Ship(Texture tex, int x, int y, String id) {
		super(tex, x, y, id);
		// TODO Auto-generated constructor stub
	}
	
	public void randomPos(int[][] tGrid){
		Random rand = new Random();
		boolean radDir = rand.nextBoolean();
		int radX = 0;
		int radY = 0;
		if(radDir)	{
			if(direct == shipdir.Vertical)	rotate();
			radX = rand.nextInt(10-size-1);
			radY = rand.nextInt(9);
			
		}
		else{
			if(direct == shipdir.Horizon)	rotate();
			radX = rand.nextInt(9);
			radY = rand.nextInt(10-size-1);
		}
		
		pos.x = radX;
		pos.y = radY;
		
		this.x = cellSize * radX;
		this.y = cellSize * radY;
		
	}
	
	public void rotate(){
		if(direct == shipdir.Horizon)	direct = shipdir.Vertical;
		else	direct = shipdir.Horizon;
		
		this.rotate(this.width, 0, 90);
		Snaping((int)this.x,(int)this.y);
	}

	public void Snaping(int dx,int dy){
		int snapX = 0;
		int snapY = 0;

		int markX = 0,markY = 0;
		int grandX = 10000;
		int grandY = 10000;
		
		for(int i=0; i<10 ; i++){
			int disX = Math.abs(dx - cellSize*i);
			int disY = Math.abs(dy - cellSize*i);
			if(disX < grandX && disX >= 0){
				grandX = disX;
				markX = i;
			}
			if(disY < grandY && disY >= 0){
				grandY = disY;
				markY = i;
			}			
		}

		if(dx < 0) snapX = 0;
		if(dy < 0) snapY = 0;
		
		if(direct == shipdir.Horizon && markX > (10 - size)){
			markX = 10 - size;
		}
		
		if(direct == shipdir.Vertical && markY > (10 - size)){
			markY = 10 - size;
		}
		
		pos.x = markX;
		pos.y = markY;
		
		snapX = cellSize * markX;
		snapY = cellSize * markY;
		
		this.x = snapX;
		this.y = snapY;
	}
	
	public ArrayList<Position> getHolder(){
		ArrayList<Position> holder = new ArrayList<Position>();
		if(this.direct == shipdir.Horizon){
			for(int i=pos.x;i<pos.x+this.size;i++){
				Position hPos = new Position(i,pos.y);
				holder.add(hPos);	
			}
		}
		else{
			for(int i=pos.y;i<pos.y+this.size;i++){
				Position hPos = new Position(pos.x,i);
				holder.add(hPos);	
			}
		}
		return holder;
	}
	
	public ArrayList<Position> getBuffer(){
		ArrayList<Position> buffer = new ArrayList<Position>();
		
		if(this.direct == shipdir.Horizon){
			for(int i=pos.x-1;i<pos.x+this.size+1;i++){
				if(i<0 || i>=10) continue;
				
				for(int j=pos.y-1; j<pos.y + 2 ; j++){
					if(j<0 || j>=10) continue;
					
					Position bPos = new Position(i,j);
					buffer.add(bPos);					
				}
			}
		}
		else{
			for(int i=pos.y-1;i<pos.y+this.size+1;i++){
				if(i<0 || i>=10) continue;
				
				for(int j=pos.x-1; j<pos.x + 2 ; j++){
					if(j<0 || j>=10) continue;
					
					Position bPos = new Position(j,i);
					buffer.add(bPos);					
				}
			}	
		}
		
		return buffer;
	}
	
	
	public boolean Damage(){
		this.dmg++;
		
		if(this.dmg == this.size)
		{
			destoryed = true;
			this.setVisibility(true);
			return true;
		}
		else 
			return false;
	}

}
