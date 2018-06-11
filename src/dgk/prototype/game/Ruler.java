package dgk.prototype.game;

import dgk.prototype.input.InputManager;
import dgk.prototype.util.Animation;
import dgk.prototype.util.SpriteSheet;
import dgk.prototype.util.Vec2D;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public class Ruler extends Person {

    private Camera worldCamera;


    /**
     *      DIR         L / R       IDLE
     *
     *      SOUTH       7, 9        8
     *      NORTH       16, 18      17
     *      EAST        13, 15      14
     *      WEST        10, 12      11
     */

    private Animation breathingAnimation;

    private int[][] walkingAnimations;

    private int currentAnim;

    private int animationFrame = 1;

    private long startTime = -1L;
    private long animTime = 125;

    // Animations are 48x48

    public Ruler(double x, double y) {
        super(SpriteSheet.RULER, "Ruler", x, y);

        // 4 columns for each direction
        // 3 rows for each of 3 animation states (L/R and IDLE
        this.walkingAnimations = new int[4][3];

        walkingAnimations[0][0] = 8 + 19;
        walkingAnimations[1][0] = 17 + 19;
        walkingAnimations[2][0] = 14 + 19;
        walkingAnimations[3][0] = 11 + 19;

        walkingAnimations[0][1] = 7 + 19;
        walkingAnimations[1][1] = 16 + 19;
        walkingAnimations[2][1] = 13 + 19;
        walkingAnimations[3][1] = 10 + 19;

        walkingAnimations[0][2] = 9 + 19;
        walkingAnimations[1][2] = 18 + 19;
        walkingAnimations[2][2] = 15 + 19;
        walkingAnimations[3][2] = 12 + 19;

        currentAnim = walkingAnimations[0][0];

        this.breathingAnimation = new Animation(51, 3, 400L, true);

        worldCamera = GameWindow.getInstance().getWorldCamera();
    }

    @Override
    public void render() {
        drawShadow(worldCamera);

        glEnable(GL_TEXTURE_2D);

        GL11.glMatrixMode(GL_MODELVIEW_MATRIX);
        GL11.glPushMatrix();

        GL11.glScalef(worldCamera.getZoom(), worldCamera.getZoom(), 0);
        GL11.glTranslated((getPosition().x - worldCamera.getPosition().getX()), (getPosition().y - worldCamera.getPosition().getY()), 0);
        // http://www.idevgames.com/forums/thread-2389.html

        GameWindow.getInstance().resourceManager.getSpriteSheet("RulerWalkAnimations").bindTexture(currentAnim);

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

        if(isSelected)
            drawOutline();

        GL11.glColor4f(1, 1, 1, 1);
    }

    private void drawOutline() {
        final int highlightAnimation = currentAnim + 13;

        GL11.glMatrixMode(GL_MODELVIEW_MATRIX);
        GL11.glPushMatrix();

        GL11.glTranslated((getPosition().x - worldCamera.getPosition().getX()), (getPosition().y - worldCamera.getPosition().getY()), 0);

        GameWindow.getInstance().resourceManager.getSpriteSheet("RulerGlow").bindTexture(highlightAnimation);

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

    private void switchAnimFrame() {
        if(animationFrame == 2) {
            animationFrame = 1;
        }else if(animationFrame == 1) {
            animationFrame = 2;
        }
    }

    private void updateStartTime() {
        if(startTime == -1L) {
            startTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onUpdate() {
        GameWindow gw = GameWindow.getInstance();

        if(gw.isKeyPressed(GLFW.GLFW_KEY_S)) {
            setDirection(Direction.SOUTH);
            this.velocity.setX(0);
            this.velocity.setY(2);

            isMoving = true;
            //breathingAnimation.stop();
        }else if(gw.isKeyPressed(GLFW.GLFW_KEY_W)) {
            setDirection(Direction.NORTH);
            this.velocity.setX(0);
            this.velocity.setY(-2);

            isMoving = true;
            //breathingAnimation.stop();
        }else if(gw.isKeyPressed(GLFW.GLFW_KEY_A)) {
            setDirection(Direction.EAST);
            this.velocity.setX(-2);
            this.velocity.setY(0);

            isMoving = true;
            //breathingAnimation.stop();
        }else if(gw.isKeyPressed(GLFW.GLFW_KEY_D)) {
            setDirection(Direction.WEST);
            this.velocity.setX(2);
            this.velocity.setY(0);

            isMoving = true;
            //breathingAnimation.stop();
        }else{
            this.velocity.setX(0);
            this.velocity.setY(0);
            isMoving = false;
            startTime = -1L;
            animationFrame = 1;
        }

        //if(!isIdle()) {
            updateStartTime();

            switch (getDirection()) {
                case NORTH:
                    if (isMoving) {
                        if (System.currentTimeMillis() - startTime >= animTime) {
                            startTime = System.currentTimeMillis();
                            switchAnimFrame();
                        }
                        currentAnim = walkingAnimations[1][animationFrame];
                    } else {
                        currentAnim = walkingAnimations[1][0];
                    }
                    break;
                case SOUTH:
                    if (isMoving) {
                        if (System.currentTimeMillis() - startTime >= animTime) {
                            startTime = System.currentTimeMillis();
                            switchAnimFrame();
                        }
                        currentAnim = walkingAnimations[0][animationFrame];
                    } else {
                        currentAnim = walkingAnimations[0][0];
                    }
                    break;
                case WEST:
                    if (isMoving) {
                        if (System.currentTimeMillis() - startTime >= animTime) {
                            startTime = System.currentTimeMillis();
                            switchAnimFrame();
                        }
                        currentAnim = walkingAnimations[2][animationFrame];
                    } else {
                        currentAnim = walkingAnimations[2][0];
                    }
                    break;
                case EAST:
                    if (isMoving) {
                        if (System.currentTimeMillis() - startTime >= animTime) {
                            startTime = System.currentTimeMillis();
                            switchAnimFrame();
                        }
                        currentAnim = walkingAnimations[3][animationFrame];
                    } else {
                        currentAnim = walkingAnimations[3][0];
                    }
                    break;
            }
        //}
//        else{
//            breathingAnimation.start();
//
//            breathingAnimation.onUpdate();
//
//            currentAnim = breathingAnimation.getCurrentFrameTextureId();
//        }

        this.lastPosition = new Vec2D(getPosition().getX(), getPosition().getY());

        this.getPosition().add(getVelocity());

        this.checkForCollision(GameWindow.getInstance().world);

        InputManager manager = GameWindow.getInstance().inputManager;

        Vec2D mousePos = manager.getMousePosition();
        int mX = (int) mousePos.getX() + (int) worldCamera.getPosition().getX();
        int mY = (int) mousePos.getY() + (int) worldCamera.getPosition().getY();

        if((mX >= getPosition().getX() && mX <= (getPosition().getX() + 64)) && (mY >= getPosition().getY() && mY <= (getPosition().getY() + 64))) {
            isSelected = true;
        }else{
            isSelected = false;
        }
    }
}
