package br.game.halligator.starting;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.VideoView;
import br.game.halligator.R;
import br.game.halligator.activity.MainScreenActivity;

public class SplashActivity extends Activity implements OnCompletionListener {
	@Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        VideoView video = (VideoView) findViewById(R.id.videoView);
        video.setVideoPath("android.resource://br.game.halligator/raw/" + R.raw.splash);
        video.start();
        video.setOnCompletionListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp){
        Intent intent = new Intent(this, MainScreenActivity.class);
        startActivity(intent);
        finish();
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Intent intent = new Intent(this, MainScreenActivity.class);
        startActivity(intent);
        finish();
		return super.onTouchEvent(event);
	}
    
}
