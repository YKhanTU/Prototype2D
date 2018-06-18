package dgk.prototype.util;

import dgk.prototype.game.ResourceManager;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public class SpriteSheet {

    public static final int PUDDLE = 1;
    public static final int GRASS_2 = 2;
    public static final int STONE = 3;
    public static final int BRICK = 4;
    public static final int DIRT = 5;
    public static final int WATER = 6;
    public static final int DIRT_PATH_HORIZONTAL = 7;
    public static final int DIRT_PATH_VERTICAL = 8;

    public static final int RULER = 21;
    public static final int PEASANT = 5;
    public static final int WOOD_GATE = 95;

    public static final int TREE = 19;
    public static final int SHRUB = 20;

    public static final int WOOD_WALL_NORTH_1A = 22;
    public static final int WOOD_WALL_NORTH_1B = 23;
    public static final int WOOD_WALL_NORTH_1C = 24;

    public static final int WOOD_WALL_SOUTH_1A = 96;
    public static final int WOOD_WALL_SOUTH_1B = 97;

    public static final int WOOD_WALL_WEST_1A = 147;
    public static final int WOOD_WALL_WEST_1B = 148;
    public static final int WOOD_WALL_WEST_1C = 149;
    public static final int WOOD_WALL_WEST_STAIRS = 150;

    public static final int WOOD_WALL_EAST_1A = 152;
    public static final int WOOD_WALL_EAST_1B = 153;
    public static final int WOOD_WALL_EAST_1C = 154;
    // public static final int WOOD_WALL_EAST_STAIRS = 150;



    public static final int CONVERSATION_ICON = 115;
    public static final int DISEASE_ICON = 116;

    public static final int CHARACTER_ZOMBIE_TEST = 145;

    private ResourceManager resourceManager;

    private String sheetName;

    private BufferedImage spriteSheet;

    private ArrayList<Integer> textureMap;

    public SpriteSheet(ResourceManager resourceManager, String sheetName) {
        this.resourceManager = resourceManager;
        this.sheetName = sheetName;

        try {
            this.spriteSheet = ImageIO.read(new File("res/" + sheetName + ".png"));
            System.out.println("Sprite Sheet .PNG Height and Width: " + spriteSheet.getWidth() + ", " + spriteSheet.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.textureMap = new ArrayList<Integer>();
    }

    public void bindTexture(int id) {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void loadTexture(int x, int y, int size) {
        int id = glGenTextures(); // 1 ---> MAX INTEGER SIZE

        int[] pixels = new int[size * size];
        spriteSheet.getRGB(x, y, size, size, pixels, 0, size);

        ByteBuffer buffer = BufferUtils.createByteBuffer(size * size * 4);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int pixel = pixels[i * size + j];
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        int width = size;
        int height = size;

        buffer.flip();

        glBindTexture(GL_TEXTURE_2D, id);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        System.out.println(sheetName + " ---> [ " + x + ", " + y + "] has been loaded. (w: " + size + ", y: " + size + ") = " + id);

        glBindTexture(GL_TEXTURE_2D, 0);

        textureMap.add(id);

        resourceManager.onTextureLoad();
    }

    public void loadTexture(int column, int row, int w, int h) {
        int id = glGenTextures();

        int[] pixels = new int[w * h];
        spriteSheet.getRGB(column * w, row * h, w, h, pixels, 0, w);

        ByteBuffer buffer = BufferUtils.createByteBuffer(w * h * 4);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int pixel = pixels[y * w + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        int width = w;
        int height = h;

        buffer.flip();

        glBindTexture(GL_TEXTURE_2D, id);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        System.out.println(sheetName + " ---> [ " + column + ", " + row + "] has been loaded. (w: " + w + ", y: " + h + ") = " + id);

        glBindTexture(GL_TEXTURE_2D, 0);

        textureMap.add(id);

        resourceManager.onTextureLoad();
    }

    public void loadTextureManually(int x, int y, int w, int h) {
        int id = glGenTextures(); // 1 ---> MAX INTEGER SIZE

        int[] pixels = new int[w * h];
        spriteSheet.getRGB(x, y, w, h, pixels, 0, w);

        ByteBuffer buffer = BufferUtils.createByteBuffer(w * h * 4);

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                int pixel = pixels[i * w + j];
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        int width = w;
        int height = h;

        buffer.flip();

        glBindTexture(GL_TEXTURE_2D, id);

        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        System.out.println(sheetName + " ---> [ " + x + ", " + y + "] has been loaded. (w: " + w + ", y: " + h + ") = " + id);

        glBindTexture(GL_TEXTURE_2D, 0);

        textureMap.add(id);

        resourceManager.onTextureLoad();
    }

    public void destroyTextures() {
        for(int i : textureMap) {
            glDeleteTextures(i);
        }
    }
}
