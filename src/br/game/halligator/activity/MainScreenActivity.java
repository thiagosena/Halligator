package br.game.halligator.activity;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import br.game.halligator.GameContext;
import br.game.halligator.R;
import br.game.halligator.services.client.model.Usuario;
import br.game.halligator.services.client.webservice.UsuarioREST;

public class MainScreenActivity extends RoboActivity {

	@InjectView(R.id.main_screen_button_run_game) 
	private ImageButton playButton;

	@InjectView(R.id.main_screen_top_players) 
	private ImageButton topPlayersButton;

	@InjectView(R.id.main_screen_option)
	private ImageButton optionsButton;

	@InjectView(R.id.main_screen_history)
	private ImageButton historyButton;

	@InjectView(R.id.main_screen_login)
	private ImageButton loginButton;

	@InjectView(R.id.tvUsuario)
	private TextView tvUsuario;

	private Usuario usu = new Usuario(); 

	//Contante responsável por chamar o método showDialog()
	private static final int SHOW_DIALOG = 0;

	private String login;
	private String senha;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);
		initializeGame(playButton, R.drawable.main_screen_button_play_on, HalligatorActivity.class);
		initializeButton(topPlayersButton, R.drawable.main_screen_button_top_players_on, TopRecordScreenActivity.class);
		initializeButton(optionsButton, R.drawable.main_screen_button_options_on, OptionsScreenActivity.class);
		initializeButton(historyButton, R.drawable.main_screen_button_history_on, HistoryScoreScreenActivity.class);
		initializeLogin(loginButton, R.drawable.login48, LoginActivity.class);

		GameContext.I.engine.setSharedPreferences(getSharedPreferences(GameContext.PREFS_NAME, Context.MODE_PRIVATE));

		setVolumeControlStream(AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

		GameContext.I.assets.music = new MediaPlayer[2];

		GameContext.I.assets.music[0] = MediaPlayer.create(this, R.raw.abertura);
		GameContext.I.assets.music[0].setLooping(true);

		GameContext.I.engine.playMusic(0);

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case SHOW_DIALOG:

			//Primeiro precisamos criar um inflater que adapte o conteudo do xml para o AlertDialog
			LayoutInflater factory = LayoutInflater.from(this);
			final View textEntryView = factory.inflate(R.layout.login, null); //passamos o XML criado
			return new AlertDialog.Builder(MainScreenActivity.this)
			.setTitle("Login").setView(textEntryView)
			.setPositiveButton("Entrar", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {

					//onClick acionado caso o usuario aperte o botao "Entrar"
					final EditText edit1 = (EditText) textEntryView.findViewById(R.id.username_edit);
					final EditText edit2 = (EditText) textEntryView.findViewById(R.id.password_edit);
					login = edit1.getText().toString();
					senha = edit2.getText().toString();

					UsuarioREST cliREST = new UsuarioREST();

					try {
						usu = cliREST.getUsuario(login, senha);

					} catch (Exception e) {
						e.printStackTrace();
						gerarToast(e.getMessage());
					}

					if(usu.getNome() != null){
						tvUsuario.setText(usu.getNome());

					} else {
						usu.setNome("Anonymous");
						usu.setPontuacao("0");
					}

				}

				private void gerarToast(CharSequence message) {
					int duration = Toast.LENGTH_LONG;
					Toast toast = Toast
							.makeText(getApplicationContext(), message, duration);
					toast.show();
				}
				
			}).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {

					//onClick acionado caso o usuario aperte o botão "Cancelar"
					dialog.cancel();
				}
			}).create(); //por fim criamos o AlertDialog depois de todo construído (título, layout, botões e ações)

		}

		//Se o valor passado pelo parâmetro não for o da constante SHOW_DIALOG retornamos null
		return null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		playButton.setBackgroundResource(R.drawable.main_screen_button_play_off);
		topPlayersButton.setBackgroundResource(R.drawable.main_screen_button_top_players_off);
		optionsButton.setBackgroundResource(R.drawable.main_screen_button_options_off);
		historyButton.setBackgroundResource(R.drawable.main_screen_button_history_off);
		loginButton.setBackgroundResource(R.drawable.login48);
		//GameContext.I.engine.playMusic(0);
	}
	
	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		if(pKeyCode == KeyEvent.KEYCODE_HOME && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			GameContext.I.engine.pauseMusic(0);
			return true;
		}
		return super.onKeyDown(pKeyCode, pEvent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		GameContext.I.engine.stopMusic(0);

	}

	private void initializeButton(final ImageButton button, final int buttonOnDrawableId, final Class<? extends Activity> targetActivity){
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				button.setBackgroundResource(buttonOnDrawableId);
				final Intent intent = new Intent(MainScreenActivity.this, targetActivity);
				startActivity(intent);
			}
		});
	}

	private void initializeLogin(final ImageButton button, final int buttonOnDrawableId, final Class<? extends Activity> targetActivity){
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				button.setBackgroundResource(buttonOnDrawableId);
				showDialog(SHOW_DIALOG);
			}
		});
	}

	private void initializeGame(final ImageButton button, final int buttonOnDrawableId, final Class<? extends Activity> targetActivity){
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				GameContext.I.engine.pauseMusic(0);

				button.setBackgroundResource(buttonOnDrawableId);
				final Intent intent = new Intent(MainScreenActivity.this, targetActivity); 

				Bundle params = new Bundle();

				if(usu.getPontuacao() == null && usu.getNome() == null){
					usu.setPontuacao("0");
					usu.setNome("Anonymous");
					usu.setSenha("0");
					usu.setEmail("anonymous@halligator.com");
				}

				params.putString("usuario", usu.getNome());
				params.putString("senha", usu.getSenha());
				params.putString("email", usu.getEmail());
				params.putString("pontuacao", usu.getPontuacao());

				Log.i("Pontuacao: ", usu.getPontuacao());
				intent.putExtras(params);
				startActivity(intent);
			}
		});
	}
}
