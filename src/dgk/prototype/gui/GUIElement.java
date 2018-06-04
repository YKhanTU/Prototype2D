package dgk.prototype.gui;

public interface GUIElement {

    void render();
    void onUpdate();
    void onMouseInput(double x, double y, boolean press);
}
