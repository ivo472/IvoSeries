package br.ivo.ivoseries.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import br.ivo.ivoseries.adapter.AdapterChapters;
import br.ivo.seriessearch.database.InfoCaps;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

public class FragmentBaixados extends FragmentChapters {

		public FragmentBaixados( LayoutInflater inflater, ViewGroup container ){
		super(inflater,container);
		
		setAdapter( new AdapterChapters(getContext() ) );
		updateFiles();		
	}
		
	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		super.onActionItemClicked(mode, item);
		if( FragmentChapters.MENU_EXCLUIR_VIDEO == item.getItemId() ){
			updateFiles();
			((AdapterChapters) getAdapter()).notifyDataSetChanged();
		}
		return true;
	}
	
	private void updateFiles(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
		File fPath = new File(prefs.getString("localsave", "/sdcard/IvoSeriesSearch/"));
		
		File [] listFiles = fPath.listFiles();
		ArrayList<InfoCaps> listCaps = new ArrayList<InfoCaps>();
		Arrays.sort(listFiles);
		InfoCaps info;
		for( int nCount = 0; nCount < listFiles.length; nCount++ ){
			if( listFiles[nCount].getName().endsWith(".mp4") || listFiles[nCount].getName().endsWith(".mkv") || listFiles[nCount].getName().endsWith(".avi") ){
				info = new InfoCaps();
				info.setChapter(listFiles[nCount].getName().substring(0, listFiles[nCount].getName().lastIndexOf(".")));
				listCaps.add(info);
			}
		}
		
		((AdapterChapters) getAdapter()).setItems(listCaps);
	}
}
