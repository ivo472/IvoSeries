package br.ivo.seriessearch.database;

public class InfoCaps {
	private int Id = 0;
	private String Chapter = "";
	private int Status = 0;
	private int LegendaStatus = 0;
	private String magnetlink = "";
	private String info = "";
	private String torrentLink;
	private String ext = null;
	public static final int SERIE_NAO_VISTO = 0;
	public static final int SERIE_VISTO = 1;
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getChapter() {
		return Chapter;
	}
	public void setChapter(String chapter) {
		Chapter = chapter;
	}
	public int getStatus() {
		return Status;
	}
	public void setStatus(int status) {
		Status = status;
	}
	public int getLegendaStatus() {
		return LegendaStatus;
	}
	public void setLegendaStatus(int legendaStatus) {
		LegendaStatus = legendaStatus;
	}
	public String getMagnetlink() {
		return magnetlink;
	}
	public void setMagnetlink(String magnetlink) {
		this.magnetlink = magnetlink;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getTorrentLink() {
		return torrentLink;
	}
	public void setTorrentLink(String torrentLink) {
		this.torrentLink = torrentLink;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
}
