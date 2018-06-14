package dgk.prototype.game.tile;

import dgk.prototype.game.*;
import dgk.prototype.util.Vec2D;

import java.io.Serializable;
import java.util.ArrayList;

public class TileMap implements Serializable {


    // TODO: Each TileMap Chunk is 128x128 or 64x64 tiles in length.
    // TODO: Create a 'Zone' or 'Bounds' class that represents a city's bounds or a zone where
    // TODO: a specific Enemy can be spawned.

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

    /**
     * Debug purposes only.
     */
    public static final transient boolean BUILDING_MODE = true;

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
            //throw new IndexOutOfBoundsException("You are attempting to place an object out of Tile Map bounds! (" + gridX + ", " + gridY + ")");
            return null;
        }

        if(tileMap[gridX][gridY] != null) {
            return tileMap[gridX][gridY];
        }

        System.out.println("You have grabbed a null tile from the TileMap.");
        return null;
    }

    public BuildingComponent getBuildingComponent(int gridX, int gridY) {
        for(GameObject go : gameObjects) {
            if(go instanceof BuildingComponent) {
                BuildingComponent component = (BuildingComponent) go;

                if(gridX == component.getGridX() && gridY == component.getGridY()) {
                    return component;
                }
            }
        }

        return null;
    }

    /**
     * Returns an array of length 4 of all possible neighbors of a tile.
     * DOES NOT INCLUDE DIAGONAL NEIGHBORS!
     * @param tile
     * @return Tile[] with a length of 4.
     */
    public Tile[] getNeighbors(Tile tile) {
        // An array of 4, assuming it's a tile of size 48
        Tile[] tiles = new Tile[4];

        int tX = tile.getGridX();
        int tY = tile.getGridY();

        tiles[0] = getTile(tX + 1, tY);
        tiles[1] = getTile(tX - 1, tY);
        tiles[2] = getTile(tX, tY + 1);
        tiles[3] = getTile(tX, tY - 1);

        return tiles;
    }

    /**
     * Called whenever a Tile is selected on the TileMap.
     * @param tile
     */
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

        int sX = (int) Math.ceil(camera.getPosition().getX() / TileMap.TILE_SIZE) - 1;
        int sY = (int) Math.ceil(camera.getPosition().getY() / TileMap.TILE_SIZE) - 1;

        tileRenderCount = 0;

        ArrayList<Tile> tileToArray = new ArrayList<Tile>();

        for(int i = sX; i < ((800) / TileMap.TILE_SIZE) + sX + 2; i++) {
            for(int j = sY; j < (600 / TileMap.TILE_SIZE) + sY + 2; j++) {
                if(i < 0 || j < 0)
                    continue;

                if(tileMap[i][j] != null) {

                    //tileToArray.add(tileMap[i][j]);

                    tileMap[i][j].render();
                    tileRenderCount++;
                }
            }
        }

        //System.out.println(tileRenderCount);

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

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

}
