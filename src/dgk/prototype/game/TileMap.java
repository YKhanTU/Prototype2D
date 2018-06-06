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
     * The limit of GameObjects that are allowed in the game.
     */
    public static final transient int GAMEOBJECT_LIMIT = 200;

    /**
     * Debug mode for testing purposes, including grids and highlights for development.
     */
    public static final transient boolean DEBUG_MODE = true;

    // TODO: Make the TileMap compatible for multiple layers per 64x64
    private Block[][] tileMap;

    private ArrayList<GameObject> gameObjects;

    public TileMap() {
        this.tileMap = new Block[MAP_SIZE][MAP_SIZE];
        this.gameObjects = new ArrayList<GameObject>();
    }

    public boolean addTile(Block block, int gridX, int gridY) {
        if((gridX < 0 || gridY < 0) || (gridX > 127 || gridY > 127)) {
            throw new IndexOutOfBoundsException("You are attempting to place an object out of Tile Map bounds! (" + gridX + ", " + gridY + ")");
        }

        if(tileMap[gridX][gridY] != null) {
            return false;
        }

        tileMap[gridX][gridY] = block;

        return true;
    }

    public void addGameObject(GameObject gameObject) {
        this.gameObjects.add(gameObject);
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

        for(GameObject go: gameObjects) {
            Vec2D pos = go.getPosition();

            if(((pos.getX() >= camera.getPosition().getX()) && (pos.getX() <= camera.getPosition().getX() + camera.getWidth())) &&
                    (pos.getY() >= camera.getPosition().getY() && pos.getY() <= camera.getPosition().getY() + camera.getHeight())) {
                System.out.println("poop");
                go.render();
            }
        }

    }

}
