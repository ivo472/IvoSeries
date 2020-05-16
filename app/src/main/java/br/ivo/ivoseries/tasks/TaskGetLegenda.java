package br.ivo.ivoseries.tasks;

import java.util.ArrayList;
import java.util.List;

import br.ivo.geral.CConnectData;
import br.ivo.ivoseries.R;
import br.ivo.ivoseries.activity.ActivityExtractLegenda;
import br.ivo.ivoseries.activity.ActivitySearchLegenda;
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
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;

public class TaskGetLegenda extends AsyncTask<String, Void,  String> {

    private final ITaskResult callback;
    private ProgressDialog progress;
    
    public TaskGetLegenda(final ITaskResult callback) {
    	this.callback = callback;
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(callback.getTaskContext(), "", callback.getTaskContext().getString(R.string.baixando_pacote_legenda), false, true);
    }
    
    @Override
    protected  String doInBackground(final String ... args) {
		if(!isCancelled()) {
			CTrataLegendas.getLegendaByURL(args[0],args[1]);
			//	CTrataLegendas.descompactaLegenda(args[1], args[2], args[1] );
			return args[1];
		}
		return null;
    }
    
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(!isCancelled()) {
            progress.dismiss();
            callback.onTaskFinished(result);
        }
    }    
}
