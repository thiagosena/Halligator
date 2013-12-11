package br.game.halligator.services.client.model;

import java.io.Serializable;

public class Usuario implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8025502385872806834L;
	
	private int id;
	private String nome;
	private String senha;
	private String email;
	private String pontuacao;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getSenha() {
		return senha;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPontuacao() {
		return pontuacao;
	}
	
	public void setPontuacao(String pontuacao) {
		this.pontuacao = pontuacao;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.nome + " | Score: " + this.pontuacao;
	}
}
