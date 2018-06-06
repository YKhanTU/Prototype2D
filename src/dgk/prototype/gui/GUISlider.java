package dgk.prototype.gui;

import dgk.prototype.game.Camera;
import dgk.prototype.game.GameWindow;
import dgk.prototype.util.Vec2D;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

public abstract class GUISlider implements GUIElement {

    public static final int SLIDER_WIDTH = 10;
    public static final int SLIDER_HEIGHT = 15;

    public GUI gui;

    private double x;
    private double y;

    private double sliderX;

    private double length;

    private float sliderValue;

    private boolean isPressing = false;

    public GUISlider(GUI gui, double x, double y, double length, float initValue) {
        this.gui = gui;

        this.x = x;
        this.y = y;

        this.sliderValue = initValue;

        this.sliderX = (length * sliderValue) + x;

        this.length = length;
    }

    public void render() {
        GL11.glMatrixMode(GL11.GL_MODELVIEW_MATRIX);
        GL11.glPushMatrix();

        Camera viewport = gui.getCamera();

        GL11.glScalef(gui.getCamera().getZoom(), gui.getCamera().getZoom(), 0);
        GL11.glTranslated(sliderX - viewport.getPosition().getX() - (SLIDER_WIDTH / 2), y - viewport.getPosition().getY() - (SLIDER_HEIGHT / 2), 0);

        Vec2D mousePos = gui.getMousePosition();

        if(isMouseInside(mousePos.getX(), mousePos.getY())) {
            GL11.glColor4f(.25f, .25f, .25f, .8f);
        }else {
            GL11.glColor4f(.25f, .25f, .25f, 1f);
        }

        GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2d(0, 0);
            GL11.glVertex2d(SLIDER_WIDTH,0);
            GL11.glVertex2d(SLIDER_WIDTH, SLIDER_HEIGHT);
            GL11.glVertex2d(0, SLIDER_HEIGHT);
        GL11.glEnd();

        GL11.glPopMatrix();

        GL11.glMatrixMode(GL11.GL_MODELVIEW_MATRIX);
        GL11.glPushMatrix();

        GL11.glTranslated(x - viewport.getPosition().getX(), y - viewport.getPosition().getY(), 0);

        GL11.glColor4f(.163f, .163f, .163f, .8f);

        gui.drawLine(new Vec2D(0, 0), new Vec2D(length, 0), 4f);

        GL11.glEnd();

        GL11.glPopMatrix();

        GL11.glColor3f(1f, 1f, 1f);
    }

    @Override
    public void onUpdate() {
        if(isPressing) {
            Vec2D pos = gui.getMousePosition();

            sliderValue = ((float) pos.getX() - (float) x) / (float) length;

            updateSliderValue();

            onSliderValueChange();

            this.sliderX = (length * sliderValue) + x;
        }
    }

    protected void updateSliderValue() {
        if(sliderValue > 1.0f) {
            sliderValue = 1.0f;
        }else if(sliderValue < 0.0f){
            sliderValue = 0.0f;
        }
    }

    protected void setSliderValue(float sliderValue) {
        this.sliderValue = sliderValue;
    }

    protected float getSliderValue(float sliderValue) {
        return this.sliderValue;
    }

    public abstract void onSliderValueChange();

    public boolean isPressing() {
        return this.isPressing;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getLength() {
        return length;
    }

    @Override
    public void onMouseInput(double x, double y, boolean press) {
        if(isMouseInside(x, y) && press) {
            isPressing = true;
        }

        if(isPressing && !press) {
            isPressing = false;
        }
    }

    protected boolean isMouseInside(double mouseX, double mouseY) {
        return (mouseX >= (sliderX - (SLIDER_WIDTH / 2)) && mouseX <= ((sliderX - (SLIDER_WIDTH / 2)) + SLIDER_WIDTH)) && (mouseY >= (y - (SLIDER_HEIGHT / 2)) && mouseY <= ((y - (SLIDER_HEIGHT / 2)) + SLIDER_HEIGHT));
    }

    public void onSliderMove(double mouseX) {
        this.sliderValue = ((float) this.x - (float) mouseX) / (float) length;
    }

    public float getSliderValue() {
        return sliderValue;
    }

}
