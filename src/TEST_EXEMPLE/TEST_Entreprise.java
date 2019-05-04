/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST_EXEMPLE;

import SOURCES.Utilitaires.*;
import SOURCES.Interface.InterfaceEntreprise;

/**
 *
 * @author HP Pavilion
 */
public class TEST_Entreprise implements InterfaceEntreprise{
    public int id;
    public String nom;
    public String adresse;
    public String telephone;
    public String email;
    public String siteWeb;
    private String logo;
    private String rccm;
    private String idnat;
    private String numeroImpot;
    //Details bancaires
    public String banque;
    public String intituleCompte;
    public String numeroCompte;
    public String iban;
    public String codeSwift;

    public TEST_Entreprise() {
    }

    public TEST_Entreprise(int id, String nom, String adresse, String telephone, String email, String siteWeb, String logo, String rccm, String idnat, String numeroImpot, String banque, String intituleCompte, String numeroCompte, String iban, String codeSwift) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.email = email;
        this.siteWeb = siteWeb;
        this.logo = logo;
        this.rccm = rccm;
        this.idnat = idnat;
        this.numeroImpot = numeroImpot;
        this.banque = banque;
        this.intituleCompte = intituleCompte;
        this.numeroCompte = numeroCompte;
        this.iban = iban;
        this.codeSwift = codeSwift;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSiteWeb() {
        return siteWeb;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getRccm() {
        return rccm;
    }

    public void setRccm(String rccm) {
        this.rccm = rccm;
    }

    public String getIdnat() {
        return idnat;
    }

    public void setIdnat(String idnat) {
        this.idnat = idnat;
    }

    public String getNumeroImpot() {
        return numeroImpot;
    }

    public void setNumeroImpot(String numeroImpot) {
        this.numeroImpot = numeroImpot;
    }

    public String getBanque() {
        return banque;
    }

    public void setBanque(String banque) {
        this.banque = banque;
    }

    public String getIntituleCompte() {
        return intituleCompte;
    }

    public void setIntituleCompte(String intituleCompte) {
        this.intituleCompte = intituleCompte;
    }

    public String getNumeroCompte() {
        return numeroCompte;
    }

    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getCodeSwift() {
        return codeSwift;
    }

    public void setCodeSwift(String codeSwift) {
        this.codeSwift = codeSwift;
    }

    @Override
    public String toString() {
        return "XX_Entreprise{" + "id=" + id + ", nom=" + nom + ", adresse=" + adresse + ", telephone=" + telephone + ", email=" + email + ", siteWeb=" + siteWeb + ", logo=" + logo + ", rccm=" + rccm + ", idnat=" + idnat + ", numeroImpot=" + numeroImpot + ", banque=" + banque + ", intituleCompte=" + intituleCompte + ", numeroCompte=" + numeroCompte + ", iban=" + iban + ", codeSwift=" + codeSwift + '}';
    }

    
   
}
