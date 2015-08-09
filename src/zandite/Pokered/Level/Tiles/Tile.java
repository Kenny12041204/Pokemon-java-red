package zandite.Pokered.Level.Tiles;

import zandite.Pokered.GFX.Colours;
import zandite.Pokered.GFX.Screen;
import zandite.Pokered.Level.Level;

public abstract class Tile {

    public static final Tile[] tiles = new Tile[256];
    public static final Tile VOID = new BaseTile(0, 6, 0, Colours.get(000, -1,
                    -1, -1));
    public static final Tile STONE = new BaseTile(1, 7, 0, Colours.get(-1,
            333, -1, -1));
    public static final Tile GRASS = new BaseTile(2, 8, 0, Colours.get(-1,
                    131, 141, -1));

    protected byte id;
    protected boolean solid;
    protected boolean emitter;

    public Tile(int id, boolean isSolid, boolean isEmitter) {
            this.id = (byte) id;
            if (tiles[id] != null) {
                    throw new RuntimeException("Duplicate tile id on" + id);
            }
            this.solid = isSolid;
            this.emitter = isEmitter;
            tiles[id] = this;
    }

    public byte getId() {
            return id;
    }

    public boolean isSolid() {
            return solid;
    }

    public boolean isEmitter() {
            return emitter;
    }

    public abstract void render(Screen screen, Level level, int x, int y);

}