package dgk.prototype.font;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.*;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.opengl.GL11.*;

/**
 * TrueTypeFont impementation with STB bindings.
 *
 * Full credit to: http://forum.lwjgl.org/index.php?action=profile;u=35897
 *
 * *This is none of my code.
 */
public class TrueTypeFont {

    public static final int CHAR_DATA_MALLOC_SIZE = 96;
    public static final int FONT_TEX_W = 512;
    public static final int FONT_TEX_H = FONT_TEX_W;
    public static final int BAKE_FONT_FIRST_CHAR = 32;
    public static final int GLYPH_COUNT = CHAR_DATA_MALLOC_SIZE;

    protected final STBTTBakedChar.Buffer charData;
    protected final STBTTFontinfo fontInfo;
    protected final int fontHeight;
    protected final int texGlName;
    protected final float ascent;
    protected final float descent;
    protected final float lineGap;
    protected final String fontName;
    protected boolean disposed;

    public TrueTypeFont(File ttfFile, int fontHeight) {
        fontName = ttfFile.getName();
        this.fontHeight = fontHeight;
        charData = STBTTBakedChar.malloc(CHAR_DATA_MALLOC_SIZE);
        fontInfo = STBTTFontinfo.create();
        int texGlName = 0;
        float ascent = 0, descent = 0, lineGap = 0;

        try {
            ByteBuffer ttfFileData = loadFileToByteBuffer(ttfFile);
            ByteBuffer texData = BufferUtils.createByteBuffer(FONT_TEX_W * FONT_TEX_H);
            int result = STBTruetype.stbtt_BakeFontBitmap(ttfFileData, fontHeight, texData, FONT_TEX_W, FONT_TEX_H, BAKE_FONT_FIRST_CHAR, charData);
            if (result < 1) {
                System.err.println("stbtt_BakeFontBitmap failed with return value: "+result);
            }

            try (MemoryStack stack = MemoryStack.stackPush()) {
                STBTruetype.stbtt_InitFont(fontInfo, ttfFileData);
                float pixelScale = STBTruetype.stbtt_ScaleForPixelHeight(fontInfo, fontHeight);

                IntBuffer bufAscent = stack.ints(0);
                IntBuffer bufDescent = stack.ints(0);
                IntBuffer bufLineGap = stack.ints(0);
                STBTruetype.stbtt_GetFontVMetrics(fontInfo, bufAscent, bufDescent, bufLineGap);
                ascent = bufAscent.get(0) * pixelScale;
                descent = bufDescent.get(0) * pixelScale;
                lineGap = bufLineGap.get(0) * pixelScale;
            }

            texGlName = GL11.glGenTextures();
            GL11.glBindTexture(GL_TEXTURE_2D, texGlName);
            GL11.glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, FONT_TEX_W, FONT_TEX_H, 0, GL_ALPHA, GL_UNSIGNED_BYTE, texData);
            GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            GL11.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.texGlName = texGlName;
        this.ascent = ascent;
        this.descent = descent;
        this.lineGap = lineGap;
    }

    public int getFontTextLength(String text) {
        int len = text.length();

        int totalWidth = 0;

        for(int i = 0; i < len; i++) {
            char c = text.charAt(i);

            int w = charData.get(c - TrueTypeFont.BAKE_FONT_FIRST_CHAR).x1()
                    - charData.get(c - TrueTypeFont.BAKE_FONT_FIRST_CHAR).x0();

            totalWidth += w;
        }

        return totalWidth;
    }

    public final void drawString(TrueTypeFont fontRes, String text, float x, float y) {
        int fontSize = fontRes.getPixelSize();
        y += fontRes.getAscent();
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer bufX = stack.floats(x);
            FloatBuffer bufY = stack.floats(y);

            STBTTAlignedQuad q = STBTTAlignedQuad.mallocStack(stack);
            STBTTBakedChar.Buffer charData = fontRes.getBakedCharData();

            GL11.glEnable(GL_TEXTURE_2D);
            GL11.glBindTexture(GL_TEXTURE_2D, fontRes.getGlName());

            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glColor4f(1f, 1f, 1f, 1f);

            int firstCP = TrueTypeFont.BAKE_FONT_FIRST_CHAR;
            int lastCP = TrueTypeFont.BAKE_FONT_FIRST_CHAR + TrueTypeFont.GLYPH_COUNT - 1;
            for (int i = 0; i < text.length(); i++) {
                int codePoint = text.codePointAt(i);
                if (codePoint == '\n') {
                    bufX.put(0, x);
                    bufY.put(0, y + bufY.get(0) + fontSize);
                    continue;
                } else if (codePoint < firstCP || codePoint > lastCP) {
                    continue;
                }
                STBTruetype.stbtt_GetBakedQuad(charData,
                        TrueTypeFont.FONT_TEX_W, TrueTypeFont.FONT_TEX_H,
                        codePoint - firstCP,
                        bufX, bufY, q, true);

                GL11.glTexCoord2f(q.s0(), q.t0());
                GL11.glVertex2f(q.x0(), q.y0());

                GL11.glTexCoord2f(q.s0(), q.t1());
                GL11.glVertex2f(q.x0(), q.y1());

                GL11.glTexCoord2f(q.s1(), q.t1());
                GL11.glVertex2f(q.x1(), q.y1());

                GL11.glTexCoord2f(q.s1(), q.t1());
                GL11.glVertex2f(q.x1(), q.y1());

                GL11.glTexCoord2f(q.s1(), q.t0());
                GL11.glVertex2f(q.x1(), q.y0());

                GL11.glTexCoord2f(q.s0(), q.t0());
                GL11.glVertex2f(q.x0(), q.y0());
            }
            GL11.glEnd();

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
        }
    }

    public int getGlName() {
        return texGlName;
    }

    public STBTTBakedChar.Buffer getBakedCharData() {
        return charData;
    }

    public float getAscent() {
        return ascent;
    }

    public float getDescent() {
        return descent;
    }

    public float getLineGap() {
        return lineGap;
    }

    public void dispose() {
        if (disposed) {
            return;
        }
        disposed = true;
        charData.free();
        fontInfo.free();
        if (texGlName != 0) {
            GL11.glDeleteTextures(texGlName);
        }
    }

    public boolean isDisposed() {
        return disposed;
    }

    @Override
    protected void finalize() {
        dispose();
    }

    public String getName() {
        return fontName;
    }

    public int getPixelSize() {
        return fontHeight;
    }

    public static int getCodePoint(String text, int length, int index, IntBuffer out) {
        char c1 = text.charAt(index);
        if (Character.isHighSurrogate(c1) && index + 1 < length) {
            char c2 = text.charAt(index + 1);
            if (Character.isLowSurrogate(c2)) {
                out.put(0, Character.toCodePoint(c1, c2));
                return 2;
            }
        }
        out.put(0, c1);
        return 1;
    }
    public static class GlyphDim {
        public final float width, height;
        public GlyphDim(float x0, float y0, float x1, float y1) {
            width = Math.abs(x0 - x1);
            height = Math.abs(y0 - y1);
        }
        public GlyphDim(float w, float h) {
            width = w;
            height = h;
        }
    }

    public static ByteBuffer loadFileToByteBuffer(File file) throws IOException {
        ByteBuffer buffer;
        try (FileInputStream fis = new FileInputStream(file);
             FileChannel fc = fis.getChannel();)
        {
            buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
        }
        return buffer;
    }
}
