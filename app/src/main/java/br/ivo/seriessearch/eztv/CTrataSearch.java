package br.ivo.seriessearch.eztv;
import java.util.ArrayList;

import android.util.Log;
import br.ivo.seriessearch.database.*;

public class CTrataSearch {
	ArrayList<InfoCaps> list;
	
	public CTrataSearch(String sContent, int iID){
		int iIndex = sContent.indexOf("<tr name=\"hover\" class=\"forum_header_border\">");
		int iIndexTmp = 0;
		String sTemp;
		InfoCaps info;
		list = new ArrayList<InfoCaps>();
		
		while(iIndex!=-1){
			sTemp = sContent.substring(iIndex, sContent.indexOf("</tr>",iIndex+1));
			
			iIndexTmp = sTemp.indexOf("class=\"epinfo\">");

			try{
				info = new InfoCaps();
				info.setId(iID);
				if( iIndexTmp != -1){
					iIndexTmp += "class=\"epinfo\">".length();
					info.setChapter(sTemp.substring(iIndexTmp,sTemp.indexOf("</a>",iIndexTmp)));
				}
				
				iIndexTmp = sTemp.indexOf("magnet:",iIndexTmp+1);
				
				if( iIndexTmp != -1 ){
					info.setMagnetlink(sTemp.substring(iIndexTmp, sTemp.indexOf("\" class=\"magnet\"",iIndexTmp)));
				}
				
				iIndexTmp = sTemp.indexOf("<a href",iIndexTmp+1);
				
				if( iIndexTmp != -1 ){
					iIndexTmp = sTemp.indexOf("http",iIndexTmp+1);
					if( iIndexTmp != -1 ){
						info.setTorrentLink(sTemp.substring(iIndexTmp, sTemp.indexOf("\"",iIndexTmp)));
					}
				}
				
				/*iIndexTmp = sTemp.indexOf("http://extratorrent.cc/",iIndexTmp+1);
				
				if( iIndexTmp != -1 ){
					info.setTorrentLink(sTemp.substring(iIndexTmp, sTemp.indexOf("\"",iIndexTmp)));
				}else{
					iIndexTmp = sTemp.lastIndexOf("http://www.bt-chat.com");
					
					if( iIndexTmp != -1 ){
						info.setTorrentLink(sTemp.substring(iIndexTmp, sTemp.indexOf("\"",iIndexTmp)));
					}else{
						iIndexTmp = sTemp.lastIndexOf("http://rarbg.com");
						
						if( iIndexTmp != -1 ){
							info.setTorrentLink(sTemp.substring(iIndexTmp, sTemp.indexOf("\"",iIndexTmp)));
						}
					}
				}*/
				list.add(info);
			}catch(Exception e){
				Log.i("IvoLog", e.toString());
			}
			
			iIndex = sContent.indexOf("<tr name=\"hover\" class=\"forum_header_border\">", iIndex+1);
		}
	}

	public ArrayList<InfoCaps> getList() {
		return list;
	}
	
	
}
