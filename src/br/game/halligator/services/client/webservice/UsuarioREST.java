package br.game.halligator.services.client.webservice;

import java.util.ArrayList;
import java.util.List;

import br.game.halligator.services.client.model.Usuario;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class UsuarioREST {

	//private static final String URL_WS = "http://192.168.151.159:8184/WServiceTeste/usuario/";
	private static final String URL_WS = "http://hammering.jelastic.servint.net/halligator/usuario/";

	public Usuario getUsuario(String nome, String senha) throws Exception {

		String[] resposta = new WebServiceUsuario().get(URL_WS + nome);
		
		if (resposta[0].equals("200")) {
			Gson gson = new Gson();
			Usuario usuario = gson.fromJson(resposta[1], Usuario.class);
			if (!senha.equals(usuario.getSenha()))
				throw new Exception("Senha Incorreta");
			else
				return usuario;
		} else {
			throw new Exception(resposta[1]);
		}
	}
	
	public List<Usuario> getListaUsuario() throws Exception {

		String[] resposta = new WebServiceUsuario().get(URL_WS + "buscarTodosGSON");
//		String[] resposta = new WebServiceUsuario().get(URL_WS + "buscarTodos");
		
		if (resposta[0].equals("200")) {
			Gson gson = new Gson();
			ArrayList<Usuario> listaUsuario = new ArrayList<Usuario>();
			JsonParser parser = new JsonParser();
		    JsonArray array = parser.parse(resposta[1]).getAsJsonArray();
		    
		    for (int i = 0; i < array.size(); i++) {
		    	listaUsuario.add(gson.fromJson(array.get(i), Usuario.class));
			}
			return listaUsuario;
		} else {
			throw new Exception(resposta[1]);
		}
	}
	
	public String inserirUsuario(Usuario usuario) throws Exception {
		
		Gson gson = new Gson();
		String usuarioJSON = gson.toJson(usuario);
		
		String[] resposta = new WebServiceUsuario().post(URL_WS + "inserir", usuarioJSON);
		
		if (resposta[0].equals("200")) {
			return resposta[1];
		} else {
			throw new Exception(resposta[1]);
		}
	}
	
	public String alterarUsuario(Usuario usuario) throws Exception {
		Gson gson = new Gson();
		String usuarioJSON = gson.toJson(usuario);
		
		String[] resposta = new WebServiceUsuario().post(URL_WS + "alterar", usuarioJSON);
		
		if (resposta[0].equals("200")) {
			return resposta[1];
		} else {
			throw new Exception(resposta[1]);
		}
	}
	
	public String deletarUsuario(int id) {
		
		String[] resposta = new WebServiceUsuario().get(URL_WS + "delete/" + id);
		return resposta[1];
	}
}
