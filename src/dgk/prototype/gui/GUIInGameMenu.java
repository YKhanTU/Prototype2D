package dgk.prototype.gui;

import dgk.prototype.game.GameCamera;
import dgk.prototype.game.GameWindow;

public class GUIInGameMenu extends GUI {


    public GUIInGameMenu(int width, int height) {
        super(width, height);
        init();
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
                playSound();

                GameCamera camera = GameWindow.getInstance().getWorldCamera();
                if(camera.hasTarget()) {
                    camera.setTarget(null);
                }else {
                    camera.setTarget(GameWindow.getInstance().world.ruler);
                }
            }
        });

        debugMenu.addElement(new GUIButton(this, debugMenu, "OptionsMenu Test", 680, 155, 75, 20) {
            @Override
            void onButtonClick() {
                playSound();

                GameWindow.getInstance().setGUI(new GUIOptions(800, 600));
            }
        });

        addMenu(debugMenu);


        GUIMenu buildMenu = new GUIMenu(this, "Build Menu", 0, 450, 400, 150, true, false);

        buildMenu.addElement(new GUIButton(this, buildMenu, 84, "Select", 5, 455, 32, 32) {
            @Override
            public void onButtonClick() {
                playSound();

                System.out.println("BUILDING SELECTION MODE set to SELECT.");
            }
        });

        buildMenu.addElement(new GUIButton(this, buildMenu, 82, "Place", 5 + 37, 455, 32, 32) {
            @Override
            public void onButtonClick() {
                playSound();

                System.out.println("BUILDING SELECTION MODE set to PLACE.");
            }
        });

        buildMenu.addElement(new GUIButton(this, buildMenu, 83, "Remove", 5 + 74, 455, 32, 32) {
            @Override
            public void onButtonClick() {
                playSound();

                System.out.println("BUILDING SELECTION MODE set to REMOVE.");
            }
        });

        addMenu(buildMenu);
    }
}
