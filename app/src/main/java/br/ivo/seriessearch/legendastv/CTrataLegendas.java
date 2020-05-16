package br.ivo.seriessearch.legendastv;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.rarfile.FileHeader;
import android.os.Environment;
import android.util.Log;
import br.ivo.geral.CConnectData;
import br.ivo.seriessearch.database.InfoLegendas;

public class CTrataLegendas {
	public static String sUrlSite = "http://legendas.tv/";
	public static String sUrlBusca = "http://legendas.tv/util/carrega_legendas_busca/";
	public static String sUrlBuscaIdioma = "/1";
	
	public static ArrayList<InfoLegendas> getLegendasList(String sNomeCapitulo){
		ArrayList<InfoLegendas> list = new ArrayList<InfoLegendas>();
		try{
			CConnectData.downloadFileSendCookie(sUrlBusca+sNomeCapitulo.replaceAll(" ", "%20")+sUrlBuscaIdioma, "shop2=1; shop2Cap=1; PHPSESSID=83drkl8273vdrgh3j2fe6lrjn6; au=2349612---1f5224679470448ba9af0a5c1f0a0966f831ef3d; popup-likebox=yes", Environment.getExternalStorageDirectory().getPath()+"/IvoSeriesSearch/search.htm");
			String sContent = CConnectData.readFile(Environment.getExternalStorageDirectory().getPath()+"/IvoSeriesSearch/search.htm");
			
			int iIndex = sContent.indexOf("/download/");
			
			while( iIndex != -1 ){
				InfoLegendas legenda = new InfoLegendas();
				
				legenda.setUrl(sUrlSite+sContent.substring(iIndex, sContent.indexOf("\"", iIndex)));
				iIndex = sContent.indexOf(">", iIndex)+1;
				legenda.setNome(sContent.substring(iIndex, sContent.indexOf("<", iIndex)));
				list.add(legenda);
				iIndex = sContent.indexOf("/download/", iIndex);
			}
		}catch (Exception e) {
			Log.i("IvoLog", e.toString());
		}
		
		return list;
	}
	
	public static boolean getLegendaByURL(String sUrl, String sNomeSalvar){
		try{
			CConnectData.downloadFileSendCookie(sUrl, "shop2=1; shop2Cap=1; PHPSESSID=83drkl8273vdrgh3j2fe6lrjn6; au=2349612---1f5224679470448ba9af0a5c1f0a0966f831ef3d; popup-likebox=yes", "/sdcard/IvoSeriesSearch/search1.htm");
			String sContent = CConnectData.readFile("/sdcard/IvoSeriesSearch/search1.htm");	
			
			int iIndex = sContent.indexOf("/downloadarquivo/");
			
			if( iIndex != -1 ){
				sUrl = sContent.substring(iIndex, sContent.indexOf("',", iIndex));
				
				CConnectData.downloadFileSendCookie(sUrlSite+sUrl, "shop2=1; shop2Cap=1; PHPSESSID=83drkl8273vdrgh3j2fe6lrjn6; au=2349612---1f5224679470448ba9af0a5c1f0a0966f831ef3d; popup-likebox=yes", "/sdcard/IvoSeriesSearch/"+sNomeSalvar+".rar");
				
				return existLegenda(sNomeSalvar);
			}
		}catch (Exception e) {
			Log.i("IvoLog", e.toString());
		}
		
		return false;
	}
	
