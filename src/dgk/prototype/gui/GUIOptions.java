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
        GUIMenu optionsMenu = new GUIMenu(this, "Options", GUILayout.CENTER, 200, 150, 400, 300, true, false) {
            // We do not want an 'exit' button at the top right corner.
            @Override
            protected void addUtilButtons() {}
        };

        GUILabel volumeLabel = new GUILabel(this, optionsMenu, "VolumeLabel", 35, 10, "Volume: ");
        GUILabel sliderLabel = new GUILabel(this, optionsMenu, "VolumeLabel2", 320, 10, "" + soundManager.getCurrentVolume());

        optionsMenu.addElement(new GUISlider(this, optionsMenu, 100, 20, 200, soundManager.getCurrentVolume()) {
            @Override
            public void onSliderValueChange() {
                soundManager.setCurrentVolume(getSliderValue());
                sliderLabel.setText("" + soundManager.getCurrentVolume());
                System.out.println("Volume set to: " + soundManager.getCurrentVolume());
            }
        });

        optionsMenu.addElement(volumeLabel);
        optionsMenu.addElement(sliderLabel);

        optionsMenu.addElement(new GUIButton(this, optionsMenu, "Exit", 175, 280, 50, 15) {
            @Override
            public void onButtonClick() {
                playSound();

                GameWindow gameWindow = GameWindow.getInstance();

                gameWindow.setGUI(new GUIInGameMenu(gameWindow.getWidth(), gameWindow.getHeight()));

                System.out.println("Exiting...");

            }
        });

        addMenu(optionsMenu);
    }
}
