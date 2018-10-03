package org.example.pacman.Managers;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import org.example.pacman.R;
import java.io.IOException;

public class SoundManager {

    private final Context cont;

    //Mediaplayer variables to control music
    private final MediaPlayer ingameMediaP;

    //Booleans to control if music and sfx are enabled or disabled
    private boolean musicIsEnabled = true;
    private final boolean sfxEnabled = true;

    //required to store SFX
    private SoundPool pool;

    private int coinPickup = -1;

    public SoundManager(Context context){
        this.cont = context;
       ingameMediaP = MediaPlayer.create(this.cont, R.raw.gamemusic);
        ingameMediaP.setVolume(0.5f, 0.5f);
        ingameMediaP.setLooping(true);
        soundEffectContainer();
    }

    private void soundEffectContainer()  {
        pool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);

        try {
            AssetManager assetManager = cont.getAssets();
            AssetFileDescriptor descriptor;

            //priority 1, the pool can maximum store 10 streams currently
            descriptor = assetManager.openFd("starPickUp.ogg");
            coinPickup = pool.load(descriptor, 0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays soundeffects based on the sonund identifier
     * @param soundIdentifier
     */
    public void playSound(int soundIdentifier) {
        if (sfxIsEnabled())
            pool.play(soundIdentifier, 1, 1, 0, 0, 1);
    }

    /**
     * Method returning a boolean, controls whether music is enabled
     * @return
     */
    private boolean isMusicEnabled() {
        return musicIsEnabled;
    }


    /**
     * Plays a music track if it's not null and music is enabled and if it's not already playing
     */
    public void playMusic() {
        if (ingameMediaP != null && isMusicEnabled() && !ingameMediaP.isPlaying())
            ingameMediaP.start();
    }

    /**
     * Pauses the music if it's playing
     */
    public void stopMusic() {
        if (ingameMediaP != null && ingameMediaP.isPlaying())
            ingameMediaP.pause();
    }

    private boolean sfxIsEnabled(){return sfxEnabled;}

    public void setMusicEnabled(boolean enabled) {
        this.musicIsEnabled = enabled;
        if (enabled)
            playMusic();
        else
            stopMusic();
    }

}
