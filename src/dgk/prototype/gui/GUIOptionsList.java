package dgk.prototype.gui;

import dgk.prototype.game.Camera;
import dgk.prototype.util.Color;
import org.lwjgl.opengl.GL11;

import java.util.*;

/**
 * This represents a list of 'options' that correspond to different functions.
 * For example, if an Entity is right-clicked, multiple options may come up and once
 * one of the options is clicked, an Action is performed.
 */
public class GUIOptionsList implements GUIElement {

    public static final int OPTION_WIDTH = 100;
    public static final int OPTION_HEIGHT = 20;

    private GUI gui;

    protected double x;
    protected double y;

    protected double width;
    protected double height;

    protected TreeMap<String, GUIOption> guiOptions;

    protected boolean isShowing;

    public GUIOptionsList(GUI gui, double x, double y, double width, double height) {
        this.gui = gui;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.guiOptions = new TreeMap<String, GUIOption>();
        this.isShowing = true;
    }

    public void addOption(String name, GUIOption guiOption) {
        this.guiOptions.put(name, guiOption);

    }

    public GUIOption getOption(String name) {
        if(hasOption(name))
            return guiOptions.get(name);

        return null;
    }

    public boolean hasOption(String name) {
        return guiOptions.containsKey(name);
    }

    public boolean shouldRemove() {
        return !isShowing;
    }

    @Override
    public void render() {
        GL11.glMatrixMode(GL11.GL_MODELVIEW_MATRIX);

        GL11.glPushMatrix();

        Camera camera = gui.getCamera();

        GL11.glScalef(camera.getZoom(), camera.getZoom(), 0);
        GL11.glTranslated(x - camera.getPosition().getX(), y - camera.getPosition().getY(), 0);

        drawOptions();

        GL11.glPopMatrix();

        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

    private void drawOptions() {
        if(this.guiOptions.size() == 0)
            return;

        double menuY = this.y;

        for(GUIOption option : guiOptions.values()) {
            gui.drawBorderedRect(x, menuY, OPTION_WIDTH, OPTION_HEIGHT, 1.5f, new Color(.3f, .3f, .3f, .9f), new Color(0f, 0f, 0f, 1f));
            // TODO gui.DrawString(option.getName(), x, y, ..., ....);
            menuY += OPTION_HEIGHT;
        }
    }


    @Override
    public void onUpdate() {

    }

    @Override
    public boolean onMouseInput(double x, double y, boolean press) {
        return false;
    }

    @Override
    public void onDrag(GUIMenu guiMenu) {}
}
