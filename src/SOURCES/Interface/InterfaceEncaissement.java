/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Interface;

import java.util.Date;

/**
 *
 * @author HP Pavilion
 */
public interface InterfaceEncaissement {
    
    //Conatantes - BETA
    public static final int BETA_EXISTANT = 0;
    public static final int BETA_MODIFIE = 1;
    public static final int BETA_NOUVEAU = 2;
    //Constante - DESTINATION
    public static final int DESTINATION_CAISSE = 0;
    public static final int DESTINATION_BANQUE = 1;
    
    public abstract int getId();
    public abstract int getDestination();
    public abstract String getReference();
    public abstract Date getDate();
    public abstract double getMontant();
    public abstract int getIdMonnaie();
    public abstract String getMonnaie();
    public abstract String getEffectuePar();
    public abstract String getMotif();
    public abstract int getBeta();
    
    public abstract void setId(int id);
    public abstract void setDestination(int destination);
    public abstract void setReference(String reference);
    public abstract void setDate(Date date);
    public abstract void setMontant(double montant);
    public abstract void setIdMonnaie(int idMonnaie);
    public abstract void setMonnaie(String monnaie);
    public abstract void setEffectuePar(String effectuePar);
    public abstract void setMotif(String motif);
    public abstract void setBeta(int newBeta);
}
