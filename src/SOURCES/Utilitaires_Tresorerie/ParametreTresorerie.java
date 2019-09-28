/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires_Tresorerie;

import Source.Objet.Charge;
import Source.Objet.Entreprise;
import Source.Objet.Exercice;
import Source.Objet.Monnaie;
import Source.Objet.Revenu;
import Source.Objet.Utilisateur;
import java.util.Vector;

/**
 *
 * @author HP Pavilion
 */
public class ParametreTresorerie {
    public Entreprise entreprise;
    public Exercice exercice;
    public Utilisateur utilisateur;
    public Vector<Monnaie> monnaies;
    public Vector<Revenu> revenus;
    public Vector<Charge> charges;

    public ParametreTresorerie(Entreprise entreprise, Exercice exercice, Utilisateur utilisateur, Vector<Monnaie> monnaies, Vector<Revenu> revenus, Vector<Charge> charges) {
        this.entreprise = entreprise;
        this.exercice = exercice;
        this.utilisateur = utilisateur;
        this.monnaies = monnaies;
        this.revenus = revenus;
        this.charges = charges;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
    

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    public Exercice getExercice() {
        return exercice;
    }

    public void setExercice(Exercice exercice) {
        this.exercice = exercice;
    }

    public Vector<Monnaie> getMonnaies() {
        return monnaies;
    }

    public void setMonnaies(Vector<Monnaie> monnaies) {
        this.monnaies = monnaies;
    }

    public Vector<Revenu> getRevenus() {
        return revenus;
    }

    public void setRevenus(Vector<Revenu> revenus) {
        this.revenus = revenus;
    }

    public Vector<Charge> getCharges() {
        return charges;
    }

    public void setCharges(Vector<Charge> charges) {
        this.charges = charges;
    }

    @Override
    public String toString() {
        return "ParametreTresorerie{" + "entreprise=" + entreprise + ", exercice=" + exercice + ", utilisateur=" + utilisateur + ", monnaies=" + monnaies + ", revenus=" + revenus + ", charges=" + charges + '}';
    }
}
















