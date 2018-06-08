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

    private int textureId;

    public GUI(int width, int height) {
        this.camera = new Camera(0, 0, width, height);

        this.guiMenus = new ArrayList<GUIMenu>();
        this.guiNotifications = new ArrayList<GUINotification>();
    }

    public void init() {
        GUIMenu inGameMenu = new GUIMenu(this, "In-Game UI", 645, 5, 150, 200, true, true);

        /**
         * TODO: Add function for GUIMenu that adds an exit button to the UI automatically.
         * TODO for later: Add functions that add minimize, maximize, and pin buttons.
         * TODO as well: Also add texturing to the GUI Elements (Slider, Button, etc)
         */
        inGameMenu.addElement(new GUIButton(this, 48, "Close", 770, 15, 15, 15) {
            @Override
            void onButtonClick() {
                inGameMenu.close();
            }
        });

        inGameMenu.addElement(new GUISlider(this, 670, 75, 100, .25f) {
            @Override
            public void onSliderValueChange() {
                GameWindow.getInstance().getWorldCamera().setChaseFactor(getSliderValue());
            }
        });

        inGameMenu.addElement(new GUIVerticalSlider(this, 670, 100, 100, 20) {
            @Override
            public void onSliderValueChange() {
                System.out.println(getSliderValue());
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
        GL11.glMatrixMode(GL11.GL_MODELVIEW_MATRIX);
        GL11.glPushMatrix();

        GL11.glTranslated(start.getX(), start.getY(), 0);

        GL11.glLineWidth(width);

        GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex2d(0, 0);
            GL11.glVertex2d(end.getX(), end.getY());
        GL11.glEnd();

        GL11.glPopMatrix();
    }

    public void drawRect(double x, double y, double width, double height) {
        GL11.glMatrixMode(GL11.GL_MODELVIEW_MATRIX);
        GL11.glPushMatrix();

        GL11.glTranslated(x, y, 0);

        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glVertex2d(0, 0);
            GL11.glVertex2d(width, 0);
            GL11.glVertex2d(width, height);
            GL11.glVertex2d(0, height);
        }
        GL11.glEnd();

        GL11.glPopMatrix();
    }

    private void drawBorder(double x, double y, double width, double height, float borderWidth) {
        GL11.glMatrixMode(GL11.GL_MODELVIEW_MATRIX);
        GL11.glPushMatrix();

        GL11.glTranslated(x, y, 0);

        GL11.glLineWidth(borderWidth);

        GL11.glBegin(GL11.GL_LINES);
        {
            GL11.glVertex2d(0, 0);
            GL11.glVertex2d(width, 0);
            GL11.glVertex2d(width, 0);
            GL11.glVertex2d(width, height);
            GL11.glVertex2d(0, height);
            GL11.glVertex2d(width, height);
            GL11.glVertex2d(0, 0);
            GL11.glVertex2d(0, height);
        }
        GL11.glEnd();

        GL11.glPopMatrix();
    }

    public void drawBorderedRect(double x, double y, double width, double height, float borderWidth) {
        drawRect(x, y, width, height);
        drawBorder(x, y, width, height, borderWidth);
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

    public void update() {
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
