/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import SOURCES.Interface.InterfaceDecaissement;
import SOURCES.Interface.InterfaceEncaissement;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class DonneesTresorerie {
    public Vector<InterfaceEncaissement> encaissements;
    public Vector<InterfaceDecaissement> decaissements;

    public DonneesTresorerie(Vector<InterfaceEncaissement> encaissements, Vector<InterfaceDecaissement> decaissements) {
        this.encaissements = encaissements;
        this.decaissements = decaissements;
    }

    public Vector<InterfaceEncaissement> getEncaissements() {
        return encaissements;
    }

    public void setEncaissements(Vector<InterfaceEncaissement> encaissements) {
        this.encaissements = encaissements;
    }

    public Vector<InterfaceDecaissement> getDecaissements() {
        return decaissements;
    }

    public void setDecaissements(Vector<InterfaceDecaissement> decaissements) {
        this.decaissements = decaissements;
    }

    @Override
    public String toString() {
        return "DonneesTresorerie{" + "encaissements=" + encaissements + ", decaissements=" + decaissements + '}';
    }
}
