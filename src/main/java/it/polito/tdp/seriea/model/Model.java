package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

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
		grafo=new SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);

	}

	public ArrayList<Team> getAllSquadre() {
		squadre= dao.listTeams();
		return squadre;
	}
	
	private LinkedList<Integer> anni;
	private HashMap<Integer, Integer> risultati;

	public HashMap<Integer, Integer> getPuntiStagioni(Team t) {
		ArrayList<Match>partite=dao.getPuntiTotali(t);
		System.out.println(partite);
		risultati= new HashMap<Integer, Integer>();
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
		Graphs.addAllVertices(grafo, anni);
		for(Integer a1: anni){
			for(Integer a2: anni) {
				int risultato1= risultati.get(a1);
				int risultato2= risultati.get(a2);
				if(risultato1<risultato2) {
					Graphs.addEdge(grafo, a1, a2, (a2-a1));
				}
				if(risultato2<risultato1) {
					Graphs.addEdge(grafo, a2, a1, (a1-a2));
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
			if(totale>max) {
				annoVincente=a;
				max=totale;
			}
		}
		
		return new Vincente(annoVincente,max);
		
	}

}
