package br.game.halligator.activity;

import java.util.ArrayList;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import br.game.halligator.R;
import br.game.halligator.db.RecordeSQLiteOpenHelper;
import br.game.halligator.db.RecordeService;

public class HistoryScoreScreenActivity extends ChildScreenActivity {

	@InjectView(R.id.historico)
	private ListView historico;
	
	private RecordeService recordService;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		RecordeSQLiteOpenHelper recordSqliteOpenHelper = new RecordeSQLiteOpenHelper(this);

		this.recordService = new RecordeService(recordSqliteOpenHelper);
		this.recordService.open();

		ArrayList<String> listHist = recordService.getHistorico();
		ArrayList<String> listHist2 = new ArrayList<String>();

		ArrayAdapter<String> adaptadorLista = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listHist2);

		this.historico.setAdapter(adaptadorLista);
		int cont = 0;
		for(int i = 0; i < (listHist.size()/3); i++){
			cont = i*3;
			listHist2.add(listHist.get(cont).toString()+" | "+listHist.get(cont+1).toString()+" | " +listHist.get(cont+2).toString());
			adaptadorLista.notifyDataSetInvalidated();
		}
		this.recordService.close();
		
	}
	
	@Override
	protected int getContentViewId() {
		return R.layout.history_score_screen;
	}
}
