package dgk.prototype.game.tile;

import dgk.prototype.game.*;
import dgk.prototype.game.entities.Peasant;
import dgk.prototype.game.entities.Ruler;
import dgk.prototype.util.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class World {

    public static final int RAIN_WIDTH = 48;
    public static final int RAIN_HEIGHT = 96;

    public static final byte LOWER_LAYER = 0;
    public static final byte MID_LAYER = 1;
    public static final byte UPPER_LAYER = 2;

    private ArrayList<Entity> entities;
    private TileMap tileMap;

    private ParticleSystem weatherSystem;

    /**
     * Used to hold the render order of all objects on the screen. TEMPORARY.
     */
    protected ArrayList<IEntity> renderLayerList;

    public Ruler ruler;

    public World() {
        this.entities = new ArrayList<Entity>();
        this.tileMap = new TileMap();

        this.renderLayerList = new ArrayList<IEntity>();
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

        // TODO Format this in a better way.

        for(int i = 0; i < 128; i++) {
            for(int j = 0; j < 128; j++) {
                addTile(new Tile(SpriteSheet.GRASS_2, World.LOWER_LAYER, i * 48, j * 48, 48), i, j);
            }
        }

        addGameObject(new TileShrub(150, 200));
        addGameObject(new TileShrub(600, 234));
        addGameObject(new TileTree(600, 500));
        addGameObject(new TileTree(300, 200));

        AnimatableTile gateAnimation = new AnimatableTile(-1, World.MID_LAYER, 500, 500, 130);

        gateAnimation.setAnimation(new Animation(98, 4, 300, true));

        //addGameObject(gateAnimation);

        Tile testTile = new Tile(SpriteSheet.WOOD_GATE, World.MID_LAYER, 500 - 96, 500, 96);
        testTile.setPassable(false);
        //addGameObject(testTile);

        entities.add(ruler);
        entities.add(new Peasant(96, 64));

        this.weatherSystem = new ParticleSystemRain(new Vec2D(0, -96), 800 - (48));
    }

    private void addTile(Tile tile, int x, int y) {
        this.tileMap.addTile(tile, x, y);
    }

    private void addGameObject(GameObject gameObject) {
        this.tileMap.addGameObject(gameObject);
    }

//    public Tile getTile(int x, int y) {
//        return tileMap.getTile(x, y);
//    }

//    private void removeTile(int x, int y) {
//        tileMap.removeTile(x, y);
//    }

    /**
     * This will save all of the data to a .world file that contains the data for
     * all of the Entities, GameObjects, Persons, Item data, and so on.
     */
    public void save() {

    }

    public void onUpdate() {
        tileMap.onUpdate(this);

        for(IEntity entity : entities) {
            entity.onUpdate();
        }

        weatherSystem.onUpdate();
    }

    public void render() {
        tileMap.render(this);

//        Collections.sort(entities, new Comparator<Entity>() {
//
//            @Override
//            public int compare(Entity o1, Entity o2) {
//                if(o1.getPosition().getY() >= o2.getPosition().getY()) {
//                    return 1;
//                }else if(o1.getPosition().getY() < o2.getPosition().getY()) {
//                    return -1;
//                }
//
//                return 0;
//            }
//        });

        for(Entity entity : entities) {
            renderLayerList.add(entity);
            //entity.render();
        }

        Collections.sort(renderLayerList, new Comparator<IEntity>() {
            @Override
            public int compare(IEntity o1, IEntity o2) {
                AABB axisAlignedBB1 = o1.getAABB();
                AABB axisAlignedBB2 = o2.getAABB();

                if(axisAlignedBB1.getMin().getY() > axisAlignedBB2.getMin().getY()) {
                    return 1;
                }else if(axisAlignedBB1.getMin().getY() < axisAlignedBB2.getMin().getY()) {
                    return -1;
                }

//                if(o1.getPosition().getY() > o2.getPosition().getY()) {
//                    return 1;
//                }else if(o1.getPosition().getY() < o2.getPosition().getY()) {
//                    return -1;
//                }

                return 0;
            }
        });

        for(IEntity ent : renderLayerList) {
            ent.render();
        }

        renderLayerList.clear();

        weatherSystem.render();
    }

    // TODO THIS IS TEMPORARY AND SHOULD BE REMOVED IMMEDIATELY... SOON.
    public boolean onMouseInput(double mX, double mY, boolean press) {
        if(press) {
            GameCamera worldCamera = GameWindow.getInstance().getWorldCamera();

            System.out.println(worldCamera.getZoom() * TileMap.TILE_SIZE);

            int gridX = (int) Math.floor((mX + GameWindow.getInstance().getWorldCamera().getPosition().getX()) / (TileMap.TILE_SIZE * worldCamera.getZoom()));
            int gridY = (int) Math.floor((mY + GameWindow.getInstance().getWorldCamera().getPosition().getY()) / (TileMap.TILE_SIZE * worldCamera.getZoom()));

            BuildingComponent component = tileMap.getBuildingComponent(gridX, gridY);

            if(component != null) {
                System.out.println("Cannot build here!");
            }else{
                WallComponent newWall = new WallComponent(ComponentType.WOOD, World.MID_LAYER, gridX * 48, gridY * 48, 48, Direction.NORTH);
                addGameObject(newWall);
                newWall.onAdd(this);
                return true;
            }

            Tile tile = tileMap.getTile(gridX, gridY);

            if(tile == null) {
                return true;
            }

            tileMap.onTileSelection(tile);

            return true;
        }

        return false;
    }

    public Vec2D getGridCoordinates(Entity entity) {
        Vec2D position = entity.getPosition();

        int gridX = (int) Math.floor(position.getX() / TileMap.TILE_SIZE);
        int gridY = (int) Math.floor(position.getY() / TileMap.TILE_SIZE);

        return new Vec2D(gridX, gridY);
    }

    public ArrayList<Entity> getEntities() {
        return this.entities;
    }

    public TileMap getTileMap() {
        return this.tileMap;
    }
}
