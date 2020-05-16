package br.ivo.ivoseries.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.ivo.ivoseries.R;
import br.ivo.ivoseries.base.IvoBaseAdapter;
import br.ivo.seriessearch.database.InfoLegendas;

public class AdapterLegendas extends IvoBaseAdapter<InfoLegendas> {
	private String sNameDefault;
	
	public AdapterLegendas(Context context, String sNameDefault) {
		super(context);
		this.sNameDefault = sNameDefault;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View view = inflater.inflate(R.layout.line_info, null);
		
		InfoLegendas info = getItem(position);
		
		if( info.getNome().indexOf(sNameDefault) == 0 ){
			view.setBackgroundColor(Color.LTGRAY);
		}
		TextView txtName = (TextView)view.findViewById(R.id.name);
		//TextView txtLegendaStatus = (TextView)view.findViewById(R.id.legendastatus);
		
		txtName.setText(info.getNome());
		
		return view;
	}

}
