package br.game.halligator.activity;

import java.util.ArrayList;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import br.game.halligator.R;
import br.game.halligator.services.client.model.Usuario;
import br.game.halligator.services.client.webservice.UsuarioREST;


public class TopRecordScreenActivity extends ChildScreenActivity {
	
	@InjectView(R.id.list_score) 
	private ListView listaPontuacao;
		
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// initialize text workers
		
		UsuarioREST cliREST = new UsuarioREST();
		
		ArrayList<Usuario> listaUsuarios = new ArrayList<Usuario>();
		ArrayList<String> listaUsuarios2 = new ArrayList<String>();

		ArrayAdapter<String> adaptadorLista = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaUsuarios2);

		this.listaPontuacao.setAdapter(adaptadorLista);
				
		try{
			
			listaUsuarios = (ArrayList<Usuario>) cliREST.getListaUsuario();
			
		} catch(Exception e){
			e.printStackTrace();
			gerarToast(e.getMessage());
		}
		
		
		for (int i = 0; i < listaUsuarios.size(); i++) {
			listaUsuarios2.add(listaUsuarios.get(i).toString());
			adaptadorLista.notifyDataSetChanged();
			Log.i("USUARIO", " ->"+i);
		}
		
	}
	
	@Override
	protected int getContentViewId() {
		return R.layout.top_players_screen;
	}

	private void gerarToast(CharSequence message) {
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast
				.makeText(getApplicationContext(), message, duration);
		toast.show();
	}
}
