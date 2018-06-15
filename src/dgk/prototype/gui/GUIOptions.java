package dgk.prototype.gui;

import dgk.prototype.game.GameWindow;
import dgk.prototype.sound.SoundManager;

public class GUIOptions extends GUI {

    private SoundManager soundManager;

    public GUIOptions(int width, int height) {
        super(width, height);

        this.soundManager = GameWindow.getInstance().soundManager;

        init();
    }

    @Override
    public void init() {
        GUIMenu optionsMenu = new GUIMenu(this, "Options", 200, 150, 400, 300, true, false) {
            // We do not want an 'exit' button at the top right corner.
            @Override
            protected void addUtilButtons() {}
        };

        optionsMenu.addElement(new GUISlider(this, optionsMenu, 300, 170, 200, soundManager.getCurrentVolume()) {
            @Override
            public void onSliderValueChange() {
                soundManager.setCurrentVolume(getSliderValue());
                System.out.println("Volume set to: " + soundManager.getCurrentVolume());
            }
        });

        optionsMenu.addElement(new GUIButton(this, optionsMenu, "Exit", (800 / 2) - 25, 430, 50, 15) {
            @Override
            public void onButtonClick() {
                playSound();

                GameWindow.getInstance().setGUI(new GUIInGameMenu(800, 600));

                System.out.println("Exiting...");

            }
        });

        addMenu(optionsMenu);
    }
}
