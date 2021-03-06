package dgk.prototype.gui;

public class GUISelectionBubble implements GUIElement {

    private GUI gui;

    private boolean isSelected;

    public GUISelectionBubble(GUI gui) {
        this.gui = gui;
        this.isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public void render() {

    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onDrag(GUIMenu guiMenu) {}

    @Override
    public boolean onMouseInput(double x, double y, boolean press) {
        return false;
    }

    @Override
    public void onWindowResize() {}
}
