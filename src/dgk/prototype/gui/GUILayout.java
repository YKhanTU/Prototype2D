package dgk.prototype.gui;

/**
 * This Enum represents the position on the screen that a GUIMenu or GUIElement is 'attached' to.
 * When the screen is resized or the GUI is scaled, this will allow us to determine where to move the GUIMenu/GUIElement.
 */
public enum GUILayout {

    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
    CENTER,
    CENTER_TOP,
    CENTER_LEFT,
    CENTER_RIGHT,
    CENTER_BOTTOM;
}
