package br.game.halligator.activity;

import java.io.IOException;
import java.util.Random;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;

import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;

import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.app.AlertDialog;
import android.content.DialogInterface;

import br.game.halligator.GameContext;
import br.game.halligator.db.RecordeSQLiteOpenHelper;
import br.game.halligator.db.RecordeService;
import br.game.halligator.services.client.model.Usuario;
import br.game.halligator.services.client.webservice.UsuarioREST;

public class HalligatorActivity extends SimpleBaseGameActivity implements IOnMenuItemClickListener {

	/* CONSTANTS */
	private static final int CAMERA_WIDTH = 320;
	private static final int CAMERA_HEIGHT = 480;
	private static final int FONT_SIZE = 16;

	/* PATHS */
	public static final String GRAPHIC_ASSETS_PATH = "gfx/";
	public static final String AUDIO_ASSETS_PATH = "audio/";
	public static final String FONTS_ASSETS_PATH = "font/";

	/* ITENS DO MENU PAUSE */
	private static final int MENU_RESTART = 0;
	private static final int MENU_QUIT = 1;
	private static final int MENU_RESUME = 2;

	/* TEXTURA DOS PERSONAGENS */
	private TiledTextureRegion mBowserTextureRegion;
	//private TiledTextureRegion mTrollTextureRegion;

	/* TEXTURA DOS BITMAP */
	private BitmapTextureAtlas mAutoParallaxBackgroundTexture;
	private BitmapTextureAtlas mBitmapTextureAtlasBalloon;
	private BitmapTextureAtlas mBitmapTextureAtlasPauseBotao;
	private BitmapTextureAtlas mBitmapTextureAtlas;

	/* TEXTURA DOS OBJETOS */
	private ITextureRegion mBackgroundBack;
	private ITextureRegion mBackgroundSky;
	private TiledTextureRegion mBalloonTextureRegion;
	private ITextureRegion mPauseTextureRegion;

	/* TEXTURA DOS MENUS */	
	private final ITextureRegion[] menuTextureRegions = new ITextureRegion[3];

	/* ANIMACAO DOS PERSONAGENS */
	private AnimatedSprite bowser;
	//protected ArrayList<Alligator> b;

	/* CENARIO */
	private Camera mCamera;
	private Scene mMainScene;
	private HUD hud_bowser;

	/* TEMPO */
	public boolean stoptime = false;
	public String time = "Time: ";
	String tempoatual = "20";
	private Text textotime;

	//public CountDownTimer countDownTime;
	CountDown count;

	private final long startTime = 30000;// Tempo de inicio do jogo, usado no score
	private final long interval = 1000;
	long temporestante;

	/* PONTUACAO */
	private Long pontos = 0L;
	private Long recorde;
	private Font pontuacaoFonte;
	private Text textoPontuacao;

	//BANCO
	private RecordeService recordService;

	//USUARIO
	private Usuario usu;

	//MENU
	private MenuScene mainMenu;

	//MUSIC
	private Music myMusic;

	public void sendRecordREST(String record) {
		int pontuacaoAtual = Integer.parseInt(usu.getPontuacao());
		int novaPontuacao = Integer.parseInt(record);

		if(pontuacaoAtual < novaPontuacao){
			usu.setPontuacao(record);
			UsuarioREST usuREST = new UsuarioREST();
			try {
				Log.i("Enviando Record", "Aguardando resposta..");
				Log.i("USUARIO NOME", usu.getNome());
				String resposta = usuREST.alterarUsuario(this.usu);

				Log.i("Resposta", resposta);
			} catch (Exception e) {
				e.printStackTrace();
				//gerarToast("falha na rede");
				return;
			} 
		}
	}


