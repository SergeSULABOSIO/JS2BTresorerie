/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.ModelsTable;

import BEAN_BARRE_OUTILS.Bouton;
import BEAN_MenuContextuel.RubriqueSimple;
import SOURCES.CallBack.EcouteurValeursChangees;
import SOURCES.Interface.InterfaceDecaissement;
import SOURCES.Utilitaires.Util;
import java.awt.Color;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author HP Pavilion
 */
public class ModeleListeDecaissement extends AbstractTableModel {

    private String[] titreColonnes = {"N°", "Date", "Source", "Reference", "Motif", "Nature", "Bénéficiaire", "Montant", "Monnaie"};
    private Vector<InterfaceDecaissement> listeData = new Vector<>();
    private Vector<InterfaceDecaissement> listeDataExclus = new Vector<>();
    private JScrollPane parent;
    private EcouteurValeursChangees ecouteurModele;
    private Bouton btEnreg;
    private RubriqueSimple mEnreg;

    public ModeleListeDecaissement(JScrollPane parent, Bouton btEnreg, RubriqueSimple mEnreg, EcouteurValeursChangees ecouteurModele) {
        this.parent = parent;
        this.ecouteurModele = ecouteurModele;
        this.mEnreg = mEnreg;
        this.btEnreg = btEnreg;
    }

    public void setListeDecaissements(Vector<InterfaceDecaissement> listeData) {
        this.listeData = listeData;
        redessinerTable();
    }

