package br.ivo.ivoseries.activity;

import java.util.ArrayList;

import br.ivo.ivoseries.R;
import br.ivo.ivoseries.adapter.AdapterLegendas;
import br.ivo.ivoseries.tasks.ITaskResult;
import br.ivo.ivoseries.tasks.TaskGetLegenda;
import br.ivo.ivoseries.tasks.TaskSearchLegendas;
import br.ivo.seriessearch.database.InfoCaps;
import br.ivo.seriessearch.database.InfoLegendas;
import br.ivo.seriessearch.legendastv.CTrataLegendas;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class ActivityExtractLegenda extends Activity implements OnItemClickListener{
	private String chapter;
	private ListView list = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_list);
		list = (ListView)findViewById(R.id.list);
		
		Intent intent = this.getIntent();
		
		if (intent != null) {
			if( intent.hasExtra("chapter") ){
				chapter = intent.getStringExtra("chapter");
			}
		}
		setTitle(chapter);
		AdapterLegendas adapter = new AdapterLegendas(this, chapter.replace(" ", ".")); 
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		ArrayList<InfoLegendas> legendas = new ArrayList<InfoLegendas>();
		ArrayList<String> list = CTrataLegendas.getListFilesRar(chapter.replace(" ", "."));
		for( String nome : list ){
			InfoLegendas object = new InfoLegendas();
			object.setNome(nome);
			legendas.add(object);
		}
		adapter.setItems(legendas);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		InfoLegendas legenda = (InfoLegendas) arg0.getItemAtPosition(arg2);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(arg1.getContext());

		CTrataLegendas.descompactaLegenda(legenda.getNome(),prefs.getString("localsave", "/sdcard/IvoSeriesSearch/"), chapter.replace(" ", ".") );
		
		setResult(15);
		finish();
	}

	
}
