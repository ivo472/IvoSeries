package br.ivo.ivoseries.activity;

import br.ivo.ivoseries.R;
import br.ivo.ivoseries.adapter.AdapterLegendas;
import br.ivo.ivoseries.tasks.ITaskResult;
import br.ivo.ivoseries.tasks.TaskGetLegenda;
import br.ivo.ivoseries.tasks.TaskSearchLegendas;
import br.ivo.seriessearch.database.InfoCaps;
import br.ivo.seriessearch.database.InfoLegendas;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class ActivitySearchLegenda extends Activity implements OnItemClickListener, ITaskResult{
	private InfoCaps info = new InfoCaps();
	private EditText edit = null;
	private ListView list = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_legenda_search);
		edit = (EditText)findViewById(R.id.text);
		list = (ListView)findViewById(R.id.list);
		
		Intent intent = this.getIntent();
		
		if (intent != null) {
			if( intent.hasExtra("id") ){
				info.setId(intent.getIntExtra("id",-1));
				info.setChapter(intent.getStringExtra("chapter"));
				info.setStatus(intent.getIntExtra("status",-1));
			}
		}
		setTitle(info.getChapter());
		edit.setText(info.getChapter());
		list.setAdapter(new AdapterLegendas(this, info.getChapter().replace(" ", ".")));
		list.setOnItemClickListener(this);
	}

	public void onText(View v){
		TaskSearchLegendas task = new TaskSearchLegendas(list);
		task.execute(edit.getText().toString());
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edit.getWindowToken(), 0);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		InfoLegendas legenda = (InfoLegendas) arg0.getItemAtPosition(arg2);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(arg1.getContext());
		TaskGetLegenda task = new TaskGetLegenda(this);
		task.execute(new String [] { legenda.getUrl(),info.getChapter().replace(" ", "."),prefs.getString("localsave", "/sdcard/IvoSeriesSearch/")} );
		//finish();
	}
	
	@Override
	public Context getTaskContext() {
		return list.getContext();
	}

	@Override
	public void onTaskFinished(Object object) {
		Intent intent = new Intent( getBaseContext(), ActivityExtractLegenda.class );
		intent.putExtra("chapter", (String)object);
		startActivityForResult(intent, 15);		
	}
	
	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		if( requestCode == 15 && resultCode == 15 ){
			finish();
		}
	}
	
}
