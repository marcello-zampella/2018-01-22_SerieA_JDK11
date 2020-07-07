package it.polito.tdp.seriea.model;

public class PuntiSeason {
	
	int stagione;
	int punti;
	public PuntiSeason(int stagione, int punti) {
		super();
		this.stagione = stagione;
		this.punti = punti;
	}
	public int getStagione() {
		return stagione;
	}
	public void setStagione(int stagione) {
		this.stagione = stagione;
	}
	public int getPunti() {
		return punti;
	}
	public void setPunti(int punti) {
		this.punti = punti;
	}
	
	

}
