package br.game.halligator.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class RecordeService {

	/* Essa classe eh responsavel por consultar, inserir,
	 * atualizar e deletar. Aqui utilizamos dois metodos,
	 * um para selecionar o recorde atual e outro para 
	 * alterar.
	 */

	private RecordeSQLiteOpenHelper recordSQLiteOpenHelper;
	private SQLiteDatabase database;


	/* Construtor sendo inicializado com um atributo da classe RecordeSQLiteOpenHelper */
	public RecordeService(RecordeSQLiteOpenHelper precordSQLiteOpenHelper) {
		this.recordSQLiteOpenHelper = precordSQLiteOpenHelper;
	}

	/* Esse metodo faz a inicializacao do banco de dados, ele recebe a nova instancia
	 * do metodo getWritableDatabase da classe RecordeSQLiteOpenHelper */
	public void open(){
		this.database = this.recordSQLiteOpenHelper.getWritableDatabase();
	}

	public void novoHistorico(Long recorde, String name) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"); 
		Date date = new Date();

		ContentValues values = new ContentValues();
		values.put(Recorde.KEY_RECORDE, recorde); 
		values.put(Recorde.KEY_NAME, name);
		values.put(Recorde.KEY_DATE, dateFormat.format(date));
		
		try {

			this.database.insert(Recorde.TABLE_NAME, null, values);
		} catch (SQLiteException ex) {
			Log.e("test", ex.getMessage());
			throw ex;
		} finally {
			this.database.close();
		}
		
		this.database.close(); // Closing database connection
	}

	public ArrayList<String> getHistorico() {
		String[] columns = new String[]{Recorde.KEY_ID, Recorde.KEY_RECORDE, Recorde.KEY_DATE, Recorde.KEY_NAME};

		Cursor cursor;
		cursor = this.database.query(Recorde.TABLE_NAME, 
				columns, 
				null, null, 
				null, null, Recorde.KEY_RECORDE+ " DESC");
		ArrayList<String> historico = new ArrayList<String>();
		cursor.moveToFirst();
		while(cursor.isAfterLast() == false){
			historico.add(cursor.getString(1));
			historico.add(cursor.getString(2));
			historico.add(cursor.getString(3));
			cursor.moveToNext();
		}

		return historico;


	}

	/* Esse metodo retorna o ultimo recorde. Aqui utilizamos a funcao query para fazer 
	 * um select e um Cursor para recuperar os dados. */
	public Long getRecorde() {
		Cursor cursor;
		cursor = this.database.query(Recorde.TABLE_NAME, 
				new String[]{Recorde.KEY_ID+",MAX("+Recorde.KEY_RECORDE+") as "+Recorde.KEY_RECORDE}, 
				null, null, 
				null, null, null);
		cursor.moveToFirst();
		return cursor.getLong(cursor.getColumnIndex(Recorde.KEY_RECORDE));
	}

	/* Esse metodo atualiza o novo recorde; utilizamos um objeto do ContentValues 
	 * para passar os valores que queremos atualizar. */
	public void novoRecorde(Long recorde, String name){

		ContentValues values = new ContentValues();
		values.put(Recorde.KEY_RECORDE, recorde);
		this.database.update(Recorde.TABLE_NAME, values, "nome = ?", new String[]{name});
	}
	
	public int deleteAll(){
		
		return this.database.delete(Recorde.TABLE_NAME, null, null);
	}
	
	public void close() {
		this.database.close();
	}
}
