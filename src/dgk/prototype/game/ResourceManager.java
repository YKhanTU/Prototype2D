package dgk.prototype.game;

import dgk.prototype.util.IManager;
import dgk.prototype.util.SpriteSheet;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

public class ResourceManager implements IManager {

    /**
     * The total amount of textures that we need to load.
     * Used to determine the 'progress' of the ResourceManager.
     */
    public static final int GOAL = 69;

    protected int currentProgress;

    private boolean isComplete;

    private HashMap<String, SpriteSheet> spriteSheetMap;

    public ResourceManager() {
        this.currentProgress = 0;
        this.isComplete = false;
        this.spriteSheetMap = new HashMap<String, SpriteSheet>();
    }

    @Override
    public void start() {
        SpriteSheet tileSheet = new SpriteSheet(this,"sprites/test/A2/A1updated");

        // GRASS
        tileSheet.loadTexture(1, 2, 48, 48);
        tileSheet.loadTexture(1, 3, 48, 48);
        // STONE AND BRICK
        tileSheet.loadTexture(0, 1, 48, 48);
        tileSheet.loadTexture(1, 1, 48, 48);
        // DIRT
        tileSheet.loadTexture(1, 0, 48, 48);
        // WATER
        tileSheet.loadTexture(0, 2, 48, 48);

        // PATHWAYS (DIRT)
        //      HORIZONTAL SIDEWAYS PIECE
        tileSheet.loadTexture(2, 1, 48, 48);
        //      VERTICAL SIDEWAYS PIECE
        tileSheet.loadTexture(2, 0, 48, 48);
        //      FOUR WAY MULTI-CONNECTOR PIECE
        tileSheet.loadTexture(2, 2, 48, 48);
        //      LEFT-CORNER PIECE
        tileSheet.loadTexture(3, 0, 48, 48);
        //      RIGHT-CORNER PIECE
        tileSheet.loadTexture(4, 0, 48, 48);
        //      BOTTOM LEFT-CORNER PIECE
        tileSheet.loadTexture(3, 1, 48, 48);
        //      BOTTOM RIGHT-CORNER PIECE
        tileSheet.loadTexture(4, 1, 48, 48);
        //      THREE WAY MULTI-CONNECTOR PIECE
        tileSheet.loadTexture(3, 2, 48, 48);
        //      SIDE END 1 PIECE
        tileSheet.loadTexture(3, 3, 48, 48);
        //      SIDE END 2 PIECE
        tileSheet.loadTexture(4, 3, 48, 48);
        //      SIDE END 3 PIECE
        tileSheet.loadTexture(5, 2, 48, 48);
        //      SIDE END 4 PIECE
        tileSheet.loadTexture(5, 3, 48, 48);

        // SCENERY
        //      TREES
        tileSheet.loadTexture(288, 0, 96);
        //      SHRUBS
        tileSheet.loadTexture(6, 2, 48, 48);

        // WALLS (WOOD)
        //      WOOD GATE
        tileSheet.loadTexture(528, 347, 96);
        //      WOOD WALL NORTH PIECE 1A
        tileSheet.loadTexture(8, 1, 48, 48);
        //      WOOD WALL NORTH PIECE 1B
        tileSheet.loadTexture(8, 2, 48, 48);
        //      WOOD WALL NORTH PIECE 1C
        tileSheet.loadTexture(11, 0, 48, 48);
        //      WOOD WALL NORTH STAIR 1
        tileSheet.loadTexture(8, 7, 48, 48);


        //      WOOD WALL SOUTH PIECE 1A
        //tileSheet.loadTexture(8, 1, 48, 48);

        spriteSheetMap.put("TileSpriteSheet", tileSheet);

        SpriteSheet uiElements = new SpriteSheet(this, "InterfaceSheetTrans");
        uiElements.loadTexture(0, 0, 32, 32);
        uiElements.loadTexture(1, 0, 32, 32);
        uiElements.loadTexture(2, 0, 32, 32);
        uiElements.loadTexture(3, 0, 32, 32);

        spriteSheetMap.put("UISpriteSheet", uiElements);

        SpriteSheet rulerWalkingAnims = new SpriteSheet(this, "sprites/test/sprites");
        for(int col = 0; col < 4; col++) {
            for(int row = 0; row < 3; row++) {
                rulerWalkingAnims.loadTexture(row, col, 50, 50);
            }
        }

        spriteSheetMap.put("RulerWalkAnimations", rulerWalkingAnims);

        SpriteSheet personShadow = new SpriteSheet(this, "Shadow");
        personShadow.loadTexture(0, 0, 32, 32);
        spriteSheetMap.put("Shadow", personShadow);

        SpriteSheet rulerWalkingHighlight = new SpriteSheet(this, "sprites/test/sprites_glow");
        for(int col = 0; col < 4; col++) {
            for(int row = 0; row < 3; row++) {
                rulerWalkingHighlight.loadTexture(row, col, 50, 50);
            }
        }

        spriteSheetMap.put("RulerGlow", rulerWalkingHighlight);

        SpriteSheet rulerBreathingAnims = new SpriteSheet(this, "sprites/test/A2/Ruler_breathing");
        for(int row = 0; row < 3; row++) {
            rulerBreathingAnims.loadTexture(row, 0, 50, 50);
        }

        spriteSheetMap.put("RulerIdleAnimations", rulerBreathingAnims);

        SpriteSheet peasantWalkingAnims = new SpriteSheet(this, "sprites/test/sprites2");

        for(int col = 0; col < 4; col++) {
            for(int row = 0; row < 3; row++) {
                peasantWalkingAnims.loadTexture(row, col, 48, 48);
            }
        }

        spriteSheetMap.put("PeasantWalkAnimations", peasantWalkingAnims);

        SpriteSheet peasantWalkingHighlights = new SpriteSheet(this, "sprites/test/sprites2_glow");

        for(int col = 0; col < 4; col++) {
            for(int row = 0; row < 3; row++) {
                peasantWalkingHighlights.loadTexture(row, col, 48, 48);
            }
        }

        spriteSheetMap.put("PeasantGlow", peasantWalkingHighlights);

        // TODO - To finish.

        /*rulerAnimations = new SpriteSheet("sprites/test/sprites");

        for(int col = 0; col < 4; col++) {
            for(int row = 0; row < 3; row++) {
                rulerAnimations.loadTexture(row, col, 50, 50);
            }
        }

        peasantAnimations = new SpriteSheet("sprites/sprites2");

        for(int col = 0; col < 4; col++) {
            for(int row = 0; row < 3; row++) {
                peasantAnimations.loadTexture(row, col, 48, 48);
            }
        }

        spriteSheet.loadTexture(6, 2, 48, 48);
        spriteSheet.loadTexture(234, 0, 96);

        spriteSheet.loadTexture(11, 1, 48, 48);
        spriteSheet.loadTexture(11, 0, 48, 48);
        spriteSheet.loadTexture(11, 2, 48, 48);

        rulerHighlights = new SpriteSheet("sprites/test/sprites_glow");

        for(int col = 0; col < 4; col++) {
            for(int row = 0; row < 3; row++) {
                rulerHighlights.loadTexture(row, col, 50, 50);
            }
        }*/

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

    public void onTextureLoad() {
        currentProgress++;
    }

    public double getCurrentProgress() {
        return currentProgress / GOAL;
    }

    public boolean isComplete() {
        return this.isComplete;
    }

    public void setComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    @Override
    public void onUpdate() {}
}
