/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.ModelsTable;

import SOURCES.CallBack.EcouteurValeursChangees;
import SOURCES.Interface.InterfaceEncaissement;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author HP Pavilion
 */
public class ModeleListeEncaissement extends AbstractTableModel {

    private String[] titreColonnes = {"N°", "Date", "Destination", "Reference", "Motif", "Nature", "Effectué par", "Montant", "Monnaie"};
    private Vector<InterfaceEncaissement> listeData = new Vector<>();
    private JScrollPane parent;
    private EcouteurValeursChangees ecouteurModele;

    public ModeleListeEncaissement(JScrollPane parent, EcouteurValeursChangees ecouteurModele) {
        this.parent = parent;
        this.ecouteurModele = ecouteurModele;
    }
    
    public void setListeEncaissements(Vector<InterfaceEncaissement> listeData) {
        this.listeData = listeData;
        redessinerTable();
    }

    public InterfaceEncaissement getEncaissement(int row) {
        if (row < listeData.size() && row != -1) {
            InterfaceEncaissement art = listeData.elementAt(row);
            if (art != null) {
                return art;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public InterfaceEncaissement getEncaissement_id(int id) {
        if (id != -1) {
            for (InterfaceEncaissement art : listeData) {
                if (id == art.getId()) {
                    return art;
                }
            }
        }
        return null;
    }
    
    public Vector<InterfaceEncaissement> getListeData() {
        return this.listeData;
    }

    public void actualiser() {
        //System.out.println("actualiser - Enseignant...");
        redessinerTable();
    }

    public void AjouterEncaissement(InterfaceEncaissement newEncaissement) {
        this.listeData.add(0, newEncaissement);
        redessinerTable();
    }

    public void SupprimerEncaissement(int row) {
        if (row < listeData.size() && row != -1) {
            InterfaceEncaissement articl = listeData.elementAt(row);
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
        //{"N°", "Date", "Destination", "Reference", "Motif", "Nature", "Effectué par", "Montant", "Monnaie"};
        switch (columnIndex) {
            case 0:
                return (rowIndex + 1) + "";
            case 1:
                return listeData.elementAt(rowIndex).getDate();
            case 2:
                return listeData.elementAt(rowIndex).getDestination();
            case 3:
                return listeData.elementAt(rowIndex).getReference();
            case 4:
                return listeData.elementAt(rowIndex).getMotif();
            case 5:
                return listeData.elementAt(rowIndex).getIdRevenu();
            case 6:
                return listeData.elementAt(rowIndex).getEffectuePar();
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
        //{"N°", "Date", "Destination", "Reference", "Motif", "Nature", "Effectué par", "Montant", "Monnaie"};
        switch (columnIndex) {
            case 0:
                return String.class;//N°
            case 1:
                return Date.class;//Date
            case 2:
                return Integer.class;//Destination
            case 3:
                return String.class;//Reference
            case 4:
                return String.class;//Motif
            case 5:
                return Integer.class;//Nature
            case 6:
                return String.class;//Effectué par
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
        if(columnIndex == 0){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //{"N°", "Date", "Destination", "Reference", "Motif", "Nature", "Effectué par", "Montant", "Monnaie"};
        InterfaceEncaissement Iencaisse = listeData.get(rowIndex);
        String avant = Iencaisse.toString();
        switch (columnIndex) {
            case 1:
                Iencaisse.setDate((Date)aValue);
                break;
            case 2:
                Iencaisse.setDestination(Integer.parseInt(aValue + ""));
                break;
            case 3:
                Iencaisse.setReference(aValue + "");
                break;
            case 4:
                Iencaisse.setMotif(aValue + "");
                break;
            case 5:
                Iencaisse.setIdRevenu(Integer.parseInt(aValue + ""));
                break;
            case 6:
                Iencaisse.setEffectuePar(aValue + "");
                break;
            case 7:
                Iencaisse.setMontant(Double.parseDouble(aValue + ""));
                break;
            case 8:
                Iencaisse.setIdMonnaie(Integer.parseInt(aValue + ""));
                break;
            default:
                break;
        }
        String apres = Iencaisse.toString();
        if(!avant.equals(apres)){
            if(Iencaisse.getBeta() == InterfaceEncaissement.BETA_EXISTANT){
                Iencaisse.setBeta(InterfaceEncaissement.BETA_MODIFIE);
            }
        }
        listeData.set(rowIndex, Iencaisse);
        ecouteurModele.onValeurChangee();
        fireTableDataChanged();
    }

}
