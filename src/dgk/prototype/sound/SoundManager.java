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

    private float currentVolume;

    public SoundManager() {
        this.soundMap = new HashMap<String, Sound>();
        this.currentVolume = .1f;
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

        Sound uiButtonClick = new Sound("res/sounds/buttonClick.ogg", .5f, 1f, 1f, false);

        Sound walkingSound = new Sound ("res/sounds/walkingSound.ogg", .5f, .8f, 1f, false);

        Sound rainLoop = new Sound("res/sounds/rainLoop.ogg", .05f, .5f, 1f, true);

        soundMap.put("buttonClick", uiButtonClick);
        soundMap.put("walkingSound", walkingSound);
        soundMap.put("rainLoop", rainLoop);
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

    public float getCurrentVolume() {
        return currentVolume;
    }

    public void setCurrentVolume(float volume) {
        if(volume < 0.0f) {
            this.currentVolume = 0.0f;
            return;
        }else if(volume > 1.5f) {
            this.currentVolume = 1.5f;
            return;
        }

        for(Sound sound: soundMap.values()) {
            if(sound.isLooping()) {
                sound.onVolumeChange();
            }
        }

        this.currentVolume = volume;
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
