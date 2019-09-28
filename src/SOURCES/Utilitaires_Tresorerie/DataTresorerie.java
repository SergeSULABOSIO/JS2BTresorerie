/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.Utilitaires_Tresorerie;

/**
 *
 * @author user
 */
public class DataTresorerie {

    public ParametreTresorerie parametreTresorerie;

    public DataTresorerie(ParametreTresorerie parametreTresorerie) {
        this.parametreTresorerie = parametreTresorerie;
    }

    
    public ParametreTresorerie getParametreTresorerie() {
        return parametreTresorerie;
    }

    public void setParametreTresorerie(ParametreTresorerie parametreTresorerie) {
        this.parametreTresorerie = parametreTresorerie;
    }

    @Override
    public String toString() {
        return "DataTresorerie{" + "parametreTresorerie=" + parametreTresorerie + '}';
    }
}
