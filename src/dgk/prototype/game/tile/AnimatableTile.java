package dgk.prototype.game.tile;

import dgk.prototype.game.Camera;
import dgk.prototype.game.GameWindow;
import dgk.prototype.util.Animation;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

/**
 * An AnimatableTile represents a tile that has an Animation.
 * This may be used for scenery or tiles that represent buildings, gates, and other
 * objects that require animations.
 */
public class AnimatableTile extends Tile {

    private Animation animation;

    public AnimatableTile(int textureId, byte renderLayer, int x, int y, int size) {
        super(textureId, renderLayer, x, y, size);

        setPassable(true);
        setStatic(true);
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public boolean hasAnimation() {
        return (animation != null);
    }
}
