package br.ivo.seriessearch.eztv;

import br.ivo.geral.CConnectData;

public class CEztvUrls {
	public static final String sPrincipalUrl = "https://eztv.ag/";
	public static final String sUrlSearch = "https://eztv.ag/search/";
	
	public static String getContentPostSeriesSearch(int id){
		return "q1=&q2="+id+"&search=Search";
	}
	
	public static String getSearch(int id){
		String sContent = "";
		
		sContent = CConnectData.getHtmlWithPost(sUrlSearch, null, getContentPostSeriesSearch(id));
		
		return sContent;
	}
}
