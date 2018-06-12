package dgk.prototype.gui;

import dgk.prototype.game.Camera;
import dgk.prototype.game.GameWindow;
import dgk.prototype.util.Color;
import dgk.prototype.util.Vec2D;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;

/**
 * The GUI, or graphical user interface.
 *
 * The GUI will take the user's input and perform actions and commands based on the input received.
 * The GUI will also be responsive with camera movement, to perform fancy animations and whatnot.
 */
public abstract class GUI {

    public static final int FONT_HEIGHT = 24;

    public static final int NOTIFICATION_LIMIT = 5;
    public static final int NOTIFICATION_Y = 300;

    private Camera camera;

    private ArrayList<GUIMenu> guiMenus;
    private ArrayList<GUINotification> guiNotifications;

    private int textureId;

    public GUI(int width, int height) {
        this.camera = new Camera(0, 0, width, height);

        this.guiMenus = new ArrayList<GUIMenu>();
        this.guiNotifications = new ArrayList<GUINotification>();
    }

    public abstract void init();

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

    public void addNotification(GUINotification notification) {
        if(guiNotifications.size() == NOTIFICATION_LIMIT)
            return;

        guiNotifications.add(notification);
    }

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

    public void drawRect(double x, double y, double width, double height, Color color) {
        GL11.glMatrixMode(GL11.GL_MODELVIEW_MATRIX);
        GL11.glPushMatrix();

        GL11.glTranslated(x, y, 0);

        GL11.glColor4f(color.getR(), color.getG(), color.getB(), color.getA());

        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glVertex2d(0, 0);
            GL11.glVertex2d(width, 0);
            GL11.glVertex2d(width, height);
            GL11.glVertex2d(0, height);
        }
        GL11.glEnd();

        GL11.glPopMatrix();

        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

    private void drawBorder(double x, double y, double width, double height, float borderWidth, Color color) {
        GL11.glMatrixMode(GL11.GL_MODELVIEW_MATRIX);
        GL11.glPushMatrix();

        GL11.glTranslated(x, y, 0);

        GL11.glLineWidth(borderWidth);

        GL11.glColor4f(color.getR(), color.getG(), color.getB(), color.getA());

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

        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

    public void drawBorderedRect(double x, double y, double width, double height, float borderWidth, Color rectColor, Color borderColor) {
        drawRect(x, y, width, height, rectColor);
        drawBorder(x, y, width, height, borderWidth, borderColor);
    }

    /**
     * Draws all of the GUI Notifications.
     */
    private void drawGUINotifications() {
        //final int x = 800 - GUINotification.WIDTH;
        int y = NOTIFICATION_Y;

        Collections.sort(guiNotifications, new Comparator<GUINotification>() {

            @Override
            public int compare(GUINotification o1, GUINotification o2) {
                if(o1.getTimeLeft() > o2.getTimeLeft()) {
                    return 1;
                }else{
                    return -1;
                }
            }
        });

        for(GUINotification guiNotification : guiNotifications) {
            guiNotification.y = y;
            guiNotification.render();
            y += GUINotification.HEIGHT;
        }
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
        boolean guiMenuClicked = false;

        for(GUIMenu guiMenu : guiMenus) {
            if(guiMenu.onMouseInput(x, y,  press)) {
                guiMenuClicked = true;
            }
        }

        if(!guiMenuClicked) {
            GameWindow.getInstance().world.onMouseInput(x, y, press);
        }

        // DEBUG CODE --------------------------------------------------------------------------------

//        if(!guiMenuClicked) {
//            if(press) {
//                addNotification(new GUINotification(this, "You just lost 1000000 Gold!"));
//            }
//        }

        // DEBUG CODE --------------------------------------------------------------------------------
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

        if(hasNotifications()) {
            updateGUINotifications();
        }
    }

    /**
     * Goes through all of the current GUI Notifications and updates them.
     * If a GUI Notification's hang time has exceeded the time past since it
     * has been put on the screen, it is removed from the GUINotifications List.
     */
    private void updateGUINotifications() {
        Iterator<GUINotification> notificationIterator = guiNotifications.iterator();

        while(notificationIterator.hasNext()) {
            GUINotification notification = notificationIterator.next();

            if(notification.shouldRemove()) {
                notificationIterator.remove();
            }else{
                notification.onUpdate();
            }
        }
    }

    public void render() {
        for(GUIMenu guiMenu : guiMenus) {
            guiMenu.render();
        }

        drawGUINotifications();
    }

    /**
     * This lets us know if we have any active notifications in the GUI at the moment.
     * @return Returns the size of the GUINotifications ArrayList.
     */
    public boolean hasNotifications() {
        return guiNotifications.size() > 0;
    }

    public Camera getCamera() {
        return this.camera;
    }
}
