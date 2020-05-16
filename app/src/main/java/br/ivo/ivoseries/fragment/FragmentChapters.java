package br.ivo.ivoseries.fragment;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import br.ivo.ivoseries.R;
import br.ivo.ivoseries.activity.ActivityExtractLegenda;
import br.ivo.ivoseries.activity.ActivitySearchLegenda;
import br.ivo.ivoseries.activity.ActivitySerieChapters;
import br.ivo.ivoseries.adapter.AdapterChapters;
import br.ivo.ivoseries.base.IvoBaseFragment;
import br.ivo.ivoseries.dialog.ChooseLegenda;
import br.ivo.ivoseries.dialog.ExtrairLegenda;
import br.ivo.seriessearch.database.DBCaps;
import br.ivo.seriessearch.database.InfoCaps;
import br.ivo.seriessearch.database.InfoSeries;
import br.ivo.seriessearch.legendastv.CTrataLegendas;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;

public class FragmentChapters extends IvoBaseFragment {

	public static final int NOTIFY_ROW_ADAPTER = 0;
	
	public static final int MENU_MAGNET_LINK = Menu.FIRST+1;
	public static final int MENU_PROCURAR_LEGENDA = Menu.FIRST+2;
	public static final int MENU_VER = Menu.FIRST+3;
	public static final int MENU_EXCLUIR_LEGENDA = Menu.FIRST+4;
	public static final int MENU_EXCLUIR_VIDEO = Menu.FIRST+5;
	public static final int MENU_PROCURAR_LEGENDA_EDIT = Menu.FIRST+6;
	public static final int MENU_EXTRAIR_LEGENDA = Menu.FIRST+7;
	public static final int MENU_MUDAR_VISTO = Menu.FIRST+8;

	private static Thread thread = null;
	private String sNomeCapitulo = null;
	private String sNomeOriginal = null;
	private static ProgressDialog progress;
	
	public FragmentChapters( LayoutInflater inflater, ViewGroup container ){
		super(inflater,container,R.layout.fragment_list);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		InfoCaps info = ((InfoCaps)getAdapter().getItem(position));
		((AdapterChapters) getAdapter()).setPositionSelected(position);		
		((AdapterChapters) getAdapter()).notifyDataSetChanged();
	}
	
