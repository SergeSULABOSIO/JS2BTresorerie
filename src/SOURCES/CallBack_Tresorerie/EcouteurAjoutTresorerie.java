/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.CallBack_Tresorerie;

import SOURCES.ModelsTable_Tresorerie.ModeleListeDecaissement;
import SOURCES.ModelsTable_Tresorerie.ModeleListeEncaissement;


/**
 *
 * @author HP Pavilion
 */
public abstract class EcouteurAjoutTresorerie {
    public abstract void setAjoutEncaissement(ModeleListeEncaissement modeleListeEncaissement);
    public abstract void setAjoutDecaissement(ModeleListeDecaissement modeleListeDecaissement);
}


