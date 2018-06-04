package dgk.prototype.util;

import dgk.prototype.game.GameWindow;
import dgk.prototype.game.IEntity;
import dgk.prototype.game.Camera;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public class Particle implements IEntity {

    private Camera worldCamera;

    private int textureId;

    private Vec2D position;
    private Vec2D velocity;

    private int width;
    private int height;

    private float scale;

    private float angle;

    private float lifeSpan;

    public Particle(int textureId, int width, int height, float scale, Vec2D initPosition) {
        this(textureId, width, height, scale, initPosition, new Vec2D(0, 0));
    }

    public Particle(int textureId, int width, int height, float scale, Vec2D initPosition, Vec2D initVelocity) {
        this.textureId = textureId;

        this.position = initPosition;
        this.velocity = initVelocity;

        this.angle = (float) velocity.getAngle();

        this.width = width;
        this.height = height;

        this.scale = scale;

        this.lifeSpan = 50.0f;


        this.worldCamera = GameWindow.getInstance().getWorldCamera();
    }

    @Override
    public void render() {
        glEnable(GL_TEXTURE_2D);

        GL11.glMatrixMode(GL_MODELVIEW_MATRIX);
        GL11.glPushMatrix();

        GL11.glScalef(scale, scale, 0);

        GL11.glTranslated(getPosition().x - worldCamera.getPosition().getX() + width / 2, getPosition().y - worldCamera.getPosition().getY() + height / 2, 0);
        GL11.glRotatef(getAngle(), 0 ,0, 1);
        GL11.glTranslated(-width / 2, -height / 2, 0);

        this.getTexture().bind();

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
        if(!isDead()) {
            position.add(velocity);

            lifeSpan -= .5f;
        }
    }

    public Texture getTexture() {
        return TextureLoader.getTexture(textureId);
    }

    public boolean isDead() {
        return (lifeSpan <= 0f);
    }

    public Vec2D getPosition() {
        return this.position;
    }

    public void applyForce(Vec2D acceleration) {
        this.velocity.add(acceleration);
    }

    public void stop() {
        this.velocity.normalize();
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

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
