package dgk.prototype.game;

import dgk.prototype.util.SpriteSheet;
import dgk.prototype.util.Vec2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class World {

    public static final byte LOWER_LAYER = 0;
    public static final byte MID_LAYER = 1;
    public static final byte UPPER_LAYER = 2;

    private ArrayList<Entity> entities;
    private TileMap tileMap;

    public Ruler ruler;

    public World() {
        this.entities = new ArrayList<Entity>();
        this.tileMap = new TileMap();
    }

    /**
     * Eventually this function will load data stored on the computer of all
     * of the Entities -> GameObjects, Persons, Item data, and so on.
     *
     * TODO: Block Types
     *  TODO:   AnimatableTile (Buildings, etc.) Walls, StaticTile (Scenery), DynamicTile (Animatable + Physics Oriented Tile), DestructibleTile (Animatable + Destruction Functionality)
     *
     */
    public void load() {
        ruler = new Ruler(32, 64);

        //GameWindow.getInstance().getWorldCamera().setTarget(ruler);

        for(int i = 0; i < 32; i++) {
            for(int j = 0; j < 32; j++) {
                addTile(new Tile(SpriteSheet.GRASS, World.LOWER_LAYER, i * 48, j * 48, 48), i, j);
            }
        }

        addGameObject(new Tile(SpriteSheet.SHRUB, World.MID_LAYER, 200, 200, 48));

        int y = 7;

        for(int i = 0; i < 5; i++) {
            int p = i + 3;

            addGameObject(new Tile(SpriteSheet.WOOD_WALL_NORTH_1B, World.UPPER_LAYER, p * 48, (y - 1) * 48, 48));
            addGameObject(new Tile(SpriteSheet.WOOD_WALL_NORTH_1A, World.UPPER_LAYER, p * 48, y * 48, 48));
            addGameObject(new Tile(SpriteSheet.WOOD_WALL_NORTH_1C, World.UPPER_LAYER, p * 48, (y + 1) * 48, 48));
        }

        entities.add(ruler);
        entities.add(new Peasant(96, 64));
    }

    private void addTile(Tile tile, int x, int y) {
        this.tileMap.addTile(tile, x, y);
    }

    private void addGameObject(GameObject gameObject) {
        this.tileMap.addGameObject(gameObject);
    }

//    public Tile getTile(int x, int y) {
//        int gridX = 64 * x;
//        int gridY = 64 * y;
//
//        if(x < 0 || y < 0) {
//            return null;
//        }
//
//        for(GameObject gameObject : tileMap) {
//            if(gameObject instanceof Tile) {
//                Tile b = (Tile) gameObject;
//
//                if(b.getGridX() == gridX && b.getGridY() == gridY) {
//                    return b;
//                }
//            }
//        }
//
//        return null;
//    }

//    private void removeTile(int x, int y) {
//        Iterator<GameObject> it = tileMap.iterator();
//
//        while(it.hasNext()) {
//            GameObject gameObject = it.next();
//
//            if(gameObject instanceof Tile) {
//                Tile block = (Tile) gameObject;
//
//                if(x == block.getGridX() && y == block.getGridY()) {
//                    it.remove();
//                    return;
//                }
//            }
//        }
//    }
    /**
     * This will save all of the data to a .world file that contains the data for
     * all of the Entities, GameObjects, Persons, Item data, and so on.
     */
    public void save() {

    }

    public void onUpdate() {
        for(IEntity entity : entities) {
            entity.onUpdate();
        }
    }

    public void render() {
        tileMap.render();

        Collections.sort(entities, new Comparator<Entity>() {

            @Override
            public int compare(Entity o1, Entity o2) {
                if(o1.getPosition().getY() >= o2.getPosition().getY()) {
                    return 1;
                }else if(o1.getPosition().getY() < o2.getPosition().getY()) {
                    return -1;
                }

                return 0;
            }
        });

        for(IEntity entity : entities) {
            entity.render();
        }
    }

    public Vec2D getGridCoordinates(Entity entity) {
        Vec2D position = entity.getPosition();

        int gridX = (int) Math.ceil(position.getX() / TileMap.TILE_SIZE);
        int gridY = (int) Math.ceil(position.getY() / TileMap.TILE_SIZE);

        return new Vec2D(gridX, gridY);
    }

    public ArrayList<Entity> getEntities() {
        return this.entities;
    }
}
