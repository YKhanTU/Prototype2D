package dgk.prototype.gui;

import dgk.prototype.game.Camera;
import dgk.prototype.game.GameWindow;
import dgk.prototype.game.ResourceManager;
import dgk.prototype.util.Color;
import dgk.prototype.util.Vec2D;
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

    private ResourceManager resourceManager;

    protected double x;
    protected double y;

    protected double width;
    protected double height;

    protected TreeMap<String, GUIOption> guiOptions;

    protected boolean isShowing;

    public GUIOptionsList(GUI gui, double x, double y, double width, double height) {
        this.gui = gui;
        this.resourceManager = GameWindow.getInstance().resourceManager;
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
        drawOptions();
    }

    private void drawOptions() {
        if(!isShowing)
            return;

        if(this.guiOptions.size() == 0)
            return;

        double menuY = 0 + this.y;

        int ctr = 0;

        Set<String> nameSet = guiOptions.keySet();
        String[] names = nameSet.toArray(new String[nameSet.size()]);

        for(GUIOption option : guiOptions.values()) {
            gui.drawBorderedRect(x, menuY, OPTION_WIDTH, OPTION_HEIGHT, 1.5f, new Color(.3f, .3f, .3f, .9f), new Color(0f, 0f, 0f, 1f));
            gui.drawString(resourceManager.getFont("GUILabelFont"), names[ctr], (float) x + 2, (float) menuY + 2);
            menuY += OPTION_HEIGHT;

            ctr++;
        }
    }


    @Override
    public void onUpdate() {

    }

    @Override
    public boolean onMouseInput(double x, double y, boolean press) {
        if(isMouseInside(x, y, this.x, this.y, OPTION_WIDTH, OPTION_HEIGHT * (guiOptions.size() + 1))) {

            double optionX = this.x + 0;
            double optionY = this.y + 0;

            for(int i = 0; i < guiOptions.size(); i++) {
                System.out.println(optionX + ", " + optionY);

                if(isShowing && isMouseInside(x, y, optionX, optionY, OPTION_WIDTH, OPTION_HEIGHT) && press) {
                    Set<String> nameSet = guiOptions.keySet();
                    String[] names = nameSet.toArray(new String[nameSet.size()]);
                    String key = names[i];
                    GUIOption option = guiOptions.get(key);

                    option.onSelection();

                    isShowing = !isShowing;

                    return true;
                }

                optionY += OPTION_HEIGHT;
            }

            return true;
        }

        return false;
    }

    public boolean isMouseInside(double mX, double mY, double x, double y, double w, double h) {
        return (mX >= x && mX <= (x + w)) && (mY >= y && mY <= (y + h));
    }

    @Override
    public void onDrag(GUIMenu guiMenu) {}

    @Override
    public void onWindowResize() {}
}
