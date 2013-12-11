package br.game.halligator.starting;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import br.game.halligator.activity.MainScreenActivity;

import android.content.Intent;

public class SplashMain {
	
	private SceneType currentScene;
	private BaseGameActivity activity;
	private Engine engine;
	private Camera camera;
	
	private Scene splashScene;
	private Scene titleScene;
	private Scene mainGameScene;
	private BitmapTextureAtlas splashTextureAtlas;
	private ITextureRegion splashTextureRegion;
	
	public enum SceneType
	{
		SPLASH,
		TITLE,
		MAINGAME
	}
	
	public SplashMain(BaseGameActivity activity, Engine engine, Camera camera) {
		this.activity = activity;
		this.engine = engine;
		this.camera = camera;		
	}

	//Method loads all of the splash scene resources
	public void loadSplashSceneResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		/* Imagem da tela de splash */
		this.splashTextureAtlas = new BitmapTextureAtlas(this.activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		this.splashTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.splashTextureAtlas, this.activity, "splash.png", 0, 0);
		this.splashTextureAtlas.load();
	}
	
	//Method loads all of the resources for the game scenes
	public void loadGameSceneResources() {

	}
	
	//Method creates the Splash Scene
	public Scene createSplashScene() {
		//Create the Splash Scene and set background colour to red and add the splash logo.
		this.splashScene = new Scene();
		
		//splashScene.setBackground(new Background(1, 0, 0));
		Sprite splash = new Sprite(0, 0, this.splashTextureRegion, this.activity.getVertexBufferObjectManager());
		//splash.setScale(1.5f);
		splash.setPosition((camera.getWidth() - splash.getWidth()) * 0.5f, (camera.getHeight() - splash.getHeight()) * 0.5f);
		splashScene.attachChild(splash);
		
		return splashScene;
	}
	
	//Method creates all of the Game Scenes
	public void createGameScenes() {		
		Intent intent = new Intent(activity, SplashActivity.class);
		activity.startActivity(intent);
		activity.finish();
	}
	
	//Method allows you to get the currently active scene
	public SceneType getCurrentScene() {
		return currentScene;
		
	}
	
	//Method allows you to set the currently active scene
	public void setCurrentScene(SceneType scene) {
		currentScene = scene;
		switch (scene)
		{
		case SPLASH:
			break;
		case TITLE:
			engine.setScene(titleScene);
			break;
		case MAINGAME:
			engine.setScene(mainGameScene);
			break;
		}		
	}
		
}
