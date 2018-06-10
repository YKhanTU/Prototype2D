package dgk.prototype.game;

import dgk.prototype.util.AABB;
import dgk.prototype.util.Vec2D;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class Tile extends GameObject {

    //
    // TODO: If a Tile has an AABB, we are going to make the AABB split into 'half'
    // TODO: in order to determine z ordering for rendering.
    //
    //

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

    public Tile(int textureId, byte renderLayer, int x, int y, int size) {
        super(textureId, new Vec2D(x, y));

        this.size = size;

        this.renderLayer = renderLayer;

        this.isPassable = true;

        this.isStatic = true;

        worldCamera = GameWindow.getInstance().getWorldCamera();
    }

    public byte getRenderLayer() {
        return renderLayer;
    }

    public boolean isPassable() {
        return isPassable;
    }

    @Override
    public void render() {
        glEnable(GL_TEXTURE_2D);

        GL11.glMatrixMode(GL_MODELVIEW_MATRIX);
        GL11.glPushMatrix();

        GL11.glScalef(worldCamera.getZoom(), worldCamera.getZoom(), 0);
        GL11.glTranslated(getPosition().x - worldCamera.getPosition().getX(), getPosition().y - worldCamera.getPosition().getY(), 0);

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

        if(TileMap.DEBUG_MODE) {
            drawOutline();
        }

        GL11.glPopMatrix();

        glDisable(GL_TEXTURE_2D);
    }

    /**
     * A debug outline drawn over the Tile.
     */
    private void drawOutline() {
        GL11.glColor4f(0, 1f, 0, 1);

        GL11.glLineWidth(2f);

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

        GL11.glColor4f(1, 1, 1, 1f);
    }

    @Override
    public void onUpdate() {}

    @Override
    public AABB getAABB() {
        return new AABB(getPosition(), new Vec2D(getPosition().getX() + size, getPosition().getY() + size));
    }

    @Override
    public void onCollision(IEntity other) {

    }
}
