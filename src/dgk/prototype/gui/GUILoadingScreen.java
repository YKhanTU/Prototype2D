package dgk.prototype.gui;

import dgk.prototype.game.ResourceManager;
import dgk.prototype.util.Color;

public class GUILoadingScreen extends GUI {

    public static final int PROGRESS_BAR_WIDTH = 200;

    private int progress;
    private int goal;

    public GUILoadingScreen(int width, int height, ResourceManager resourceManager) {
        super(width, height);

        this.progress = 0;
        this.goal = 100;
    }

    @Override
    public void init() {}

    @Override
    public void render() {
        this.drawBorderedRect(800 / 2 - 100, 600 / 2 - 20, PROGRESS_BAR_WIDTH * getCurrentProgress(), 40, 2f,
                new Color(0f, 1f, 0f, 1f),
                new Color(0f, 0f, 0f, 1f));
    }

    @Override
    public void update() {
        super.update();

        if(!isComplete()) {
            progress++;
        }
    }

    public double getCurrentProgress() {
        return ((double) progress / (double) goal);
    }

    public boolean isComplete() {
        return getCurrentProgress() >= 1.0D;
    }
}
