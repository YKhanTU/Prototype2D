package dgk.prototype.gui;

import dgk.prototype.font.TrueTypeFont;
import dgk.prototype.game.GameWindow;

public class GUILabel implements GUIElement {

    private GUI gui;
    private GUIMenu guiMenu;

    private TrueTypeFont font;

    private String text;

    private double x;
    private double y;

    private double refX;
    private double refY;

    public GUILabel(GUI gui, GUIMenu guiMenu, double x, double y, String text) {
        this.gui = gui;
        this.guiMenu = guiMenu;
        this.x = x;
        this.y = y;
        this.refX = x - guiMenu.x;
        this.refY = y - guiMenu.y;
        this.font = GameWindow.getInstance().resourceManager.getFont("GUILabelFont");
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void render() {
        gui.drawString(font, text, (float) x, (float) y);
    }

    @Override
    public void onUpdate() {}

    @Override
    public boolean onMouseInput(double x, double y, boolean press) {
        return false;
    }

    @Override
    public void onDrag(GUIMenu guiMenu) {
        this.x = guiMenu.x + refX;
        this.y = guiMenu.y + refY;
    }
}
