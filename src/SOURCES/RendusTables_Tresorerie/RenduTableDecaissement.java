/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.RendusTables_Tresorerie;



import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import SOURCES.ModelsTable_Tresorerie.ModeleListeDecaissement;
import SOURCES.Utilitaires_Tresorerie.ParametreTresorerie;
import SOURCES.Utilitaires_Tresorerie.UtilTresorerie;
import Source.GestionEdition;
import Source.Interface.InterfaceDecaissement;
import Source.Objet.Agent;
import Source.Objet.Charge;
import Source.Objet.CouleurBasique;
import Source.Objet.Decaissement;
import Source.Objet.Monnaie;
import Source.UI.CelluleTableauSimple;
import java.util.Date;

/**
 *
 * @author user
 */
public class RenduTableDecaissement implements TableCellRenderer {

    private ImageIcon iconeEdition;
    private ModeleListeDecaissement modeleListeDecaissement;
    private ParametreTresorerie parametreTresorerie;
    private CouleurBasique couleurBasique;
    private GestionEdition gestionEdition;

    public RenduTableDecaissement(GestionEdition gestionEdition, CouleurBasique couleurBasique, ImageIcon iconeEdition, ModeleListeDecaissement modeleListeDecaissement, ParametreTresorerie parametreTresorerie) {
        this.iconeEdition = iconeEdition;
        this.couleurBasique = couleurBasique;
        this.modeleListeDecaissement = modeleListeDecaissement;
        this.parametreTresorerie = parametreTresorerie;
        this.gestionEdition = gestionEdition;
    }
        
    private String getSource(int source) {
        String src = "CAISSE";
        if (source == InterfaceDecaissement.SOURCE_BANQUE) {
            src = "BANQUE";
        }
        return src;
    }
    
    private String getNature(int nature) {
        for(Charge Icharge : this.parametreTresorerie.getCharges()){
            if(nature == Icharge.getId()){
                return "" + Icharge.getNom();
            }
        }
        return "";
    }
    
    private String getMonnaie(int nature) {
        for(Monnaie Imonnaie : this.parametreTresorerie.getMonnaies()){
            if(nature == Imonnaie.getId()){
                return "" + Imonnaie.getCode();
            }
        }
        return "";
    }
    

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //{"N°", "Date", "Source", "Reference", "Motif", "Nature", "Bénéficiaire", "Montant", "Monnaie"};
        CelluleTableauSimple cellule = null;
        int idDecaisse = modeleListeDecaissement.getDecaissement(row).getId();
        ImageIcon logoEdition = null;
        if(idDecaisse != -100){
            logoEdition = iconeEdition;
        }
        
        ImageIcon icone = null;
        if(gestionEdition != null){
            Decaissement agent = this.modeleListeDecaissement.getDecaissement(row);
            if(agent != null){
                if(gestionEdition.isEditable(agent.getId(), 1)){
                    icone = logoEdition;
                }
            }
        }
        
        switch (column) {
            case 0:
                cellule = new CelluleTableauSimple(couleurBasique, " " + value + " ", CelluleTableauSimple.ALIGNE_CENTRE, null);
                break;
            case 1:
                cellule = new CelluleTableauSimple(couleurBasique, " " + UtilTresorerie.getDateFrancais((Date)value) + " ", CelluleTableauSimple.ALIGNE_GAUCHE, icone);
                break;
            case 2:
                cellule = new CelluleTableauSimple(couleurBasique, " " + getSource(Integer.parseInt(value+"")) + " ", CelluleTableauSimple.ALIGNE_GAUCHE, icone);
                break;
            case 3:
                cellule = new CelluleTableauSimple(couleurBasique, " " + value + " ", CelluleTableauSimple.ALIGNE_GAUCHE, icone);
                break;
            case 4:
                cellule = new CelluleTableauSimple(couleurBasique, " " + value + " ", CelluleTableauSimple.ALIGNE_GAUCHE, icone);
                break;
            case 5:
                cellule = new CelluleTableauSimple(couleurBasique, " " + getNature(Integer.parseInt(value+"")) + " ", CelluleTableauSimple.ALIGNE_GAUCHE, icone);
                break;
            case 6:
                cellule = new CelluleTableauSimple(couleurBasique, " " + value + " ", CelluleTableauSimple.ALIGNE_GAUCHE, icone);
                break;
            case 7:
                cellule = new CelluleTableauSimple(couleurBasique, " " + UtilTresorerie.getMontantFrancais(Double.parseDouble(value+"")) + " ", CelluleTableauSimple.ALIGNE_DROITE, icone);
                break;
            case 8:
                cellule = new CelluleTableauSimple(couleurBasique, " " + getMonnaie(Integer.parseInt(value+"")) + " ", CelluleTableauSimple.ALIGNE_GAUCHE, icone);
                break;
        }
        cellule.ecouterSelection(isSelected, row, getBeta(row), hasFocus);
        return cellule;
    }
    
    private int getBeta(int row) {
        if (this.modeleListeDecaissement != null) {
            Decaissement Idec = this.modeleListeDecaissement.getDecaissement(row);
            if (Idec != null) {
                return Idec.getBeta();
            }
        }
        return InterfaceDecaissement.BETA_NOUVEAU;
    }
}



















