package dgk.prototype.gui;

import dgk.prototype.game.GameWindow;

public class GUIInGameMenu extends GUI {



    public GUIInGameMenu(int width, int height) {
        super(width, height);
    }

    @Override
    public void init() {
        GUIMenu inGameMenu = new GUIMenu(this, "In-Game UI", 645, 5, 150, 200, true, true);

        inGameMenu.addElement(new GUISlider(this, inGameMenu,670, 75, 100, .25f) {
            @Override
            public void onSliderValueChange() {
                GameWindow.getInstance().getWorldCamera().setChaseFactor(getSliderValue());
            }
        });

        addMenu(inGameMenu);
    }
}
