package dgk.prototype.game.tile;

import dgk.prototype.game.Direction;
import dgk.prototype.util.Vec2D;

import static dgk.prototype.game.Direction.*;

/**
 * Represents a 'Zone' bounded by a minimum
 * and maximum value (Vec2D) in grid coordinates on the TileMap.
 */
public class Zone {

    private World world;

    private Vec2D origin;

    private Vec2D minimumBounds;
    private Vec2D maximumBounds;

    private BuildingComponent[][] zoneMap;

    public Zone(World world, Vec2D minimumBounds, Vec2D maximumBounds) {
        this.world = world;

        this.minimumBounds = minimumBounds;
        this.maximumBounds = maximumBounds;
        this.origin = new Vec2D(0, 0);

        int offsetX = (int) maximumBounds.getX() - (int) minimumBounds.getX();
        int offsetY = (int) maximumBounds.getY() - (int) minimumBounds.getY();

        this.zoneMap = new BuildingComponent[offsetX][offsetY];
    }

    public Zone(World world, int minGridX, int minGridY, int maxGridX, int maxGridY) {
        this(world, new Vec2D(minGridX, minGridY), new Vec2D(maxGridX, maxGridY));
    }

    public boolean hasBuildingComponent(int gridX, int gridY) {
        // Account for the origin.
        if(!isInside(gridX, gridY)) {
            return false;
        }

        return zoneMap[gridX][gridY] != null;
    }

    public BuildingComponent getBuildingComponent(int gridX, int gridY) {
        if(!isInside(gridX, gridY)) {
            System.out.println("You are attempting to get a building component that is not in bounds!");
            return null;
        }

        if(hasBuildingComponent(gridX, gridY)) {
            return zoneMap[gridX][gridY];
        }

        return null;
    }

    public void addBuildingComponent(BuildingComponent buildingComponent, int gridX, int gridY) {
        if(!isInside(gridX, gridY)) {
            System.out.println("You cannot place down a BuildingComponent outside the Zone!");
            return;
        }

        if(hasBuildingComponent(gridX, gridY)) {
            System.out.println("You cannot place down a BuildingComponent here, there is already one here!");
            return;
        }

        Direction direction = buildingComponent.getDirection();

        if((direction == WEST || direction == EAST) && hasHorizontalNeighbors(gridX, gridY)) {
            System.out.println("You cannot place here because you have horizontal neighbors.");
            return;
        }

        if((direction == NORTH || direction == SOUTH) && hasVerticalNeighbors(gridX, gridY)) {
            System.out.println("You cannot place here because you have vertical neighbors.");
            return;
        }

        zoneMap[gridX][gridY] = buildingComponent;

        world.getTileMap().addGameObject(buildingComponent);

        WallComponent wc = (WallComponent) buildingComponent;

        wc.onAdd(world);
    }

    public BuildingComponent[][] getZoneMap() {
        return zoneMap;
    }

    public boolean hasVerticalNeighbors(int gridX, int gridY) {
        return (hasBuildingComponent(gridX, gridY + 1) ||
                hasBuildingComponent(gridX, gridY - 1));
    }

    public boolean hasHorizontalNeighbors(int gridX, int gridY) {
        return (hasBuildingComponent(gridX + 1, gridY) ||
                hasBuildingComponent(gridX - 1, gridY));
    }

    /**
     * Returns the origin, or position of the 'zone' grid-wise.
     * @return
     */
    public Vec2D getOrigin() {
        return origin;
    }

    /**
     * Sets the origin, which also sets the min and max bounds based on the new origin.
     * @param origin
     */

    public void setOrigin(Vec2D origin) {
        this.origin = origin;
        minimumBounds.add(origin);
        maximumBounds.add(origin);
    }

    /**
     * Returns if a grid coordinate is inside this zone or not.
     * @param gridX
     * @param gridY
     * @return true if the grid coordinates are in the zone or not, otherwise false.
     */
    public boolean isInside(int gridX, int gridY) {
        return ((gridX >= minimumBounds.getX() && gridX < maximumBounds.getX()) &&
                (gridY >= minimumBounds.getY()) && gridY < maximumBounds.getY());
    }
}
