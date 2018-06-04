package dgk.prototype.gui;

import dgk.prototype.game.Camera;
import dgk.prototype.game.GameWindow;
import dgk.prototype.util.Vec2D;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Iterator;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

/**
 * The GUI, or graphical user interface.
 *
 * The GUI will take the user's input and perform actions and commands based on the input received.
 * The GUI will also be responsive with camera movement, to perform fancy animations and whatnot.
 */
public class GUI {

    public static final int FONT_HEIGHT = 24;

    private Camera camera;

    private ArrayList<GUIMenu> guiMenus;
    private ArrayList<GUINotification> guiNotifications;


    public GUI(int width, int height) {
        this.camera = new Camera(0, 0, width, height);

        this.guiMenus = new ArrayList<GUIMenu>();
        this.guiNotifications = new ArrayList<GUINotification>();
    }

    public void init() {
        GUIMenu inGameMenu = new GUIMenu(this, "In-Game UI", 645, 5, 150, 200, true);

        /**
         * TODO: Add function for GUIMenu that adds an exit button to the UI automatically.
         * TODO for later: Add functions that add minimize, maximize, and pin buttons.
         * TODO as well: Also add texturing to the GUI Elements (Slider, Button, etc)
         */
        inGameMenu.addElement(new GUIButton(this, "Close", 780, 10, 10, 10) {
            @Override
            void onButtonClick() {
                inGameMenu.close();
            }
        });

        addMenu(inGameMenu);
    }

    public void onClose() {
        guiMenus.clear();
        guiNotifications.clear();
    }

    public void addMenu(GUIMenu menu) {
        guiMenus.add(menu);
    }

    //public void drawString(String text, int x, int y) {
    //    trueTypeFont.drawString(x, y, text);
    //}

    public void drawLine(Vec2D start, Vec2D end, float width) {
        GL11.glLineWidth(width);

        GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2d(start.getX(), start.getY());
            GL11.glVertex2d(end.getX(), end.getY());
        GL11.glEnd();
    }

    public Vec2D getMousePosition() {
        DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);

        glfwGetCursorPos(GameWindow.getInstance().getHandle(), xBuffer, yBuffer);

        double mX = xBuffer.get(0);
        double mY = yBuffer.get(0);

        return new Vec2D(mX, mY);
    }

    public void onMouseInput(double x, double y, boolean press) {
        for(GUIMenu guiMenu : guiMenus) {
            guiMenu.onMouseInput(x, y,  press);
        }
    }

    public void onUpdate() {
        Iterator<GUIMenu> menuIterator = guiMenus.iterator();

        while(menuIterator.hasNext()) {
            GUIMenu menu = menuIterator.next();

            if(menu.shouldRemove()) {
                menuIterator.remove();
            }else{
                menu.onUpdate();
            }
        }
    }

    public void render() {
        for(GUIMenu guiMenu : guiMenus) {
            guiMenu.render();
        }
    }

    public Camera getCamera() {
        return this.camera;
    }
}
