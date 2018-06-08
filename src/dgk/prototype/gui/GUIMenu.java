package dgk.prototype.gui;

import dgk.prototype.game.Camera;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * The GUI Menu, which can contain any possible UI element to create a User Interface
 * that is interactable and is either on the game's front menu or in-game with numerous types
 * of functionality.
 */
public class GUIMenu implements GUIElement {

    private GUI gui;

    private String name;

    private double x;
    private double y;
    private double width;
    private double height;

    private short renderLayer = 0;

    private ArrayList<GUIElement> elements;

    private boolean isDraggable;

    private boolean shouldRemove;

    public GUIMenu(GUI gui, String name, double x, double y, double width, double height, boolean alwaysOnTop, boolean isDraggable) {
        this.gui = gui;
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        if(alwaysOnTop)
            renderLayer = 99;

        this.elements = new ArrayList<GUIElement>();

        this.isDraggable = isDraggable;

        this.shouldRemove = false;
    }

    public void addElement(GUIElement element) {
        elements.add(element);
    }

    /**
     * This function adds the exit, minimize, maximize, and pin buttons to the UI.
     */
    private void addUtilButtons() {

    }

    public void drawMenu() {
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        Camera viewport = gui.getCamera();

        GL11.glMatrixMode(GL11.GL_MODELVIEW_MATRIX);
        GL11.glPushMatrix();

        GL11.glTranslated(x - viewport.getPosition().getY(), y - viewport.getPosition().getX(), 0);

        GL11.glColor4f(.7f, .7f, .7f, .85f);

        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glVertex2d(0, 0);
            GL11.glVertex2d(width, 0);
            GL11.glVertex2d(width, height);
            GL11.glVertex2d(0, height);
        }
        GL11.glEnd();

        GL11.glPopMatrix();

        GL11.glColor3f(1f, 1f, 1f);
    }

    @Override
    public void render() {
        drawMenu();

        for(GUIElement elements : elements) {
            elements.render();
        }
    }

    @Override
    public void onUpdate() {
        for(GUIElement elements : elements) {
            elements.onUpdate();
        }
    }

    @Override
    public void onMouseInput(double x, double y, boolean press) {
        for(GUIElement elements : elements) {
            elements.onMouseInput(x, y, press);
        }
    }

    public void close() {
        shouldRemove = true;
    }

    public boolean shouldRemove() {
        return shouldRemove;
    }
}
