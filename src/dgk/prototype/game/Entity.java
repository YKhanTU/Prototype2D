package dgk.prototype.game;

import dgk.prototype.util.Texture;
import dgk.prototype.util.TextureLoader;
import dgk.prototype.util.Vec2D;
import org.lwjgl.opengl.GL11;

public abstract class Entity implements IEntity {

    private int textureId;

    private Vec2D position;

    public Entity(int textureId, double x, double y) {
        this.textureId = textureId;
        this.position = new Vec2D(x, y);
    }

    public Vec2D getPosition() {
        return this.position;
    }

    public int getTextureId() {
        return this.textureId;
    }
}
