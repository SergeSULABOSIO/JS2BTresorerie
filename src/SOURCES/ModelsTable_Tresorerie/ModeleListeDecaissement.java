/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.ModelsTable_Tresorerie;

import BEAN_BARRE_OUTILS.Bouton;
import BEAN_MenuContextuel.RubriqueSimple;
import Source.Callbacks.EcouteurSuppressionElement;
import Source.Callbacks.EcouteurValeursChangees;
import Source.GestionEdition;
import Source.Interface.InterfaceDecaissement;
import Source.Objet.Charge;
import Source.Objet.CouleurBasique;
import Source.Objet.Cours;
import Source.Objet.Decaissement;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author HP Pavilion
 */
public class ModeleListeDecaissement extends AbstractTableModel {

    private String[] titreColonnes = {"N°", "Date", "Source", "Reference", "Motif", "Nature", "Bénéficiaire", "Montant", "Monnaie"};
    private Vector<Decaissement> listeData = new Vector<>();
    private JScrollPane parent;
    private EcouteurValeursChangees ecouteurModele;
    private Bouton btEnreg;
    private RubriqueSimple mEnreg;
    private CouleurBasique couleurBasique;
    private JProgressBar progress;
    private GestionEdition gestionEdition;
    private Vector<Charge> listeCharges = new Vector<>();

    public ModeleListeDecaissement(Vector<Charge> listeCharges, GestionEdition gestionEdition, JProgressBar progress, CouleurBasique couleurBasique, JScrollPane parent, Bouton btEnreg, RubriqueSimple mEnreg, EcouteurValeursChangees ecouteurModele) {
        this.parent = parent;
        this.progress = progress;
        this.couleurBasique = couleurBasique;
        this.ecouteurModele = ecouteurModele;
        this.mEnreg = mEnreg;
        this.btEnreg = btEnreg;
        this.gestionEdition = gestionEdition;
        this.listeCharges = listeCharges;
    }
    
    private String getCharge(int idCharge) {
        for (Charge cha : listeCharges) {
            if (cha.getId() == idCharge) {
                return cha.getNom();
            }
        }
        return "Null";
    }

    public void setDonneesDecaissements(Decaissement decaissement) {
        if (progress != null) {
            progress.setVisible(true);
            progress.setIndeterminate(true);
        }
        listeData.add(decaissement);
        actualiser();
        if (progress != null) {
            progress.setVisible(false);
            progress.setIndeterminate(false);
        }
    }

    public void setListeDecaissements(Vector<Decaissement> listeData) {
        this.listeData = listeData;
        redessinerTable();
    }

