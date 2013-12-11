package br.game.halligator.activity;

import roboguice.inject.InjectView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import br.game.halligator.GameContext;
import br.game.halligator.R;

public class GameOverScreenActivity extends ChildScreenActivity {

	@InjectView(R.id.main_screen_facebook)
	private ImageButton facebookButton;
	
	@InjectView(R.id.pontuacao)
	private TextView pontuacao;
	
	@InjectView(R.id.recorde)
	private TextView record;

	private Long pontos = 0L;
	private String recorde;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		GameContext.I.engine.setSharedPreferences(getSharedPreferences(GameContext.PREFS_NAME, Context.MODE_PRIVATE));

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		GameContext.I.assets.music[1] = MediaPlayer.create(this, R.raw.gameover);
		GameContext.I.assets.music[1].setLooping(true);

		GameContext.I.engine.playMusic(1);
		
		//Enviar pontos para o facebook
		initializeButton(facebookButton, R.drawable.button_compartilhar_on, FacebookActivity.class);
		
		Intent intent = getIntent();
		Bundle params = intent.getExtras();

		this.pontos = params.getLong("pontos");
		this.recorde = params.getString("recorde");
		
		pontuacao.setText(pontos.toString());
		record.setText(recorde);
	}

	@Override
	protected int getContentViewId() {
		return R.layout.gameover_screen;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		GameContext.I.engine.stopMusic(1);
		GameContext.I.engine.playMusic(0);
	}

	private void initializeButton(final ImageButton button, final int buttonOnDrawableId, final Class<? extends Activity> targetActivity){
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				button.setBackgroundResource(buttonOnDrawableId);
				final Intent intent = new Intent(GameOverScreenActivity.this, targetActivity); 

				Bundle params = new Bundle();
				params.putLong("pontos", pontos);
				intent.putExtras(params);
				startActivity(intent);
				
				finish();
			}
		});
	}

}