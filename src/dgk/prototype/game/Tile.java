package dgk.prototype.game;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class Tile extends BuildingComponent {

    private transient Camera worldCamera;

    private int size;

    private byte renderLayer;

    private boolean isPassable;

    public Tile(int textureId, byte renderLayer, int x, int y, int size) {
        super(textureId, x, y, size, size);

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
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2f(0, 0);

            GL11.glTexCoord2f(1, 0);
            GL11.glVertex2f(size, 0);

            GL11.glTexCoord2f(1, 1);
            GL11.glVertex2f(size, size);

            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2f(0, size);
        GL11.glEnd();

        GL11.glPopMatrix();

        glDisable(GL_TEXTURE_2D);
    }

    @Override
    public void onUpdate() {}
}
