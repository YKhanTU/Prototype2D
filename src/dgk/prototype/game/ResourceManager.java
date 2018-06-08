package dgk.prototype.game;

import dgk.prototype.util.IManager;
import dgk.prototype.util.SpriteSheet;

import java.util.HashMap;

public class ResourceManager implements IManager {

    private HashMap<String, SpriteSheet> spriteSheetMap;

    public ResourceManager() {
        this.spriteSheetMap = new HashMap<String, SpriteSheet>();
    }

    @Override
    public void start() {
        // Load all Sprites Sheets
    }

    /**
     * This method destroys all of the textures that the SpriteSheets hold.
     */
    @Override
    public void stop() {
        for(SpriteSheet spriteSheet : spriteSheetMap.values()) {
            spriteSheet.destroyTextures();
        }
    }

    /**
     * Obtains the SpriteSheet related to its name.
     * If no SpriteSheet with 'name' is found, it will return null.
     * @param name The name of the SpriteSheet .PNG
     * @return The SpriteSheet instance
     */
    public SpriteSheet getSpriteSheet(String name) {
        if(!spriteSheetMap.containsKey(name))
            return null;

        return spriteSheetMap.get(name);
    }


    @Override
    public void onUpdate() {}
}
