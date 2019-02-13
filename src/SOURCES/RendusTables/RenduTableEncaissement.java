/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.RendusTables;


import SOURCES.Interface.InterfaceEncaissement;
import SOURCES.Interface.InterfaceMonnaie;
import SOURCES.Interface.InterfaceRevenu;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import SOURCES.ModelsTable.ModeleListeEncaissement;
import SOURCES.UI.CelluleSimpleTableau;
import SOURCES.Utilitaires.ParametreTresorerie;
import SOURCES.Utilitaires.Util;
import java.util.Date;

/**
 *
 * @author user
 */
public class RenduTableEncaissement implements TableCellRenderer {

    private ImageIcon iconeEdition;
    private ModeleListeEncaissement modeleListeEncaissement;
    private ParametreTresorerie parametreTresorerie;

    public RenduTableEncaissement(ImageIcon iconeEdition, ModeleListeEncaissement modeleListeEncaissement, ParametreTresorerie parametreTresorerie) {
        this.iconeEdition = iconeEdition;
        this.modeleListeEncaissement = modeleListeEncaissement;
        this.parametreTresorerie = parametreTresorerie;
    }
        
    private String getDestination(int destination) {
        String src = "CAISSE";
        if (destination == InterfaceEncaissement.DESTINATION_BANQUE) {
            src = "BANQUE";
        }
        return src;
    }
    
    private String getNature(int nature) {
        for(InterfaceRevenu Irevenu : this.parametreTresorerie.getRevenus()){
            if(nature == Irevenu.getId()){
                return "" + Irevenu.getNom();
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
        //{"N°", "Date", "Destination", "Reference", "Motif", "Nature", "Effectué par", "Montant", "Monnaie"};
        CelluleSimpleTableau cellule = null;
        switch (column) {
            case 0:
                cellule = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_CENTRE, null);
                break;
            case 1:
                cellule = new CelluleSimpleTableau(" " + Util.getDateFrancais((Date)value) + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, iconeEdition);
                break;
            case 2:
                cellule = new CelluleSimpleTableau(" " + getDestination(Integer.parseInt(value+"")) + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, iconeEdition);
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
                cellule = new CelluleSimpleTableau(" " + value + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, iconeEdition);
                break;
            case 7:
                cellule = new CelluleSimpleTableau(" " + Util.getMontantFrancais(Double.parseDouble(value+"")) + " ", CelluleSimpleTableau.ALIGNE_DROITE, iconeEdition);
                break;
            case 8:
                cellule = new CelluleSimpleTableau(" " + getMonnaie(Integer.parseInt(value+"")) + " ", CelluleSimpleTableau.ALIGNE_GAUCHE, iconeEdition);
                break;
        }
        cellule.ecouterSelection(isSelected, row, getBeta(row), hasFocus);
        return cellule;
    }
    
    private int getBeta(int row) {
        if (this.modeleListeEncaissement != null) {
            InterfaceEncaissement Ienc = this.modeleListeEncaissement.getEncaissement(row);
            if (Ienc != null) {
                return Ienc.getBeta();
            }
        }
        return InterfaceEncaissement.BETA_NOUVEAU;
    }
}
