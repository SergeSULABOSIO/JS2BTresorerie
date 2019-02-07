/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.EditeursTable;


import SOURCES.Interface.InterfaceRevenu;
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
public class EditeurRevenu extends AbstractCellEditor implements TableCellEditor {

    private JComboBox<String> champEditionCombo = new JComboBox();
    private ParametreTresorerie parametreTresorerie;
    
    public EditeurRevenu(ParametreTresorerie parametreTresorerie) {
        this.parametreTresorerie = parametreTresorerie;
        initCombo();
    }

    public void initCombo() {
        this.champEditionCombo.removeAllItems();
        if (this.parametreTresorerie != null) {
            Vector<InterfaceRevenu> listeCharges = this.parametreTresorerie.getRevenus();
            if(listeCharges != null){
                for(InterfaceRevenu monnaie : listeCharges){
                    this.champEditionCombo.addItem(monnaie.getNom());
                }
            }
        }
    }
    
    
    private int getIdRevenu(String nomRevenu){
        for(InterfaceRevenu Icha : this.parametreTresorerie.getRevenus()){
            if(Icha.getNom().trim().toUpperCase().equals(nomRevenu.trim().toUpperCase())){
                return Icha.getId();
            }
        }
        return -1;
    }
    
    private String getRevenu(int idRevenu){
        for(InterfaceRevenu Icha : this.parametreTresorerie.getRevenus()){
            if(Icha.getId() == idRevenu){
                return Icha.getNom();
            }
        }
        return "Null";
    }
    
    
    @Override
    public Object getCellEditorValue() {
        //Après édition de l'utilisateur
        return getIdRevenu(champEditionCombo.getSelectedItem() + "");
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //Pendant édition de l'utilisateur
        initCombo();
        String defaultSelection = getRevenu(Integer.parseInt(value+""));
        champEditionCombo.setSelectedItem(defaultSelection);
        return champEditionCombo;
    }

}
