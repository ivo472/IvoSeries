package br.ivo.seriessearch.eztv;

import java.util.ArrayList;

import android.util.Log;
import br.ivo.seriessearch.database.InfoSeries;

public class CTrataListaSeries {
	private ArrayList<InfoSeries> list;
	
	public CTrataListaSeries(String sConteudo) {
		super();
		
		list = new ArrayList<InfoSeries>();
		InfoSeries info;
		if( sConteudo.indexOf("\"id\"")!= -1){
			int iIndex = sConteudo.indexOf("\"id\"");
			while(iIndex != -1){
				try{
					info = new InfoSeries();
					
					iIndex += 4;
					
					if( iIndex != -1 ){
						iIndex = sConteudo.indexOf("\"", iIndex+1);
					
						if( iIndex != -1 ){
							
								info.setId(Integer.parseInt(sConteudo.substring(iIndex+1, sConteudo.indexOf("\"", iIndex+1))));
							
						}
					}
					
					iIndex = sConteudo.indexOf("text", iIndex+1);
					if( iIndex != -1 ){
						iIndex = sConteudo.indexOf("\"", iIndex+6);
						
						if( iIndex != -1 ){
							info.setTitulo(sConteudo.substring(iIndex+1, sConteudo.indexOf("\"", iIndex+1)));
						}
					}
					list.add(info);
				}catch(Exception e){
					Log.i("IvoLog",e.toString());
				}
				iIndex = sConteudo.indexOf("\"id\"", iIndex+1);
			}
		}
	}
	
	public ArrayList<InfoSeries> getList(){
		return list;
	}
}
