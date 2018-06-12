package dgk.prototype.game;

import java.util.ArrayList;

public abstract class BuildingComponent extends AnimatableTile {

    private ComponentType type;
    private Direction direction;

    private boolean isConnected;

    public BuildingComponent(int textureId, byte renderingLayer, int x, int y, int size) {
        this(textureId, renderingLayer, x, y, size, Direction.NORTH);
    }

    public BuildingComponent(int textureId, byte renderingLayer, int x, int y, int size, Direction direction) {
        super(textureId, renderingLayer, x, y, size);

        this.direction = direction;
        isConnected = false;
    }

    public ComponentType getType() {
        return this.type;
    }

    public void setType(ComponentType type) {
        this.type = type;
    }

    public boolean hasType() {
        return (this.type != null);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        onDirectionChange();
    }

    public int getGridX() {
        return ((int) Math.ceil(this.getPosition().getX() / TileMap.TILE_SIZE));
    }

    public int getGridY() {
        return ((int) Math.ceil(this.getPosition().getY() / TileMap.TILE_SIZE));
    }

    public boolean isConnected() {
        return isConnected;
    }

    abstract void onDirectionChange();
}