    public InterfaceDecaissement getDecaissement(int row) {
        if (row < listeData.size() && row != -1) {
            InterfaceDecaissement art = listeData.elementAt(row);
            if (art != null) {
                return art;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void chercher(Date dateA, Date dateB, String motcle, int idMonnaie, int idSource, int idCharge) {
        this.listeData.addAll(this.listeDataExclus);
        this.listeDataExclus.removeAllElements();
        for (InterfaceDecaissement Idecaissement : this.listeData) {
            if (Idecaissement != null) {
                search_verifier_periode(Idecaissement, dateA, dateB, motcle, idMonnaie, idSource, idCharge);
            }
        }
        //En fin, on va nettoyer la liste - en enlevant tout objet qui a été black listé
        search_nettoyer();
    }
    
    private void search_verifier_periode(InterfaceDecaissement Idecaissement, Date dateA, Date dateB, String motcle, int idMonnaie, int idSource, int idCharge) {
        if (Idecaissement != null) {
            boolean apresA = (Idecaissement.getDate().after(dateA) || Idecaissement.getDate().equals(dateA));
            boolean avantB = (Idecaissement.getDate().before(dateB) || Idecaissement.getDate().equals(dateB));
            if (apresA == true && avantB == true) {
                //On ne fait rien
            } else {
                search_blacklister(Idecaissement);
            }
            search_verifier_monnaie(Idecaissement, motcle, idMonnaie, idSource, idCharge);
        }
    }
    
    private void search_verifier_monnaie(InterfaceDecaissement Idecaissement, String motcle, int idMonnaie, int idSource, int idCharge) {
        if (Idecaissement != null) {
            if (idMonnaie == -1) {
                //On ne fait rien
            } else if(Idecaissement.getIdMonnaie() != idMonnaie){
                search_blacklister(Idecaissement);
            }
            search_verifier_source(Idecaissement, motcle, idSource, idCharge);
        }
    }
    
    private void search_verifier_source(InterfaceDecaissement Idecaissement, String motcle, int idSource, int idCharge) {
        if (Idecaissement != null) {
            if (idSource == -1) {
                //On ne fait rien
            } else if(Idecaissement.getSource()!= idSource){
                search_blacklister(Idecaissement);
            }
            search_verifier_charge(Idecaissement, motcle, idCharge);
        }
    }
    
    private void search_verifier_charge(InterfaceDecaissement Idecaissement, String motcle, int idCharge) {
        if (Idecaissement != null) {
            if (idCharge == -1) {
                //On ne fait rien
            } else if(Idecaissement.getIdCharge() != idCharge){
                search_blacklister(Idecaissement);
            }
            search_verifier_motcle(Idecaissement, motcle);
        }
    }  
    
    private void search_verifier_motcle(InterfaceDecaissement Idecaissement, String motcle) {
        if (Idecaissement != null) {
            if (motcle.trim().length() == 0) {
                //On ne fait rien
            } else if(Util.contientMotsCles(Idecaissement.getBeneficiaire(), motcle) == false && Util.contientMotsCles(Idecaissement.getMotif(), motcle) == false && Util.contientMotsCles(Idecaissement.getReference(), motcle) == false){
                search_blacklister(Idecaissement);
            }
        }
    }
    
    private void search_blacklister(InterfaceDecaissement Idecaissement) {
        if (Idecaissement != null && this.listeDataExclus != null) {
            if (!listeDataExclus.contains(Idecaissement)) {
                this.listeDataExclus.add(Idecaissement);
            }
        }
    }

    private void search_nettoyer() {
        if (this.listeDataExclus != null && this.listeData != null) {
            this.listeDataExclus.forEach((IeleveASupp) -> {
                this.listeData.removeElement(IeleveASupp);
            });
            redessinerTable();
        }
    }

    public InterfaceDecaissement getDecaissement_id(int id) {
        if (id != -1) {
            for (InterfaceDecaissement art : listeData) {
                if (id == art.getId()) {
                    return art;
                }
            }
        }
        return null;
    }

    public Vector<InterfaceDecaissement> getListeData() {
        return this.listeData;
    }

    public void actualiser() {
        //System.out.println("actualiser - Enseignant...");
        redessinerTable();
    }

    public void AjouterDecaissement(InterfaceDecaissement newDecaissement) {
        this.listeData.add(0, newDecaissement);
        mEnreg.setCouleur(Color.blue);
        btEnreg.setCouleur(Color.blue);
        redessinerTable();
    }

    public void SupprimerDecaissement(int row) {
        if (row < listeData.size() && row != -1) {
            InterfaceDecaissement articl = listeData.elementAt(row);
            if (articl != null) {
                int dialogResult = JOptionPane.showConfirmDialog(parent, "Etes-vous sûr de vouloir supprimer cette liste?", "Avertissement", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    if (row <= listeData.size()) {
                        this.listeData.removeElementAt(row);
                    }
                    redessinerTable();
                }
            }
        }
    }

    public void viderListe() {
        int dialogResult = JOptionPane.showConfirmDialog(parent, "Etes-vous sûr de vouloir vider cette liste?", "Avertissement", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            this.listeData.removeAllElements();
            redessinerTable();
        }
    }

    public void redessinerTable() {
        fireTableDataChanged();
        ecouteurModele.onValeurChangee();
    }

    @Override
    public int getRowCount() {
        return listeData.size();
    }

    @Override
    public int getColumnCount() {
        return titreColonnes.length;
    }

    @Override
    public String getColumnName(int column) {
        return titreColonnes[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //{"N°", "Date", "Source", "Reference", "Motif", "Nature", "Bénéficiaire", "Montant", "Monnaie"};
        switch (columnIndex) {
            case 0:
                return (rowIndex + 1) + "";
            case 1:
                return listeData.elementAt(rowIndex).getDate();
            case 2:
                return listeData.elementAt(rowIndex).getSource();
            case 3:
                return listeData.elementAt(rowIndex).getReference();
            case 4:
                return listeData.elementAt(rowIndex).getMotif();
            case 5:
                return listeData.elementAt(rowIndex).getIdCharge();
            case 6:
                return listeData.elementAt(rowIndex).getBeneficiaire();
            case 7:
                return listeData.elementAt(rowIndex).getMontant();
            case 8:
                return listeData.elementAt(rowIndex).getIdMonnaie();
            default:
                return "Null";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        //{"N°", "Date", "Source", "Reference", "Motif", "Nature", "Bénéficiaire", "Montant", "Monnaie"};
        switch (columnIndex) {
            case 0:
                return String.class;//N°
            case 1:
                return Date.class;//Date
            case 2:
                return Integer.class;//Source
            case 3:
                return String.class;//Reference
            case 4:
                return String.class;//Motif
            case 5:
                return Integer.class;//Nature
            case 6:
                return String.class;//Bénéficiaire
            case 7:
                return Double.class;//Montant
            case 8:
                return Integer.class;//Monnaie
            default:
                return Object.class;
        }

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //{"N°", "Date", "Source", "Reference", "Motif", "Nature", "Bénéficiaire", "Montant", "Monnaie"};
        InterfaceDecaissement Idecaisse = listeData.get(rowIndex);
        String avant = Idecaisse.toString();
        switch (columnIndex) {
            case 1:
                Idecaisse.setDate((Date) aValue);
                break;
            case 2:
                Idecaisse.setSource(Integer.parseInt(aValue + ""));
                break;
            case 3:
                Idecaisse.setReference(aValue + "");
                break;
            case 4:
                Idecaisse.setMotif(aValue + "");
                break;
            case 5:
                Idecaisse.setIdCharge(Integer.parseInt(aValue + ""));
                break;
            case 6:
                Idecaisse.setBeneficiaire(aValue + "");
                break;
            case 7:
                Idecaisse.setMontant(Double.parseDouble(aValue + ""));
                break;
            case 8:
                Idecaisse.setIdMonnaie(Integer.parseInt(aValue + ""));
                break;
            default:
                break;
        }
        String apres = Idecaisse.toString();
        if (!avant.equals(apres)) {
            if (Idecaisse.getBeta() == InterfaceDecaissement.BETA_EXISTANT) {
                Idecaisse.setBeta(InterfaceDecaissement.BETA_MODIFIE);
                mEnreg.setCouleur(Color.blue);
                btEnreg.setCouleur(Color.blue);
            }
        }
        listeData.set(rowIndex, Idecaisse);
        ecouteurModele.onValeurChangee();
        fireTableDataChanged();
    }

}
