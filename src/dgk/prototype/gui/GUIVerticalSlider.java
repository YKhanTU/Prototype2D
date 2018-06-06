package dgk.prototype.gui;

import dgk.prototype.game.Camera;
import dgk.prototype.util.Vec2D;
import org.lwjgl.opengl.GL11;

public abstract class GUIVerticalSlider extends GUISlider {

    public static final int SLIDER_WIDTH = 8;

    /**
     * The 'y' coordinate of the slider bar in the slider,
     * whereas getY() returns the actual position of the whole
     * GUIVerticalSlider itself.
     */
    private double sliderY;

    /**
     * The height of the bar. If the height of the bar is set in the constructor greater
     * than the desired length of the GUIVerticalSlider, then the barHeight is set to 25% of the length.
     */
    private int barHeight;

    /**
     * We always start off with an initValue of 1f because we are at the top of the page. As the value decreases,
     * we use that to move the 'page' down or GUI elements down within the viewport.
     * @param gui The parent GUI
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param length How long the slider is. For a vertical slider, this is the actual length of the clickable part unlike
     *               the horizontal slider, that uses the length value for the actual length of the whole slider.
     */
    public GUIVerticalSlider(GUI gui, double x, double y, double length, int barHeight) {
        super(gui, x, y, length, 1f);

        this.sliderY = y;
        this.barHeight = barHeight;

        if(this.barHeight >= length) {
            this.barHeight = (int) (length * .25f);
        }
    }

    @Override
    public void render() {
        GL11.glMatrixMode(GL11.GL_MODELVIEW_MATRIX);
        GL11.glPushMatrix();

        Camera viewport = gui.getCamera();

        GL11.glScalef(gui.getCamera().getZoom(), gui.getCamera().getZoom(), 0);
        GL11.glTranslated(getX() - viewport.getPosition().getX() - (SLIDER_WIDTH / 2), sliderY - viewport.getPosition().getY(), 0);

        Vec2D mousePos = gui.getMousePosition();

        if(isMouseInside(mousePos.getX(), mousePos.getY())) {
            GL11.glColor4f(.3f, .3f, .3f, .8f);
        }else {
            GL11.glColor4f(.3f, .3f, .3f, 1f);
        }

        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glVertex2d(0, 0);
            GL11.glVertex2d(SLIDER_WIDTH, 0);
            GL11.glVertex2d(SLIDER_WIDTH, barHeight);
            GL11.glVertex2d(0, barHeight);
        }
        GL11.glEnd();

        GL11.glPopMatrix();

        GL11.glMatrixMode(GL11.GL_MODELVIEW_MATRIX);
        GL11.glPushMatrix();

        GL11.glTranslated(getX() - viewport.getPosition().getX() - (SLIDER_WIDTH / 2), getY() - viewport.getPosition().getY(), 0);

        GL11.glColor4f(.1f, .1f, .1f, .5f);

        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glVertex2d(0, 0);
            GL11.glVertex2d(SLIDER_WIDTH, 0);
            GL11.glVertex2d(SLIDER_WIDTH, getLength());
            GL11.glVertex2d(0, getLength());
        }
        GL11.glEnd();

        GL11.glPopMatrix();

        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

    @Override
    public void onUpdate() {
        if(isPressing()) {
            Vec2D pos = gui.getMousePosition();

            double mY = pos.getY();

            setSliderValue(((float) mY - (float) getY()) / ((float) getLength() - barHeight));

            updateSliderValue();

            onSliderValueChange();

            this.sliderY = ((getLength() - barHeight) * getSliderValue()) + getY();
        }
    }

    protected boolean isMouseInside(double mouseX, double mouseY) {
        return (mouseX >= (getX() - (SLIDER_WIDTH / 2)) && mouseX <= ((getX() - (SLIDER_WIDTH / 2)) + SLIDER_WIDTH)) && (mouseY >= sliderY && mouseY <= ((sliderY + barHeight)));
    }
}
