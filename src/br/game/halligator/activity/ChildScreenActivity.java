package br.game.halligator.activity;

import br.game.halligator.R;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public abstract class ChildScreenActivity  extends RoboActivity {

	@InjectView(R.id.back_button)
	private ImageButton backButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getContentViewId());
		
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				backButton.setBackgroundResource(R.drawable.button_back_on);
				//final Intent intent = new Intent(ChildScreenActivity.this, MainScreenActivity.class); 
				ChildScreenActivity.this.onBackPressed();
				ChildScreenActivity.this.finish();
			}
		});
	}
	
	abstract protected int getContentViewId();
	
	@Override
	protected void onStop() {
		super.onStop();
		backButton.setBackgroundResource(R.drawable.button_back_on);
	}
}