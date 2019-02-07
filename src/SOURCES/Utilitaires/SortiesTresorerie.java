/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import SOURCES.CallBack.EcouteurEnregistrement;
import SOURCES.Interface.InterfaceDecaissement;
import SOURCES.Interface.InterfaceEncaissement;
import java.util.Vector;

/**
 *
 * @author user
 */

public class SortiesTresorerie {
    private EcouteurEnregistrement ecouteurEnregistrement;
    private Vector<InterfaceDecaissement> listeDecaissements;
    private Vector<InterfaceEncaissement> listeEncaissements;

    public SortiesTresorerie(EcouteurEnregistrement ecouteurEnregistrement, Vector<InterfaceDecaissement> listeDecaissements, Vector<InterfaceEncaissement> listeEncaissements) {
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

    public Vector<InterfaceDecaissement> getListeDecaissements() {
        return listeDecaissements;
    }

    public void setListeDecaissements(Vector<InterfaceDecaissement> listeDecaissements) {
        this.listeDecaissements = listeDecaissements;
    }

    public Vector<InterfaceEncaissement> getListeEncaissements() {
        return listeEncaissements;
    }

    public void setListeEncaissements(Vector<InterfaceEncaissement> listeEncaissements) {
        this.listeEncaissements = listeEncaissements;
    }

    @Override
    public String toString() {
        return "SortiesTresorerie{" + "ecouteurEnregistrement=" + ecouteurEnregistrement + ", listeDecaissements=" + listeDecaissements + ", listeEncaissements=" + listeEncaissements + '}';
    }
}
