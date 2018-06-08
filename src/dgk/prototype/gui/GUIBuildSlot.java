package dgk.prototype.gui;

public abstract class GUIBuildSlot extends GUIButton {

    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;

    public GUIBuildSlot(GUI gui, int textureId, double x, double y) {
        super(gui, textureId, null, x, y, WIDTH, HEIGHT);
    }
}
