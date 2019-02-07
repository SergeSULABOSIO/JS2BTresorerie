/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.CallBack;

import SOURCES.Utilitaires.SortiesTresorerie;

/**
 *
 * @author HP Pavilion
 */
public abstract class EcouteurTresorerie {
    public abstract void onEnregistre(SortiesTresorerie sortiesFacture);
    
}
