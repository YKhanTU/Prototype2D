package dgk.prototype.gui;

import dgk.prototype.game.Camera;
import dgk.prototype.game.GameWindow;
import dgk.prototype.util.Vec2D;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;

public abstract class GUIButton implements GUIElement {

    private GUI gui;

    private int textureId;

    private String label;

    private double x;
    private double y;

    private double width;
    private double height;

    public GUIButton(GUI gui, String label, double x, double y, double width, double height) {
        this(gui, 0, label, x, y, width, height);
    }

    public GUIButton(GUI gui, int textureId, String label, double x, double y, double width, double height) {
        this.gui = gui;
        this.textureId = textureId;
        this.label = label;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render() {
        glEnable(GL_TEXTURE_2D);

        GL11.glMatrixMode(GL11.GL_MODELVIEW_MATRIX);
        GL11.glPushMatrix();

        Camera viewport = gui.getCamera();

        Vec2D mousePos = gui.getMousePosition();

        if(isMouseInside(mousePos.getX(), mousePos.getY())) {
            if(!hasTexture()) {
                GL11.glColor4f(.25f, .25f, .25f, .8f);
            }else{
                GL11.glColor4f(1f, 1f, 1f, .5f);
            }
        }else{
            if(!hasTexture()) {
                GL11.glColor4f(.25f, .25f, .25f, 1.0f);
            }else{
                GL11.glColor4f(1f, 1f, 1f, 1f);
            }
        }

        GL11.glScalef(gui.getCamera().getZoom(), gui.getCamera().getZoom(), 0);
        GL11.glTranslated(x - viewport.getPosition().getX(), y - viewport.getPosition().getY(), 0);

        if(hasTexture()) {
            GameWindow.getInstance().uiElements.bindTexture(textureId);

            GL11.glBegin(GL11.GL_QUADS);
            {
                GL11.glTexCoord2d(0, 0);
                GL11.glVertex2d(0, 0);

                GL11.glTexCoord2d(1, 0);
                GL11.glVertex2d(width, 0);

                GL11.glTexCoord2d(1, 1);
                GL11.glVertex2d(width, height);

                GL11.glTexCoord2d(0, 1);
                GL11.glVertex2d(0, height);
            }
            GL11.glEnd();

            GL11.glPopMatrix();

            GL11.glColor4f(1, 1, 1,1);
            return;
        }else {
            GL11.glBegin(GL11.GL_QUADS);
            {
                GL11.glVertex2d(0, 0);
                GL11.glVertex2d(width, 0);
                GL11.glVertex2d(width, height);
                GL11.glVertex2d(0, height);
            }
            GL11.glEnd();
        }

        GL11.glColor4f(0f, 0f, 0f, 1.0f);

        GL11.glLineWidth(1f);

        GL11.glTranslated(-2, -2, 0);

        GL11.glBegin(GL11.GL_LINES);
        {
            GL11.glVertex2d(0, 0);
            GL11.glVertex2d(width + 4, 0);

            GL11.glVertex2d(width + 4, 0);
            GL11.glVertex2d(width + 4, height + 4);

            GL11.glVertex2d(0, height + 4);
            GL11.glVertex2d(width + 4, height + 4);

            GL11.glVertex2d(0, 0);
            GL11.glVertex2d(0, height + 4);
        }
        GL11.glEnd();

        if(label != null) {
            //gui.drawString(label, (int) (x - viewport.getPosition().getX() + 5), (int) (y - viewport.getPosition().getY() + 5));
        }

        GL11.glPopMatrix();

        GL11.glColor3f(1f, 1f, 1f);
    }

    /**
     * The overrideable function that provides this GUIButton with functionality.
     * This is called anytime that a Left Click Click is detected for the button.
     */
    abstract void onButtonClick();

    @Override
    public void onUpdate() {}

    @Override
    public void onMouseInput(double x, double y, boolean press) {
        if(isMouseInside(x, y) && press) {
            onButtonClick();
        }
    }

    public boolean isMouseInside(double mX, double mY) {
        return (mX >= x && mX <= (x + width)) && (mY >= y && mY <= (y + height));
    }

    private boolean hasTexture() {
        return textureId != 0;
    }
}
