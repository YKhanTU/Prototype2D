package dgk.prototype.gui;

import dgk.prototype.game.GameCamera;
import dgk.prototype.game.GameWindow;

public class GUIInGameMenu extends GUI {


    public GUIInGameMenu(int width, int height) {
        super(width, height);
    }

    @Override
    public void init() {
        GUIMenu debugMenu = new GUIMenu(this, "In-Game UI", 645, 5, 150, 200, false, true);

        debugMenu.addElement(new GUISlider(this, debugMenu,670, 75, 100, .25f) {
            @Override
            public void onSliderValueChange() {
                GameWindow.getInstance().getWorldCamera().setChaseFactor(getSliderValue());
            }
        });

        debugMenu.addElement(new GUIButton(this, debugMenu, "Camera Toggle", 680, 125, 75, 20) {
            @Override
            void onButtonClick() {
                GameCamera camera = GameWindow.getInstance().getWorldCamera();
                if(camera.hasTarget()) {
                    camera.setTarget(null);
                }else {
                    camera.setTarget(GameWindow.getInstance().world.ruler);
                }
            }
        });

        addMenu(debugMenu);


        GUIMenu buildMenu = new GUIMenu(this, "Build Menu", 0, 450, 400, 150, true, false);


        addMenu(buildMenu);
    }
}
