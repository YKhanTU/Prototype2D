package dgk.prototype.gui;

public class GUIVerticalSlider extends GUISlider {

    /**
     * We always start off with an initValue of 1f because we are at the top of the page. As the value decreases,
     * we use that to move the 'page' down or GUI elements down within the viewport.
     * @param gui The parent GUI
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param length How long the slider is. For a vertical slider, this is the actual length of the clickable part unlike
     *               the horizontal slider, that uses the length value for the actual length of the whole slider.
     */
    public GUIVerticalSlider(GUI gui, double x, double y, double length) {
        super(gui, x, y, length, 1f);
    }

    @Override
    public void onSliderValueChange() {

    }
}
