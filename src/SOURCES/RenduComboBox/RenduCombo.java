/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.RenduComboBox;

import SOURCES.UI.CelluleCombo;
import SOURCES.UI.CelluleSimpleTableau;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author HP Pavilion
 */
public class RenduCombo implements ListCellRenderer{
    private ImageIcon icone;

    public RenduCombo(ImageIcon icone) {
        this.icone = icone;
    }
    

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        CelluleCombo cellule = new CelluleCombo(" " + value + " ", CelluleSimpleTableau.ALIGNE_DROITE, icone);
        cellule.ecouterSelection(isSelected, index);
        return cellule;
    }
    
}
