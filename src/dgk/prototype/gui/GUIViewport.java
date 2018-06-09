package dgk.prototype.gui;

import dgk.prototype.game.Camera;

/**
 * This GUIViewport extends the Camera class in order to represent a 'Page' in the GUI.
 * This class is specifically made to work/coincide with the GUIVerticalSlider for GUIMenus
 * that need a 'scroll' bar style.
 */
public class GUIViewport extends Camera {

    protected GUI gui;
    protected GUIVerticalSlider verticalSlider;

    /**
     * The max height of the GUIViewport, or basically how far
     * you can move the viewport down to display the other elements.
     */
    protected int maxHeight;

    public GUIViewport(GUI gui, GUIVerticalSlider verticalSlider, double x, double y, double width, double height) {
        super(x, y, width, height);

        this.gui = gui;
        this.verticalSlider = verticalSlider;
    }

    public void onSliderChange() {
        float sliderValue = this.verticalSlider.getSliderValue();
    }

}
