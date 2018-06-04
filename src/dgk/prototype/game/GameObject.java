package dgk.prototype.game;

import dgk.prototype.util.Texture;
import dgk.prototype.util.TextureLoader;
import dgk.prototype.util.Vec2D;

public abstract class GameObject implements IEntity {

    private int textureId;
    private Vec2D position;

    public GameObject(int textureId, Vec2D initPosition) {
        this.textureId = textureId;
        this.position = initPosition;
    }

    public Vec2D getPosition() {
        return this.position;
    }

    public int getTextureId() {
        return this.textureId;
    }
}
