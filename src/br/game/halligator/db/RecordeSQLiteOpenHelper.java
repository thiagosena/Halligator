package br.game.halligator.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecordeSQLiteOpenHelper extends SQLiteOpenHelper {
	
	/* Essa classe representa o banco de dados, ela estende
	 * de SQLiteOpenHelper, e eh nela que montaremos toda a
	 * estrutura do banco de dados; */
	
	public static final String DATABASE_NAME = "Database_Recorde";
	public static final int DATABASE_VERSION = 1;
	
	/* O construtor precisa de um Context que eh a nossa aplicacao,
	 * de um nome que eh o banco de dados de um factory que estamos passando
	 * como nulo e a versao do banco de dados. */
	public RecordeSQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	
	/* O metodo onCreate eh o metodo chamado para criar o banco de dados, nele 
	 * temos nosso create table e no caso, o insert do record 0 */
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		String CREATE_TABLE_RECORD = "CREATE TABLE "+ Recorde.TABLE_NAME +
				" ("+Recorde.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				 Recorde.KEY_RECORDE + " LONG, " +
				 Recorde.KEY_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, " + 
				 Recorde.KEY_NAME + " TEXT);";
		
		arg0.execSQL(CREATE_TABLE_RECORD);

	}
	
	
	/* O metodo onUpgrade eh o metodo chamado ao atualizar a aplicacao,
	 * o parametro arg1 eh a versao antiga do banco de dados e o parametro arg2
	 * eh a nova versao, quando ha necessidade de alterar o banco de acordo com a
	 * versao podemos utilizar esse metodo. Por enquanto nao utilizaremos esse
	 * recurso. */
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
