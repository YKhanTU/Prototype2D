package dgk.prototype.game.tile;

import dgk.prototype.game.ComponentType;
import dgk.prototype.game.Direction;
import dgk.prototype.game.tile.AnimatableTile;
import dgk.prototype.game.tile.TileMap;
import dgk.prototype.util.Vec2D;

public class BuildingComponent extends AnimatableTile {

    protected static final int INIT_HEALTH = 250;

    private ComponentType type;
    private Direction direction;

    private int health;

    private boolean isConnected;

    public BuildingComponent(int textureId, byte renderingLayer, int x, int y, int size) {
        this(textureId, renderingLayer, x, y, size, Direction.NORTH);
    }

    public BuildingComponent(int textureId, byte renderingLayer, int x, int y, int size, Direction direction) {
        super(textureId, renderingLayer, x, y, size);

        this.direction = direction;

        this.health = INIT_HEALTH;

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

    public boolean isDestroyed() {
        return (health <= 0);
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

    public void onDirectionChange() {}
}
