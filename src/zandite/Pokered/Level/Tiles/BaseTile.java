package zandite.Pokered.Level.Tiles;

import zandite.Pokered.GFX.Screen;
import zandite.Pokered.Level.Level;

public class BaseTile extends Tile {

	protected int tileId;
	protected int tileColor;
	
	public BaseTile(int id, int x, int y, int tileColor) {
		super(id, false, false);
		this.tileId = x + y;
		this.tileColor = tileColor;
	}

	
	public void render(Screen screen, Level level, int x, int y) {
		screen.render(x, y, tileId, tileColor, false, false);
	}

}