	 @Override
	 public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		 menu.add(Menu.NONE, MENU_MUDAR_VISTO, Menu.NONE, "Visto");
		 menu.add(Menu.NONE, MENU_MAGNET_LINK, Menu.NONE, "MagnectLink");
		 //menu.add(Menu.NONE, MENU_PROCURAR_LEGENDA, Menu.NONE, "Procurar Legenda");
		 //menu.add(Menu.NONE, MENU_PROCURAR_LEGENDA_EDIT, Menu.NONE, "Procurar Legenda Com Nome");
		 //menu.add(Menu.NONE, MENU_EXTRAIR_LEGENDA, Menu.NONE, "Extrair Legenda");
		 //menu.add(Menu.NONE, MENU_EXCLUIR_LEGENDA, Menu.NONE, "Excluir legenda");
		 //menu.add(Menu.NONE, MENU_EXCLUIR_VIDEO, Menu.NONE, "Excluir Video");
		 return true;
	 }
	 
	 @Override
	 public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		 int iAtualItem = (Integer)mode.getTag();//menuInfo.position;
		 InfoCaps info = (InfoCaps)getAdapter().getItem(iAtualItem);
		 
		 switch (item.getItemId()) {
		 case MENU_MAGNET_LINK:
			 getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(info.getMagnetlink())));
			 break;
		 case MENU_VER:
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
			File f = new File(prefs.getString("localsave", "/sdcard/IvoSeriesSearch/")+info.getChapter().replaceAll(" ", ".")+info.getExt());
			
			if ( f.exists() )
			{
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
				getContext().startActivity(it);
			}
			break;
			
		case MENU_PROCURAR_LEGENDA:
			{
				Intent intent = new Intent( getContext(), ActivitySearchLegenda.class );
				intent.putExtra( "id", info.getId() ); 
				intent.putExtra("chapter", info.getChapter());
				intent.putExtra( "status", info.getStatus() );
				
				getContext().startActivity( intent );
			}
			break;
			
			/*if( sNomeCapitulo == null || !sNomeOriginal.contentEquals(info.getChapter().replaceAll(" ", ".")) )
				 sNomeCapitulo = info.getChapter().replaceAll(" ", ".");
			else if( sNomeCapitulo.indexOf(".") != -1 )
			{
				int iIndex = 0;
				for( iIndex = 0; iIndex < sNomeCapitulo.length()-7; iIndex++ ){
					if( sNomeCapitulo.charAt(iIndex) == '.' && sNomeCapitulo.charAt(iIndex+1) == 'S' 
							&& sNomeCapitulo.charAt(iIndex+4) == 'E' && sNomeCapitulo.charAt(iIndex+7) == '.' )
					{
						sNomeCapitulo = sNomeCapitulo.substring(0, iIndex + 8) + "HDTV";//sNomeCapitulo.substring(sNomeCapitulo.indexOf("HDTV"));
					}
				}
				//sNomeCapitulo = sNomeCapitulo.substring(0,sNomeCapitulo.lastIndexOf("."))+".HDTV";
			}
			
			sNomeOriginal = info.getChapter().replaceAll(" ", ".");
			
			procurarLegenda();
			break;*/
			
		case MENU_PROCURAR_LEGENDA_EDIT:
			
			if( sNomeCapitulo == null || !sNomeOriginal.contentEquals(info.getChapter().replaceAll(" ", ".")) )
				 sNomeCapitulo = info.getChapter().replaceAll(" ", ".");
			else if( sNomeCapitulo.indexOf(".") != -1 )
			{
				int iIndex = 0;
				for( iIndex = 0; iIndex < sNomeCapitulo.length()-7; iIndex++ ){
					if( sNomeCapitulo.charAt(iIndex) == '.' && sNomeCapitulo.charAt(iIndex+1) == 'S' 
							&& sNomeCapitulo.charAt(iIndex+4) == 'E' && sNomeCapitulo.charAt(iIndex+7) == '.' )
					{
						sNomeCapitulo = sNomeCapitulo.substring(0, iIndex + 8) + "HDTV";//sNomeCapitulo.substring(sNomeCapitulo.indexOf("HDTV"));
					}
				}
				//sNomeCapitulo = sNomeCapitulo.substring(0,sNomeCapitulo.lastIndexOf("."))+".HDTV";
			}
			
			sNomeOriginal = info.getChapter().replaceAll(" ", ".");
			
			new ChooseLegenda(getContext(), (Handler)this, sNomeOriginal, sNomeCapitulo).show();
			//procurarLegenda();
			break;
		case MENU_EXCLUIR_VIDEO:
			SharedPreferences prefs2 = PreferenceManager.getDefaultSharedPreferences(getContext());
			File file = new File(prefs2.getString("localsave", "/sdcard/IvoSeriesSearch/")+info.getChapter().replaceAll(" ", ".")+info.getExt());
			file.delete();
			sendEmptyMessage(NOTIFY_ROW_ADAPTER);
			
		case MENU_EXCLUIR_LEGENDA:
			{
				SharedPreferences prefs1 = PreferenceManager.getDefaultSharedPreferences(getContext());
			
				String sNome = prefs1.getString("localsave", "/sdcard/IvoSeriesSearch/")+info.getChapter().replaceAll(" ", ".")+".srt";
				
				File root = new File(sNome);
				root.delete();
				//deleteFile(sNome);
				sendEmptyMessage(NOTIFY_ROW_ADAPTER);
			}
			break;
			
		case MENU_EXTRAIR_LEGENDA:
			if( CTrataLegendas.existLegenda(info.getChapter().replaceAll(" ", ".")) )
			{
				Intent intent = new Intent( getContext(), ActivityExtractLegenda.class );
				intent.putExtra("chapter", info.getChapter() );
				getContext().startActivity(intent);
			}
			else{
				Toast.makeText(getContext(), R.string.rar_not_downloaded, Toast.LENGTH_LONG).show();
			}
			break;
		case MENU_MUDAR_VISTO:
			info.setStatus(info.getStatus() == InfoCaps.SERIE_NAO_VISTO ? InfoCaps.SERIE_VISTO : InfoCaps.SERIE_NAO_VISTO);
			DBCaps db = new DBCaps(getContext());
			db.atualizarItem(info);
			db.close();
			sendEmptyMessage(NOTIFY_ROW_ADAPTER);
			break;
		default:
			break;
		}
		 mode.finish();
		 return true;
	 }
	 
	 private void procurarLegenda(){
			progress = ProgressDialog.show(getContext(), sNomeCapitulo, getContext().getString(R.string.aguarde), false, true);
			if( thread == null ){
				thread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
													
						if( CTrataLegendas.downloadLegenda(sNomeCapitulo, sNomeOriginal) )
						{						
							CTrataLegendas.descompactaLegenda(sNomeOriginal, prefs.getString("localsave", "/sdcard/IvoSeriesSearch/"), sNomeOriginal );
						}
						
						sendEmptyMessage(NOTIFY_ROW_ADAPTER);
						thread = null;
						progress.dismiss();
					}
				});
				thread.start();
			}
		}
		
		private void atualizarSeries(){
			/*progress = ProgressDialog.show(this, getContext().getString(R.string.msg_sem_dados_series), getContext().getString(R.string.aguarde), false, true);
			if( thread == null ){
				thread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						//String sContent = CEztvUrls.getSearch(ID);
						//String sCookie = CConnectData.getCookie(CEztvUrls.sPrincipalUrl);
						if( info.getId() == -2 ){
							SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
							File fPath = new File(prefs.getString("localsave", "/sdcard/IvoSeriesSearch/"));
							//String [] list = fPath.list();
							File [] list = fPath.listFiles();
							ArrayList<InfoCaps> listCaps = new ArrayList<InfoCaps>();
							InfoCaps info;
							for( int nCount = 0; nCount < list.length; nCount++ ){
								if( list[nCount].getName().endsWith(".mp4") || list[nCount].getName().endsWith(".mkv") || list[nCount].getName().endsWith(".avi") ){
									info = new InfoCaps();
									info.setChapter(list[nCount].getName().substring(0, list[nCount].getName().lastIndexOf(".")));
									listCaps.add(info);
								}
							}
							rowadapter.setList(listCaps);
							notify.sendEmptyMessage(NOTIFY_ROW_ADAPTER);
						}else{
							if( info.getId() > -1 )
								CConnectData.downloadFileWithPost(CEztvUrls.sUrlSearch, null, CEztvUrls.getContentPostSeriesSearch(info.getId()), "/sdcard/IvoSeriesSearch/"+info.getId()+".htm");
							else
								CConnectData.downloadFile(CEztvUrls.sPrincipalUrl, "/sdcard/IvoSeriesSearch/"+info.getId()+".htm");
							
							String sContent = CConnectData.readFile("/sdcard/IvoSeriesSearch/"+info.getId()+".htm");
		
							if( sContent != null ){
								CTrataSearch trata = new CTrataSearch(sContent,info.getId());
								rowadapter.setList(trata.getList());
								if( info.getId() > -1 ){
									DBCaps db = new DBCaps(getApplicationContext());
									db.adicionar(trata.getList());
									db.close();
								}
								sendEmptyMessage(NOTIFY_ROW_ADAPTER);
							}
						}
						thread = null;
						progress.dismiss();
					}
				});
				thread.start();
			}*/
		}
		
	 public void handleMessage(Message msg ){
		 if(msg.what==NOTIFY_ROW_ADAPTER){
				((BaseAdapter) getAdapter()).notifyDataSetChanged();
			}else if ( msg.what == ChooseLegenda.EVENT_LEGENDA ){
				Bundle data = msg.getData();
				sNomeCapitulo = data.getString("data");
				procurarLegenda();
			}else if ( msg.what == ExtrairLegenda.EVENT_EXTRAIR_LEGENDA ){
				Bundle data = msg.getData();
				
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
				CTrataLegendas.descompactaLegenda(data.getString("data"), prefs.getString("localsave", "/sdcard/IvoSeriesSearch/"), sNomeOriginal );
			}
	 }
	 
	 private void menuVer(){
		 
	 }
}
