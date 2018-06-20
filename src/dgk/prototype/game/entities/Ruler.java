package dgk.prototype.game.entities;

import dgk.prototype.game.Camera;
import dgk.prototype.game.Direction;
import dgk.prototype.game.GameWindow;
import dgk.prototype.game.tile.Node;
import dgk.prototype.game.tile.Tile;
import dgk.prototype.game.tile.TileMap;
import dgk.prototype.input.InputManager;
import dgk.prototype.sound.Sound;
import dgk.prototype.util.Animation;
import dgk.prototype.util.SpriteSheet;
import dgk.prototype.util.Vec2D;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public class Ruler extends Person {

    private static final int WALKING_ANIM_SPEED = 125;
    private static final int IDLE_ANIM_SPEED = 250;

    private Camera worldCamera;


    /**
     *      DIR         L / R       IDLE
     *
     *      SOUTH       7, 9        8
     *      NORTH       16, 18      17
     *      EAST        13, 15      14
     *      WEST        10, 12      11
     */

    private Animation northWalking;
    private Animation southWalking;
    private Animation eastWalking;
    private Animation westWalking;

    private Animation northIdle;
    private Animation southIdle;
    private Animation eastIdle;
    private Animation westIdle;

    private Animation currentAnimation;

    // Animations are 32x48

    public Ruler(double x, double y) {
        super(SpriteSheet.RULER, "Ruler", x, y);

        // 4 columns for each direction
        // 3 rows for each of 3 animation states (L/R and IDLE

        northWalking = new Animation(39, 3, WALKING_ANIM_SPEED, true);
        southWalking = new Animation(30, 3, WALKING_ANIM_SPEED, true);
        westWalking = new Animation(33, 3, WALKING_ANIM_SPEED, true);
        eastWalking = new Animation(36, 3, WALKING_ANIM_SPEED, true);

        northIdle = new Animation(117, 5, IDLE_ANIM_SPEED, true);
        southIdle = new Animation(102, 5, IDLE_ANIM_SPEED, true);
        westIdle = new Animation(107, 5, IDLE_ANIM_SPEED, true);
        eastIdle = new Animation(112, 5, IDLE_ANIM_SPEED, true);

        this.currentAnimation = northIdle;

        //walkingSound = GameWindow.getInstance().soundManager.getSound("walkSound");

        worldCamera = GameWindow.getInstance().getWorldCamera();
    }

    @Override
    public void render() {
        drawShadow(worldCamera);

        glEnable(GL_TEXTURE_2D);

        GL11.glMatrixMode(GL_MODELVIEW);
        GL11.glPushMatrix();

        GL11.glScalef(worldCamera.getZoom(), worldCamera.getZoom(), 0);
        GL11.glTranslated((getPosition().x - worldCamera.getPosition().getX()), (getPosition().y - worldCamera.getPosition().getY()), 0);
        // http://www.idevgames.com/forums/thread-2389.html

        //GameWindow.getInstance().resourceManager.getSpriteSheet("RulerWalkAnimations").bindTexture(currentAnim);
        GL11.glBindTexture(GL_TEXTURE_2D, currentAnimation.getCurrentFrameTextureId());

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

        if(isSelected) {
            drawOutline();
            drawHealthBar(worldCamera);
            //drawSelectionInfo(worldCamera);
        }

        GL11.glColor4f(1, 1, 1, 1);
    }

    private void drawOutline() {
        int highlightAnimation = 0;

        // TODO: Make the OFFSETS constants in this class.
        if(isMoving) {
            highlightAnimation = currentAnimation.getCurrentFrameTextureId() + 13;
        }else{
            highlightAnimation = currentAnimation.getCurrentFrameTextureId() + 23;
        }

        GL11.glMatrixMode(GL_MODELVIEW);
        GL11.glPushMatrix();

        GL11.glScalef(worldCamera.getZoom(), worldCamera.getZoom(), 0);
        GL11.glTranslated((getPosition().x - worldCamera.getPosition().getX()), (getPosition().y - worldCamera.getPosition().getY()), 0);

        GL11.glBindTexture(GL_TEXTURE_2D, highlightAnimation);

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
                switch (getDirection()) {
                    case NORTH:
                        currentAnimation = northIdle;
                        break;
                    case SOUTH:
                        currentAnimation = southIdle;
                        break;
                    case EAST:
                        currentAnimation = eastIdle;
                        break;
                    case WEST:
                        currentAnimation = westIdle;
                        break;
                }

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

                switch (getDirection()) {
                    case NORTH:
                        currentAnimation = northIdle;
                        break;
                    case SOUTH:
                        currentAnimation = southIdle;
                        break;
                    case EAST:
                        currentAnimation = eastIdle;
                        break;
                    case WEST:
                        currentAnimation = westIdle;
                        break;
                }

                isMoving = false;
            }
        }

        currentAnimation.onUpdate();

        this.lastPosition = new Vec2D(getPosition().getX(), getPosition().getY());

        this.getPosition().add(getVelocity());

        this.checkForCollision(GameWindow.getInstance().world);

        this.checkForSelection();
    }
}
