package br.game.halligator.activity;

import roboguice.inject.InjectView;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import br.game.halligator.GameContext;
import br.game.halligator.R;

import com.google.inject.Inject;


public class OptionsScreenActivity extends ChildScreenActivity {
	
	@InjectView(R.id.sound_select_image) 
	private ImageButton soundSelectImage;
	
	@InjectView(R.id.sound_select_checkbox) 
	private ImageButton soundSelectCheckbox;
	
	@InjectView(R.id.vibrations_select_image) 
	private ImageButton vibrationSelectImage;
	
	@InjectView(R.id.vibrations_select_checkbox) 
	private ImageButton vibrationSelectCheckbox;
	
	@Inject
	private SharedPreferences settings; 
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// initialize buttons workers
		(new SoundStateChangeWorker()).initialize();
		(new VibrationStateChangeWorker()).initialize();
	}
	
	@Override
	protected int getContentViewId() {
		return R.layout.options_screen;
	}
	
	private abstract class StateChangeWorker implements OnClickListener {
		
		void initialize(){
			changeState(settings.getBoolean(getKey(), false));
			bindListeners();
			
		}
		
		public void onClick(View v) {
			final String key = getKey();
			final boolean option = !settings.getBoolean(key, false);
			changeState(option);
			final SharedPreferences.Editor editor = settings.edit();
		    editor.putBoolean(key, option);
		    editor.commit();
		}
		
		void changeState(final boolean option){
			if (option){ 
				onSelect();
			} else {
				onDeselect();
			}
		}
		
		abstract void bindListeners();
		
		abstract void onSelect();
		
		abstract void onDeselect();
		
		abstract String getKey();
	}
	
	private class SoundStateChangeWorker extends StateChangeWorker {

		@Override
		String getKey() {
			Log.i("TESTE Musica", "Pegou a chave");
			return GameContext.SOUND_OPTION;
			
		}

		@Override
		void bindListeners() {
			soundSelectCheckbox.setOnClickListener(this);
			soundSelectImage.setOnClickListener(this);
			Log.i("TESTE Musica", "Musica bindListeners");
		}

		@Override
		void onSelect() {
			soundSelectCheckbox.setBackgroundResource(R.drawable.options_checkbox_on);
			soundSelectImage.setBackgroundResource(R.drawable.options_sound_on);
			//changeState(settings.getBoolean(getKey(), false));
			GameContext.I.assets.music[0].start();
			Log.i("TESTE Musica", "Musica Inicializada");
		}

		@Override
		void onDeselect() {
			soundSelectCheckbox.setBackgroundResource(R.drawable.opitons_checkbox_off);
			soundSelectImage.setBackgroundResource(R.drawable.options_sound_off);
			GameContext.I.engine.pauseMusic(0);
			Log.i("TESTE Musica", "Musica Pausada");
		}
	}
	
	private class VibrationStateChangeWorker extends StateChangeWorker {

		@Override
		String getKey() {
			return GameContext.VIBRATIONS_OPTION;
		}

		@Override
		void bindListeners() {
			vibrationSelectCheckbox.setOnClickListener(this);
			vibrationSelectImage.setOnClickListener(this);
		}

		@Override
		void onSelect() {
			vibrationSelectCheckbox.setBackgroundResource(R.drawable.options_checkbox_on);
			vibrationSelectImage.setBackgroundResource(R.drawable.options_vibrations_on);
		}

		@Override
		void onDeselect() {
			vibrationSelectCheckbox.setBackgroundResource(R.drawable.opitons_checkbox_off);
			vibrationSelectImage.setBackgroundResource(R.drawable.options_vibrations_off);
		}
	}
}
