package it.polito.tdp.seriea;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.TreeMap;

import it.polito.tdp.seriea.model.Model;
import it.polito.tdp.seriea.model.Team;
import it.polito.tdp.seriea.model.Vincente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<Team> boxSquadra;

    @FXML
    private Button btnSelezionaSquadra;

    @FXML
    private Button btnTrovaAnnataOro;

    @FXML
    private Button btnTrovaCamminoVirtuoso;

    @FXML
    private TextArea txtResult;

    @FXML
    void doSelezionaSquadra(ActionEvent event) {
    	Team t=this.boxSquadra.getValue();
    	TreeMap<Integer, Integer> lista=model.getPuntiStagioni(t);
    	this.txtResult.clear();
    	for (int i : lista.keySet()) {
			this.txtResult.appendText("Stagione "+i+" punti totali "+lista.get(i)+"\n");
		}

    }

    @FXML
    void doTrovaAnnataOro(ActionEvent event) {
    	Vincente v=this.model.getAnnata(this.boxSquadra.getValue());
    	this.txtResult.appendText("L'annata vincente della squadra: anno "+v.getAnno()+" punti "+v.getPunti()+"\n");

    }

    @FXML
    void doTrovaCamminoVirtuoso(ActionEvent event) {
    	LinkedList<Integer> cammino=this.model.getCamminoVirtuoso(this.boxSquadra.getValue());
    	int k=0;
    	for(Integer anno:cammino) {
    		k++;
    		this.txtResult.appendText(k+") anno "+anno+" punteggio "+this.model.punteggioAnno(anno)+"\n"); 
    	}

    }

    @FXML
    void initialize() {
        assert boxSquadra != null : "fx:id=\"boxSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnSelezionaSquadra != null : "fx:id=\"btnSelezionaSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaAnnataOro != null : "fx:id=\"btnTrovaAnnataOro\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaCamminoVirtuoso != null : "fx:id=\"btnTrovaCamminoVirtuoso\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";
        this.boxSquadra.getItems().clear();
        

    }

	public void setModel(Model model) {
		this.model = model;
		this.boxSquadra.getItems().addAll(model.getAllSquadre());
	}
}
