package br.ivo.seriessearch.database;

import java.util.ArrayList;

import br.ivo.geral.DBSQLiteHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBCaps { 
	private DBSQLiteHelper dbhelper;
	private SQLiteDatabase db;
	private String table = "capsinfo";
	
	public static final String[] SCRIPT_CREATE = new String[]{ 
						"create table capsinfo ( " + 
						"id integer, chapter text, status integer, legendastatus integer, magnetlink text, info text, torrentlink text, primary key(id, chapter) );"
						};
	
	public DBCaps( Context context ){
		dbhelper = new DBSQLiteHelper( context, "downloads", 1, SCRIPT_CREATE, "DROP TABLE IF EXISTS capsinfo" );
        db = dbhelper.getWritableDatabase();
	}
	
	public void close(){ 
		db.close();
		dbhelper.close();
	}
	
	public int count(int id){
		int iCount = 0;
		try{
			Cursor c = db.query( table, new String[]{ "id" }, "id=?", new String [] {""+id}, null, null, null );
	        c.moveToFirst();
	        iCount = c.getCount();	        	
		}catch( Exception e ){}
		
		return iCount;
	}
	
	public boolean adicionar( InfoCaps item ){
		boolean result = false;
		try{
			ContentValues cont = new ContentValues();
		    
	    	cont.put( "id", item.getId() );
	    	cont.put( "chapter", item.getChapter() );
	    	cont.put( "status", item.getStatus()  );
	    	cont.put( "legendastatus", item.getLegendaStatus()  );
	    	cont.put( "magnetlink", item.getMagnetlink()  );
	    	cont.put( "info", item.getInfo()  );
	    	cont.put( "torrentlink", item.getTorrentLink()  );
	    	
	    	result = db.insert( table, null, cont ) == -1 ? false : true;
		}catch(Exception e){
			
		}
    	
    	return result;
	}
	
	public void adicionar( ArrayList<InfoCaps> itens ){
		ContentValues cont ;
		InfoCaps item;
		for(int nCount = 0; nCount < itens.size();nCount++){
			try{
				item = itens.get(nCount);
				cont = new ContentValues();
			    
		    	cont.put( "id", item.getId() );
		    	cont.put( "chapter", item.getChapter() );
		    	cont.put( "status", item.getStatus()  );
		    	cont.put( "legendastatus", item.getLegendaStatus()  );
		    	cont.put( "magnetlink", item.getMagnetlink()  );
		    	cont.put( "info", item.getInfo()  );
		    	cont.put( "torrentlink", item.getTorrentLink()  );
		    	
		    	db.insert( table, null, cont );
			}catch(Exception e){
				
			}
		}
	}
	
	public ArrayList<InfoCaps> getListByEstado( int id ){
		InfoCaps info ;
		ArrayList<InfoCaps> list = new ArrayList<InfoCaps>();
		try{
			Cursor c = db.query( table, new String[]{ "id", "chapter", "status", "legendastatus", "magnetlink", "info", "torrentlink" }, "id=?", new String [] {""+id}, null, null, null );
			c.moveToLast();
			for( int nCount = 0; nCount < c.getCount(); nCount ++ ){
				info = new InfoCaps();
	        	
	        	info.setId(c.getInt(0));
	        	info.setChapter(c.getString(1));
	        	info.setStatus(c.getInt(2));
	        	info.setLegendaStatus(c.getInt(3));
	        	info.setMagnetlink(c.getString(4));
	        	info.setInfo(c.getString(5));
	        	info.setTorrentLink(c.getString(6));
	        	
	        	list.add(info);
	        	
	        	c.moveToPrevious();
			}
        	
		}catch(Exception e){			
		}
        
		return list;
	}
	
	public void atualizarItem(InfoCaps item) {
		try {
			ContentValues cont = new ContentValues();
			cont.put( "id", item.getId() );
	    	cont.put( "chapter", item.getChapter() );
	    	cont.put( "status", item.getStatus()  );
	    	cont.put( "legendastatus", item.getLegendaStatus()  );
	    	cont.put( "magnetlink", item.getMagnetlink()  );
	    	cont.put( "info", item.getInfo()  );
	    	cont.put( "torrentlink", item.getTorrentLink()  );
			db.update( table, cont, "chapter=?", new String[] { item.getChapter()});
		} catch (Exception e) {
		}
	}
	
	/*public void adicionar( InfoCapitulo item ){
		ContentValues cont = new ContentValues();
	    
    	cont.put( "nomecapitulo", item.getNomeCapitulo() );
    	cont.put( "html", item.getHtml() );
    	cont.put( "estado", item.getEstado() );
    	cont.put( "manga", item.getNomeManga() );
    	cont.put( "site", item.getSite() );
    	
    	db.insert( "capinfo", null, cont );
	}
	
	public ArrayList<InfoCapitulo> getListByEstado( int estado ){
		InfoCapitulo info ;
		ArrayList<InfoCapitulo> list = new ArrayList<InfoCapitulo>();
		try{
			Cursor c = db.query( "capinfo", new String[]{ "nomecapitulo", "html", "estado", "manga", "site" }, estado == -1 ? null:"estado=?", estado == -1 ? null:new String [] {""+estado}, null, null, null );
			c.moveToFirst();
			for( int nCount = 0; nCount < c.getCount(); nCount ++ ){
				info = new InfoCapitulo();
	        	
	        	info.setNomeCapitulo(c.getString(0));
	        	info.setHtml(c.getString(1));
	        	info.setEstado(c.getInt(2));
	        	info.setNomeManga(c.getString(3));
	        	info.setSite(c.getInt(4));
	        	
	        	list.add(info);
	        	
	        	c.moveToNext();
			}
        	
		}catch(Exception e){			
		}
        
		return list;
	}
	
	public ArrayList<InfoCapitulo> getListByNomeManga( String sNomeManga, int Site ){
		InfoCapitulo info ;
		ArrayList<InfoCapitulo> list = new ArrayList<InfoCapitulo>();
		try{
			Cursor c = db.query( "capinfo", new String[]{ "nomecapitulo", "html", "estado", "manga", "site" }, "manga=? and site=?", new String [] {sNomeManga,""+Site}, null, null, null );
			c.moveToFirst();
			for( int nCount = 0; nCount < c.getCount(); nCount ++ ){
				info = new InfoCapitulo();
	        	
	        	info.setNomeCapitulo(c.getString(0));
	        	info.setHtml(c.getString(1));
	        	info.setEstado(c.getInt(2));
	        	info.setNomeManga(c.getString(3));
	        	info.setSite(c.getInt(4));
	        	
	        	list.add(info);
	        	
	        	c.moveToNext();
			}
        	
		}catch(Exception e){			
		}
        
		return list;
	}
	
	public ArrayList<InfoCapitulo> getListDowloading( ){
		InfoCapitulo info ;
		ArrayList<InfoCapitulo> list = new ArrayList<InfoCapitulo>();
		try{
			Cursor c = db.query( "capinfo", new String[]{ "nomecapitulo", "html", "estado", "manga", "site" }, "estado=? or estado=? or estado=? or estado=?", new String [] {""+InfoCapitulo.STATE_BAIXANDO, ""+InfoCapitulo.STATE_BAIXANDO_NOVO, ""+InfoCapitulo.STATE_BAIXANDO_AGORA_NOVO, ""+InfoCapitulo.STATE_BAIXANDO_AGORA}, null, null, null );
			c.moveToFirst();
			do{
				info = new InfoCapitulo();
	        	
	        	info.setNomeCapitulo(c.getString(0));
	        	info.setHtml(c.getString(1));
	        	info.setEstado(c.getInt(2));
	        	info.setNomeManga(c.getString(3));
	        	info.setSite(c.getInt(4));
	        	
	        	list.add(info);
	        }while(c.moveToNext());
		}catch(Exception e){			
		}
        
		return list;
	}
	
	public InfoCapitulo getFirstByEstado( int estado ){
		InfoCapitulo info = null;
		
		try{			
			Cursor c = db.query( "capinfo", new String[]{ "nomecapitulo", "html", "estado", "manga", "site" }, "estado=?", new String [] {""+estado}, null, null, null );
			c.moveToFirst();
			if( c.getCount()>0 ){
				info = new InfoCapitulo();
	        	info.setNomeCapitulo(c.getString(0));
	        	info.setHtml(c.getString(1));
	        	info.setEstado(c.getInt(2));
	        	info.setNomeManga(c.getString(3));
	        	info.setSite(c.getInt(4));
			}
		}catch(Exception e){			
		}
        
		return info;
	}
	
	public InfoCapitulo getFirst( String sManga, int site ){
		InfoCapitulo info = null;

		try{			
			Cursor c = db.query( "capinfo", new String[]{ "nomecapitulo", "html", "estado", "manga", "site" }, "nomecapitulo=? and site=?", new String [] {sManga,""+site}, null, null, null );
			c.moveToFirst();
			if( c.getCount()>0 ){
				info = new InfoCapitulo();
	        	info.setNomeCapitulo(c.getString(0));
	        	info.setHtml(c.getString(1));
	        	info.setEstado(c.getInt(2));
	        	info.setNomeManga(c.getString(3));
	        	info.setSite(c.getInt(4));
			}
		}catch(Exception e){			
		}

		return info;
	}
	
	public InfoCapitulo getItemByHtml( String sHtml ){
		InfoCapitulo info = null;

		try{			
			Cursor c = db.query( "capinfo", new String[]{ "nomecapitulo", "html", "estado", "manga", "site" }, "html=?", new String [] {sHtml}, null, null, null );
			c.moveToFirst();
			if( c.getCount()>0 ){
				info = new InfoCapitulo();
	        	info.setNomeCapitulo(c.getString(0));
	        	info.setHtml(c.getString(1));
	        	info.setEstado(c.getInt(2));
	        	info.setNomeManga(c.getString(3));
	        	info.setSite(c.getInt(4));
			}
		}catch(Exception e){			
		}

		return info;
	}
	
	public InfoCapitulo getByName( String sNomeCapitulo, int site ){
		InfoCapitulo info = null;
		
		try{			
			Cursor c = db.query( "capinfo", new String[]{ "nomecapitulo", "html", "estado", "manga", "site" }, "nomecapitulo=? and site=?", new String [] {sNomeCapitulo,""+site}, null, null, null );
			c.moveToFirst();
			if( c.getCount()>0 ){
				info = new InfoCapitulo();
	        	info.setNomeCapitulo(c.getString(0));
	        	info.setHtml(c.getString(1));
	        	info.setEstado(c.getInt(2));
	        	info.setNomeManga(c.getString(3));
	        	info.setSite(c.getInt(4));
			}
		}catch(Exception e){			
		}
        
		return info;
	}
	
	public boolean existMangaNovo( String sNomeManga, int site ){
		boolean bExist = false;
		try{			
			Cursor c = db.query( "capinfo", new String[]{ "nomecapitulo" }, "manga=? and site=? and (estado=? or estado=?)", new String [] {sNomeManga,""+site,""+InfoCapitulo.STATE_NOVO,""+InfoCapitulo.STATE_OK_NOVO}, null, null, null );
			if( c.getCount()>0 ){
				bExist = true;
			}
		}catch(Exception e){			
		}
        
		return bExist;
	}
	public void AtualizarItem(InfoCapitulo item) {
		try {
			ContentValues cont = new ContentValues();
			cont.put( "html", item.getHtml() );
	    	cont.put( "estado", item.getEstado() );
	    	cont.put( "manga", item.getNomeManga() );
	    	cont.put( "site", item.getSite() );
			db.update("capinfo", cont, "nomecapitulo=? and site=?", new String[] { item.getNomeCapitulo(), ""+item.getSite() });
		} catch (Exception e) {
		}
	}
	
	public void AtualizarTodosEstado(String sNomeManga,String sComparacao,int estado, int novoestado) {
		try {
			db.execSQL("update capinfo set estado="+novoestado+" where manga='"+sNomeManga+"' and estado "+sComparacao+estado);
		} catch (Exception e) {
		}
	}
	
	public void ExcluirItem(InfoCapitulo item) {
		try {
			db.delete("capinfo", "nomecapitulo=? and site=?", new String[] { item.getNomeCapitulo(), ""+item.getSite() });
		} catch (Exception e) {
		}
	}
	
	public void ExcluirItens(String sNomeManga, int site, int estado) {
		try {
			String [] sString = ( estado == -1 ? new String[] { sNomeManga, ""+site } : new String[] { sNomeManga, ""+site, ""+estado } );
			db.delete("capinfo", "manga=? and site=?" + (estado == -1 ? "" : " and estado=?"), sString );
		} catch (Exception e) {
			Log.i("IvoLog", e.toString());
		}
	}*/
}
