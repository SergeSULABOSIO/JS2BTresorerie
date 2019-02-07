/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.EditeursTable;


import SOURCES.Interface.InterfaceCharge;
import SOURCES.Interface.InterfaceMonnaie;
import SOURCES.Utilitaires.ParametreTresorerie;
import java.awt.Component;
import java.util.Vector;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author user
 */
public class EditeurCharge extends AbstractCellEditor implements TableCellEditor {

    private JComboBox<String> champEditionCombo = new JComboBox();
    private ParametreTresorerie parametreTresorerie;
    
    public EditeurCharge(ParametreTresorerie parametreTresorerie) {
        this.parametreTresorerie = parametreTresorerie;
        initCombo();
    }

    public void initCombo() {
        this.champEditionCombo.removeAllItems();
        if (this.parametreTresorerie != null) {
            Vector<InterfaceCharge> listeCharges = this.parametreTresorerie.getCharges();
            if(listeCharges != null){
                for(InterfaceCharge monnaie : listeCharges){
                    this.champEditionCombo.addItem(monnaie.getNom());
                }
            }
        }
    }
    
    
    private int getIdCharge(String nomCharge){
        for(InterfaceCharge Icha : this.parametreTresorerie.getCharges()){
            if(Icha.getNom().trim().toUpperCase().equals(nomCharge.trim().toUpperCase())){
                return Icha.getId();
            }
        }
        return -1;
    }
    
    private String getCharge(int idCharge){
        for(InterfaceCharge Icha : this.parametreTresorerie.getCharges()){
            if(Icha.getId() == idCharge){
                return Icha.getNom();
            }
        }
        return "Null";
    }
    
    
    @Override
    public Object getCellEditorValue() {
        //Après édition de l'utilisateur
        return getIdCharge(champEditionCombo.getSelectedItem() + "");
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //Pendant édition de l'utilisateur
        initCombo();
        String defaultSelection = getCharge(Integer.parseInt(value+""));
        champEditionCombo.setSelectedItem(defaultSelection);
        return champEditionCombo;
    }

}
