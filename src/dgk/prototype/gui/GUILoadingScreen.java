package dgk.prototype.gui;

import dgk.prototype.game.ResourceManager;

public class GUILoadingScreen extends GUI {

    private GUIProgressBar progressBar;

    public GUILoadingScreen(int width, int height, ResourceManager resourceManager) {
        super(width, height);

        this.progressBar = new GUIProgressBar(this, resourceManager);
    }

    @Override
    public void init() {}

    @Override
    public void render() {
        progressBar.render();
    }
}
