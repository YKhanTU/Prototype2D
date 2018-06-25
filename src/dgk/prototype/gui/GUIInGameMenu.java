package dgk.prototype.gui;

import dgk.prototype.game.GameCamera;
import dgk.prototype.game.GameWindow;
import dgk.prototype.game.Nation;
import dgk.prototype.game.entities.Ruler;
import dgk.prototype.game.tile.Tile;
import dgk.prototype.game.tile.TileMap;
import dgk.prototype.game.tile.World;
import dgk.prototype.util.Color;
import dgk.prototype.util.SpriteSheet;

public class GUIInGameMenu extends GUI {

    private static final int NATION_INFORMATION_WIDTH = 150;
    private static final int NATION_INFORMATION_HEIGHT = 250;

    public GUIInGameMenu(int width, int height) {
        super(width, height);
        init();
    }

    @Override
    public void init() {
        GUIMenu debugMenu = new GUIMenu(this, "In-Game UI", GUILayout.TOP_RIGHT, 645, 5, 150, 200, false, true);

        debugMenu.addElement(new GUISlider(this, debugMenu,25, 70, 100, .25f) {
            @Override
            public void onSliderValueChange() {
                GameWindow.getInstance().getWorldCamera().setChaseFactor(getSliderValue());
            }
        });

        debugMenu.addElement(new GUIButton(this, debugMenu, "Camera Toggle", 35, 120, 75, 20) {
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

        debugMenu.addElement(new GUIButton(this, debugMenu, "Options Menu", 35, 150, 75, 20) {
            @Override
            void onButtonClick() {
                playSound();

                GameWindow.getInstance().setGUI(new GUIOptions(800, 600));
            }
        });

        addMenu(debugMenu);

        GUIMenu buildMenu = new GUIMenu(this, "Build Menu", GUILayout.BOTTOM_LEFT, 5, 450, 400, 145, true, false);

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

        //Nation playerNation = GameWindow.getInstance().world.playerNation;

        GUIMenu nationMenu = new GUIMenu(this, "Nation Menu", GUILayout.TOP_LEFT, 5, 5, NATION_INFORMATION_WIDTH, NATION_INFORMATION_HEIGHT, false, true);

        nationMenu.addElement(new GUIButton(this, nationMenu, 122, "Currency", 5, 20, 32, 32) {
            @Override
            public void onButtonClick() {}
        });

        nationMenu.addElement(new GUILabel(this, nationMenu, "CurrencyLabel",40, 30, "0"));

        nationMenu.addElement(new GUIButton(this, nationMenu, SpriteSheet.POPULATION_ICON, "Population", 5, 57, 32, 32) {

            @Override
            void onButtonClick() {}
        });

        nationMenu.addElement(new GUILabel(this, nationMenu, "PopulationLabel",40, 67, "1"));

        nationMenu.addElement(new GUIButton(this, nationMenu, SpriteSheet.FOOD_ICON, "Food", 5, 94, 32, 32) {

            @Override
            void onButtonClick() {}
        });

        nationMenu.addElement(new GUILabel(this, nationMenu,"FoodLabel", 40, 104, "100"));

        nationMenu.addElement(new GUIButton(this, nationMenu, SpriteSheet.WOOD_ICON, "Wood", 5, 131, 32, 32) {

            @Override
            void onButtonClick() {}
        });

        nationMenu.addElement(new GUILabel(this, nationMenu, "WoodLabel",40, 141, "100"));


        addMenu(nationMenu);


        GUIOptionsList testDropdown = new GUIOptionsList(this, 50, 50, 150, 500);
        testDropdown.addOption("Walk here", () -> System.out.println("RuneScape 2007 was the best of its kind."));
        testDropdown.addOption("Select Tile", () -> System.out.println("Tiled Selected!"));

        addDropDownMenu(testDropdown);
    }

    @Override
    public void render() {
        super.render();
    }
}
