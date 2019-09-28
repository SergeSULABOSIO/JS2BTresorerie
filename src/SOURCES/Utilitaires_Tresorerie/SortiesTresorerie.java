/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires_Tresorerie;



import Source.Callbacks.EcouteurEnregistrement;
import Source.Interface.InterfaceDecaissement;
import Source.Interface.InterfaceEncaissement;
import Source.Objet.Decaissement;
import Source.Objet.Encaissement;
import java.util.Vector;

/**
 *
 * @author user
 */

public class SortiesTresorerie {
    private EcouteurEnregistrement ecouteurEnregistrement;
    private Vector<Decaissement> listeDecaissements;
    private Vector<Encaissement> listeEncaissements;

    public SortiesTresorerie(EcouteurEnregistrement ecouteurEnregistrement, Vector<Decaissement> listeDecaissements, Vector<Encaissement> listeEncaissements) {
        this.ecouteurEnregistrement = ecouteurEnregistrement;
        this.listeDecaissements = listeDecaissements;
        this.listeEncaissements = listeEncaissements;
    }

    public EcouteurEnregistrement getEcouteurEnregistrement() {
        return ecouteurEnregistrement;
    }

    public void setEcouteurEnregistrement(EcouteurEnregistrement ecouteurEnregistrement) {
        this.ecouteurEnregistrement = ecouteurEnregistrement;
    }

    public Vector<Decaissement> getListeDecaissements() {
        return listeDecaissements;
    }

    public void setListeDecaissements(Vector<Decaissement> listeDecaissements) {
        this.listeDecaissements = listeDecaissements;
    }

    public Vector<Encaissement> getListeEncaissements() {
        return listeEncaissements;
    }

    public void setListeEncaissements(Vector<Encaissement> listeEncaissements) {
        this.listeEncaissements = listeEncaissements;
    }

    @Override
    public String toString() {
        return "SortiesTresorerie{" + "ecouteurEnregistrement=" + ecouteurEnregistrement + ", listeDecaissements=" + listeDecaissements + ", listeEncaissements=" + listeEncaissements + '}';
    }

    
}