    public Decaissement getDecaissement(int row) {
        if (row < listeData.size() && row != -1) {
            Decaissement art = listeData.elementAt(row);
            if (art != null) {
                return art;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void reinitialiserListe() {
        if (progress != null) {
            progress.setVisible(false);
            progress.setIndeterminate(false);
        }
        this.listeData.removeAllElements();
        redessinerTable();
    }

    public Decaissement getDecaissement_id(int id) {
        if (id != -1) {
            for (Decaissement art : listeData) {
                if (id == art.getId()) {
                    return art;
                }
            }
        }
        return null;
    }

    public Vector<Decaissement> getListeData() {
        return this.listeData;
    }

    public void actualiser() {
        //System.out.println("actualiser - Enseignant...");
        redessinerTable();
    }

    public void AjouterDecaissement(Decaissement newDecaissement) {
        this.listeData.add(0, newDecaissement);
        mEnreg.setCouleur(couleurBasique.getCouleur_foreground_objet_nouveau());                                        //mEnreg.setCouleur(Color.blue);
        btEnreg.setForeground(couleurBasique.getCouleur_foreground_objet_nouveau());                                   //btEnreg.setForeground(Color.blue);
        redessinerTable();
    }

    public void SupprimerDecaissement(int row, EcouteurSuppressionElement ecouteurSuppressionElement) {
        if (row < listeData.size() && row != -1) {
            Decaissement articl = listeData.elementAt(row);
            if (articl != null) {
                int idASUupp = articl.getId();
                if (ecouteurSuppressionElement.onCanDelete(idASUupp, articl.getSignature()) == true) {
                    if (idASUupp == -100) {
                        return;
                    }
                    int dialogResult = JOptionPane.showConfirmDialog(parent, "Etes-vous sûr de vouloir supprimer cette liste?", "Avertissement", JOptionPane.YES_NO_OPTION);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        if (row <= listeData.size()) {
                            this.listeData.removeElementAt(row);
                        }
                        redessinerTable();
                        if (ecouteurSuppressionElement != null) {
                            ecouteurSuppressionElement.onDeletionComplete(idASUupp, articl.getSignature());
                        }
                    }
                }

            }
        }
    }

    public void viderListe() {
        int dialogResult = JOptionPane.showConfirmDialog(parent, "Etes-vous sûr de vouloir vider cette liste?", "Avertissement", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            this.listeData.removeAllElements();
            redessinerTable();
        }
    }

    public void redessinerTable() {
        fireTableDataChanged();
        ecouteurModele.onValeurChangee();
    }

    @Override
    public int getRowCount() {
        return listeData.size();
    }

    @Override
    public int getColumnCount() {
        return titreColonnes.length;
    }

    @Override
    public String getColumnName(int column) {
        return titreColonnes[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //{"N°", "Date", "Source", "Reference", "Motif", "Nature", "Bénéficiaire", "Montant", "Monnaie"};
        switch (columnIndex) {
            case 0:
                return (rowIndex + 1) + "";
            case 1:
                return listeData.elementAt(rowIndex).getDate();
            case 2:
                return listeData.elementAt(rowIndex).getSource();
            case 3:
                return listeData.elementAt(rowIndex).getReference();
            case 4:
                return listeData.elementAt(rowIndex).getMotif();
            case 5:
                return listeData.elementAt(rowIndex).getIdCharge();
            case 6:
                return listeData.elementAt(rowIndex).getBeneficiaire();
            case 7:
                return listeData.elementAt(rowIndex).getMontant();
            case 8:
                return listeData.elementAt(rowIndex).getIdMonnaie();
            default:
                return "Null";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        //{"N°", "Date", "Source", "Reference", "Motif", "Nature", "Bénéficiaire", "Montant", "Monnaie"};
        switch (columnIndex) {
            case 0:
                return String.class;//N°
            case 1:
                return Date.class;//Date
            case 2:
                return Integer.class;//Source
            case 3:
                return String.class;//Reference
            case 4:
                return String.class;//Motif
            case 5:
                return Integer.class;//Nature
            case 6:
                return String.class;//Bénéficiaire
            case 7:
                return Double.class;//Montant
            case 8:
                return Integer.class;//Monnaie
            default:
                return Object.class;
        }

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        Decaissement eleve = null;
        boolean canEdit = false;
        if (listeData.size() > rowIndex) {
            eleve = listeData.elementAt(rowIndex);
            canEdit = gestionEdition.isEditable(eleve.getId(), 1);
        }
        if (canEdit == true) {
            Decaissement decaisse = listeData.get(rowIndex);
            if (decaisse.getId() == -100) {
                return false;
            } else {
                if (columnIndex == 0) {
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //{"N°", "Date", "Source", "Reference", "Motif", "Nature", "Bénéficiaire", "Montant", "Monnaie"};
        Decaissement Idecaisse = listeData.get(rowIndex);
        String avant = Idecaisse.toString();
        switch (columnIndex) {
            case 1:
                Idecaisse.setDate((Date) aValue);
                break;
            case 2:
                Idecaisse.setSource(Integer.parseInt(aValue + ""));
                break;
            case 3:
                Idecaisse.setReference(aValue + "");
                break;
            case 4:
                Idecaisse.setMotif(aValue + "");
                break;
            case 5:
                Idecaisse.setIdCharge(Integer.parseInt(aValue + ""));
                Idecaisse.setCharge(getCharge(Idecaisse.getIdCharge()));
                break;
            case 6:
                Idecaisse.setBeneficiaire(aValue + "");
                break;
            case 7:
                Idecaisse.setMontant(Double.parseDouble(aValue + ""));
                break;
            case 8:
                Idecaisse.setIdMonnaie(Integer.parseInt(aValue + ""));
                break;
            default:
                break;
        }
        String apres = Idecaisse.toString();
        if (!avant.equals(apres)) {
            if (Idecaisse.getBeta() == InterfaceDecaissement.BETA_EXISTANT) {
                Idecaisse.setBeta(InterfaceDecaissement.BETA_MODIFIE);
                mEnreg.setCouleur(couleurBasique.getCouleur_foreground_objet_nouveau());                                        //mEnreg.setCouleur(Color.blue);
                btEnreg.setForeground(couleurBasique.getCouleur_foreground_objet_nouveau());                                   //btEnreg.setForeground(Color.blue);
            }
        }
        listeData.set(rowIndex, Idecaisse);
        ecouteurModele.onValeurChangee();
        fireTableDataChanged();
    }

}
