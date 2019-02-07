/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires;

import SOURCES.Interface.InterfaceCharge;
import SOURCES.Interface.InterfaceEntreprise;
import SOURCES.Interface.InterfaceExercice;
import SOURCES.Interface.InterfaceMonnaie;
import SOURCES.Interface.InterfaceRevenu;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class ParametreTresorerie {
    public InterfaceEntreprise entreprise;
    public InterfaceExercice exercice;
    public Vector<InterfaceMonnaie> monnaies;
    public Vector<InterfaceRevenu> revenus;
    public Vector<InterfaceCharge> charges;
    public String nomUtilisateur;
    public int idUtilisateur;

    public ParametreTresorerie(InterfaceEntreprise entreprise, InterfaceExercice exercice, Vector<InterfaceMonnaie> monnaies, Vector<InterfaceRevenu> revenus, Vector<InterfaceCharge> charges, String nomUtilisateur, int idUtilisateur) {
        this.entreprise = entreprise;
        this.exercice = exercice;
        this.monnaies = monnaies;
        this.revenus = revenus;
        this.charges = charges;
        this.nomUtilisateur = nomUtilisateur;
        this.idUtilisateur = idUtilisateur;
    }

    public InterfaceEntreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(InterfaceEntreprise entreprise) {
        this.entreprise = entreprise;
    }

    public InterfaceExercice getExercice() {
        return exercice;
    }

    public void setExercice(InterfaceExercice exercice) {
        this.exercice = exercice;
    }

    public Vector<InterfaceMonnaie> getMonnaies() {
        return monnaies;
    }

    public void setMonnaies(Vector<InterfaceMonnaie> monnaies) {
        this.monnaies = monnaies;
    }

    public Vector<InterfaceRevenu> getRevenus() {
        return revenus;
    }

    public void setRevenus(Vector<InterfaceRevenu> revenus) {
        this.revenus = revenus;
    }

    public Vector<InterfaceCharge> getCharges() {
        return charges;
    }

    public void setCharges(Vector<InterfaceCharge> charges) {
        this.charges = charges;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    @Override
    public String toString() {
        return "ParametreTresorerie{" + "entreprise=" + entreprise + ", exercice=" + exercice + ", monnaies=" + monnaies + ", revenus=" + revenus + ", charges=" + charges + ", nomUtilisateur=" + nomUtilisateur + ", idUtilisateur=" + idUtilisateur + '}';
    }
}
