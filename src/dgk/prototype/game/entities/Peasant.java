package dgk.prototype.game.entities;

import dgk.prototype.game.Camera;
import dgk.prototype.game.GameWindow;
import dgk.prototype.input.InputManager;
import dgk.prototype.util.SpriteSheet;
import dgk.prototype.util.Vec2D;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public class Peasant extends Person {

    private Camera worldCamera;

    private int[][] walkingAnimations;

    private int currentAnim;

    private int animationFrame = 1;

    private long startTime = -1L;
    private long animTime = 150;

    public Peasant(double x, double y) {
        super(SpriteSheet.PEASANT, "Peasant", x, y);

        this.walkingAnimations = new int[4][3];

        walkingAnimations[0][0] = 20 + 39;
        walkingAnimations[1][0] = 29 + 39;
        walkingAnimations[2][0] = 26 + 39;
        walkingAnimations[3][0] = 23 + 39;

        walkingAnimations[0][1] = 19 + 39;
        walkingAnimations[1][1] = 28 + 39;
        walkingAnimations[2][1] = 25 + 39;
        walkingAnimations[3][1] = 22 + 39;

        walkingAnimations[0][2] = 21 + 39;
        walkingAnimations[1][2] = 30 + 39;
        walkingAnimations[2][2] = 27 + 39;
        walkingAnimations[3][2] = 24 + 39;

        currentAnim = walkingAnimations[0][0];

        worldCamera = GameWindow.getInstance().getWorldCamera();
    }

    @Override
    public void render() {
        drawShadow(worldCamera);

        glEnable(GL_TEXTURE_2D);

        GL11.glMatrixMode(GL_MODELVIEW_MATRIX);
        GL11.glPushMatrix();

        GL11.glScalef(worldCamera.getZoom(), worldCamera.getZoom(), 0);
        GL11.glTranslated(getPosition().x - worldCamera.getPosition().getX() - 4, getPosition().y - worldCamera.getPosition().getY(), 0);

        GameWindow.getInstance().resourceManager.getSpriteSheet("PeasantWalkAnimations").bindTexture(currentAnim);

        GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2f(0, 0);

            GL11.glTexCoord2f(1, 0);
            GL11.glVertex2f(64, 0);

            GL11.glTexCoord2f(1, 1);
            GL11.glVertex2f(64, 64);

            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2f(0, 64);
        GL11.glEnd();

        GL11.glPopMatrix();

        if(isSelected)
            drawOutline();

        glDisable(GL_TEXTURE_2D);
    }

    private void drawOutline() {
        final int highlightAnimation = currentAnim + 12;

        GL11.glMatrixMode(GL_MODELVIEW_MATRIX);
        GL11.glPushMatrix();

        GL11.glTranslated((getPosition().x - worldCamera.getPosition().getX() - 5), (getPosition().y - worldCamera.getPosition().getY()), 0);

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
