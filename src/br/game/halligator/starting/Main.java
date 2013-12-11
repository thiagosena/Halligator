package br.game.halligator.starting;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

public class Main extends BaseGameActivity {
	
	/* CONSTANTES */
	private static final int CAMERA_WIDTH = 320;
	private static final int CAMERA_HEIGHT = 480;
	
	private Camera mCamera;
	private SplashMain sceneManager;

	@Override
	public EngineOptions onCreateEngineOptions() {
		mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, 
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
		return engineOptions;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		
		sceneManager = new SplashMain(this, mEngine, mCamera);
		sceneManager.loadSplashSceneResources();
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {

		pOnCreateSceneCallback.onCreateSceneFinished(sceneManager.createSplashScene());

	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		// TODO Auto-generated method stub
		mEngine.registerUpdateHandler(new TimerHandler(1f, new ITimerCallback() 
		{
			public void onTimePassed(final TimerHandler pTimerHandler) 
			{
				mEngine.unregisterUpdateHandler(pTimerHandler);
				sceneManager.loadGameSceneResources();
				sceneManager.createGameScenes();
				sceneManager.setCurrentScene(SplashMain.SceneType.TITLE);
			}
		}));

		pOnPopulateSceneCallback.onPopulateSceneFinished();

	}

}
