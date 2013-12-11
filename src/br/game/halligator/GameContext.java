package br.game.halligator;


import org.andengine.audio.sound.Sound;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Vibrator;

public final class GameContext {
	
	public static final String PREFS_NAME = "hammering.options";  

	public static final String VIBRATIONS_OPTION = "vibrations";

	public static final String SOUND_OPTION = "sound";

	public static final boolean DEFAULT_OPTION = false;

	public static final float CAMERA_WIDTH = 480;
	
	public static final float CAMERA_HEIGHT = 320;

	public static final GameContext I = new GameContext();

	public final Assets assets = new Assets();

	public final GamePlay gamePlay = new GamePlay();

	public final Pools pools = new Pools();
	
	public final Engine engine = new Engine();
	

	public static class GamePlay {

	}

	private GameContext() {
	}

	public static class Pools {

	}

	public static class Assets {

		public Sound[] sounds;
		public MediaPlayer[] music;
		
	}


	public static class Engine {

		private static final int VIBRATION_LENGTH = 100;
		
		private Vibrator vibratorManeger;

		private SharedPreferences sharedPreferences;

		public void setSharedPreferences(SharedPreferences sharedPreferences) {
			this.sharedPreferences = sharedPreferences;
		}

		public void setVibratorManeger(final Vibrator vibratorManeger){
			this.vibratorManeger = vibratorManeger;
		}

		public void vibrate(){
			if (sharedPreferences.getBoolean(VIBRATIONS_OPTION, DEFAULT_OPTION)) {
				vibratorManeger.vibrate(VIBRATION_LENGTH);
			}
		}

		public void playSound(final int soundIndex){
			if (sharedPreferences.getBoolean(SOUND_OPTION, DEFAULT_OPTION)) {
				GameContext.I.assets.sounds[soundIndex].play();
			}
		}
		
		public void pauseSound(final int soundIndex){
			if (sharedPreferences.getBoolean(SOUND_OPTION, DEFAULT_OPTION)) {
				GameContext.I.assets.sounds[soundIndex].pause();
			}
		}
		
		public void stopSound(final int soundIndex){
			if (sharedPreferences.getBoolean(SOUND_OPTION, DEFAULT_OPTION)) {
				GameContext.I.assets.sounds[soundIndex].stop();
			}
		}
		
		public void playMusic(final int soundIndex){
			if (sharedPreferences.getBoolean(SOUND_OPTION, DEFAULT_OPTION)) {
				GameContext.I.assets.music[soundIndex].start();
			}
		}
		
		public void pauseMusic(final int soundIndex){
			if (sharedPreferences.getBoolean(SOUND_OPTION, DEFAULT_OPTION)) {
				GameContext.I.assets.music[soundIndex].pause();
			}
		}
		
		public void stopMusic(final int soundIndex){
			if (sharedPreferences.getBoolean(SOUND_OPTION, DEFAULT_OPTION)) {
				GameContext.I.assets.music[soundIndex].stop();
			}
		}
		
		public boolean isPlayMusic(){
			if (sharedPreferences.getBoolean(SOUND_OPTION, DEFAULT_OPTION)) {
				return true;
			} else {
				return false;
			}
		}

	}

}
