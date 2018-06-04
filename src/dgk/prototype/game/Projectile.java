package dgk.prototype.game;

import dgk.prototype.util.Vec2D;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

/**
 * A projectile, which is an entity that is thrown, shot, or falling.
 *
 * This class will represent arrows, cannon balls, and so on and deal with collision with other GameObjects.
 */
public class Projectile extends GameObject {

    private GameCamera worldCamera;

    private Vec2D initVelocity;

    private int width;
    private int height;

    public Projectile(int textureId, Vec2D initPosition, int width, int height, Vec2D initVelocity) {
        super(textureId, initPosition);

        this.worldCamera = GameWindow.getInstance().getWorldCamera();

        this.width = width;
        this.height = height;

        this.initVelocity = initVelocity;
    }

    @Override
    public void render() {
        glEnable(GL_TEXTURE_2D);

        GL11.glMatrixMode(GL_MODELVIEW_MATRIX);
        GL11.glPushMatrix();

        //GL11.glScalef(scale, scale, 0);

        GL11.glTranslated(getPosition().x - worldCamera.getPosition().getX() + width / 2, getPosition().y - worldCamera.getPosition().getY() + height / 2, 0);
        GL11.glRotatef((float) getPosition().getAngle(), 0 ,0, 1);
        GL11.glTranslated(-width / 2, -height / 2, 0);

        //this.getTexture().bind();

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(0, 0);

        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(width, 0);

        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(width, height);

        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(0, height);
        GL11.glEnd();

        GL11.glPopMatrix();

        glDisable(GL_TEXTURE_2D);
    }

    @Override
    public void onUpdate() {

    }
}
