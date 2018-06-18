package dgk.prototype.util;

import dgk.prototype.game.GameWindow;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class RainParticle extends AnimatableParticle {
    public RainParticle(int textureId, int width, int height, float scale, Vec2D initPosition, Vec2D initVelocity) {
        super(textureId, width, height, scale, initPosition, initVelocity);
    }

    public void render() {
        glEnable(GL_TEXTURE_2D);

        GL11.glMatrixMode(GL_MODELVIEW);
        GL11.glPushMatrix();

        GL11.glScalef(getScale(), getScale(), 0);

        GL11.glTranslated(getPosition().x - getWorldCamera().getPosition().getX() + getWidth() / 2, getPosition().y - getWorldCamera().getPosition().getY() + getHeight() / 2, 0);
        GL11.glRotatef(90, 0, 0, 1);
        GL11.glTranslated(-getWidth() / 2, -getHeight() / 2, 0);

        GameWindow.getInstance().resourceManager.getSpriteSheet("RainParticles").bindTexture(getAnimation().getCurrentFrameTextureId());

        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2f(0, 0);

            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2f(getWidth(), 0);

            GL11.glTexCoord2f(1, 1);
            GL11.glVertex2f(getWidth(), getHeight());

            GL11.glTexCoord2f(1, 0);
            GL11.glVertex2f(0, getHeight());
        }
        GL11.glEnd();

        GL11.glPopMatrix();

        glDisable(GL_TEXTURE_2D);
    }

    public boolean hasHitGround() {
        return getAnimation().getCurrentFrameTextureId() > 87;
    }

    @Override
    public void onUpdate() {
    if(!isDead()) {
        getAnimation().onUpdate();

        if(!hasHitGround()) {
            getPosition().add(getVelocity());
        }else{
            this.stop();
        }

        lifeSpan -= .5f;
    }

}

    @Override
    public boolean isDead() {
        return getAnimation().isStopped() || super.isDead();
    }
}
