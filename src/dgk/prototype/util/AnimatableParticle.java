package dgk.prototype.util;

import dgk.prototype.game.GameWindow;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public abstract class AnimatableParticle extends Particle {

    private Animation animation;

    public AnimatableParticle(int textureId, int width, int height, float scale, Vec2D initPosition, Vec2D initVelocity) {
        super(textureId, width, height, scale, initPosition, initVelocity);
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public Animation getAnimation() {
        return animation;
    }

    public boolean isDead() {
        return animation.isAnimationComplete();
    }
}
