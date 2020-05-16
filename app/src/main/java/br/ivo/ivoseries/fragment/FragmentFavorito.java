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
import android.os.Message;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

public class FragmentFavorito extends IvoBaseFragment {
	
	public FragmentFavorito( LayoutInflater inflater, ViewGroup container ){
		super(inflater, container,R.layout.fragment_list);
		setAdapter(new AdapterSeries(getContext()));		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		InfoSeries info = ((InfoSeries)getAdapter().getItem(position));
		Intent intent = new Intent( getContext(), ActivitySerieChapters.class );
		intent.putExtra( "id", info.getId() ); 
		intent.putExtra("titulo", info.getTitulo());
		intent.putExtra( "status", info.getStatus() );
		
		getContext().startActivity( intent );		
	}
	
	 @Override
	 public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		 menu.add(Menu.NONE, Menu.FIRST+1, Menu.NONE, R.string.excluir);
		 return true;
	 }
	 
	 @Override
	 public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		 if( item.getItemId() == Menu.FIRST+1 ){
			int iAtualItem = (Integer)mode.getTag();//menuInfo.position;
			InfoSeries info = ((InfoSeries)getAdapter().getItem(iAtualItem));
			DBSeries db = new DBSeries(getContext());
			db.ExcluirItem(info);
			db.close();
			sendEmptyMessage(0);
		 }
		 mode.finish();
		 return true;
	 }
	 
	 public void handleMessage(Message msg ){
		 DBSeries db = new DBSeries(getContext());
		 ArrayList<InfoSeries> list = db.getListPreference();
		 db.close();
		 ((IvoBaseAdapter<InfoSeries>) getAdapter()).setItems(list);
		 ((IvoBaseAdapter<InfoSeries>) getAdapter()).notifyDataSetChanged();
	 }
}
