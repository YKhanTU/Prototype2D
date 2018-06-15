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

    private float pitch = 1.0f;

    private int bufferPointer;
    private int sourcePointer;

    public Sound(String fileName) {
        this.soundManager = GameWindow.getInstance().soundManager;
        //File file = new File("res/sounds/" + fileName + ".wav");

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
        // GAIN (volume)
        alSourcef(sourcePointer, AL_GAIN, soundManager.getCurrentVolume());
        // PITCH
        alSourcef(sourcePointer, AL_PITCH, pitch);

        alSourcePlay(sourcePointer);
    }

    public void destroy() {
        alDeleteBuffers(bufferPointer);
        alDeleteSources(sourcePointer);
    }
}
