package br.ivo.ivoseries.fragment;

import java.util.ArrayList;

import br.ivo.ivoseries.R;
import br.ivo.ivoseries.activity.ActivitySerieChapters;
import br.ivo.ivoseries.adapter.AdapterSeries;
import br.ivo.ivoseries.base.IvoBaseAdapter;
import br.ivo.ivoseries.base.IvoBaseFragment;
import br.ivo.seriessearch.database.DBSeries;
import br.ivo.seriessearch.database.InfoSeries;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

public class FragmentSearch extends IvoBaseFragment implements OnClickListener{
	private EditText edit = null;
	private Button btnProcurar = null;
	private AdapterSeries adapter = null;
	
	public static final int NOTIFY_UPDATE_SERIES_DATA = 1;
	
	public FragmentSearch( LayoutInflater inflater, ViewGroup container ){
		super(inflater, container,R.layout.fragment_search);
		edit = (EditText)getView().findViewById(R.id.text);
		btnProcurar = (Button)getView().findViewById(R.id.button1);
		
		btnProcurar.setOnClickListener(this);
		
		adapter = new AdapterSeries(getContext());		
		setAdapter(adapter);
	}
	
	public void handleMessage(Message msg) {
		if( msg.what == NOTIFY_UPDATE_SERIES_DATA ){
			adapter.notifyDataSetChanged();	
		}
	}
	
	@Override
	public void onClick(View v) {
		if(edit.getText().toString().length()==0){
			DBSeries db = new DBSeries(getView().getContext());
			ArrayList<InfoSeries> list = db.getList();
			adapter.setItems(list);
			db.close();
			sendEmptyMessage(NOTIFY_UPDATE_SERIES_DATA);
		}else{
			DBSeries db = new DBSeries(getView().getContext());
			ArrayList<InfoSeries> list = db.search(edit.getText().toString());
			adapter.setItems(list);
			db.close();
			sendEmptyMessage(NOTIFY_UPDATE_SERIES_DATA);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		InfoSeries info = ((InfoSeries)adapter.getItem(position));
		Intent intent = new Intent( getContext(), ActivitySerieChapters.class );
		intent.putExtra( "id", info.getId() ); 
		intent.putExtra("titulo", info.getTitulo());
		intent.putExtra( "status", info.getStatus() );
		
		getContext().startActivity( intent );		
	}
	
	@Override
	 public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		//int iAtualItem = (Integer)mode.getTag();//menuInfo.position;
		//InfoSeries info = ((InfoSeries)getAdapter().getItem(iAtualItem));
		//if( info.getStatus() != InfoSeries.STATUS_CHECK ){
			menu.add(Menu.NONE, Menu.FIRST+1, Menu.NONE, R.string.adicionar);
			return true;
		//}
		
		//return false;	
	 }
	 
	 @Override
	 public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		 if( item.getItemId() == Menu.FIRST+1 ){
			int iAtualItem = (Integer)mode.getTag();//menuInfo.position;
			InfoSeries info = ((InfoSeries)getAdapter().getItem(iAtualItem));
			DBSeries db = new DBSeries(getContext());
			info.setStatus(InfoSeries.STATUS_CHECK);
			db.atualizarItem(info);
			db.close();
			sendEmptyMessage(NOTIFY_UPDATE_SERIES_DATA);
		 }
		 mode.finish();
		 return true;
	 }
}