	@Override
	public EngineOptions onCreateEngineOptions() {
		RecordeSQLiteOpenHelper recordSqliteOpenHelper = new RecordeSQLiteOpenHelper(this);
		this.recordService = new RecordeService(recordSqliteOpenHelper);
		this.recordService.open();

		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		Intent intent = getIntent();
		Bundle params = intent.getExtras();
		this.usu = new Usuario();

		String nome = params.getString("usuario");
		String senha = params.getString("senha");
		String email = params.getString("email");
		String pontuacao = params.getString("pontuacao");

		this.usu.setEmail(email);
		this.usu.setNome(nome);
		this.usu.setSenha(senha);
		this.usu.setPontuacao(pontuacao);

		final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
		engineOptions.getAudioOptions().setNeedsSound(true);
		engineOptions.getAudioOptions().setNeedsMusic(true);
		return engineOptions;
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath(GRAPHIC_ASSETS_PATH);
		SoundFactory.setAssetBasePath(AUDIO_ASSETS_PATH);
		MusicFactory.setAssetBasePath(AUDIO_ASSETS_PATH);
		FontFactory.setAssetBasePath(FONTS_ASSETS_PATH);

		// menu texture load
		final BitmapTextureAtlas generalTexture = new BitmapTextureAtlas(this.getTextureManager(), 512, 256, TextureOptions.BILINEAR);
		this.menuTextureRegions[0] = BitmapTextureAtlasTextureRegionFactory.createFromAsset(generalTexture, this, "game_menu_restart_button.png", 0, 0);
		this.menuTextureRegions[1] = BitmapTextureAtlasTextureRegionFactory.createFromAsset(generalTexture, this, "game_menu_quit_button.png", 121, 0);
		this.menuTextureRegions[2] = BitmapTextureAtlasTextureRegionFactory.createFromAsset(generalTexture, this, "game_menu_resume_button.png", 242, 0);
		generalTexture.load();

		/* BALAO */
		this.mBitmapTextureAtlasBalloon = new BitmapTextureAtlas(this.getTextureManager(), 104, 82, TextureOptions.BILINEAR);
		this.mBalloonTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasBalloon, this, "balao.png", 0, 0, 2, 1);
		this.mBitmapTextureAtlasBalloon.load();


