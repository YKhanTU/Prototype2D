package dgk.prototype.gui;

import dgk.prototype.game.GameCamera;
import dgk.prototype.game.GameWindow;

public class GUIInGameMenu extends GUI {

    private static final int NATION_INFORMATION_WIDTH = 150;
    private static final int NATION_INFORMATION_HEIGHT = 250;

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

        debugMenu.addElement(new GUIButton(this, debugMenu, "Options Menu", 680, 155, 75, 20) {
            @Override
            void onButtonClick() {
                playSound();

                GameWindow.getInstance().setGUI(new GUIOptions(800, 600));
            }
        });

        addMenu(debugMenu);

        GUIMenu buildMenu = new GUIMenu(this, "Build Menu", 5, 450, 400, 145, true, false);

//        buildMenu.addElement(new GUIButton(this, buildMenu, 114, "Select", 5, 455, 32, 32) {
//            @Override
//            public void onButtonClick() {
//                playSound();
//
//                System.out.println("BUILDING SELECTION MODE set to SELECT.");
//            }
//        });
//
//        buildMenu.addElement(new GUIButton(this, buildMenu, 115, "Place", 5 + 37, 455, 32, 32) {
//            @Override
//            public void onButtonClick() {
//                playSound();
//
//                System.out.println("BUILDING SELECTION MODE set to PLACE.");
//            }
//        });
//
//        buildMenu.addElement(new GUIButton(this, buildMenu, 116, "Remove", 5 + 74, 455, 32, 32) {
//            @Override
//            public void onButtonClick() {
//                playSound();
//
//                System.out.println("BUILDING SELECTION MODE set to REMOVE.");
//            }
//        });

        addMenu(buildMenu);

        GUIMenu nationMenu = new GUIMenu(this, "Nation Menu", 5, 5, NATION_INFORMATION_WIDTH, NATION_INFORMATION_HEIGHT, false, true);

        nationMenu.addElement(new GUIButton(this, nationMenu, 122, "Currency", 10, 25, 32, 32) {
            @Override
            public void onButtonClick() {}
        });

        nationMenu.addElement(new GUILabel(this, nationMenu, 45, 35, "100"));

        addMenu(nationMenu);
    }
}
