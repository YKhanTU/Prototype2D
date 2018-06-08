package dgk.prototype.game;

import dgk.prototype.util.AABB;
import dgk.prototype.util.Vec2D;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class Tile extends GameObject {

    private transient Camera worldCamera;

    private int size;

    private byte renderLayer;

    private boolean isPassable;

    public Tile(int textureId, byte renderLayer, int x, int y, int size) {
        super(textureId, new Vec2D(x, y));

        this.size = size;

        this.isPassable = true;

        this.renderLayer = renderLayer;

        worldCamera = GameWindow.getInstance().getWorldCamera();
    }

    public byte getRenderLayer() {
        return renderLayer;
    }

    @Override
    public void render() {
        glEnable(GL_TEXTURE_2D);

        GL11.glMatrixMode(GL_MODELVIEW_MATRIX);
        GL11.glPushMatrix();

        GL11.glScalef(worldCamera.getZoom(), worldCamera.getZoom(), 0);
        GL11.glTranslated(getPosition().x - worldCamera.getPosition().getX(), getPosition().y - worldCamera.getPosition().getY(), 0);

        GameWindow.getInstance().spriteSheet.bindTexture(getTextureId());

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

        drawOutline();

        GL11.glPopMatrix();

        glDisable(GL_TEXTURE_2D);
    }

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
