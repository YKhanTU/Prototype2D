package dgk.prototype.gui;

import dgk.prototype.game.Camera;
import dgk.prototype.util.Color;
import dgk.prototype.util.Vec2D;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * The GUI Menu, which can contain any possible UI element to create a User Interface
 * that is interactable and is either on the game's front menu or in-game with numerous types
 * of functionality.
 */
public class GUIMenu implements GUIElement {

    private GUI gui;

    private String name;

    private GUILayout layout;

    protected double x;
    protected double y;
    
    protected double refX;
    protected double refY;

    private double width;
    private double height;

    private short renderLayer = 0;

    private ArrayList<GUIElement> elements;

    private boolean isAlwaysOnTop;
    private boolean isDraggable;
    private boolean isBeingDragged;
    private boolean isPinned;

    private boolean shouldRemove;

    public GUIMenu(GUI gui, String name, GUILayout layout, double x, double y, double width, double height, boolean alwaysOnTop, boolean isDraggable) {
        this.gui = gui;
        this.name = name;
        this.layout = layout;
        this.x = x;
        this.y = y;
        this.refX = 0;
        this.refY = 0;
        this.width = width;
        this.height = height;

        this.isAlwaysOnTop = alwaysOnTop;

        if(alwaysOnTop)
            renderLayer = 99;

        this.elements = new ArrayList<GUIElement>();

        this.isDraggable = isDraggable;
        this.isBeingDragged = false;
        this.isPinned = false;

        this.shouldRemove = false;

        addUtilButtons();

        // Deal with the Layout
        onLayoutChange();
    }

    public GUIMenu(GUI gui, String name, double x, double y, double width, double height) {
        this(gui, name, GUILayout.CENTER, x, y, width, height, false, false);
    }

    public void addElement(GUIElement element) {
        elements.add(element);
    }

    /**
     * This function adds the exit, minimize, maximize, and pin buttons to the UI.
     */
    protected void addUtilButtons() {
        if(!isAlwaysOnTop) {
            addElement(new GUIButton(gui, this, 26, "Close", width - 20, 5, 15, 15) {
                @Override
                void onButtonClick() {
                    close();
                }
            });
        }

        if(isDraggable) {
            addElement(new GUIButton(gui, this, 28, "Pin", width - 40, 5, 15, 15) {
                @Override
                void onButtonClick() {
                    if (isPinned) {
                        System.out.println("Menu Un-pinned!");
                    } else {
                        System.out.println("Menu pinned.");
                    }

                    isPinned = !isPinned;
                }
            });
            addElement(new GUIButton(gui, this, 27, "Minimize",  width- 60, 5, 15, 15) {
                @Override
                void onButtonClick() {
                    System.out.println("Minimize Button Clicked.");
                }
            });
        }
    }

    public void drawMenu() {
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        Camera viewport = gui.getCamera();

        gui.drawBorderedRect(x - viewport.getPosition().getX(), y - viewport.getPosition().getY(), width, height,
                2f, new Color(.7f, .7f, .7f, .85f), new Color(0f, 0f, 0f, 1f));
    }

    public GUILabel getGUILabel(String name) {
        for(GUIElement element : elements) {
            if(element instanceof GUILabel) {
                GUILabel label = (GUILabel) element;

                if(name.equalsIgnoreCase(label.getName())) {
                    return label;
                }
            }
        }

        return null;
    }

    @Override
    public void render() {
        drawMenu();

        for(GUIElement elements : elements) {
            elements.render();
        }
    }

    @Override
    public void onUpdate() {
        if(isDraggable && isBeingDragged && !isPinned) {
            this.onDrag(this);
        }

        for(GUIElement elements : elements) {
            elements.onUpdate();
        }
    }

    @Override
    public boolean onMouseInput(double x, double y, boolean press) {
        // This variable is used to determine if any elements were clicked inside the menu.
        if(!press && isBeingDragged) {
            isBeingDragged = false;
            return false;
        }

        boolean elementClicked = false;

        for(GUIElement elements : elements) {
            if(elements.onMouseInput(x, y, press)) {

                if(!elementClicked) {
                    elementClicked = true;
                }
            }
        }

        if((x >= this.x && x <= (this.x + width)) && (y >= this.y && y <= (this.y + height)) && !elementClicked) {
            if(press) {
                if(isBeingDragged) {
                    return false;
                }else if(isPinned) {
                    return true;
                }

                isBeingDragged = true;

                this.refX = x - this.x;
                this.refY = y - this.y;

                return true;
            }
        }

        if(elementClicked) {
            return true;
        }

        return false;
    }

    @Override
    public void onDrag(GUIMenu guiMenu) {

        Vec2D pos = gui.getMousePosition();

        double mX = pos.getX();
        double mY = pos.getY();

        this.x = mX - refX;
        this.y = mY - refY;

        for(GUIElement element : elements) {
            element.onDrag(this);
        }
    }

    @Override
    public void onWindowResize() {
        onLayoutChange();
    }

    protected void onLayoutChange() {
        GUILayout layout = this.layout;

        double width = gui.getCamera().getWidth();
        double height = gui.getCamera().getHeight();

        double borderX = GUI.BORDER_WIDTH;
        double borderY = GUI.BORDER_WIDTH;

        if(isDraggable) {
            if(x >= (width / 2)) {
                //borderX = width - (this.x + this.width);
                //this.x = (width - (this.width)) - borderX;
                this.x = (width - this.width) - borderX;
            }

            if(y >= (height / 2)) {
                //borderY = height - (this.y + this.height);
                //this.y = (height - this.height) - borderY;
                this.y = borderY;
            }

            for(GUIElement guiElement : elements) {
                guiElement.onDrag(this);
            }

            return;
        }

        switch(layout) {
            case TOP_LEFT:
                this.x = GUI.BORDER_WIDTH;
                this.y = GUI.BORDER_WIDTH;
                break;
            case TOP_RIGHT:
                this.x = (width - this.width) - GUI.BORDER_WIDTH;
                this.y = GUI.BORDER_WIDTH;
                break;
            case BOTTOM_LEFT:
                this.x = GUI.BORDER_WIDTH;
                this.y = (height - this.height) - GUI.BORDER_WIDTH;
                break;
            case BOTTOM_RIGHT:
                this.x = (width - this.width) - GUI.BORDER_WIDTH;
                this.y = (height - this.height) - GUI.BORDER_WIDTH;
                break;
            case CENTER:
                this.x = (width / 2) - (this.width / 2);
                this.y = (height / 2) - (this.height / 2);
                break;
            default:
                break;
        }

        for(GUIElement guiElement : elements) {
            guiElement.onDrag(this);
        }
    }

    public void close() {
        shouldRemove = true;
    }

    public boolean shouldRemove() {
        return shouldRemove;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
