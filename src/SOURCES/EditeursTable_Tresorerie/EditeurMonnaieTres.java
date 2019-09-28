/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.EditeursTable_Tresorerie;

import SOURCES.Utilitaires_Tresorerie.ParametreTresorerie;
import Source.Objet.Monnaie;
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
public class EditeurMonnaieTres extends AbstractCellEditor implements TableCellEditor {

    private JComboBox<String> champEditionCombo = new JComboBox();
    private ParametreTresorerie parametreTresorerie;

    public EditeurMonnaieTres(ParametreTresorerie parametreTresorerie) {
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
            Vector<Monnaie> listeMonnaies = this.parametreTresorerie.getMonnaies();
            if (listeMonnaies != null) {
                for (Monnaie monnaie : listeMonnaies) {
                    this.champEditionCombo.addItem(monnaie.getCode());
                }
            }
        }
    }

    private int getIdMonnaie(String monnaie) {
        for (Monnaie Imonnaie : this.parametreTresorerie.getMonnaies()) {
            if (Imonnaie.getCode().trim().toUpperCase().equals(monnaie.trim().toUpperCase())) {
                return Imonnaie.getId();
            }
        }
        return -1;
    }

    private String getMonnaie(int idMonnaie) {
        for (Monnaie Imonnaie : this.parametreTresorerie.getMonnaies()) {
            if (Imonnaie.getId() == idMonnaie) {
                return Imonnaie.getCode();
            }
        }
        return "Null";
    }

    @Override
    public Object getCellEditorValue() {
        //Après édition de l'utilisateur
        if(champEditionCombo != null){
            return getIdMonnaie(champEditionCombo.getSelectedItem() + "");
        }else{
            return -1;
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //Pendant édition de l'utilisateur
        initCombo();
        String defaultSelection = getMonnaie(Integer.parseInt(value + ""));
        if (champEditionCombo.getItemCount() != 0) {
            champEditionCombo.setSelectedItem(defaultSelection);
            return champEditionCombo;
        } else {
            return null;
        }

    }

}


