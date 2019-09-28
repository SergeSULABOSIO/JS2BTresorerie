/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires_Tresorerie;


import Source.Objet.Decaissement;
import Source.Objet.Encaissement;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class DonneesTresorerie {
    public Vector<Encaissement> encaissements;
    public Vector<Decaissement> decaissements;

    public DonneesTresorerie(Vector<Encaissement> encaissements, Vector<Decaissement> decaissements) {
        this.encaissements = encaissements;
        this.decaissements = decaissements;
    }

    public Vector<Encaissement> getEncaissements() {
        return encaissements;
    }

    public void setEncaissements(Vector<Encaissement> encaissements) {
        this.encaissements = encaissements;
    }

    public Vector<Decaissement> getDecaissements() {
        return decaissements;
    }

    public void setDecaissements(Vector<Decaissement> decaissements) {
        this.decaissements = decaissements;
    }

    @Override
    public String toString() {
        return "DonneesTresorerie{" + "encaissements=" + encaissements + ", decaissements=" + decaissements + '}';
    }

    
}




