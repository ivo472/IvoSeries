package br.ivo.ivoseries.adapter;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import br.ivo.ivoseries.R;
import br.ivo.ivoseries.base.IvoBaseAdapter;
import br.ivo.seriessearch.database.InfoCaps;
import br.ivo.seriessearch.legendastv.CTrataLegendas;

public class AdapterChapters extends IvoBaseAdapter<InfoCaps> {

	SharedPreferences prefs = null;
	public int iPositionSelected = -1;
	public CNotify notify = new CNotify();
	public static final String [] extensions = new String [] {"mp4", "mkv", "avi", "webm"};
	public static final String [] status = new String [] {"Salvo", "Baixando", "Pronto"};
	private final String [] locals;
	
	public AdapterChapters(Context context) {
		super(context);
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		locals = new String [3];
		locals[0] = prefs.getString("localsave", "/sdcard/IvoSeriesSearch/");
		locals[1] = prefs.getString("localbaixando", "/sdcard/IvoSeriesSearch/");
		locals[2] = prefs.getString("localready", "/sdcard/IvoSeriesSearch/");
	}

	public void setPositionSelected( int iPosition ){
		if( iPosition == iPositionSelected )
			iPositionSelected = -1;
		else
			iPositionSelected = iPosition;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View view = null;
		String local = prefs.getString("localsave", "/sdcard/IvoSeriesSearch/");
		InfoCaps info = getItem(position);
		String cap = info.getChapter().replaceAll(" ", ".");
		final int pos = position;
		File f = new File(local+"/"+cap+".mp4");
		int iPosExt = 0;
		int iPosDir = 0;
		
		for( iPosDir = 0; iPosDir < locals.length; iPosDir++ ){
			for( iPosExt = 0; iPosExt < extensions.length; iPosExt++ ){
				f = new File(locals[iPosDir]+cap+"."+extensions[iPosExt]);
				if( f.exists() )
					break;
			}
			if( f.exists() )
				break;
		}
		
		if(iPositionSelected == position){
			view = inflater.inflate(R.layout.line_info_detail, null);
		}else if(iPosDir < 3){
			view = inflater.inflate(R.layout.line_info_botoes, null);
			ImageView btn = (ImageView)view.findViewById(R.id.imageButton);
			btn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.i("Ivo", "Click Position="+pos);
					Message msg = new Message();
					Bundle data = msg.getData();
					data.putInt("position", pos);
					notify.sendMessage(msg);						
				}
			});
		}
		else{
			view = inflater.inflate(R.layout.line_info, null);
		}
		
		
		TextView txtName = (TextView)view.findViewById(R.id.name);
		TextView txtStatus = (TextView)view.findViewById(R.id.status);
		TextView txtLegendaStatus = (TextView)view.findViewById(R.id.legendastatus);
		
		txtName.setText(info.getChapter());
		txtStatus.setText("");
		//txtStatus.setText( info.getStatus() == InfoCaps.SERIE_NAO_VISTO ? "Não Visto" : "Visto" );
		/*if( iPosExt < 4 )
			info.setExt( "."+extensions[iPosExt] );
		
		if( iPosDir < 3 && iPosExt < 4 ){
			txtStatus.setText( status[iPosDir]+" "+extensions[iPosExt].toUpperCase() );
		}*/
		
		/*if(f.exists()){
			info.setExt(".mp4");
			txtStatus.setText("MP4 file");
		}else{
			f = new File(local+"/"+cap+".mkv");
			if(f.exists()){
				info.setExt(".mkv");
				txtStatus.setText("MKV file");
			}else{
				f = new File(local+"/"+cap+".avi");
				if(f.exists()){
					info.setExt(".avi");
					txtStatus.setText("AVI file");
				}
			}
		}
		
		
		if( !f.exists() ){
			local = prefs.getString("localbaixando", "/sdcard/IvoSeriesSearch/");
			f = new File(local+"/"+cap+".mp4");
			if(f.exists()){
				info.setExt(".mp4");
				txtStatus.setText("Baixando MP4");
			}else{
				f = new File(local+"/"+cap+".mkv");
				if(f.exists()){
					info.setExt(".mkv");
					txtStatus.setText("Baixando MKV");
				}else{
					f = new File(local+"/"+cap+".avi");
					if(f.exists()){
						info.setExt(".avi");
						txtStatus.setText("Baixando AVI");
					}
				}
			}
		}
		
		if( !f.exists() ){
			local = prefs.getString("localready", "/sdcard/IvoSeriesSearch/");
			f = new File(local+"/"+cap+".mp4");
			if(f.exists()){
				info.setExt(".mp4");
				txtStatus.setText("Pronto MP4");
			}else{
				f = new File(local+"/"+cap+".mkv");
				if(f.exists()){
					info.setExt(".mkv");
					txtStatus.setText("Pronto MKV");
				}else{
					f = new File(local+"/"+cap+".avi");
					if(f.exists()){
						info.setExt(".avi");
						txtStatus.setText("Pronto AVI");
					}
				}
			}
		}
		/*if( iIndex == position ){
			ArrayList<String> list = CTrataLegendas.getListFilesRar(info.getChapter());
			String legendas = "";
			for(int nCount = 0; nCount < list.size(); nCount++){
				legendas += "\n" + list.get(nCount);
			}
			txtLegendaStatus.setText(legendas);
		}else{
			String legenda = CTrataLegendas.existLegenda(info.getChapter())?"Legenda Baixada":"Sem legenda";
			f = new File(local+"/"+cap+".srt");
			if(f.exists())
				legenda += " - SRT file";
			txtLegendaStatus.setText(legenda);
		}*/
		
		/*String legenda = CTrataLegendas.existLegenda(cap)?"Legenda Baixada":"Sem legenda";
		File fl = new File(prefs.getString("localsave", "/sdcard/IvoSeriesSearch/")+"/"+cap+".srt");
		if(fl.exists())
			legenda += " - SRT file";
		txtLegendaStatus.setText(legenda);*/
		
		if( iPositionSelected == position ){
			TextView txtArqVideo = (TextView)view.findViewById(R.id.nomearquivo);
			TextView txtArqLegenda = (TextView)view.findViewById(R.id.nomelegenda);
			TextView txtRar = (TextView)view.findViewById(R.id.rar);
			TextView txtMagnet = (TextView)view.findViewById(R.id.magnet);
			TextView txtTorrent = (TextView)view.findViewById(R.id.torrent);
			ArrayList<String> list = CTrataLegendas.getListFilesRar(info.getChapter().replaceAll(" ", "."));
			String legendas = "";
			for(int nCount = 0; nCount < list.size(); nCount++){
				legendas += "\n" + list.get(nCount);
			}
			txtRar.setText(legendas);
			
			txtMagnet.setText(info.getMagnetlink());
			txtTorrent.setText(info.getTorrentLink());
			//txtArqLegenda.setText(fl.exists() ? fl.getAbsolutePath():"");
			//txtArqVideo.setText((f.exists() ? f.getAbsolutePath() : ""));
		}	
		return view;
	}

	public class CNotify extends Handler {
		public void handleMessage(Message msg) {
			
			int position = msg.getData().getInt("position");
			
			String local = prefs.getString("localsave", "/sdcard/IvoSeriesSearch/");
			String cap = getItem(position).getChapter().replaceAll(" ", ".")+getItem(position).getExt();
			
			File f = new File(local+"/"+cap);
			Log.i("Ivo", "File "+f.getName()+" exist "+f.exists());
			if(f.exists()){
				String type = null;
			    try {
			        URL u = f.toURL();
			        URLConnection uc = null;
			        uc = u.openConnection();
			        type = uc.getContentType();
			    } catch (Exception e) {
			        
			    }
			    
				Intent it = new Intent();
				it.setAction(android.content.Intent.ACTION_VIEW);
				it.setDataAndType(Uri.fromFile(f), type);
				it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getContext().startActivity(it);
			}
		}
	}
}
