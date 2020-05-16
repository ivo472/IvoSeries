package br.ivo.ivoseries.tasks;

import java.util.ArrayList;
import java.util.List;

import br.ivo.geral.CConnectData;
import br.ivo.ivoseries.R;
import br.ivo.ivoseries.base.IvoBaseAdapter;
import br.ivo.ivoseries.fragment.FragmentChapters;
import br.ivo.seriessearch.database.InfoCaps;
import br.ivo.seriessearch.eztv.CEztvUrls;
import br.ivo.seriessearch.eztv.CTrataSearch;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class TaskGetLastSeries extends AsyncTask<Void, Void,  ArrayList<InfoCaps>> {

    private final FragmentChapters frag;
    private ProgressDialog progress;
    
    public TaskGetLastSeries(final FragmentChapters callback) {
    	this.frag = callback;
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(frag.getContext(), frag.getContext().getString(R.string.msg_sem_dados_series), frag.getContext().getString(R.string.aguarde), false, true);
    }
    
    @Override
    protected  ArrayList<InfoCaps> doInBackground(final Void... args) {
		if(!isCancelled()) {
			CConnectData.downloadFile(CEztvUrls.sPrincipalUrl, "/sdcard/IvoSeriesSearch/last.htm");
			
			String sContent = CConnectData.readFile("/sdcard/IvoSeriesSearch/last.htm");

			if( sContent != null ){
				CTrataSearch trata = new CTrataSearch(sContent,-1);
				return trata.getList();
			}
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
    protected void onPostExecute(ArrayList<InfoCaps> result) {
        super.onPostExecute(result);
        if(!isCancelled()) {
            ((IvoBaseAdapter<InfoCaps>) frag.getAdapter()).setItems(result);
            ((IvoBaseAdapter<InfoCaps>) frag.getAdapter()).notifyDataSetChanged();
            progress.dismiss();
        }
    }
    
}
