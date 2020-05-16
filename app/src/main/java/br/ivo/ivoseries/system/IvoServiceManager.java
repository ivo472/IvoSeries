package br.ivo.ivoseries.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import br.ivo.geral.CConnectData;
import br.ivo.ivoseries.R;
import br.ivo.ivoseries.R.drawable;
import br.ivo.ivoseries.R.string;
import br.ivo.ivoseries.activity.MainActivity;
import br.ivo.seriessearch.Internet;
import br.ivo.seriessearch.database.DBCaps;
import br.ivo.seriessearch.database.DBSeries;
import br.ivo.seriessearch.database.InfoCaps;
import br.ivo.seriessearch.database.InfoSeries;
import br.ivo.seriessearch.eztv.CEztvUrls;
import br.ivo.seriessearch.eztv.CTrataSearch;
import br.ivo.seriessearch.legendastv.CTrataLegendas;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log; 
import android.widget.Toast;
import android.os.*;

public class IvoServiceManager extends Service implements Runnable
{
	private SharedPreferences prefs = null;
	public static Handler handle = null;
	public boolean bContinue = true;
	
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	} 

	@Override
	public void onCreate()
	{
		NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancelAll();
		super.onCreate();
		Toast.makeText(getApplicationContext(), "Service Started", Toast.LENGTH_LONG).show();
		bContinue = true;
		new Thread(this).start();
	}

	@Override
	public void onDestroy()
	{
		bContinue = false;
		super.onDestroy();		
	}
		
	@Override
	public void run() {
		while(bContinue){
			try{
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				long last_time = prefs.getLong("last_time", -1 ); 
				long now = System.currentTimeMillis();
				if( prefs.getBoolean("autodownload", false)){
					if( ( now > last_time || last_time == -1 ) 
							&& Internet.haveInternet(getApplicationContext(), Integer.parseInt(prefs.getString("connection", "3"))) ){
						//moveTorrentDownloadedFiles();
						checkSeries();
						if( prefs.getBoolean("check_subtitle", false) ){
							checkLegendas();
						}
						Editor editor = prefs.edit();
						editor.putLong("last_time", now+1800000); 
						editor.commit();
					}
				}
				Thread.sleep(5000);
			}catch (Exception e) {
				Log.i("Ivo", "Run Exception="+e.toString());
			}
		}
	}
	
	//==========================NOTIFICATION BAR=====================================
	public void showBarNotification(int imagem, boolean onGoing, int id, Intent intent, CharSequence titulo, CharSequence msg)
	{
		try
		{
			NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
			Notification n = new Notification(imagem, titulo, System.currentTimeMillis());
			PendingIntent p;
			
			p = PendingIntent.getActivity(this, id+1, intent, Intent.FLAG_ACTIVITY_CLEAR_TOP);
			n.setLatestEventInfo(this, titulo, msg, p); 

			if (onGoing)
				n.flags |= Notification.FLAG_ONGOING_EVENT;
			else
			{
				n.flags |= Notification.FLAG_AUTO_CANCEL;
				n.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
			}

			nm.notify(R.string.app_name + id + 1, n);

		}
		catch ( Exception e )
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	public void cancelarNotificacao(int id)
	{
		NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(R.string.app_name + id + 1);
	}
	
	private int iId = 1;
	private void checkSeries(){
		DBSeries dbSeries = new DBSeries(getApplicationContext());
		DBCaps   dbCaps = new DBCaps(getApplicationContext());
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		try{
			ArrayList<InfoSeries> list = dbSeries.getListPreference();
			InfoSeries info;
			Intent intent = new Intent(IvoServiceManager.this, MainActivity.class);
			for(int iIndex = 0; iIndex < list.size();iIndex++){
				info = list.get(iIndex);
				showBarNotification(R.drawable.ic_launcher, true, 0, intent, "Atualizando Serie", info.getTitulo() );
				CConnectData.downloadFile(CEztvUrls.sUrlSearch + "?" + CEztvUrls.getContentPostSeriesSearch(info.getId()), "/sdcard/IvoSeriesSearch/"+info.getId()+".htm");
				String sContent = CConnectData.readFile("/sdcard/IvoSeriesSearch/"+info.getId()+".htm");
				
				if( sContent != null ){
					CTrataSearch trata = new CTrataSearch(sContent,info.getId());
					ArrayList<InfoCaps> list1 = trata.getList();
					ArrayList<InfoCaps> list2 = dbCaps.getListByEstado(info.getId());

					if( list2.size() == 0 ){
						dbCaps.adicionar(list1);
					}else{
						for(int nCount=0;nCount<list1.size();nCount++){
							for( int nCount2=0;nCount2<list2.size();nCount2++){
								if(list1.get(nCount).getMagnetlink().contentEquals(list2.get(nCount2).getMagnetlink())){
									list1.remove(nCount);
									list2.remove(nCount2);
									nCount--;
									break;
								}
							}								
						}
						int downloadMaxTorrent = 3; 
						while(list1.size()>0 ){
							InfoCaps down = list1.get(0);
							boolean b720p = false;
							if( prefs.getBoolean("720p", false)){
								if( down.getChapter().indexOf("720p") != -1 || down.getChapter().indexOf("1080p") != -1 ){
									b720p = true;
								}
							}
							
							if( dbCaps.adicionar(down) ){
								if( !b720p && downloadMaxTorrent > 0){
									CConnectData.downloadFile(down.getTorrentLink(), prefs.getString("localincomming", "/sdcard/IvoSeriesSearch/")+down.getChapter().replaceAll(" ", ".")+".torrent");
									iId++;
									showBarNotification(R.drawable.ic_launcher, false, iId, intent, "Serie Adicionada - "+(b720p?"720p":"HDTV"), down.getChapter());
								}
							}
							downloadMaxTorrent--;
							list1.remove(0);
						}
					}
				}
			}
		}catch (Exception e) {
			
		}
		cancelarNotificacao(0);
		dbCaps.close();
		dbSeries.close();
	}
	
	private void checkLegendas(){
		try{
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			File fPath = new File(prefs.getString("localsave", "/sdcard/IvoSeriesSearch/"));
			File [] list = fPath.listFiles();
			String sNomeCapitulo;
			Intent intent = new Intent(IvoServiceManager.this, MainActivity.class);
			String local = prefs.getString("localsave", "/sdcard/IvoSeriesSearch/");
			
			for( int nCount = 0; nCount < list.length; nCount++ ){
				showBarNotification(R.drawable.ic_launcher, true, 0, intent, "Procurando Legendas", ""+nCount+" de "+list.length);
				if( list[nCount].getName().endsWith(".mp4") || list[nCount].getName().endsWith(".mkv") || list[nCount].getName().endsWith(".avi") ){
					sNomeCapitulo = list[nCount].getName().substring(0, list[nCount].getName().lastIndexOf(".")).replaceAll(" ", ".");
					
					File fl = new File(local+"/"+sNomeCapitulo+".srt");
					if(!fl.exists()){
						
						if( !CTrataLegendas.existLegenda(sNomeCapitulo) ){
							showBarNotification(R.drawable.ic_launcher, true, 0, intent, "Procurando Legendas", sNomeCapitulo);
							CTrataLegendas.downloadLegenda(sNomeCapitulo, sNomeCapitulo);
						}

						CTrataLegendas.descompactaLegenda(sNomeCapitulo, prefs.getString("localsave", "/sdcard/IvoSeriesSearch/"),sNomeCapitulo);
						
						fl = new File(local+"/"+sNomeCapitulo.replaceAll(" ", ".")+".srt");
						if(fl.exists()){
							showBarNotification(R.drawable.ic_launcher, false, iId, intent, "Legenda", sNomeCapitulo);
							iId++;
						}
					}				
				}
			}
		}catch (Exception e) {
		}
		cancelarNotificacao(0);
	}
	
	private void moveTorrentDownloadedFiles(){
		try{
			Intent intent = new Intent(IvoServiceManager.this, MainActivity.class);
			
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			File fPath = new File(prefs.getString("localready", "/sdcard/IvoSeriesSearch/"));
			String sPathSave = prefs.getString("localsave", "/sdcard/IvoSeriesSearch/");
			File [] list = fPath.listFiles();
			for(File file : list){
				if( file.isFile() ){
					showBarNotification(R.drawable.ic_launcher, true, 0, intent, getString(R.string.moving_files), file.getName());
					FileChannel in = new FileInputStream(file).getChannel();
					FileChannel out = new FileOutputStream(new File(sPathSave+file.getName())).getChannel();
					in.transferTo(0, in.size(), out);		
					file.delete();
				}else if( file.isDirectory() ){
					Log.i("Ivo", "isDirectory");
					String sFileName = "";
					if( -1 == file.getName().indexOf("[") )
						sFileName = file.getName();
					else
						sFileName = file.getName().substring(0, file.getName().indexOf("["));
					File [] fileList = file.listFiles();
					//Log.i("Ivo", "fileList="+fileList.toString());
					for( File fileDir : fileList ){
						if( fileDir.getName().endsWith("mp4") || fileDir.getName().endsWith("avi") 
								|| fileDir.getName().endsWith("mkv") || fileDir.getName().endsWith("srt") ){
							showBarNotification(R.drawable.ic_launcher, true, 0, intent, getString(R.string.moving_files), sFileName);
							Log.i("Ivo", "file="+sFileName);
							Log.i("Ivo", "fileDir length="+fileDir.getName().length());
							String extension = fileDir.getName().substring(fileDir.getName().length() - 4 );	
							Log.i("Ivo", "extesion="+extension);
							FileChannel in = new FileInputStream(fileDir).getChannel();
							File fOut = new File(sPathSave+sFileName+extension);
							FileChannel out = new FileOutputStream(fOut).getChannel();
							Log.i("Ivo", "In ="+fileDir.getPath()+fileDir.getName());
							Log.i("Ivo", "In size="+in.size());
							Log.i("Ivo", "Out="+sPathSave+sFileName+"."+extension);
							//in.transferTo(0, in.size(), out);
														
							for( long nCount = 0; nCount < in.size(); ){
								nCount += in.transferTo(nCount, 60*1024*1024, out);
								Log.i("Ivo", "Count="+nCount+";Size="+in.size() );
								Thread.sleep(10);
							}//*/
							fileDir.delete();
							file.delete();
						}
					}
				}
			}
		}catch(Exception e){
			Log.i("Ivo", "moveTorrentDownloadedFiles Exception="+e.toString());
		}
	}
}
