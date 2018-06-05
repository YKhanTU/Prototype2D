package dgk.prototype.gui;

public class GUINotification implements GUIElement {

    private static final int WIDTH = 150;
    private static final int HEIGHT = 75;

    private GUI gui;

    /**
     * The message that our GUI notification will be sending out.
     */
    private String message;

    private long startTime;

    /**
     * The amount of time that this notification will "hang" in the GUI before being removed.
     */
    private long hangTime;

    public GUINotification(GUI gui, String message) {
        this(gui, message, 2500);
    }

    public GUINotification(GUI gui, String message, long hangTime) {
        this.gui = gui;
        this.message = message;
        this.startTime = System.currentTimeMillis();
        this.hangTime = hangTime;
    }

    public boolean shouldRemove() {
        return (System.currentTimeMillis() - startTime >= hangTime);
    }

    @Override
    public void render() {
        gui.drawBorderedRect(0, 0, WIDTH, HEIGHT, 1f);
        // drawString(message, x + 5, y + 5);

        /**
         * Drawing a String by splitting it with a given width:
         *
         * We take the width of the 'space' we need to print into
         * We then calculate the overall length of the text, let's call it textLen
         * Then we divide the value of these two: (textLen / width)
         *
         * Then we take this value and use it to split the text given this value
         * based on character size. Boom bada bing
         *
         */
    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onMouseInput(double x, double y, boolean press) {

    }
}
