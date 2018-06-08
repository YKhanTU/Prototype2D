package dgk.prototype.game;

import dgk.prototype.util.Vec2D;

import java.io.Serializable;
import java.util.ArrayList;

public class TileMap implements Serializable {

    /**
     * The map size will be 128x128 until proper loading functionality is added.
     */
    public static final transient int MAP_SIZE = 128;

    /**
     * The size of each Tile represented in the game. Some may be larger than 48, but this is
     * the default grid size for the TileMap.
     */
    public static final transient int TILE_SIZE = 48;

    /**
     * The limit of GameObjects that are allowed in the game.
     */
    public static final transient int OBJECT_LIMIT = 200;

    /**
     * Debug mode for testing purposes, including grids and highlights for development.
     */
    public static final transient boolean DEBUG_MODE = true;

    // TODO: Make the TileMap compatible for multiple layers per 64x64
    private Tile[][] tileMap;

    private ArrayList<GameObject> gameObjects;

    private int tileRenderCount = 0;

    public TileMap() {
        this.tileMap = new Tile[MAP_SIZE][MAP_SIZE];
        this.gameObjects = new ArrayList<GameObject>();
    }

    public boolean addTile(Tile tile, int gridX, int gridY) {
        if((gridX < 0 || gridY < 0) || (gridX > 127 || gridY > 127)) {
            throw new IndexOutOfBoundsException("You are attempting to place an object out of Tile Map bounds! (" + gridX + ", " + gridY + ")");
        }

        if(tileMap[gridX][gridY] != null) {
            return false;
        }

        tileMap[gridX][gridY] = tile;

        return true;
    }

    public void addGameObject(GameObject gameObject) {
        if(this.gameObjects.size() >= OBJECT_LIMIT)
            throw new IllegalStateException("You are attempting to add too many Game Objects to the world!");

        this.gameObjects.add(gameObject);
    }

    public void render() {
        GameCamera camera = GameWindow.getInstance().getWorldCamera();

        int sX = (int) Math.ceil(camera.getPosition().getX() / TileMap.TILE_SIZE);
        int sY = (int) Math.ceil(camera.getPosition().getY() / TileMap.TILE_SIZE);

        tileRenderCount = 0;

        for(int i = sX; i < ((800) / TileMap.TILE_SIZE) + sX; i++) {
            for(int j = sY; j < (600 / TileMap.TILE_SIZE) + sY; j++) {
                if(tileMap[i][j] != null) {
                    tileMap[i][j].render();
                    tileRenderCount++;
                }
            }
        }

        for(GameObject go: gameObjects) {
            Vec2D pos = go.getPosition();

            if(((pos.getX() >= camera.getPosition().getX()) && (pos.getX() <= camera.getPosition().getX() + camera.getWidth())) &&
                    (pos.getY() >= camera.getPosition().getY() && pos.getY() <= camera.getPosition().getY() + camera.getHeight())) {
                go.render();
            }
        }

    }

}
