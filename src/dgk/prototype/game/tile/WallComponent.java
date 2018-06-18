package dgk.prototype.game.tile;

import dgk.prototype.game.ComponentType;
import dgk.prototype.game.Direction;
import dgk.prototype.game.tile.BuildingComponent;
import dgk.prototype.game.tile.Tile;
import dgk.prototype.util.SpriteSheet;
import org.lwjglx.test.spaceinvaders.Sprite;

import java.util.ArrayList;

public class WallComponent extends BuildingComponent {

    /*


    STRATEGY FOR BUILDING WALLS/GATES/STAIRS:

                - Each and every building component (wall component) has Health, isDestroyed, and an Owner (Nation ID)
                - Each building component's 'sub component' will not have collision boxes. The collision AABB will be set
                inside the Component's class over multiple tiles to provide proper collisions instead of setting it for
                each individual tile.


     */

    private static final int MAX_SUB_COMPONENTS = 2;

    private BuildingComponent componentOne;
    private BuildingComponent componentTwo;
    //private Tile[] subComponents;

    public WallComponent(ComponentType type, byte renderLayer, int x, int y, int size, Direction initDirection) {
        super(0, renderLayer, x, y, size, initDirection);

        setType(type);
        onDirectionChange();
    }

    /**
     * Adds the sub-components to the TileMap rendering queue.
     * This should be changed because right now this isn't the "safest" way,
     * and we need a better way to delete things from the list as well.
     * @param world
     */
    public void onAdd(World world) {
        TileMap tileMap = world.getTileMap();

        if(componentOne != null) {
            tileMap.addGameObject(componentOne);
        }

        if(componentTwo != null) {
            tileMap.addGameObject(componentTwo);
        }
    }

    public void onRemove(World world) {
        TileMap tileMap = world.getTileMap();
    }


    @Override
    public void render() {
        super.render();

//        for(Tile t : subComponents) {
//            if(t != null) {
//                t.render();
//            }
//        }
    }

    @Override
    public void onDirectionChange() {
        if(getType() == ComponentType.WOOD) {
            switch (getDirection()) {
                case NORTH:
                    this.textureId = SpriteSheet.WOOD_WALL_NORTH_1A;
                    this.setPassable(true);
                    componentOne = new BuildingComponent(SpriteSheet.WOOD_WALL_NORTH_1B, getRenderLayer(), getGridX() * 48, getGridY() * 48 + 48, TileMap.TILE_SIZE);
                    componentTwo = new BuildingComponent(SpriteSheet.WOOD_WALL_NORTH_1C, getRenderLayer(), getGridX() * 48, getGridY() * 48 - 48, TileMap.TILE_SIZE);
                    componentOne.setPassable(false);
                    componentTwo.setPassable(false);
                    break;
                case SOUTH:
                    this.textureId = SpriteSheet.WOOD_WALL_SOUTH_1A;
                    this.setPassable(false);
                    componentOne = new BuildingComponent(SpriteSheet.WOOD_WALL_SOUTH_1B, getRenderLayer(), getGridX() * 48, getGridY() * 48 + 48, TileMap.TILE_SIZE);
                    componentTwo = null;
                    componentOne.setPassable(false);
                    break;
                case EAST:
                    this.textureId = SpriteSheet.WOOD_WALL_EAST_1A;
                    this.setPassable(true);
                    componentOne = new BuildingComponent(SpriteSheet.WOOD_WALL_WEST_1B, getRenderLayer(), getGridX() * 48 - 48, getGridY() * 48, TileMap.TILE_SIZE);
                    componentTwo = new BuildingComponent(SpriteSheet.WOOD_WALL_EAST_1C, getRenderLayer(), getGridX() * 48, getGridY() * 48 + 48, TileMap.TILE_SIZE);
                    componentOne.setPassable(false);
                    componentTwo.setPassable(false);
                    break;
                case WEST:
                    this.textureId = SpriteSheet.WOOD_WALL_WEST_1A;
                    this.setPassable(true);
                    componentOne = new BuildingComponent(SpriteSheet.WOOD_WALL_EAST_1B, getRenderLayer(), getGridX() * 48 + 48, getGridY() * 48, TileMap.TILE_SIZE);
                    componentTwo = new BuildingComponent(SpriteSheet.WOOD_WALL_WEST_1C, getRenderLayer(), getGridX() * 48, getGridY() * 48 + 48, TileMap.TILE_SIZE);
                    componentOne.setPassable(false);
                    componentTwo.setPassable(false);
                    break;
            }
        }else if(getType() == ComponentType.WOOD) {
            switch (getDirection()) {
                case NORTH:
                    System.out.println("Not supported yet D:");
                    break;
                case SOUTH:
                    System.out.println("Not supported yet D:");
                    break;
                case EAST:
                    System.out.println("Not supported yet D:");
                    break;
                case WEST:
                    System.out.println("Not supported yet D:");
                    break;
            }
        }
    }
}
