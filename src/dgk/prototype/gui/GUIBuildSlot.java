package dgk.prototype.gui;

public abstract class GUIBuildSlot extends GUIButton {

    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;

    // TODO: Create a GUIOptionsListFactory that has default GUIOptionLists so we can easily generate them when we click a Button.

    public GUIBuildSlot(GUI gui, GUIMenu guiMenu, int textureId, double x, double y) {
        super(gui, guiMenu, textureId, null, x, y, WIDTH, HEIGHT);
    }
}
