package tech.drainwalk.services.players;

import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;
import tech.drainwalk.api.impl.interfaces.IInstanceAccess;
import tech.drainwalk.api.impl.models.DrainwalkResource;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MusicPlayer implements IInstanceAccess {

    private Clip clip;
    private FloatControl volumeControl;

    public void playWAVFromResource(String resourcePath) {
        playWAVFromResource(resourcePath, 1);
    }

    public void playWAVFromResource(String resourcePath, float volume) {
        new Thread(() -> {
            ResourceLocation resourceLocation = new DrainwalkResource(resourcePath);

            try {
                System.out.println("Attempting to load resource: " + resourceLocation);
                IResource resource = mc.getResourceManager().getResource(resourceLocation);
                try (InputStream inputStream = resource.getInputStream();
                     BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
                    System.out.println("Resource loaded successfully, starting playback.");
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedInputStream);
                    clip = AudioSystem.getClip();
                    clip.open(audioStream);

                    // Получение контроля громкости
                    if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                        volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    }
                    setVolume(volume);

                    clip.start();
                    clip.addLineListener(event -> {
                        if (event.getType() == LineEvent.Type.STOP) {
                            clip.close();
                        }
                    });
                    System.out.println("Playback started.");
                } catch (Exception e) {
                    e.fillInStackTrace();
                    System.err.println("Error during WAV playback: " + e.getMessage());
                }
            } catch (IOException e) {
                e.fillInStackTrace();
                System.err.println("Resource not found: " + e.getMessage());
            }
        }).start();
    }

    public void setVolume(float volume) {
        if (volumeControl != null) {
            // volume в диапазоне от 0.0f до 1.0f
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            float range = max - min;
            float gain = (range * volume) + min;
            volumeControl.setValue(gain);
            System.out.println("Volume set to: " + volume);
        }
    }

}
