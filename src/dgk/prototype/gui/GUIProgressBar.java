package dgk.prototype.gui;

import de.matthiasmann.twl.renderer.Resource;
import dgk.prototype.game.ResourceManager;
import dgk.prototype.util.Color;

public class GUIProgressBar implements GUIElement {

    private static final int WIDTH = 250;
    private static final int HEIGHT = 50;

    private GUI gui;
    private ResourceManager resourceManager;

    public GUIProgressBar(GUI gui, ResourceManager resourceManager) {
        this.gui = gui;
        this.resourceManager = resourceManager;
    }

    @Override
    public void render() {
        gui.drawRect(400 - (WIDTH / 2), 300 - (HEIGHT / 2), WIDTH * resourceManager.getCurrentProgress(), HEIGHT, new Color(0f, 1f, 0f, 1f));
        gui.drawBorderedRect(800 - (WIDTH / 2), 600 - (HEIGHT / 2), WIDTH, HEIGHT, 3f, new Color(0f, 0f, 0f, 0f), new Color(1f, 1f, 1f, 1f));
    }

    @Override
    public void onUpdate() {}

    @Override
    public boolean onMouseInput(double x, double y, boolean press) {
        return false;
    }

    @Override
    public void onDrag(GUIMenu guiMenu) {

    }

    @Override
    public void onWindowResize() {}
}
