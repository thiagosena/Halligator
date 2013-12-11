package br.game.halligator.activity;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.game.halligator.R;
import br.game.halligator.services.client.model.Usuario;
import br.game.halligator.services.client.webservice.UsuarioREST;

public class LoginActivity extends ChildScreenActivity {

	@InjectView(R.id.etUsuario)
	private EditText usuario;
	
	@InjectView(R.id.etSenhA)
	private EditText senha;
	
	@InjectView(R.id.btEntrar)
	private Button entrar;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        entrar.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), MainScreenActivity.class);
                Bundle params = new Bundle();
                
                String respostaUsuario = usuario.getText().toString();
                String respostaSenha = senha.getText().toString();
                
                Usuario usu = new Usuario();
                
                UsuarioREST cliREST = new UsuarioREST();
                
                try {
					usu = cliREST.getUsuario(respostaUsuario, respostaSenha);
					
					params.putString("usuario", usu.getNome());
	                params.putString("senha", usu.getSenha());
	                params.putString("email", usu.getEmail());
	                params.putString("pontuacao", usu.getPontuacao());
	                
				} catch (Exception e) {
					e.printStackTrace();
					gerarToast(e.getMessage());
				}
                
                
                intent.putExtras(params);
                startActivity(intent);
                finish();
            }
        });
    }
    
    @Override
	protected int getContentViewId() {
		return R.layout.activity_login;
	}

    private void gerarToast(CharSequence message) {
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast
				.makeText(getApplicationContext(), message, duration);
		toast.show();
	}
    
}
