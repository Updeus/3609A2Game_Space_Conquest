import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

//Jarod Esareesingh 
//816026811
public class SoundManger {

    private final URL shoot;
    private final URL hit;
    private final URL destroy;
    private final URL playerDeath;
    private final URL background;

    public SoundManger() {
        this.shoot = this.getClass().getClassLoader().getResource("sounds/shoot.wav");
        this.hit = this.getClass().getClassLoader().getResource("sounds/hit.wav");
        this.destroy = this.getClass().getClassLoader().getResource("sounds/destroy.wav");
        this.playerDeath = this.getClass().getClassLoader().getResource("sounds/playerDestroyed.wav");
        this.background = this.getClass().getClassLoader().getResource("sounds/background.wav");
    }

    public void soundShoot() {
        play(shoot);
    }

    public void soundHit() {
        play(hit);
    }

    public void soundDestroy() {
        play(destroy);
    }

    public void soundPlayerDeath() {
        play(playerDeath);
    }

    public void playBackground() {
        playClip(background, true);
    }

    public void stopBackground() {
        stopClip(background);
    }

    private void play(URL url) {
        try {
            // Get audio input stream from the URL
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            // Get a new clip and open it with the audio input stream
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            // Add a line listener to the clip to stop the clip when it finishes playing
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                }
            });
            // Close the audio input stream
            audioIn.close();
            // Start playing the clip
            clip.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            // Print the error message if an exception occurs
            System.err.println(e);
        }
    }

    public void stopClip(URL url) {
        try {
            // Get audio input stream from the URL
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            // Get a new clip and open it with the audio input stream
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            // Stop the clip if it is playing
            if (clip != null && clip.isRunning()) {
                clip.stop();
            }
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            // Print the error message if an exception occurs
            System.err.println(e);
        }
    }

    private void playClip(URL url, boolean looping) {
        try {
            // Get audio input stream from the URL
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            // Get a new clip and open it with the audio input stream
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            // Set the frame position of the clip to 0
            clip.setFramePosition(0);
            // Start playing the clip either continuously or once
            if (looping) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.start();
            }
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            // Print the error message if an exception occurs
            System.err.println(e);
        }
    }
}