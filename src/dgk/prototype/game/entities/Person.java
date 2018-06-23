package dgk.prototype.game.entities;

import dgk.prototype.game.*;
import dgk.prototype.game.tile.GameObject;
import dgk.prototype.game.tile.Tile;
import dgk.prototype.game.tile.World;
import dgk.prototype.input.InputManager;
import dgk.prototype.util.AABB;
import dgk.prototype.util.SpriteSheet;
import dgk.prototype.util.Vec2D;
import org.lwjgl.opengl.GL11;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public abstract class Person extends Entity {

    public static final int WIDTH = 32;
    public static final int HEIGHT = 48;

    public static final int ICON_SIZE = 32;

    protected double movementSpeed;

    private Person spouse;

    protected Vec2D lastPosition;

    public Person(int textureId, String name, double x, double y) {
        super(textureId, name, x, y);

        this.movementSpeed = 2.0D;
        this.spouse = null;
    }

    public double getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(double movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public boolean isMarried() {
        return hasSpouse();
    }

    public boolean hasSpouse() {
        return (spouse == null) ? false : true;
    }

    public Person getSpouse() {
        return spouse;
    }

    public void setSpouse(Person spouse) {
        this.spouse = spouse;
    }

    @Override
    public AABB getAABB() {
        Vec2D position = getPosition();
        Vec2D AxisAlignedBBpos = new Vec2D(position.getX() + 20, position.getY() + 40);

        /**
         * AABB
         * [            0
         *            --|--     ____
         *             /\     [     ]
         *                     ^^^^^
         *                      64x16
         */

        return new AABB(AxisAlignedBBpos, new Vec2D(getPosition().getX() + 44, getPosition().getY() + 52));
    }

    @Override
    public void onCollision(IEntity other) {
        System.out.println("Collision detected.");
    }

    /**
     * Draws the shadow of the Person. For now, it just draws a simple low opacity eclipse that
     * goes under the player's name. This simplifies the implementation for 'ray tracing' or any
     * kind of advance lighting techniques (at least for now)
     * @param worldCamera
     */
    protected void drawShadow(Camera worldCamera) {
        glEnable(GL_TEXTURE_2D);

        GL11.glMatrixMode(GL_MODELVIEW);
        GL11.glPushMatrix();

        GL11.glScalef(worldCamera.getZoom(), worldCamera.getZoom(), 0);
        GL11.glTranslated(getPosition().getX() - worldCamera.getPosition().getX() + 16, getPosition().getY() - worldCamera.getPosition().getY() + 46, 0);

        GameWindow.getInstance().resourceManager.getSpriteSheet("Shadow").bindTexture(42);

        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2f(0, 0);

            GL11.glTexCoord2f(1, 0);
            GL11.glVertex2f(32, 0);

            GL11.glTexCoord2f(1, 1);
            GL11.glVertex2f(32, 32);

            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2f(0, 32);
        }
        GL11.glEnd();

        GL11.glPopMatrix();
        GL11.glDisable(GL_TEXTURE_2D);
    }

    protected void drawHealthBar(Camera worldCamera) {
        int currentHealth = getHealthPoints();

        final int healthBarWidth = 60;
        final int healthBarHeight = 10;

        GL11.glDisable(GL_TEXTURE_2D);

        GL11.glMatrixMode(GL_MODELVIEW);
        GL11.glPushMatrix();

        GL11.glColor4f(0f, 1f, 0f, 1f);

        GL11.glScalef(worldCamera.getZoom(), worldCamera.getZoom(), 0);
        GL11.glTranslated(getPosition().getX() - worldCamera.getPosition().getX() + 2, getPosition().getY() - worldCamera.getPosition().getY() - (healthBarHeight + 5), 0);

        float healthRatio = ((float) currentHealth / 100.0f);

        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glVertex2f(0, 0);
            GL11.glVertex2f(healthBarWidth * healthRatio, 0);
            GL11.glVertex2f(healthBarWidth * healthRatio, healthBarHeight);
            GL11.glVertex2f(0, healthBarHeight);
        }
        GL11.glEnd();

        GL11.glPopMatrix();

        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

    /**
     * Draws information above the character when they are selected.
     */
    protected void drawSelectionInfo(Camera worldCamera) {
        glEnable(GL_TEXTURE_2D);

        GL11.glMatrixMode(GL_MODELVIEW);
        GL11.glPushMatrix();

        GL11.glScalef(worldCamera.getZoom(), worldCamera.getZoom(), 0);
        GL11.glTranslated(getPosition().getX() - worldCamera.getPosition().getX() - 32, getPosition().getY() - worldCamera.getPosition().getY(), 0);

        GameWindow.getInstance().resourceManager.getSpriteSheet("UISpriteSheet").bindTexture(SpriteSheet.CONVERSATION_ICON);

        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2f(0, 0);

            GL11.glTexCoord2f(1, 0);
            GL11.glVertex2f(ICON_SIZE, 0);

            GL11.glTexCoord2f(1, 1);
            GL11.glVertex2f(ICON_SIZE, ICON_SIZE);

            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2f(0, ICON_SIZE);
        }
        GL11.glEnd();

        GL11.glPopMatrix();
        GL11.glDisable(GL_TEXTURE_2D);

        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

    protected void drawDiseaseIcon(Camera worldCamera) {
        glEnable(GL_TEXTURE_2D);

        GL11.glMatrixMode(GL_MODELVIEW);
        GL11.glPushMatrix();

        GL11.glScalef(worldCamera.getZoom(), worldCamera.getZoom(), 0);
        GL11.glTranslated(getPosition().getX() - worldCamera.getPosition().getX() - 32, getPosition().getY() - worldCamera.getPosition().getY() - 32, 0);
        GameWindow.getInstance().resourceManager.getSpriteSheet("UISpriteSheet").bindTexture(SpriteSheet.DISEASE_ICON);

        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2f(0, 0);

            GL11.glTexCoord2f(1, 0);
            GL11.glVertex2f(32, 0);

            GL11.glTexCoord2f(1, 1);
            GL11.glVertex2f(32, 32);

            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2f(0, 32);
        }
        GL11.glEnd();

        GL11.glPopMatrix();
        GL11.glDisable(GL_TEXTURE_2D);
    }

    protected void checkForSelection() {
        InputManager manager = GameWindow.getInstance().inputManager;
        GameCamera worldCamera = GameWindow.getInstance().getWorldCamera();

        Vec2D mousePos = manager.getMousePosition();

        // Translate, Rotate, then Scale

        // Un-scale ----> un-translate

        int mX = (int) (mousePos.getX() / worldCamera.getZoom()) + (int) worldCamera.getPosition().getX();
        int mY = (int) (mousePos.getY() / worldCamera.getZoom()) + (int) worldCamera.getPosition().getY();

        if((mX >= getPosition().getX() && mX <= (getPosition().getX() + 64)) && (mY >= getPosition().getY() && mY <= (getPosition().getY() + 64))) {
            isSelected = true;
        }else{
            isSelected = false;
        }
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
