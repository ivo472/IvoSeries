package br.ivo.ivoseries.tasks;

import java.util.ArrayList;
import java.util.List;

import br.ivo.geral.CConnectData;
import br.ivo.ivoseries.R;
import br.ivo.ivoseries.base.IvoBaseAdapter;
import br.ivo.ivoseries.fragment.FragmentChapters;
import br.ivo.seriessearch.database.DBCaps;
import br.ivo.seriessearch.database.InfoCaps;
import br.ivo.seriessearch.database.InfoLegendas;
import br.ivo.seriessearch.database.InfoSeries;
import br.ivo.seriessearch.eztv.CEztvUrls;
import br.ivo.seriessearch.eztv.CTrataSearch;
import br.ivo.seriessearch.legendastv.CTrataLegendas;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ListView;

public class TaskSearchLegendas extends AsyncTask<String, Void,  ArrayList<InfoLegendas>> {

    private final ListView frag;
    private ProgressDialog progress;
    
    public TaskSearchLegendas(final ListView callback) {
    	this.frag = callback;
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(frag.getContext(), "", frag.getContext().getString(R.string.procurando), false, true);
    }
    
    @Override
    protected  ArrayList<InfoLegendas> doInBackground(final String ... args) {
		if(!isCancelled()) {
			ArrayList<InfoLegendas> list = CTrataLegendas.getLegendasList(args[0]);
			
			return list;
		}
		return null;
    }

    
    /*@Override
    protected void onProgressUpdate(final List<SmsBO>... values) {
    	super.onProgressUpdate(values);
    	if( !isCancelled() )
    	callback.onSmsListRetrived(values[0]);
    }*/
    
    @Override
    protected void onPostExecute(ArrayList<InfoLegendas> result) {
        super.onPostExecute(result);
        if(!isCancelled()) {
            ((IvoBaseAdapter<InfoLegendas>) frag.getAdapter()).setItems(result);
            ((IvoBaseAdapter<InfoLegendas>) frag.getAdapter()).notifyDataSetChanged();
            progress.dismiss();
        }
    }    
}
