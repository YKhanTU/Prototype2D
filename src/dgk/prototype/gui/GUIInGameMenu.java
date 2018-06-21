package dgk.prototype.gui;

import dgk.prototype.game.GameCamera;
import dgk.prototype.game.GameWindow;
import dgk.prototype.game.entities.Ruler;
import dgk.prototype.game.tile.Tile;
import dgk.prototype.game.tile.TileMap;
import dgk.prototype.game.tile.World;
import dgk.prototype.util.Color;

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

        GUIMenu nationMenu = new GUIMenu(this, "Nation Menu", GUILayout.TOP_LEFT, 5, 5, NATION_INFORMATION_WIDTH, NATION_INFORMATION_HEIGHT, false, true);

        nationMenu.addElement(new GUIButton(this, nationMenu, 122, "Currency", 5, 20, 32, 32) {
            @Override
            public void onButtonClick() {}
        });

        nationMenu.addElement(new GUILabel(this, nationMenu, 40, 30, "100"));

        addMenu(nationMenu);
    }

    @Override
    public void render() {
//        GameWindow gw = GameWindow.getInstance();
//        World world = gw.world;
//
//        Ruler r = world.ruler;
//
//        if(r != null) {
//            Tile t = world.getTileMap().getTile(r.getGridX(), r.getGridY());
//
//            double gX = t.getGridX() * TileMap.TILE_SIZE;
//            double gY = t.getGridY() * TileMap.TILE_SIZE;
//
//            this.drawRect(gX - gw.getWorldCamera().getPosition().getX(), gY  - gw.getWorldCamera().getPosition().getY(), t.getSize(), t.getSize(),
//                    new Color(1f, 1f, 1f, .3f));
//        }

        super.render();
    }
}