		/*PAUSE BUTTON*/
		this.mBitmapTextureAtlasPauseBotao = new BitmapTextureAtlas(this.getTextureManager(), 90, 90, TextureOptions.BILINEAR);
		this.mPauseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlasPauseBotao, this, "pause.png", 0, 0);
		this.mBitmapTextureAtlasPauseBotao.load();

		/* PERSORNAGENS */
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 328, 90, TextureOptions.BILINEAR);
		this.mBowserTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "bowser.png", 0, 0, 6, 2);
		//this.mTrollTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "enemy.png", 73, 0, 3, 4);
		this.mBitmapTextureAtlas.load();

		/* BACKGROUND */
		this.mAutoParallaxBackgroundTexture = new BitmapTextureAtlas(this.getTextureManager(), 1024, 1024);
		this.mBackgroundBack = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, this, "back1.png", 0, 188);
		this.mBackgroundSky = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, this, "ceu2.png", 0, 0);
		this.mAutoParallaxBackgroundTexture.load();

		/* PONTUACAO */
		final ITexture droidFontTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		this.pontuacaoFonte = FontFactory.createFromAsset(this.getFontManager(), droidFontTexture, this.getAssets(), "Droid.ttf", FONT_SIZE, true, Color.WHITE);
		this.pontuacaoFonte.load();

		/* AUDIOS */
		try {
			GameContext.I.assets.sounds = new Sound[]{
					SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "laser_1.mp3"),
					//SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "theme.mp3")
			};
			this.myMusic = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "theme.mp3");
			this.myMusic.setLooping(true);
			this.myMusic.setVolume(10);
		} catch (final IOException e) {
			Log.e("onLoadResources error", e.getLocalizedMessage());
		}

	}


	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		/* Colocando o background na cena */
		this.mMainScene = new Scene();
		final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-2.0f, new Sprite(0, 0, this.mBackgroundSky, vertexBufferObjectManager)));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, new Sprite(0, CAMERA_HEIGHT - this.mBackgroundBack.getHeight(), this.mBackgroundBack, vertexBufferObjectManager)));
		this.mMainScene.setBackground(autoParallaxBackground);

		GameContext.I.engine.setVibratorManeger((Vibrator)getSystemService(Context.VIBRATOR_SERVICE));
		GameContext.I.engine.setSharedPreferences(getSharedPreferences(GameContext.PREFS_NAME, Context.MODE_PRIVATE));

		//music
	    HalligatorActivity.this.myMusic.play();
		
		/* Funny bowser. */
		this.hud_bowser = new HUD();
		this.bowser = showBowserRandom();
		hud_bowser.registerTouchArea(bowser);
		hud_bowser.attachChild(bowser);
		this.mCamera.setHUD(hud_bowser);
		this.bowser.setVisible(true);
		createSpriteSpawnTimeHandler();

		/* Balao na cena */
		final AnimatedSprite balloon = new AnimatedSprite(100, 100, this.mBalloonTextureRegion, this.getVertexBufferObjectManager());
		balloon.registerEntityModifier(new MoveModifier(100, 100, 300, 100, -80));
		balloon.animate(100);
		this.mMainScene.attachChild(balloon);

		/* Pontuacao */
		this.recorde = this.recordService.getRecorde();
		//final Text bitmapText = new Text(10, 10, this.mBitmapFont, "Pontuacao: "+pontos, new TextOptions(HorizontalAlign.RIGHT), this.getVertexBufferObjectManager());
		this.textoPontuacao = new Text(10, 10, this.pontuacaoFonte, " "+this.pontos+"  |  "+this.recorde, new TextOptions(HorizontalAlign.LEFT), this.getVertexBufferObjectManager());
		this.mMainScene.attachChild(this.textoPontuacao);

		

		/* Botao de pause no topo da tela (abrir menu ao clicar nele) */
		//this.hud_paused = new HUD();//sera que funciona com o mesmo hud?
		final Sprite pausedButton = new Sprite((CAMERA_WIDTH - this.mPauseTextureRegion.getWidth())/2, 10, this.mPauseTextureRegion, this.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					if(HalligatorActivity.this.mMainScene.hasChildScene()) {
						HalligatorActivity.this.mMainScene.clearChildScene();
						HalligatorActivity.this.mMainScene.back();
						HalligatorActivity.this.count.onResume();
						//HalligatorActivity.this.startPlayingMod();
						HalligatorActivity.this.bowser.setVisible(true);

					} else {
						HalligatorActivity.this.count.onPause();
						HalligatorActivity.this.bowser.setVisible(false);
						//this.mEngine.stop();
						HalligatorActivity.this.mMainScene.setChildScene(HalligatorActivity.this.mainMenu, false, true, true);
						//HalligatorActivity.this.mModPlayer.pause();
						//this.mEngine.start();

					}
				}
				return true;
			}
		};

		this.mMainScene.registerTouchArea(pausedButton);
		this.mMainScene.attachChild(pausedButton);
		
		/* Tempo */
		this.textotime = new Text(200, 10, this.pontuacaoFonte, " "+this.time +" ", new TextOptions(HorizontalAlign.LEFT), this.getVertexBufferObjectManager());
		this.mMainScene.attachChild(this.textotime);

		runOnUiThread(new Runnable() {
			public void run() {
				if(!stoptime) {
					count = new CountDown(startTime, interval);
					count.start();
				}
			}
		});

		this.setUpMainMenu();

		this.mMainScene.setUserData(myMusic);
		
		return this.mMainScene;

	}



	protected void setUpMainMenu() {
		final Rectangle background = new Rectangle(0, 0, GameContext.CAMERA_HEIGHT, GameContext.CAMERA_WIDTH, this.getVertexBufferObjectManager());
		background.setColor(0.0f, 0.0f, 0.0f);
		background.setAlpha(0.7f);

		mainMenu = new MenuScene(this.mCamera);
		mainMenu.attachChild(background);

		final SpriteMenuItem restartMenuItem = new SpriteMenuItem(MENU_RESTART, menuTextureRegions[0], this.getVertexBufferObjectManager());
		restartMenuItem.setPosition(30,270);
		mainMenu.addMenuItem(restartMenuItem);

		final SpriteMenuItem quitMenuItem = new SpriteMenuItem(MENU_QUIT, menuTextureRegions[1], this.getVertexBufferObjectManager());
		quitMenuItem.setPosition(170,270);
		mainMenu.addMenuItem(quitMenuItem);

		final SpriteMenuItem reausemMenuItem = new SpriteMenuItem(MENU_RESUME, menuTextureRegions[2], this.getVertexBufferObjectManager());
		reausemMenuItem.setPosition(100,170);
		mainMenu.addMenuItem(reausemMenuItem);

		mainMenu.setBackgroundEnabled(false);
		mainMenu.setOnMenuItemClickListener(this);

	}

	/* CLASSE PARA A CONTAGEM DO TEMPO */
	class CountDown extends CountDownTimer{
		//countDownTime = new CountDownTimer(startTime, interval) {

		public CountDown(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}
		@Override
		public void onTick(long millisUntilFinished) {
			temporestante= millisUntilFinished;
			if (millisUntilFinished < 10000){
				time = "Time: 0"+ String.valueOf(millisUntilFinished).substring(0, 1);
				//tempoatual = String.valueOf(millisUntilFinished).substring(0, 1);
			}else{
				time = "Time: " + String.valueOf(millisUntilFinished).substring(0, 2);
				//tempoatual = String.valueOf(millisUntilFinished).substring(0, 2);;

			}
			HalligatorActivity.this.textotime.setText(time);

		}
		public void onPause(){

			HalligatorActivity.this.stoptime = true;
			count.cancel();

		}
		public void onResume() {
			HalligatorActivity.this.stoptime = false;
			count = new CountDown(temporestante, interval);
			count.start();

		}

		@Override
		public void onFinish() {
			count.cancel();
			HalligatorActivity.this.recordService.novoHistorico(HalligatorActivity.this.pontos, usu.getNome());
			sendRecordREST(String.valueOf(recorde));
			gameOver();

		}
	}

	private void createSpriteSpawnTimeHandler() {
		TimerHandler spriteTimerHandler;
		float mEffectSpawnDelay = 3f;
		spriteTimerHandler = new TimerHandler(mEffectSpawnDelay, true, new ITimerCallback(){

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {

				final boolean bowserEnable = HalligatorActivity.this.bowser.isVisible();
				if(bowserEnable){
					HalligatorActivity.this.bowser = showBowserRandom();
					hud_bowser.detachChildren();
					hud_bowser.clearTouchAreas();
					hud_bowser.reset();
					hud_bowser.registerTouchArea(bowser);
					hud_bowser.attachChild(bowser);

				}

			}

		});

		getEngine().registerUpdateHandler(spriteTimerHandler);
	}

	public void updateScore(){
		this.pontos++;
		if(this.pontos > this.recorde){
			this.recorde = this.pontos;
		}
		this.textoPontuacao.setText(" "+pontos+"  |  "+recorde);
		if(this.pontos % 5 == 0){
			//Colocar dificuldade aqui
		}
	}

	public void gameOver(){
		Bundle bundle = new Bundle();
		bundle.putLong("pontos", pontos);
		bundle.putString("recorde", usu.getPontuacao());
		Intent intent = new Intent(HalligatorActivity.this, GameOverScreenActivity.class);
		intent.putExtras(bundle);
		this.startActivity(intent);
		this.finish();
	}
	
	

	@Override
	protected void onPause() {
		super.onPause();
		
		this.count.onPause();
		this.bowser.setVisible(false);
		this.mMainScene.setChildScene(this.mainMenu, false, true, true);
		this.myMusic.pause();
		
	}
	
	@Override
	protected synchronized void onResume() {
		super.onResume();
		if(this.myMusic != null){
			this.myMusic.play();
		}
	}

	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		if(pKeyCode == KeyEvent.KEYCODE_MENU && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			if(this.mMainScene.hasChildScene()) {
				this.mMainScene.clearChildScene();
				this.mMainScene.back();
				this.count.onResume();
				//this.startPlayingMod();
				this.bowser.setVisible(true);

			} else {
				this.count.onPause();
				this.bowser.setVisible(false);
				//this.mEngine.stop();
				this.mMainScene.setChildScene(this.mainMenu, false, true, true);
				//this.mModPlayer.pause();
				//this.mEngine.start();

			}
			return true;
		} 

		if(pKeyCode==KeyEvent.KEYCODE_BACK) {

			//	stop time
			count.onPause();
			this.bowser.setVisible(false);
			this.mEngine.stop();
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HalligatorActivity.this);

			// set title
			alertDialogBuilder.setTitle("Hammering Alligator");

			// set dialog message
			alertDialogBuilder
			.setMessage("Deseja sair do jogo?")
			.setCancelable(false)
			.setPositiveButton("Sim",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, close
					// current activity
					GameContext.I.engine.playMusic(0);
					mEngine.stop();

					HalligatorActivity.this.finish();
					finish();
				}
			})
			.setNegativeButton("No",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, just close
					// the dialog box and do nothing
					dialog.cancel();
					//restart time
					count.onResume();
					HalligatorActivity.this.mEngine.start();
					HalligatorActivity.this.bowser.setVisible(true);

				}
			});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();

			return true;
		}
		return super.onKeyDown(pKeyCode, pEvent);
	}


	public AnimatedSprite makeBowser(int x, int y){
		return new AnimatedSprite(x, y, this.mBowserTextureRegion, this.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()) {
					final boolean bowserEnable = HalligatorActivity.this.bowser.isVisible();
					if(bowserEnable){

						//HalligatorActivity.this.bowser.setVisible(false);
						GameContext.I.engine.playSound(0);
						GameContext.I.engine.vibrate();
						HalligatorActivity.this.bowser = showBowserRandom();
						hud_bowser.detachChildren();
						hud_bowser.clearTouchAreas();
						hud_bowser.reset();
						hud_bowser.registerTouchArea(bowser);
						hud_bowser.attachChild(bowser);
						HalligatorActivity.this.updateScore();
						HalligatorActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run(){
								//Fazer alguma coisa dentro da thread de ui
							};
						});
					}

				}
				return true;
			}
		};
	}

	public AnimatedSprite showBowserRandom(){
		Random rand = new Random();
		int randomInt = rand.nextInt(9);

		AnimatedSprite bowser = null;

		int col2 = 0;
		int col3 = 0;
		switch (randomInt) {
		case 0:
			bowser = makeBowser(10, 218+100*randomInt);
			bowser.animate(100);
			break;

		case 1:
			bowser = makeBowser(10, 218+100*randomInt);
			bowser.animate(100);
			break;

		case 2:
			bowser = makeBowser(10, 218+100*randomInt);
			bowser.animate(100);
			break;

		case 3:	
			bowser = makeBowser(128, 218+100*col2);
			bowser.animate(100);
			break;

		case 4:	
			col2++;
			bowser = makeBowser(128, 218+100*col2);
			bowser.animate(100);
			break;

		case 5:	
			col2++;
			col2++;
			bowser = makeBowser(128, 218+100*col2);
			bowser.animate(100);
			break;

		case 6:	
			bowser = makeBowser(251, 218+100*col3);
			bowser.animate(100);
			break;

		case 7:	
			col3++;
			bowser = makeBowser(251, 218+100*col3);
			bowser.animate(100);
			break;

		case 8:	
			col3++;
			col3++;
			bowser = makeBowser(251, 218+100*col3);
			bowser.animate(100);
			break;

		default:
			break;
		}
		return bowser;
	}


	@Override
	public boolean onMenuItemClicked(final MenuScene pMenuScene, final IMenuItem pMenuItem,
			final float pMenuItemLocalX, final float pMenuItemLocalY) {
		Log.i("MenuItemClicked", "Foi clicado!!");
		switch(pMenuItem.getID()) {
		case MENU_RESTART:
			this.mMainScene.reset();
			this.count.cancel();
			this.count.start();
			this.bowser.setVisible(true);
			this.pontos = 0L;
			this.textoPontuacao.setText(" "+pontos+"  |  "+recorde);
			return true;
		case MENU_QUIT:
			this.finish();
			return true;
		case MENU_RESUME:
			this.mMainScene.clearChildScene();
			this.mMainScene.back();
			this.count.onPause();
			this.bowser.setVisible(true);
			return true;
		default:
			return false;
		}
	}



}
