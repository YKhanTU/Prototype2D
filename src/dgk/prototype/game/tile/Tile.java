package dgk.prototype.game.tile;

import dgk.prototype.game.Camera;
import dgk.prototype.game.GameWindow;
import dgk.prototype.game.IEntity;
import dgk.prototype.util.AABB;
import dgk.prototype.util.Vec2D;
import javafx.util.Pair;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class Tile extends GameObject {

    /**
     * A private copy of the World Camera to be used for rendering the
     * actual Tile object based on the perspective of the latter.
     */
    private transient Camera worldCamera;

    /**
     * The tile size of the Tile. Normally would be TileMap.TILE_SIZE.
     */
    private int size;

    /**
     * The 'z' layer of the Object. The higher it is, the last likely it will be rendered
     * onto the canvas. Other objects will be sorted based on how high their y value is as well.
     */
    private byte renderLayer;

    /**
     * The boolean that represents if this Tile is collidable or not. If it is passable,
     * then the tile has no AABB or onCollision functionality.
     */
    private boolean isPassable;

    /**
     * This boolean value is used to determine if this Tile moves or not. This is only used for
     * a sub-class of Tile that actually will move around or be able to be moved.
     */
    private boolean isStatic;

    protected boolean isSelected;

    private Tile parent;

    public Tile(int textureId, byte renderLayer, int x, int y, int size) {
        super(textureId, new Vec2D(x, y), size, size);

        this.size = size;

        this.renderLayer = renderLayer;

        this.isPassable = true;

        this.isStatic = true;

        this.isSelected = false;

        this.parent = null;

        this.worldCamera = GameWindow.getInstance().getWorldCamera();
    }

    public boolean hasParent() {
        return (parent != null);
    }

    public void setParent(Tile tile) {
        this.parent = tile;
    }

    public byte getRenderLayer() {
        return renderLayer;
    }

    public boolean isPassable() {
        return isPassable;
    }

    public void setPassable(boolean isPassable) {
        this.isPassable = isPassable;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    @Override
    public void render() {
        glEnable(GL_TEXTURE_2D);

        GL11.glMatrixMode(GL_MODELVIEW);
        GL11.glPushMatrix();

        //GL11.glTranslated(-(getPosition().x - worldCamera.getPosition().getX()), -(getPosition().y - worldCamera.getPosition().getY()), 0);
        GL11.glScalef(worldCamera.getZoom(), worldCamera.getZoom(), 0);
        GL11.glTranslated(getPosition().x - worldCamera.getPosition().getX(), getPosition().y - worldCamera.getPosition().getY(), 0);

        glColor4f(1f, 1f, 1f, 1f);

        GameWindow.getInstance().resourceManager.getSpriteSheet("TileSpriteSheet").bindTexture(getTextureId());

        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2f(0, 0);

            GL11.glTexCoord2f(1, 0);
            GL11.glVertex2f(size, 0);

            GL11.glTexCoord2f(1, 1);
            GL11.glVertex2f(size, size);

            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2f(0, size);
        }
        GL11.glEnd();

        int error = glGetError();

        if(error != 0) {
            System.out.println(glGetError());
        }

        GL11.glPopMatrix();

        glDisable(GL_TEXTURE_2D);

        if(TileMap.DEBUG_MODE && isSelected && TileMap.BUILDING_MODE) {
            drawOutline();
        }
    }

    /**
     * A debug outline drawn over the Tile.
     */
    private void drawOutline() {
        GL11.glMatrixMode(GL_MODELVIEW);
        GL11.glPushMatrix();

        GL11.glScalef(worldCamera.getZoom(), worldCamera.getZoom(), 0);
        GL11.glTranslated(getPosition().x - worldCamera.getPosition().getX(), getPosition().y - worldCamera.getPosition().getY(), 0);

        GL11.glColor4f(0f, 0f, 0f, 1);

        GL11.glLineWidth(4f);

        GL11.glBegin(GL_LINES);
        {
            GL11.glVertex2d(0, 0);
            GL11.glVertex2d(size, 0);

            GL11.glVertex2d(size, 0);
            GL11.glVertex2d(size, size);

            GL11.glVertex2d(0, size);
            GL11.glVertex2d(size, size);

            GL11.glVertex2d(0, 0);
            GL11.glVertex2d(0, size);
        }
        GL11.glEnd();

        GL11.glPopMatrix();

        GL11.glColor4f(1, 1, 1, 1f);
    }

    public int getSize() {
        return size;
    }

    public int getGridX() {
        return (int) Math.floor(getPosition().getX() / size);
    }

    public int getGridY() {
        return (int) Math.floor(getPosition().getY() / size);
    }

    @Override
    public void onUpdate() {}

    /**
     * Gets the coordinates on the grid where it may block the
     * player in its path. Used in the Pathfinder class.
     * @return
     */
    public Pair<Vec2D, Vec2D> getCoveredTiles() {
        AABB aabb = getAABB();

        int minGridX = (int) Math.floor(aabb.getMin().getX() / TileMap.TILE_SIZE);
        int minGridY = (int) Math.floor(aabb.getMin().getY() / TileMap.TILE_SIZE);
        int maxGridX = (int) Math.floor(aabb.getMax().getX() / TileMap.TILE_SIZE);
        int maxGridY = (int) Math.floor(aabb.getMax().getY() / TileMap.TILE_SIZE);

        System.out.println(this.getClass().getName() + ": " + minGridX + ", "  + minGridY + ", " + maxGridX + ", " + maxGridY);

        Vec2D min = new Vec2D(minGridX, minGridY);
        Vec2D max = new Vec2D(maxGridX, maxGridY);

        return new Pair<Vec2D, Vec2D>(min, max);
    }

    @Override
    public AABB getAABB() {
        return new AABB(new Vec2D(getPosition().getX(), getPosition().getY()),
                        new Vec2D(getPosition().getX() + size, getPosition().getY() + size));
    }

    @Override
    public void onCollision(IEntity other) {}

    @Override
    public boolean equals(Object o1) {
        if(!(o1 instanceof Tile)) {
            return false;
        }

        Tile other = (Tile) o1;

        return (other.getGridX() == this.getGridX() &&
                other.getGridY() == this.getGridY() &&
                other.getSize() == this.getSize());
    }

    @Override
    public String toString() {
        return getClass().getName() + ": [x: " + this.getGridX() + ", y: " + this.getGridY() + "]";
    }
}
