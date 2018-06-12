package dgk.prototype.game;

import dgk.prototype.util.Vec2D;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

    // TODO: Make the TileMap compatible for multiple layers per 48x48
    private Tile[][] tileMap;

    // TODO: Render ordering
    private ArrayList<GameObject> gameObjects;

    private ArrayList<Tile> selectedTiles;

    private int tileRenderCount = 0;

    public TileMap() {
        this.tileMap = new Tile[MAP_SIZE][MAP_SIZE];
        this.gameObjects = new ArrayList<GameObject>();

        this.selectedTiles = new ArrayList<Tile>();
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

    public Tile getTile(int gridX, int gridY) {
        if((gridX < 0 || gridY < 0) || (gridX > 127 || gridY > 127)) {
            throw new IndexOutOfBoundsException("You are attempting to place an object out of Tile Map bounds! (" + gridX + ", " + gridY + ")");
        }

        if(tileMap[gridX][gridY] != null) {
            return tileMap[gridX][gridY];
        }

        System.out.println("You have grabbed a null tile from the TileMap.");
        return null;
    }

    public void onTileSelection(Tile tile) {
        if(tile.isSelected) {
            tile.isSelected = false;
            selectedTiles.remove(tile);
            return;
        }

        tile.isSelected = true;
        selectedTiles.add(tile);
    }

    public void addGameObject(GameObject gameObject) {
        if(this.gameObjects.size() >= OBJECT_LIMIT)
            throw new IllegalStateException("You are attempting to add too many Game Objects to the world!");

        this.gameObjects.add(gameObject);
    }

    public void onUpdate(World world) {
        for(GameObject go : gameObjects) {
            go.onUpdate();
        }
    }

    public void render(World world) {
        GameCamera camera = GameWindow.getInstance().getWorldCamera();

        int sX = (int) Math.ceil(camera.getPosition().getX() / TileMap.TILE_SIZE);
        int sY = (int) Math.ceil(camera.getPosition().getY() / TileMap.TILE_SIZE);

        tileRenderCount = 0;

        ArrayList<Tile> tileToArray = new ArrayList<Tile>();

        for(int i = sX; i < ((800) / TileMap.TILE_SIZE) + sX; i++) {
            for(int j = sY; j < (600 / TileMap.TILE_SIZE) + sY; j++) {
                if(tileMap[i][j] != null) {

                    //tileToArray.add(tileMap[i][j]);

                    tileMap[i][j].render();
                    tileRenderCount++;
                }
            }
        }

//        Collections.sort(tileToArray, new Comparator<Tile>() {
//            @Override
//            public int compare(Tile o1, Tile o2) {
//                if(o1.getRenderLayer() < o1.getRenderLayer()) {
//                    return 1;
//                }else{
//                    return -1;
//                }
//            }
//        });

//        for(Tile tile : tileToArray) {
//            tile.render();
//            tileRenderCount++;
//        }

        for(GameObject go: gameObjects) {
            Vec2D pos = go.getPosition();

            if(((pos.getX() >= camera.getPosition().getX()) && (pos.getX() <= camera.getPosition().getX() + camera.getWidth())) &&
                    (pos.getY() >= camera.getPosition().getY() && pos.getY() <= camera.getPosition().getY() + camera.getHeight())) {
                world.renderLayerList.add(go);
            }
        }

    }

}
