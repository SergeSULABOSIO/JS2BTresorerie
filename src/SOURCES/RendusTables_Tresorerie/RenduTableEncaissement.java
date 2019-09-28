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
import SOURCES.ModelsTable_Tresorerie.ModeleListeEncaissement;
import SOURCES.Utilitaires_Tresorerie.ParametreTresorerie;
import SOURCES.Utilitaires_Tresorerie.UtilTresorerie;
import Source.GestionEdition;
import Source.Interface.InterfaceEncaissement;
import Source.Objet.Agent;
import Source.Objet.CouleurBasique;
import Source.Objet.Encaissement;
import Source.Objet.Monnaie;
import Source.Objet.Revenu;
import Source.UI.CelluleTableauSimple;
import java.util.Date;

/**
 *
 * @author user
 */
public class RenduTableEncaissement implements TableCellRenderer {

    private ImageIcon iconeEdition;
    private ModeleListeEncaissement modeleListeEncaissement;
    private ParametreTresorerie parametreTresorerie;
    private CouleurBasique couleurBasique;
    private GestionEdition gestionEdition;

    public RenduTableEncaissement(GestionEdition gestionEdition, CouleurBasique couleurBasique, ImageIcon iconeEdition, ModeleListeEncaissement modeleListeEncaissement, ParametreTresorerie parametreTresorerie) {
        this.iconeEdition = iconeEdition;
        this.couleurBasique = couleurBasique;
        this.modeleListeEncaissement = modeleListeEncaissement;
        this.parametreTresorerie = parametreTresorerie;
        this.gestionEdition = gestionEdition;
    }
        
    private String getDestination(int destination) {
        String src = "CAISSE";
        if (destination == InterfaceEncaissement.DESTINATION_BANQUE) {
            src = "BANQUE";
        }
        return src;
    }
    
    private String getNature(int nature) {
        for(Revenu Irevenu : this.parametreTresorerie.getRevenus()){
            if(nature == Irevenu.getId()){
                return "" + Irevenu.getNom();
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
        //{"N°", "Date", "Destination", "Reference", "Motif", "Nature", "Effectué par", "Montant", "Monnaie"};
        CelluleTableauSimple cellule = null;
        int idEncaisse = modeleListeEncaissement.getEncaissement(row).getId();
        ImageIcon logoEdition = null;
        if(idEncaisse != -100){
            logoEdition = iconeEdition;
        }
        
        ImageIcon icone = null;
        if(gestionEdition != null){
            Encaissement agent = this.modeleListeEncaissement.getEncaissement(row);
            if(agent != null){
                if(gestionEdition.isEditable(agent.getId(), 0)){
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
                cellule = new CelluleTableauSimple(couleurBasique, " " + getDestination(Integer.parseInt(value+"")) + " ", CelluleTableauSimple.ALIGNE_GAUCHE, icone);
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
        if (this.modeleListeEncaissement != null) {
            InterfaceEncaissement Ienc = this.modeleListeEncaissement.getEncaissement(row);
            if (Ienc != null) {
                return Ienc.getBeta();
            }
        }
        return InterfaceEncaissement.BETA_NOUVEAU;
    }
}




















