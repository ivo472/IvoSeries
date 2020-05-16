package br.ivo.ivoseries.base;

import br.ivo.ivoseries.R;
import android.content.Context;
import android.os.Handler;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class IvoBaseFragment extends Handler implements OnItemClickListener, OnItemLongClickListener, ActionMode.Callback {

	private View fragment;
	private ListView list = null;
	//private ListAdapter adapter = null;
	private IvoBaseAdapter<?> adapter = null;
	private ActionMode mActionMode = null; 
	
	public IvoBaseFragment( LayoutInflater inflater, ViewGroup container, int resource ){
		fragment = inflater.inflate(resource, container, false);
		
		list = (ListView)fragment.findViewById(R.id.list);
		list.setOnItemClickListener(this);
		list.setOnItemLongClickListener(this);
	}
	
	public View getView(){
		return fragment;
	}
	
	public ListView getListView(){
		return list;
	}
	
	public ListAdapter getAdapter(){
		return adapter;
	}
	
	public void setAdapter( IvoBaseAdapter<?> adapter ){
		this.adapter = adapter;
		list.setAdapter(adapter);
	}
	
	public Context getContext(){
		return fragment.getContext();		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // Start the CAB using the ActionMode.Callback defined above
        mActionMode = list.startActionMode(this);
        mActionMode.setTag(new Integer(arg2));
        arg1.setSelected(true);
		return true;
	}
	
	// Called when the action mode is created; startActionMode() was called
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // Inflate a menu resource providing context menu items
        //MenuInflater inflater = mode.getMenuInflater();
        //inflater.inflate(R.menu.context_menu, menu);
        return true;
    }
    
    // Called each time the action mode is shown. Always called after onCreateActionMode, but
    // may be called multiple times if the mode is invalidated.
    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false; // Return false if nothing is done
    }

    // Called when the user selects a contextual menu item
    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            default:
                return false;
        }
    }

    // Called when the user exits the action mode
    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mActionMode = null;
    }
}
