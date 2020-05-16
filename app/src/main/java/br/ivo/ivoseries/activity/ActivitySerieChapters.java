package br.ivo.ivoseries.activity;

import br.ivo.geral.CConnectData;
import br.ivo.ivoseries.adapter.AdapterChapters;
import br.ivo.ivoseries.base.IvoBaseAdapter;
import br.ivo.ivoseries.fragment.FragmentChapters;
import br.ivo.ivoseries.tasks.TaskUpdateSeries;
import br.ivo.seriessearch.database.DBCaps;
import br.ivo.seriessearch.database.DBSeries;
import br.ivo.seriessearch.database.InfoCaps;
import br.ivo.seriessearch.database.InfoSeries;
import br.ivo.seriessearch.eztv.CTrataSearch;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ActivitySerieChapters extends Activity{

	private static final int MENU_ATUALIZAR = Menu.FIRST + 1;
	private static final int MENU_ADICIONAR = Menu.FIRST + 2;
	
	private InfoSeries info = null;
	FragmentChapters frag = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		frag = new FragmentChapters(getLayoutInflater(), null);
		frag.setAdapter( new AdapterChapters(frag.getContext() ) );
		setContentView(frag.getView());
		
		info = new InfoSeries();
		info.setId(-1);
		
		Intent intent = this.getIntent();
		
		if (intent != null) {
			if( intent.hasExtra("id") ){
				info.setId(intent.getIntExtra("id",-1));
				info.setTitulo(intent.getStringExtra("titulo"));
				info.setStatus(intent.getIntExtra("status",-1));
			}
		}
		setTitle(info.getTitulo()+" ("+info.getId()+")");
	}
	
	@Override
	public void onResume() {		
		super.onResume();	
		DBCaps db = new DBCaps(getApplicationContext());
		if(db.count(info.getId())>0){
			//ArrayList<InfoCaps> list = db.getListByEstado(info.getId());
			String sContent = CConnectData.readFile("/sdcard/IvoSeriesSearch/"+info.getId()+".htm");

			if( sContent != null ){
				CTrataSearch trata = new CTrataSearch(sContent,info.getId());
				((IvoBaseAdapter<InfoCaps>) frag.getAdapter()).setItems(trata.getList());
			}
			
		}else{
			TaskUpdateSeries task = new TaskUpdateSeries(frag);
			task.execute(info);
		}		
		db.close();
		((IvoBaseAdapter<InfoCaps>) frag.getAdapter()).notifyDataSetChanged();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		populateMenu(menu);
		return(super.onCreateOptionsMenu(menu));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return(applyMenuChoice(item) ||	super.onOptionsItemSelected(item));
	}
		
	private void populateMenu(Menu menu) {
		menu.add( Menu.NONE, MENU_ATUALIZAR, Menu.NONE, "Atualizar" );
		if( info.getStatus() != InfoSeries.STATUS_CHECK)
			menu.add( Menu.NONE, MENU_ADICIONAR, Menu.NONE, "Adicionar" );
	}
	
	private boolean applyMenuChoice(MenuItem item) {
		boolean bReturn = true;
		switch (item.getItemId()) {
		case MENU_ATUALIZAR:
			TaskUpdateSeries task = new TaskUpdateSeries(frag);
			task.execute(info);
			break;
		case MENU_ADICIONAR:
			DBSeries db = new DBSeries(getApplicationContext());
			info.setStatus(InfoSeries.STATUS_CHECK);
			db.atualizarItem(info);
			db.close();
			Toast.makeText(getApplication(), info.getTitulo()+" adicionado com sucesso", Toast.LENGTH_LONG).show();
			break;
		default:
			bReturn = false;
			break;
				
		}
		return bReturn;
	}
}
