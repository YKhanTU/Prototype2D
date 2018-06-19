package dgk.prototype.game.tile;

import dgk.prototype.game.IEntity;
import dgk.prototype.util.AABB;
import dgk.prototype.util.Vec2D;

import java.io.Serializable;

public abstract class GameObject implements IEntity, Serializable {

    protected int textureId;
    protected Vec2D position;

    private int width;
    private int height;

    public GameObject(int textureId, Vec2D initPosition, int width, int height) {
        this.textureId = textureId;
        this.position = initPosition;
        this.width = width;
        this.height = height;
    }

    public Vec2D getPosition() {
        return this.position;
    }

    public int getTextureId() {
        return this.textureId;
    }

    public void setTextureId(int textureId) {
        this.textureId = textureId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
