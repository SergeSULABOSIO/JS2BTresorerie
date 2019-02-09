/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.RendusTables;


import SOURCES.Interface.InterfaceCharge;
import SOURCES.Interface.InterfaceDecaissement;
import SOURCES.Interface.InterfaceMonnaie;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import SOURCES.ModelsTable.ModeleListeDecaissement;
import SOURCES.UI.CelluleSimpleTableau;
import SOURCES.Utilitaires.ParametreTresorerie;
import SOURCES.Utilitaires.Util;

/**
 *
 * @author user
 */
public class RenduTableDecaissement implements TableCellRenderer {

    private ImageIcon iconeEdition;
    private ModeleListeDecaissement modeleListeDecaissement;
    private ParametreTresorerie parametreTresorerie;

    public RenduTableDecaissement(ImageIcon iconeEdition, ModeleListeDecaissement modeleListeDecaissement, ParametreTresorerie parametreTresorerie) {
        this.iconeEdition = iconeEdition;
        this.modeleListeDecaissement = modeleListeDecaissement;
        this.parametreTresorerie = parametreTresorerie;
    }
        
    private String getSource(int source) {
        String src = "CAISSE";
        if (source == InterfaceDecaissement.SOURCE_BANQUE) {
            src = "BANQUE";
        }
        return src;
    }
    
    private String getNature(int nature) {
        for(InterfaceCharge Icharge : this.parametreTresorerie.getCharges()){
            if(nature == Icharge.getId()){
                return "" + Icharge.getNom();
            }
        }
        return "";
    }
    
    private String getMonnaie(int nature) {
        for(InterfaceMonnaie Imonnaie : this.parametreTresorerie.getMonnaies()){
            if(nature == Imonnaie.getId()){
                return "" + Imonnaie.getCode();
            }
        }
        return "";
    }
    

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //{"N°", "Date", "Source", "Reference", "Motif", "Nature", "Bénéficiaire", "Montant", "Monnaie"};
        CelluleSimpleTableau cellule = null;
        switch (column) {
            case 0:
                cellule = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_CENTRE, null);
                break;
            case 1:
                cellule = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, iconeEdition);
                break;
            case 2:
                cellule = new CelluleSimpleTableau(" " + getSource(Integer.parseInt(value+"")) + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, iconeEdition);
                break;
            case 3:
                cellule = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, iconeEdition);
                break;
            case 4:
                cellule = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, iconeEdition);
                break;
            case 5:
                cellule = new CelluleSimpleTableau(" " + getNature(Integer.parseInt(value+"")) + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, iconeEdition);
                break;
            case 6:
                cellule = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_DROITE, iconeEdition);
                break;
            case 7:
                cellule = new CelluleSimpleTableau(" " + Util.getMontantFrancais(Double.parseDouble(value+"")) + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, iconeEdition);
                break;
            case 8:
                cellule = new CelluleSimpleTableau(" " + getMonnaie(Integer.parseInt(value+"")) + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, iconeEdition);
                break;
        }
        cellule.ecouterSelection(isSelected, row, getBeta(row), hasFocus);
        return cellule;
    }
    
    private int getBeta(int row) {
        if (this.modeleListeDecaissement != null) {
            InterfaceDecaissement Idec = this.modeleListeDecaissement.getDecaissement(row);
            if (Idec != null) {
                return Idec.getBeta();
            }
        }
        return InterfaceDecaissement.BETA_NOUVEAU;
    }
}
