package dgk.prototype.sound;

import dgk.prototype.game.GameWindow;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.stackMallocInt;
import static org.lwjgl.system.MemoryStack.stackPop;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.libc.LibCStdlib.free;

public class Sound {

    private SoundManager soundManager;

    private float gain;
    private float pitch;
    private float frequency;

    private boolean isLooping;

    private int bufferPointer;
    private int sourcePointer;

    public Sound(String fileName, float gain, float pitch, float frequency, boolean isLooping) {
        this.soundManager = GameWindow.getInstance().soundManager;
        //File file = new File("res/sounds/" + fileName + ".wav");

        this.isLooping = isLooping;
        this.gain = gain;
        this.pitch = pitch;
        this.frequency = frequency;

        stackPush();

        IntBuffer channelBuffer = stackMallocInt(1);
        stackPush();
        IntBuffer sampleRateBuffer = stackMallocInt(1);

        ShortBuffer audioBuffer = stb_vorbis_decode_filename(fileName, channelBuffer, sampleRateBuffer);

        int channels = channelBuffer.get();
        int sampleRate = sampleRateBuffer.get();

        stackPop();
        stackPop();

        int format = -1;

        if(channels == 1) {
            format = AL_FORMAT_MONO16;
        }else if(channels == 2) {
            format = AL_FORMAT_STEREO16;
        }

        bufferPointer = alGenBuffers();

        alBufferData(bufferPointer, format, audioBuffer, sampleRate);

        free(audioBuffer);

        sourcePointer = alGenSources();
    }

    public int getSourcePointer() {
        return sourcePointer;
    }

    public void playSound() {
        alSourcei(sourcePointer, AL_BUFFER, bufferPointer);

        if(isLooping) {
            alSourcef(sourcePointer, AL_LOOPING, AL_TRUE);
        }else{
            alSourcef(sourcePointer, AL_LOOPING, AL_FALSE);
        }

        // GAIN (volume)
        float realGain = soundManager.getCurrentVolume() * this.gain;
        alSourcef(sourcePointer, AL_GAIN, realGain);

        // FREQUENCY
        alSourcef(sourcePointer, AL_FREQUENCY, frequency);

        // PITCH
        alSourcef(sourcePointer, AL_PITCH, pitch);

        alSourcePlay(sourcePointer);
    }

    public void stopSound() {
        alSourceStop(sourcePointer);
    }

    public void destroy() {
        alDeleteBuffers(bufferPointer);
        alDeleteSources(sourcePointer);
    }

    public boolean isLooping() {
        return this.isLooping;
    }

    public float getGain(float gain) {
        return this.gain;
    }

    public void setGain(float gain) {
        this.gain = gain;
    }

    public void onVolumeChange() {
        alSourcef(sourcePointer, AL_GAIN, soundManager.getCurrentVolume() * this.gain);
    }
}
