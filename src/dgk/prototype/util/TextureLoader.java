package dgk.prototype.util;

import java.util.ArrayList;

public class TextureLoader {

    public static ArrayList<Texture> TEXTURES = new ArrayList<Texture>();

    public static void loadTextures() {
        TEXTURES.add(new Texture("32xDevBlock"));
        TEXTURES.add(new Texture("64xDevBlock"));
        TEXTURES.add(new Texture("32xRuler"));
        TEXTURES.add(new Texture("32xPeasant"));
    }

    public static void destroyTextures() {
        for(Texture texture : TEXTURES) {
            texture.destroy();
        }
    }

    public static Texture getTexture(String name) {
        for(Texture texture : TEXTURES) {
            if(texture.getName().equals(name)) {
                return texture;
            }
        }

        return null;
    }

    public static Texture getTexture(int textureId) {
        for(Texture texture : TEXTURES) {
            if(texture.getId() == textureId) {
                return texture;
            }
        }

        return null;
    }
}
