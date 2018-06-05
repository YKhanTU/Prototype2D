package dgk.prototype.game;

import java.io.Serializable;

public class TileMap implements Serializable {

    /**
     * The map size will be 128x128 until proper loading functionality is added.
     */
    public static final transient int MAP_SIZE = 128;

    /**
     * Debug mode for testing purposes, including grids and highlights for development.
     */
    public static final transient boolean DEBUG_MODE = true;

    // TODO: Make the TileMap compatible for multiple layers per 64x64
    private Block[][] tileMap;

    public TileMap() {
        this.tileMap = new Block[MAP_SIZE][MAP_SIZE];
    }

    public void render() {
        GameCamera camera = GameWindow.getInstance().getWorldCamera();

        int sX = (int) Math.ceil(camera.getPosition().getX() / World.GRID_SIZE);
        int sY = (int) Math.ceil(camera.getPosition().getY() / World.GRID_SIZE);

        for(int i = sX; i < (800 / World.GRID_SIZE); i++) {
            for(int j = sY; j < (600 / World.GRID_SIZE); j++) {
                if(tileMap[i][j] != null) {
                    tileMap[i][j].render();
                }
            }
        }

    }

}
