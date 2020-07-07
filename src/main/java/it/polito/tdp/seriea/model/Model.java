package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	private SerieADAO dao;
	private ArrayList<Team> squadre;
	private SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> grafo;

	
	public Model() {
		dao= new SerieADAO();

	}

	public ArrayList<Team> getAllSquadre() {
		squadre= dao.listTeams();
		return squadre;
	}
	
	private LinkedList<Integer> anni;
	private TreeMap<Integer, Integer> risultati;

	public TreeMap<Integer, Integer> getPuntiStagioni(Team t) {
		ArrayList<Match>partite=dao.getPuntiTotali(t);
		System.out.println(partite);
		risultati= new TreeMap<Integer, Integer>();
		anni= dao.getAnni(t);
		for (int i=0;i<anni.size();i++) {
			risultati.put(anni.get(i), 0);
		}
		for (Match m: partite) {
			if(m.getHomeTeam().equals(t) && m.getFtr().equals("H")) {
				risultati.put(m.getSeason().getSeason(), risultati.get(m.getSeason().getSeason())+3);
			} if(m.getAwayTeam().equals(t) && m.getFtr().equals("A")) {
				risultati.put(m.getSeason().getSeason(), risultati.get(m.getSeason().getSeason())+3);
			} if(m.getFtr().equals("D")) {
				risultati.put(m.getSeason().getSeason(), risultati.get(m.getSeason().getSeason())+1);
			}
			
		}
		return risultati;
	}

	public Vincente getAnnata(Team squadra) {
		grafo=new SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, anni);
		for(Integer a1: anni){
			for(Integer a2: anni) {
				if( !(a1==a2 || grafo.containsEdge(a1, a2) || grafo.containsEdge(a2,a1))) {
				int risultato1= risultati.get(a1);
				int risultato2= risultati.get(a2);
				if(risultato1<risultato2) {
					Graphs.addEdge(grafo, a1, a2, risultato2-risultato1);
				}
				if(risultato1>risultato2) {
					Graphs.addEdge(grafo, a2, a1, risultato1-risultato2);
				}
				}
			}
		}
		
		HashMap<Integer, Integer> mappa=new HashMap<Integer, Integer>();
		int max=0;
		int annoVincente=0;
		
		for (int i=0;i<anni.size();i++) {
			mappa.put(anni.get(i), 0);
		}
		
		for (Integer a: anni) {
			int uscenti=0;
			for(DefaultWeightedEdge e:grafo.outgoingEdgesOf(a)) {
				uscenti+=grafo.getEdgeWeight(e);
			}
			int entranti=0;
			for(DefaultWeightedEdge e:grafo.incomingEdgesOf(a)) {
				entranti+=grafo.getEdgeWeight(e);
			}
			int totale= entranti-uscenti;
			mappa.put(a, totale);
			if(totale>=max) {
				annoVincente=a;
				max=totale;
			}
		}
		
		return new Vincente(annoVincente,max);
		
	}
	
	int max;
	LinkedList<Integer> cammino;
	LinkedList<Integer> massimo;

	public LinkedList<Integer> getCamminoVirtuoso(Team value) {
		int livello=0; //livello e' quanto sto scendendo nella lista
		int fine=2017;
		cammino=new LinkedList<Integer>();
		massimo=new LinkedList<Integer>();
		max=0;
		espandi(livello, anni.get(0),fine);
		return massimo;
		
	}

	private void espandi(int livello, Integer anno, int fine) { //anno sarebbe l'anno in questione
			this.cammino.add(anno);
			System.out.println(anno);
		if(anno>=fine) {	//condizione di terminazione, termina tutto
			termina(cammino);
			return;

		}
		boolean flag=true;
		int k=0;
		DefaultWeightedEdge arco = null;
		while(flag) {
			k++;
			if(anno+k>fine) {
				anno+=k;
				termina(cammino);
				return;
			}
			arco=grafo.getEdge(anno, anno+k);
			if(this.anni.contains(anno+k)) //non considero caso anno inesistente
				flag=false;
		}
		anno+=k;
		if(arco!=null) {
			espandi(livello+1, anno,fine);
		} else {
			termina(cammino); //termina la lista, ma si puo continuare
			cammino.clear();
			espandi(0,anno,fine);
		}
		
	}

	private void termina(LinkedList<Integer> camm) {
		if(camm.size()>=massimo.size()) {
			massimo=new LinkedList<Integer>(cammino);
		}
		
	}
	
	public int punteggioAnno (int anno) {
		return risultati.get(anno);
	}

}
