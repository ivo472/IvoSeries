package br.ivo.ivoseries.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.ivo.ivoseries.R;
import br.ivo.ivoseries.base.IvoBaseAdapter;
import br.ivo.seriessearch.database.DBSeries;
import br.ivo.seriessearch.database.InfoSeries;

public class AdapterSeries extends IvoBaseAdapter<InfoSeries> {

	public AdapterSeries(Context context) {
		super(context);
		DBSeries db = new DBSeries(getContext());
		setItems(db.getListPreference());
		db.close();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View view = inflater.inflate(R.layout.line_info, null);
		
		InfoSeries info = getItem(position);
		TextView txtName = (TextView)view.findViewById(R.id.name);
		TextView txtLegendaStatus = (TextView)view.findViewById(R.id.legendastatus);
		
		txtName.setText(info.getTitulo());
		switch((int)info.getStatus()){
		case InfoSeries.STATUS_UNDEFINED:
			txtLegendaStatus.setText("-");
			break;
		case InfoSeries.STATUS_CHECK:
			txtLegendaStatus.setText("Favorito");
			break;
		case InfoSeries.STATUS_INFO:
			txtLegendaStatus.setText("Favorito+Infomações");
			break;
		}
		
		return view;
	}

}