	public static boolean downloadLegenda(String sNomeCapitulo, String sNomeSalvar){
		try{
			CConnectData.downloadFileSendCookie(sUrlBusca+sNomeCapitulo.replaceAll(" ", "%20")+sUrlBuscaIdioma, "shop2=1; shop2Cap=1; PHPSESSID=83drkl8273vdrgh3j2fe6lrjn6; au=2349612---1f5224679470448ba9af0a5c1f0a0966f831ef3d; popup-likebox=yes", "/sdcard/IvoSeriesSearch/search.htm");
			String sContent = CConnectData.readFile("/sdcard/IvoSeriesSearch/search.htm");
			
			int iIndex = sContent.indexOf("/download/");
			
			if( iIndex != -1 ){
				String sUrl = sContent.substring(iIndex, sContent.indexOf("\"", iIndex));
				
				CConnectData.downloadFileSendCookie(sUrlSite+sUrl, "shop2=1; shop2Cap=1; PHPSESSID=83drkl8273vdrgh3j2fe6lrjn6; au=2349612---1f5224679470448ba9af0a5c1f0a0966f831ef3d; popup-likebox=yes", "/sdcard/IvoSeriesSearch/search1.htm");
				sContent = CConnectData.readFile("/sdcard/IvoSeriesSearch/search1.htm");
			}	
			
			iIndex = sContent.indexOf("/downloadarquivo/");
			
			if( iIndex != -1 ){
				String sUrl = sContent.substring(iIndex, sContent.indexOf("',", iIndex));
				
				CConnectData.downloadFileSendCookie(sUrlSite+sUrl, "shop2=1; shop2Cap=1; PHPSESSID=83drkl8273vdrgh3j2fe6lrjn6; au=2349612---1f5224679470448ba9af0a5c1f0a0966f831ef3d; popup-likebox=yes", "/sdcard/IvoSeriesSearch/"+sNomeSalvar+".rar");
				
				return existLegenda(sNomeSalvar);
			}
		}catch (Exception e) {
			Log.i("IvoLog", e.toString());
		}
		
		return false;
	}
	
	public static boolean existLegenda(String sNomeLegenda){		
		File root = new File( "/sdcard/IvoSeriesSearch/"+sNomeLegenda+".rar" );
		
		return root.exists();
	}

	public static boolean descompactaLegenda(String sNameSerie, String local_save, String sNomeLegenda ){
		Archive a = null;
		File f = new File("/sdcard/IvoSeriesSearch/"+sNomeLegenda+".rar");
		if(!f.exists())
			return false;
		
		try{
			a = new Archive(f, null, false);  //extract mode
		}catch(Exception e){
			
		}
		//String sNameSerie = sNomeLegenda.replaceAll(" ", ".");
		
		if(a!=null){
			boolean bFound = false;
			int iTentativas = 0;
			ArrayList<FileHeader> list = new ArrayList<FileHeader>();
			a.getMainHeader().print();
			FileHeader fh = a.nextFileHeader();
			
			while (fh != null) {
				if( fh.getFileNameString().endsWith(".srt"))
					list.add(fh);
				fh = a.nextFileHeader();
			}
			if( list.size()==1){
				try {
					File out = new File(local_save+ "/"+ sNomeLegenda +".srt");
					FileOutputStream os = new FileOutputStream(out);
					a.extractFile(list.get(0), os);
					os.close();
				}catch(Exception e){
					
				}
			}else{
				while(!bFound&&iTentativas<3){
					if(iTentativas==1){
						sNameSerie = sNameSerie.substring(0,sNameSerie.lastIndexOf("-"));
					}
					if(iTentativas==2){
						sNameSerie = sNameSerie.substring(0,sNameSerie.lastIndexOf("."));
					}
					for(int nCount = 0; nCount < list.size()&&!bFound; nCount++) {
						try {
							if(list.get(nCount).getFileNameString().indexOf(sNameSerie)!=-1&&list.get(nCount).getFileNameString().endsWith(".srt")){
								sNameSerie = sNomeLegenda.replaceAll(" ", ".");
								File out = new File(local_save+ "/"+ sNomeLegenda +".srt");
								FileOutputStream os = new FileOutputStream(out);
								a.extractFile(list.get(nCount), os);
								os.close();
								bFound = true;
							}
						} catch (Exception e) {
							//System.out.println(""+e.toString());
						}
						
					}
					iTentativas++;
				}
			}
		}
		return false;
	}
	
	public static ArrayList<String> getListFilesRar(String sNomeLegenda){
		ArrayList<String> list = new ArrayList<String>();
		Archive a = null;
		File f = new File("/sdcard/IvoSeriesSearch/"+sNomeLegenda+".rar");
		if(!f.exists())
			return list;
		
		try{
			a = new Archive(f, null, false);  //extract mode
		}catch(Exception e){
			
		}
		String sNameSerie = sNomeLegenda.replaceAll(" ", ".");
		
		if(a!=null){
			a.getMainHeader().print();
			FileHeader fh = a.nextFileHeader();
			while (fh != null) {
				try {
					list.add(fh.getFileNameString());
				} catch (Exception e) {
					System.out.println(""+e.toString());
				}
				fh = a.nextFileHeader();
			}
		}
		return list;
	}
}
