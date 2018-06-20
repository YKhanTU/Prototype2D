package dgk.prototype.game.entities;

import dgk.prototype.game.Camera;
import dgk.prototype.game.Direction;
import dgk.prototype.game.GameWindow;
import dgk.prototype.game.tile.*;
import dgk.prototype.input.InputManager;
import dgk.prototype.util.Animation;
import dgk.prototype.util.SpriteSheet;
import dgk.prototype.util.Vec2D;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public class Peasant extends Person {

    /**
     * The animation between frames for the Walking animation.
     */
    private static final int WALKING_ANIM_SPEED = 100;
    /**
     * The animation between frames for the Idle animation.
     */
    private static final int IDLE_ANIM_SPEED = 250;

    private Camera worldCamera;

    private Animation northWalking;
    private Animation southWalking;
    private Animation eastWalking;
    private Animation westWalking;

    private Animation northIdle;
    private Animation southIdle;
    private Animation eastIdle;
    private Animation westIdle;

    // TEMPORARY until we get animations
    private int idleAnimationId = 59;

    private Animation currentAnimation;

    public Peasant(double x, double y) {
        super(SpriteSheet.PEASANT, "Peasant", x, y);


        World w = GameWindow.getInstance().world;
        TileMap tileMap = w.getTileMap();

        int thisGridX = (int) Math.floor(x / TileMap.TILE_SIZE);
        int thisGridY = (int) Math.floor(x / TileMap.TILE_SIZE);

        northWalking = new Animation(67, 3, WALKING_ANIM_SPEED, true);
        southWalking = new Animation(58, 3, WALKING_ANIM_SPEED, true);
        westWalking = new Animation(61, 3, WALKING_ANIM_SPEED, true);
        eastWalking = new Animation(64, 3, WALKING_ANIM_SPEED, true);

        this.currentAnimation = southWalking;

        worldCamera = GameWindow.getInstance().getWorldCamera();
    }

    @Override
    public void render() {
        drawShadow(worldCamera);

        glEnable(GL_TEXTURE_2D);

        GL11.glMatrixMode(GL_MODELVIEW);
        GL11.glPushMatrix();

        GL11.glScalef(worldCamera.getZoom(), worldCamera.getZoom(), 0);
        GL11.glTranslated(getPosition().x - worldCamera.getPosition().getX() - 4, getPosition().y - worldCamera.getPosition().getY(), 0);

        if(isMoving) {
            GameWindow.getInstance().resourceManager.getSpriteSheet("PeasantWalkAnimations").bindTexture(currentAnimation.getCurrentFrameTextureId());
        }else{
            GameWindow.getInstance().resourceManager.getSpriteSheet("PeasantWalkAnimations").bindTexture(idleAnimationId);
        }

        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2f(0, 0);

            GL11.glTexCoord2f(1, 0);
            GL11.glVertex2f(64, 0);

            GL11.glTexCoord2f(1, 1);
            GL11.glVertex2f(64, 64);

            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2f(0, 64);
        }
        GL11.glEnd();

        GL11.glPopMatrix();

//        if(isSelected)
//            drawOutline();

        glDisable(GL_TEXTURE_2D);
    }

    private void drawOutline() {
        final int highlightAnimation = currentAnimation.getCurrentFrameTextureId() + 12;

        GL11.glEnable(GL_TEXTURE_2D);

        GL11.glMatrixMode(GL_MODELVIEW);
        GL11.glPushMatrix();

        GL11.glTranslated((getPosition().x - worldCamera.getPosition().getX() - 8), (getPosition().y - worldCamera.getPosition().getY()), 0);

        GameWindow.getInstance().resourceManager.getSpriteSheet("PeasantGlow").bindTexture(highlightAnimation);

        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2f(0, 0);

            GL11.glTexCoord2f(1, 0);
            GL11.glVertex2f(64, 0);

            GL11.glTexCoord2f(1, 1);
            GL11.glVertex2f(64, 64);

            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2f(0, 64);
        }
        GL11.glEnd();

        GL11.glPopMatrix();

        glDisable(GL_TEXTURE_2D);
    }

    // onMove function
    // move (Direction)
    // setDirection(new direction)
    // set velocity in that direction
    //
    // onUpdate():
    // switch on direction
    // set new animation

    @Override
    public void onUpdate() {
        GameWindow gw = GameWindow.getInstance();

        super.onUpdate();

        if(!hasController) {
            if (isMoving) {
                switch (getDirection()) {
                    case NORTH:
                        currentAnimation = northWalking;
                        break;
                    case SOUTH:
                        currentAnimation = southWalking;
                        break;
                    case EAST:
                        currentAnimation = eastWalking;
                        break;
                    case WEST:
                        currentAnimation = westWalking;
                        break;
                }
            } else {
                getVelocity().setX(0);
                getVelocity().setY(0);
            }
        }else {
            if (gw.isKeyPressed(GLFW.GLFW_KEY_S)) {
                setDirection(Direction.SOUTH);
                this.velocity.setX(0);
                this.velocity.setY(movementSpeed);

                currentAnimation = southWalking;

                isMoving = true;
            } else if (gw.isKeyPressed(GLFW.GLFW_KEY_W)) {
                setDirection(Direction.NORTH);
                this.velocity.setX(0);
                this.velocity.setY(-movementSpeed);

                currentAnimation = northWalking;

                isMoving = true;
            } else if (gw.isKeyPressed(GLFW.GLFW_KEY_A)) {
                setDirection(Direction.WEST);
                this.velocity.setX(-movementSpeed);
                this.velocity.setY(0);

                currentAnimation = westWalking;

                isMoving = true;
            } else if (gw.isKeyPressed(GLFW.GLFW_KEY_D)) {
                setDirection(Direction.EAST);
                this.velocity.setX(movementSpeed);
                this.velocity.setY(0);

                currentAnimation = eastWalking;

                isMoving = true;
            } else {
                this.velocity.setX(0);
                this.velocity.setY(0);


                isMoving = false;
            }
        }

        currentAnimation.onUpdate();

        this.lastPosition = new Vec2D(getPosition().getX(), getPosition().getY());

        this.getPosition().add(getVelocity());

        this.checkForCollision(GameWindow.getInstance().world);

        this.checkForSelection();
    }

    // Walks to the current tile in this Entity's path.

}
