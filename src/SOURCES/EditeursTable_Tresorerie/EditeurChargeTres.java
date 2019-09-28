/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.EditeursTable_Tresorerie;



import SOURCES.Utilitaires_Tresorerie.ParametreTresorerie;
import Source.Interface.InterfaceCharge;
import Source.Objet.Charge;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author user
 */
public class EditeurChargeTres extends AbstractCellEditor implements TableCellEditor {

    private JComboBox<String> champEditionCombo = new JComboBox();
    private ParametreTresorerie parametreTresorerie;
    
    public EditeurChargeTres(ParametreTresorerie parametreTresorerie) {
        this.parametreTresorerie = parametreTresorerie;
        initCombo();
    }

    public void initCombo() {
        this.champEditionCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println("Clic: " + e.getActionCommand());
                fireEditingStopped();
            }
        });
        this.champEditionCombo.removeAllItems();
        if (this.parametreTresorerie != null) {
            Vector<Charge> listeCharges = this.parametreTresorerie.getCharges();
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

