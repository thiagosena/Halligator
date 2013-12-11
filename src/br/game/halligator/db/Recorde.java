package br.game.halligator.db;

public class Recorde {
	
	/* Essa classe representa a Tabela do banco de dados,
	 * aqui temos o nome da tabela e os campos para fazer
	 * as consultas sql;
	 */
	
	public static final String TABLE_NAME = "recorde";
	
	//quando se utiliza _id, o android ja reconhece como sendo chave primaria
	public static final String KEY_ID = "_id"; 
	
	public static final String KEY_RECORDE = "recorde";
	public static final String KEY_NAME = "nome";
	public static final String KEY_DATE = "data";
}
