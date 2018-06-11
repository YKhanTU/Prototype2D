package dgk.prototype.game;

import dgk.prototype.util.Animation;

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

}
