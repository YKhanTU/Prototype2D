package dgk.prototype.gui;

public interface GUIElement {

    void render();
    void onUpdate();
    boolean onMouseInput(double x, double y, boolean press);
    void onDrag(GUIMenu guiMenu);
}
