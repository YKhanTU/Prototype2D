package dgk.prototype.util;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public class Texture {

    private String name;
    private int id;

    private int width;
    private int height;

    public Texture(String imageName) {
        this.name = imageName;

        BufferedImage bufferedImage;

        this.id = glGenTextures();

        try {
            bufferedImage = ImageIO.read(new File("res/" + imageName + ".png"));

            int[] pixels = new int[bufferedImage.getWidth() * bufferedImage.getHeight()];
            bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), pixels, 0, bufferedImage.getWidth());

            ByteBuffer buffer = BufferUtils.createByteBuffer(bufferedImage.getWidth() * bufferedImage.getHeight() * 4);

            for(int y = 0; y < bufferedImage.getHeight(); y++) {
                for(int x = 0; x < bufferedImage.getWidth(); x++) {
                    int pixel = pixels[y * bufferedImage.getWidth() + x];
                    buffer.put((byte) ((pixel >> 16) & 0xFF));
                    buffer.put((byte) ((pixel >> 8) & 0xFF));
                    buffer.put((byte) (pixel & 0xFF));
                    buffer.put((byte) ((pixel >> 24) & 0xFF));
                }
            }

            this.width = bufferedImage.getWidth();
            this.height = bufferedImage.getHeight();

            buffer.flip();

            this.bind();

            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

            System.out.println(imageName + " has been loaded. (w: " + bufferedImage.getWidth() + ", y: " + bufferedImage.getHeight() + ")");

            glBindTexture(GL_TEXTURE_2D, 0);
        }catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }

    /**
     * Called each time before its use.
     */
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void destroy() {
        glDeleteTextures(id);
    }

}
