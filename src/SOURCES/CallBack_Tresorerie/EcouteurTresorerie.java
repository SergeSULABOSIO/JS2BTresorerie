/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.CallBack_Tresorerie;

import SOURCES.Utilitaires_Tresorerie.SortiesTresorerie;

/**
 *
 * @author HP Pavilion
 */
public abstract class EcouteurTresorerie {
    public abstract void onEnregistre(SortiesTresorerie sortiesTresorerie);
    public abstract void onDetruitElement(int idElement, int index, long signature);
    public abstract boolean onCanDelete(int idElement, int index, long signature);
    public abstract void onClosed();    
}
