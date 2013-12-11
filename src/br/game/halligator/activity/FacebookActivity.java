package br.game.halligator.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import br.game.halligator.R;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class FacebookActivity extends Activity implements DialogListener{
	
	final Facebook facebook = new Facebook("448553011876372");
	
	String FILENAME = "AndroidSSO_data";
	
	private SharedPreferences mPrefs;
	
	private Long pontos = 0L;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		try{
			String msg = "";
			super.onCreate(savedInstanceState);
			setContentView(R.layout.main);
			/*
			 * Get existing access_token if any
			 */
			mPrefs = getPreferences(MODE_PRIVATE);
			String access_token = mPrefs.getString("access_token", null);

			long expires = mPrefs.getLong("access_expires", 0);

			if(access_token != null) {
				facebook.setAccessToken(access_token);
				msg +="has token: "+access_token;
			}
			if(expires != 0) {
				facebook.setAccessExpires(expires);
				msg +=" has expires: "+expires;
			}

			Intent intent = getIntent();
			Bundle params = intent.getExtras();
			
			this.pontos = params.getLong("pontos");
			
			/*
			 * Only call authorize if the access_token has expired.
			 */
			if(!facebook.isSessionValid()) {
				msg += "Sua pontuação foi enviada!!";
				// replace APP_API_ID with your own
				facebook.authorize(this,
						new String[] {"publish_stream", "read_stream", "offline_access"}, Facebook.FORCE_DIALOG_AUTH,this);
			}
			TextView message = (TextView)this.findViewById(R.id.message);
			message.setText(msg);
		}catch (Exception e){
			TextView message = (TextView)this.findViewById(R.id.message);
			message.setText(e.toString());
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		try{
			super.onActivityResult(requestCode, resultCode, data);

			facebook.authorizeCallback(requestCode, resultCode, data);
		}catch (Exception e){
			TextView message = (TextView)this.findViewById(R.id.message);
			message.setText("onactivityresult: "+e.toString());
		}
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onComplete(Bundle values) {
		// TODO Auto-generated method stub
		if (!values.containsKey("post_id"))
		{
			try
			{
				String msg = "Sua pontuação no Halligator: " + this.pontos + " pontos!";
				String response = facebook.request("me");
				Bundle parameters = new Bundle();
				parameters.putString("message", msg);
				parameters.putString("description", "Acerte o máximo de Halligators que você conseguir!");
				parameters.putString("link", "https://pure-earth-6139.herokuapp.com/");
				parameters.putString("picture", "http://spielwiese.von-gierke.org/wp-content/uploads/wpsc/category_images/android%20games%20logo%20160x160.png");
				response = facebook.request("me/feed", parameters, "POST");
				Log.d("Tests", "got response: " + response);
				if (response == null || response.equals("") || 
						response.equals("false")) {
					Log.v("Error", "Blank response");
				}


			}
			catch (Exception e)
			{
				// TODO: handle exception
				System.out.println(e.getMessage());
				TextView message = (TextView)this.findViewById(R.id.message);
				message.setText("oncomplete: " + e.toString());
			}
		}
	}

	@Override
	public void onError(DialogError error) {
		// TODO Auto-generated method stub
		TextView message = (TextView)this.findViewById(R.id.message);
		message.setText("onerror: " + error.toString());

	}

	@Override
	public void onFacebookError(FacebookError error) {
		TextView message = (TextView)this.findViewById(R.id.message);
		message.setText("onfacebookerror: "+error.toString());

	}


}
