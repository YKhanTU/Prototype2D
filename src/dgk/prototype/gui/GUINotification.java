package dgk.prototype.gui;

public class GUINotification implements GUIElement {

    /**
     * The message that our GUI notification will be sending out.
     */
    private String message;

    private long startTime;

    /**
     * The amount of time that this notification will "hang" in the GUI before being removed.
     */
    private long hangTime;

    public GUINotification(String message) {
        this(message, 2500);
    }

    public GUINotification(String message, long hangTime) {
        this.message = message;
        this.startTime = System.currentTimeMillis();
        this.hangTime = hangTime;
    }

    public boolean shouldRemove() {
        return (System.currentTimeMillis() - startTime >= hangTime);
    }

    @Override
    public void render() {

    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onMouseInput(double x, double y, boolean press) {

    }
}
