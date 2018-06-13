package dgk.prototype.sound;

import dgk.prototype.util.IManager;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import java.util.HashMap;

import static org.lwjgl.openal.ALC10.*;

public class SoundManager implements IManager {

    private HashMap<String, Sound> soundMap;

    private long device;
    private long context;

    public SoundManager() {
        this.soundMap = new HashMap<String, Sound>();
    }

    @Override
    public void start() {
        String defaultDevice = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        device = alcOpenDevice(defaultDevice);

        int[] attr = {0};

        context = alcCreateContext(device, attr);
        alcMakeContextCurrent(context);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        if(alCapabilities.OpenAL10) {
            System.out.println("OpenAL 1.0 is supported.");
        }

        //Sound sound = new Sound("buttonClick.wav");

        //soundMap.put("buttonClick", sound);
    }

    public Sound getSound(String name) {
        if(soundMap.containsKey(name)) {
            return soundMap.get(name);
        }

        return null;
    }

    public boolean hasSound(String name) {
        return (soundMap.containsKey(name));
    }

    @Override
    public void stop() {
        alcDestroyContext(context);
        alcCloseDevice(device);
    }

    @Override
    public void onUpdate() {

    }
}
