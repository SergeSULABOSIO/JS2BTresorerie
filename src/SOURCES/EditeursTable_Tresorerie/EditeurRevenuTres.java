/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.EditeursTable_Tresorerie;

import SOURCES.Utilitaires_Tresorerie.ParametreTresorerie;
import Source.Interface.InterfaceRevenu;
import Source.Objet.Revenu;
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
public class EditeurRevenuTres extends AbstractCellEditor implements TableCellEditor {

    private JComboBox<String> champEditionCombo = new JComboBox();
    private ParametreTresorerie parametreTresorerie;

    public EditeurRevenuTres(ParametreTresorerie parametreTresorerie) {
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
            Vector<Revenu> listeCharges = this.parametreTresorerie.getRevenus();
            if (listeCharges != null) {
                for (InterfaceRevenu monnaie : listeCharges) {
                    this.champEditionCombo.addItem(monnaie.getNom());
                }
            }
        }
    }

    private int getIdRevenu(String nomRevenu) {
        for (Revenu Icha : this.parametreTresorerie.getRevenus()) {
            if (Icha.getNom().trim().toUpperCase().equals(nomRevenu.trim().toUpperCase())) {
                return Icha.getId();
            }
        }
        return -1;
    }

    private String getRevenu(int idRevenu) {
        for (Revenu Icha : this.parametreTresorerie.getRevenus()) {
            if (Icha.getId() == idRevenu) {
                return Icha.getNom();
            }
        }
        return "Null";
    }

    @Override
    public Object getCellEditorValue() {
        //Après édition de l'utilisateur
        if (champEditionCombo != null) {
            return getIdRevenu(champEditionCombo.getSelectedItem() + "");
        }else{
            return -1;
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //Pendant édition de l'utilisateur
        initCombo();
        String defaultSelection = getRevenu(Integer.parseInt(value + ""));
        if (champEditionCombo.getItemCount() != 0) {
            champEditionCombo.setSelectedItem(defaultSelection);
            return champEditionCombo;
        } else {
            return null;
        }

    }

}

