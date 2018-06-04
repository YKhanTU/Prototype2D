package dgk.prototype.game;

import dgk.prototype.util.SpriteSheet;
import dgk.prototype.util.Vec2D;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;

public class World {

    public static final int GRID_SIZE = 64;

    public static final byte LOWER_LAYER = 0;
    public static final byte MID_LAYER = 1;
    public static final byte UPPER_LAYER = 2;

    private ArrayList<Entity> entities;
    private ArrayList<GameObject> tileMap;

    public Ruler ruler;

    public World() {
        this.entities = new ArrayList<Entity>();
        this.tileMap = new ArrayList<GameObject>();
    }

    /**
     * Eventually this function will load data stored on the computer of all
     * of the Entities -> GameObjects, Persons, Item data, and so on.
     */
    public void load() {
        for(int i = 0; i < 16; i++) {
            for(int j = 0; j < 16; j++) {
                int wX = i;
                int wY = j;

                addTile(SpriteSheet.GRASS, wX, wY);
            }
        }

        ruler = new Ruler(32, 64);

        GameWindow.getInstance().getWorldCamera().setTarget(ruler);

        entities.add(ruler);
        entities.add(new Peasant(96, 64));
    }

    private void addTile(int id, int x, int y) {
        this.tileMap.add(new Block(id, LOWER_LAYER, x * GRID_SIZE, y * GRID_SIZE));
    }

    public Block getTile(int x, int y) {
        int gridX = 64 * x;
        int gridY = 64 * y;

        if(x < 0 || y < 0) {
            return null;
        }

        for(GameObject gameObject : tileMap) {
            if(gameObject instanceof Block) {
                Block b = (Block) gameObject;

                if(b.getGridX() == gridX && b.getGridY() == gridY) {
                    return b;
                }
            }
        }

        return null;
    }

    private void removeTile(int x, int y) {
        Iterator<GameObject> it = tileMap.iterator();

        while(it.hasNext()) {
            GameObject gameObject = it.next();

            if(gameObject instanceof Block) {
                Block block = (Block) gameObject;

                if(x == block.getGridX() && y == block.getGridY()) {
                    it.remove();
                    return;
                }
            }
        }
    }
    /**
     * This will save all of the data to a .world file that contains the data for
     * all of the Entities, GameObjects, Persons, Item data, and so on.
     */
    public void save() {

    }

    public void onUpdate() {
        for(GameObject go : tileMap) {
            go.onUpdate();
        }

        for(IEntity entity : entities) {
            entity.onUpdate();
        }
    }

    public void render() {
        for(GameObject go : tileMap) {
            go.render();
        }

        for(IEntity entity : entities) {
            entity.render();
        }
    }

    public Vec2D getGridCoordinates(Entity entity) {
        Vec2D position = entity.getPosition();

        int gridX = (int) Math.ceil(position.getX() / GRID_SIZE);
        int gridY = (int) Math.ceil(position.getY() / GRID_SIZE);

        return new Vec2D(gridX, gridY);
    }

    public ArrayList<Entity> getEntities() {
        return this.entities;
    }
}
