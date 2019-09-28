/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.UI_Tresorerie;

import SOURCES.MoteurRecherche_Tresorerie.MoteurRechercheTres;
import BEAN_BARRE_OUTILS.BarreOutils;
import BEAN_BARRE_OUTILS.Bouton;
import BEAN_BARRE_OUTILS.BoutonListener;
import BEAN_MenuContextuel.MenuContextuel;
import BEAN_MenuContextuel.RubriqueListener;
import BEAN_MenuContextuel.RubriqueSimple;
import ICONES.Icones;
import SOURCES.CallBack_Tresorerie.EcouteurActualisationTresorerie;
import SOURCES.CallBack_Tresorerie.EcouteurAjoutTresorerie;
import SOURCES.CallBack_Tresorerie.EcouteurTresorerie;
import SOURCES.EditeursTable_Tresorerie.EditeurChargeTres;
import SOURCES.EditeursTable_Tresorerie.EditeurDateTres;
import SOURCES.EditeursTable_Tresorerie.EditeurDestinationTres;
import SOURCES.EditeursTable_Tresorerie.EditeurMonnaieTres;
import SOURCES.EditeursTable_Tresorerie.EditeurRevenuTres;
import SOURCES.EditeursTable_Tresorerie.EditeurSourceTres;
import SOURCES.GenerateurPDF_Tresorerie.DocumentPDFTres;
import SOURCES.ModelsTable_Tresorerie.ModeleListeDecaissement;
import SOURCES.ModelsTable_Tresorerie.ModeleListeEncaissement;
import SOURCES.RendusTables_Tresorerie.RenduTableDecaissement;
import SOURCES.RendusTables_Tresorerie.RenduTableEncaissement;
import SOURCES.Utilitaires_Tresorerie.DataTresorerie;
import SOURCES.Utilitaires_Tresorerie.ParametreTresorerie;
import SOURCES.Utilitaires_Tresorerie.SortiesTresorerie;
import SOURCES.Utilitaires_Tresorerie.UtilTresorerie;
import Source.Callbacks.EcouteurEnregistrement;
import Source.Callbacks.EcouteurSuppressionElement;
import Source.Callbacks.EcouteurUpdateClose;
import Source.Callbacks.EcouteurValeursChangees;
import Source.GestionClickDroit;
import Source.GestionEdition;
import Source.Interface.InterfaceDecaissement;
import Source.Interface.InterfaceEncaissement;
import Source.Interface.InterfaceUtilisateur;
import Source.Objet.Charge;
import Source.Objet.CouleurBasique;
import Source.Objet.Decaissement;
import Source.Objet.Encaissement;
import Source.Objet.Entreprise;
import Source.Objet.Monnaie;
import Source.Objet.Revenu;
import Source.Objet.UtilObjet;
import Source.Objet.Utilisateur;
import Source.UI.NavigateurPages;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

/**
 *
 * @author HP Pavilion
 */
public class PanelTresorerie extends javax.swing.JPanel {

    /**
     * Creates new form Panel
     */
    public int indexTabSelected = 0;
    private Icones icones = null;
    private final JTabbedPane parent;
    private PanelTresorerie moi = null;
    private EcouteurUpdateClose ecouteurClose = null;
    private EcouteurAjoutTresorerie ecouteurAjout = null;
    public Bouton btEnregistrer, btAjouter, btSupprimer, btVider, btImprimer, btPDF, btFermer, btActualiser, btPDFSynth, btEdition;
    public RubriqueSimple mEnregistrer, mAjouter, mSupprimer, mVider, mImprimer, mPDF, mFermer, mActualiser, mPDFSynth, mEdition;
    private MenuContextuel menuContextuel = null;
    private BarreOutils bOutils = null;
    private EcouteurTresorerie ecouteurTresorerie = null;

    private ModeleListeEncaissement modeleListeEncaissement;
    private ModeleListeDecaissement modeleListeDecaissement;
    private MoteurRechercheTres gestionnaireRecherche = null;

    //public DonneesTresorerie donneesTresorerie;
    //public ParametreTresorerie parametreTresorerie;
    public DataTresorerie dataTresorerie;
    public double totalEncaissement = 0;
    public double totalDecaissement = 0;

    public String monnaieOutput = "";
    private int typeExport = -1;
    private JProgressBar progress;
    public EcouteurActualisationTresorerie ecouteurActualisationTresorerie = null;
    public CouleurBasique couleurBasique;

    Vector resultatTotalObjetsEncaiss = new Vector();
    Vector resultatTotalObjetsDecaiss = new Vector();

    public Encaissement selectedEncaissement = null;
    public Decaissement selectedDecaissement = null;
    private GestionEdition gestionEdition = new GestionEdition();

    public PanelTresorerie(CouleurBasique couleurBasique, JProgressBar progress, JTabbedPane parent, DataTresorerie dataTresorerie, EcouteurTresorerie ecouteurTresorerie) {
        this.initComponents();
        this.couleurBasique = couleurBasique;
        this.progress = progress;
        this.dataTresorerie = dataTresorerie;
        this.icones = new Icones();
        this.parent = parent;
        this.init();
        this.ecouteurTresorerie = ecouteurTresorerie;

        //Initialisaterus
        parametrerTableEncaissement();
        parametrerTableDecaissement();
        setIconesTabs();
        initMonnaieTotaux();
        actualiserTotalDecaissement();
        actualiserTotalEncaissement();
        actualiserBtExportTab();
        onEcouteCombo();
        ecouterClickDroit();
    }

    private void ecouterClickDroit() {
        new GestionClickDroit(menuContextuel, tableListeEncaissement, scrollListeEncaissement).init();
        new GestionClickDroit(menuContextuel, tableListeDecaissement, scrollListeDecaissement).init();
    }

    public void onEcouteCombo() {
        combototMonnaie.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    actualiserTotalDecaissement();
                    actualiserTotalEncaissement();
                    actualiserTotaux();
                }
            }
        });
    }

    public DataTresorerie getDataTresorerie() {
        return dataTresorerie;
    }

    public int getTypeExportation() {
        return typeExport;
    }

    private void actualiserBtExportTab() {
        if (btPDFSynth != null) {
            if (indexTabSelected == 0) {
                btPDFSynth.setText("Exp. Encaiss.", 12, true);
                mPDFSynth.setText("Exp. ces encaissements", 12, true);
                navigateurPagesEncaissement.setVisible(true);
                navigateurPagesDecaissement.setVisible(false);
            } else {
                btPDFSynth.setText("Exp. Décaiss.", 12, true);
                mPDFSynth.setText("Exp. ces décaissements", 12, true);
                navigateurPagesEncaissement.setVisible(false);
                navigateurPagesDecaissement.setVisible(true);
            }
        }
    }

    public void setBtEnregistrerNouveau() {
        if (mEnregistrer != null && btEnregistrer != null) {
            mEnregistrer.setCouleur(couleurBasique.getCouleur_foreground_objet_nouveau());                                        //mEnreg.setCouleur(Color.blue);
            btEnregistrer.setForeground(couleurBasique.getCouleur_foreground_objet_nouveau());
        }
    }

    public NavigateurPages getNavigateurPagesDecaissement() {
        return navigateurPagesDecaissement;
    }

    public NavigateurPages getNavigateurPagesEncaissement() {
        return navigateurPagesEncaissement;
    }

    public int getIdMonnaie(String nom) {
        int id = -1;
        for (Monnaie Im : this.dataTresorerie.getParametreTresorerie().getMonnaies()) {
            if (Im.getNom().equals(nom)) {
                return Im.getId();
            }
        }
        return id;
    }

    public int getIdRevenu(String nom) {
        int id = -1;
        for (Revenu Im : this.dataTresorerie.getParametreTresorerie().getRevenus()) {
            if (Im.getNom().equals(nom)) {
                return Im.getId();
            }
        }
        return id;
    }

    public void setDonneesEncaissement(Encaissement encaissement) {
        if (modeleListeEncaissement != null && encaissement != null) {
            modeleListeEncaissement.setDonneesEncaissement(encaissement);
        }
        if (navigateurPagesEncaissement != null) {
            navigateurPagesEncaissement.patienter(false, "Prêt.");
        }
    }

    public int getTailleResultatEncaissements() {
        if (modeleListeEncaissement != null) {
            return modeleListeEncaissement.getListeData().size();
        }
        return 0;
    }

    public int getTailleResultatDecaissements() {
        if (modeleListeDecaissement != null) {
            return modeleListeDecaissement.getListeData().size();
        }
        return 0;
    }

    public void setDonneesDecaissement(Decaissement decaissement) {
        if (modeleListeDecaissement != null && decaissement != null) {
            modeleListeDecaissement.setDonneesDecaissements(decaissement);
        }
        if (navigateurPagesDecaissement != null) {
            navigateurPagesDecaissement.patienter(false, "Prêt.");
        }
    }

    public int getIdCharge(String nom) {
        int id = -1;
        for (Charge Im : this.dataTresorerie.getParametreTresorerie().getCharges()) {
            if (Im.getNom().equals(nom)) {
                return Im.getId();
            }
        }
        return id;
    }

    public int getDestination(String nom) {
        if (nom.equals("BANQUE")) {
            return InterfaceEncaissement.DESTINATION_BANQUE;
        } else if (nom.equals("CAISSE")) {
            return InterfaceEncaissement.DESTINATION_CAISSE;
        } else {
            return -1;
        }
    }

    public boolean search_verifier_periode_decaiss(Decaissement Idecaissement, Date dateA, Date dateB) {
        if (Idecaissement != null) {
            boolean apresA = (Idecaissement.getDate().after(dateA) || Idecaissement.getDate().equals(dateA));
            boolean avantB = (Idecaissement.getDate().before(dateB) || Idecaissement.getDate().equals(dateB));
            if (apresA == true && avantB == true) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean search_verifier_periode_encaiss(Encaissement encaissement, Date dateA, Date dateB) {
        if (encaissement != null) {
            boolean apresA = (encaissement.getDate().after(dateA) || encaissement.getDate().equals(dateA));
            boolean avantB = (encaissement.getDate().before(dateB) || encaissement.getDate().equals(dateB));
            if (apresA == true && avantB == true) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean search_verifier_monnaie(Decaissement Idecaissement, int idMonnaie) {
        if (Idecaissement != null) {
            if (idMonnaie == -1) {
                return true;
            } else if (Idecaissement.getIdMonnaie() != idMonnaie) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean search_verifier_source(Decaissement Idecaissement, int idSource) {
        if (Idecaissement != null) {
            if (idSource == -1) {
                return true;
            } else if (Idecaissement.getSource() != idSource) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean search_verifier_charge(Decaissement Idecaissement, int idCharge) {
        if (Idecaissement != null) {
            if (idCharge == -1) {
                return true;
            } else if (Idecaissement.getIdCharge() != idCharge) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean search_verifier_motcle_encaiss(Encaissement encaissement, String motcle) {
        if (encaissement != null) {
            if (motcle.trim().length() == 0) {
                return true;
            } else if (UtilTresorerie.contientMotsCles(encaissement.getEffectuePar() + " " + encaissement.getMotif() + " " + encaissement.getReference(), motcle) == false) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean search_verifier_motcle_decaiss(Decaissement decaissement, String motcle) {
        if (decaissement != null) {
            if (motcle.trim().length() == 0) {
                return true;
            } else if (UtilTresorerie.contientMotsCles(decaissement.getBeneficiaire(), motcle) == false && UtilTresorerie.contientMotsCles(decaissement.getMotif(), motcle) == false && UtilTresorerie.contientMotsCles(decaissement.getReference(), motcle) == false) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Entreprise getEntreprise() {
        return this.dataTresorerie.getParametreTresorerie().getEntreprise();
    }

    public int getIndexTabSelected() {
        return indexTabSelected;
    }

    public String getNomUtilisateur() {
        return this.dataTresorerie.getParametreTresorerie().getUtilisateur().getNom();
    }

    public String getTitreDoc() {
        if (typeExport == 0) {
            return "ENCAISSEMENTS";
        } else if (typeExport == 1) {
            return "DECAISSEMENTS";
        } else {
            return "TRESORERIE";
        }
    }

    public Date getDateDocument() {
        return new Date();
    }

    private void initMonnaieTotaux() {
        String labTaux = "Taux de change des monnaies enregistrées: ";
        Monnaie monnaieLocal = null;
        combototMonnaie.removeAllItems();
        for (Monnaie monnaie : this.dataTresorerie.getParametreTresorerie().getMonnaies()) {
            combototMonnaie.addItem(monnaie.getCode() + " - " + monnaie.getNom());
            if (monnaie.getTauxMonnaieLocale() == 1) {
                monnaieLocal = monnaie;
            }
        }
        for (Monnaie monnaie : this.dataTresorerie.getParametreTresorerie().getMonnaies()) {
            if (monnaie != monnaieLocal) {
                labTaux += " 1 " + monnaie.getCode() + " = " + UtilTresorerie.getMontantFrancais(monnaie.getTauxMonnaieLocale()) + " " + monnaieLocal.getCode() + ", ";
            }
        }
        labTauxDeChange.setText(labTaux);
    }

    private Monnaie getSelectedMonnaieTotaux() {
        if (dataTresorerie != null) {
            for (Monnaie monnaie : this.dataTresorerie.getParametreTresorerie().getMonnaies()) {
                if ((monnaie.getCode() + " - " + monnaie.getNom()).equals(combototMonnaie.getSelectedItem() + "")) {
                    return monnaie;
                }
            }
        }

        return null;
    }

    private Monnaie getMonnaie(int idMonnaie) {
        for (Monnaie monnaie : this.dataTresorerie.getParametreTresorerie().getMonnaies()) {
            if (monnaie.getId() == idMonnaie) {
                return monnaie;
            }
        }
        return null;
    }

    private double getMontant(Monnaie ImonnaieOutput, Encaissement intEncaiss) {
        if (intEncaiss != null && ImonnaieOutput != null) {
            if (ImonnaieOutput.getId() == intEncaiss.getIdMonnaie()) {
                return intEncaiss.getMontant();
            } else {
                Monnaie ImonnaieOrigine = getMonnaie(intEncaiss.getIdMonnaie());
                if (ImonnaieOrigine != null) {
                    double montMonLocal = intEncaiss.getMontant() * ImonnaieOrigine.getTauxMonnaieLocale();
                    return (montMonLocal / ImonnaieOutput.getTauxMonnaieLocale());
                } else {
                    return 0;
                }
            }
        } else {
            return 0;
        }
    }

    private double getMontant(Monnaie ImonnaieOutput, Decaissement intDecaiss) {
        if (intDecaiss != null && ImonnaieOutput != null) {
            if (ImonnaieOutput.getId() == intDecaiss.getIdMonnaie()) {
                return intDecaiss.getMontant();
            } else {
                Monnaie ImonnaieOrigine = getMonnaie(intDecaiss.getIdMonnaie());
                double montMonLocal = intDecaiss.getMontant() * ImonnaieOrigine.getTauxMonnaieLocale();
                return (montMonLocal / ImonnaieOutput.getTauxMonnaieLocale());
            }
        } else {
            return 0;
        }
    }

    public void setDonneesEncaissement_all(Vector resultatTotalObjets) {
        this.resultatTotalObjetsEncaiss = resultatTotalObjets;
        actualiserTotalEncaissement();
    }

    public void setDonneesDecaissement_all(Vector resultatTotalObjets) {
        this.resultatTotalObjetsDecaiss = resultatTotalObjets;
        actualiserTotalDecaissement();
    }

    public void actualiserTotalEncaissement() {
        Monnaie ImonnaieOutput = getSelectedMonnaieTotaux();
        if (ImonnaieOutput != null) {
            monnaieOutput = ImonnaieOutput.getCode();
            double totalListe = 0;
            for (Object oo : resultatTotalObjetsEncaiss) {
                totalListe += getMontant(ImonnaieOutput, (Encaissement) oo);
            }

            double totalSel = 0;
            String montantSelected = "";
            if (modeleListeEncaissement != null) {
                for (Encaissement intEncaiss : modeleListeEncaissement.getListeData()) {
                    totalSel += getMontant(ImonnaieOutput, intEncaiss);
                }
                montantSelected = "| Sélection [" + modeleListeEncaissement.getListeData().size() + "] : " + UtilTresorerie.getMontantFrancais(totalSel) + " " + monnaieOutput;
            }

            totalEncaissement = totalListe;
            String solde = "SOLDE: " + UtilTresorerie.getMontantFrancais((totalEncaissement - totalDecaissement)) + " " + monnaieOutput;
            labTotauxEncaissement.setText("ENCAISSEMENT: " + UtilTresorerie.getMontantFrancais(totalListe) + " " + monnaieOutput + " " + montantSelected);
            labTotauxSolde.setText(solde);
        }
    }

    public double getTotalEncaissement() {
        actualiserTotalEncaissement();
        return totalEncaissement;
    }

    public double getTotalDecaissement() {
        actualiserTotalDecaissement();
        return totalDecaissement;
    }

    public String getMonnaieOutput() {
        return this.monnaieOutput;
    }

    public String getTauxChange() {
        return labTauxDeChange.getText();
    }

    public void actualiserTotalDecaissement() {
        Monnaie ImonnaieOutput = getSelectedMonnaieTotaux();
        if (ImonnaieOutput != null) {
            monnaieOutput = ImonnaieOutput.getCode();
            double totalListe = 0;
            for (Object oo : resultatTotalObjetsDecaiss) {
                totalListe += getMontant(ImonnaieOutput, (Decaissement) oo);
            }

            //Pour la selection
            double totalSel = 0;
            String montantSelected = "";
            if (modeleListeDecaissement != null) {
                for (Decaissement intDecaiss : modeleListeDecaissement.getListeData()) {
                    totalSel += getMontant(ImonnaieOutput, intDecaiss);
                }
                montantSelected = "| Sélection [" + modeleListeDecaissement.getListeData().size() + "] : " + UtilTresorerie.getMontantFrancais(totalSel) + " " + monnaieOutput;
            }
            totalDecaissement = totalListe;
            String solde = "SOLDE: " + UtilTresorerie.getMontantFrancais((totalEncaissement - totalDecaissement)) + " " + monnaieOutput;
            labTotauxDecaissement.setText("DECAISSEMENT: " + UtilTresorerie.getMontantFrancais(totalListe) + " " + monnaieOutput + " " + montantSelected);
            labTotauxSolde.setText(solde);
        }
    }

    private void actualiserTotaux() {
        if (indexTabSelected == 0) {//Encaissement
            actualiserTotalEncaissement();
        } else {
            actualiserTotalDecaissement();
        }
    }

    private void parametrerTableDecaissement() {
        initModelTableDecaissement();
        fixerColonnesTableDecaissement(true);
        ecouterSelectionTableDecaissement();
    }

    private void fixerColonnesTableDecaissement(boolean resizeTable) {
        //Parametrage du rendu de la table
        this.tableListeDecaissement.setDefaultRenderer(Object.class, new RenduTableDecaissement(gestionEdition, couleurBasique, icones.getModifier_01(), this.modeleListeDecaissement, dataTresorerie.getParametreTresorerie()));
        this.tableListeDecaissement.setRowHeight(25);

        //{"N°", "Date", "Source", "Reference", "Motif", "Nature", "Bénéficiaire", "Montant", "Monnaie"};
        setTaille(this.tableListeDecaissement.getColumnModel().getColumn(0), 30, true, null);
        setTaille(this.tableListeDecaissement.getColumnModel().getColumn(1), 110, true, new EditeurDateTres());
        setTaille(this.tableListeDecaissement.getColumnModel().getColumn(2), 80, true, new EditeurSourceTres());
        setTaille(this.tableListeDecaissement.getColumnModel().getColumn(3), 150, false, null);
        setTaille(this.tableListeDecaissement.getColumnModel().getColumn(4), 150, false, null);
        setTaille(this.tableListeDecaissement.getColumnModel().getColumn(5), 150, true, new EditeurChargeTres(dataTresorerie.getParametreTresorerie()));
        setTaille(this.tableListeDecaissement.getColumnModel().getColumn(6), 130, false, null);
        setTaille(this.tableListeDecaissement.getColumnModel().getColumn(7), 100, true, null);
        setTaille(this.tableListeDecaissement.getColumnModel().getColumn(8), 60, true, new EditeurMonnaieTres(dataTresorerie.getParametreTresorerie()));

        if (resizeTable == true) {
            this.tableListeDecaissement.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        }
    }

    private void initModelTableDecaissement() {
        this.modeleListeDecaissement = new ModeleListeDecaissement(gestionEdition, progress, couleurBasique, scrollListeDecaissement, btEnregistrer, mEnregistrer, new EcouteurValeursChangees() {
            @Override
            public void onValeurChangee() {
                if (ecouteurClose != null) {
                    ecouteurClose.onActualiser(modeleListeDecaissement.getRowCount() + " élement(s).", icones.getClient_01());
                }
            }
        });

        //Parametrage du modele contenant les données de la table
        this.tableListeDecaissement.setModel(this.modeleListeDecaissement);
    }

    public void reiniliserEncaissements() {
        if (modeleListeEncaissement != null) {
            modeleListeEncaissement.reinitialiserListe();
        }
        if (navigateurPagesEncaissement != null) {
            navigateurPagesEncaissement.patienter(false, "Prêt.");
        }
    }

    public void reiniliserDecaissements() {
        if (modeleListeDecaissement != null) {
            modeleListeDecaissement.reinitialiserListe();
        }
        if (navigateurPagesDecaissement != null) {
            navigateurPagesDecaissement.patienter(false, "Prêt.");
        }
    }

    private void ecouterSelectionTableDecaissement() {
        //On écoute les sélction
        tableListeDecaissement.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    selectedDecaissement = modeleListeDecaissement.getDecaissement(tableListeDecaissement.getSelectedRow());
                    actualiserTotaux();
                }
            }
        });
    }

    private void actualiserEditeur() {
        gestionEdition.reinitialiser();
        //Actualisation des listes
        modeleListeEncaissement.actualiser();
        modeleListeDecaissement.actualiser();
    }

    private void setEditionMode() {
        switch (indexTabSelected) {
            case 0:
                if (selectedEncaissement != null && gestionEdition != null) {
                    if (gestionEdition.isEditable(selectedEncaissement.getId(), indexTabSelected)) {
                        gestionEdition.setModeEdition(selectedEncaissement.getId(), indexTabSelected, false);
                    } else {
                        gestionEdition.setModeEdition(selectedEncaissement.getId(), indexTabSelected, true);
                    }
                    modeleListeEncaissement.actualiser();
                }
                break;
            case 1:
                if (selectedDecaissement != null && gestionEdition != null) {
                    if (gestionEdition.isEditable(selectedDecaissement.getId(), indexTabSelected)) {
                        gestionEdition.setModeEdition(selectedDecaissement.getId(), indexTabSelected, false);
                    } else {
                        gestionEdition.setModeEdition(selectedDecaissement.getId(), indexTabSelected, true);
                    }
                    modeleListeDecaissement.actualiser();
                }
                break;
            default:
        }
    }

    private void parametrerTableEncaissement() {
        initModelTableEncaissement();
        fixerColonnesTableEncaissement(true);
        ecouterSelectionTableEncaissement();
    }

    private void initModelTableEncaissement() {
        this.modeleListeEncaissement = new ModeleListeEncaissement(gestionEdition, progress, couleurBasique, scrollListeEncaissement, btEnregistrer, mEnregistrer, new EcouteurValeursChangees() {
            @Override
            public void onValeurChangee() {
                if (ecouteurClose != null) {
                    ecouteurClose.onActualiser(modeleListeEncaissement.getRowCount() + " élement(s).", icones.getClient_01());
                }
            }
        });
        //Parametrage du modele contenant les données de la table
        this.tableListeEncaissement.setModel(this.modeleListeEncaissement);
    }

    private void fixerColonnesTableEncaissement(boolean resizeTable) {
        //Parametrage du rendu de la table
        this.tableListeEncaissement.setDefaultRenderer(Object.class, new RenduTableEncaissement(gestionEdition, couleurBasique, icones.getModifier_01(), this.modeleListeEncaissement, dataTresorerie.getParametreTresorerie()));
        this.tableListeEncaissement.setRowHeight(25);

        //{"N°", "Date", "Destination", "Reference", "Motif", "Nature", "Effectué par", "Montant", "Monnaie"};
        setTaille(this.tableListeEncaissement.getColumnModel().getColumn(0), 30, true, null);
        setTaille(this.tableListeEncaissement.getColumnModel().getColumn(1), 110, true, new EditeurDateTres());
        setTaille(this.tableListeEncaissement.getColumnModel().getColumn(2), 80, true, new EditeurDestinationTres());
        setTaille(this.tableListeEncaissement.getColumnModel().getColumn(3), 150, false, null);
        setTaille(this.tableListeEncaissement.getColumnModel().getColumn(4), 150, false, null);
        setTaille(this.tableListeEncaissement.getColumnModel().getColumn(5), 150, true, new EditeurRevenuTres(dataTresorerie.getParametreTresorerie()));
        setTaille(this.tableListeEncaissement.getColumnModel().getColumn(6), 130, false, null);
        setTaille(this.tableListeEncaissement.getColumnModel().getColumn(7), 100, true, null);
        setTaille(this.tableListeEncaissement.getColumnModel().getColumn(8), 60, true, new EditeurMonnaieTres(dataTresorerie.getParametreTresorerie()));

        if (resizeTable == true) {
            this.tableListeEncaissement.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        }
    }

    public void ecouterSelectionTableEncaissement() {
        //On écoute les sélction
        tableListeEncaissement.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    selectedEncaissement = modeleListeEncaissement.getEncaissement(tableListeEncaissement.getSelectedRow());
                    actualiserTotaux();
                }
            }
        });
    }

    private void setTaille(TableColumn column, int taille, boolean fixe, TableCellEditor editor) {
        column.setPreferredWidth(taille);
        if (fixe == true) {
            column.setMaxWidth(taille);
            column.setMinWidth(taille);
        }
        if (editor != null) {
            column.setCellEditor(editor);
        }
    }

    public void init() {
        this.moi = this;
        this.labInfos.setIcon(icones.getInfos_01());
        this.labInfos.setText("Prêt.");

        this.ecouteurClose = new EcouteurUpdateClose() {
            @Override
            public void onFermer() {
                parent.remove(moi);
            }

            @Override
            public void onActualiser(String texte, ImageIcon icone) {
                labInfos.setText(texte);
                labInfos.setIcon(icone);
            }
        };

        this.ecouteurAjout = new EcouteurAjoutTresorerie() {
            @Override
            public void setAjoutEncaissement(ModeleListeEncaissement modeleListeEncaissement) {
                if (modeleListeEncaissement != null) {
                    int index = (modeleListeEncaissement.getRowCount() + 1);
                    int idExercice = dataTresorerie.getParametreTresorerie().getExercice().getId();
                    int idUtilisateur = dataTresorerie.getParametreTresorerie().getUtilisateur().getId();
                    Date date = new Date();
                    int id = -1;
                    int dest = InterfaceEncaissement.DESTINATION_CAISSE;
                    String reference = dest + "ENC" + index;
                    double montant = 0;
                    int idMonnaie = dataTresorerie.getParametreTresorerie().getMonnaies().firstElement().getId();
                    String monnaie = dataTresorerie.getParametreTresorerie().getMonnaies().firstElement().getCode();
                    String effectuePar = "";
                    String motif = "";
                    int idRevenu = -1;
                    String revenu = "";
                    int beta = InterfaceEncaissement.BETA_NOUVEAU;

                    modeleListeEncaissement.AjouterEncaissement(new Encaissement(id, dest, reference, date, montant, idMonnaie, monnaie, effectuePar, motif, idRevenu, revenu, idExercice, idUtilisateur, UtilObjet.getSignature(), beta));
                    //On sélectionne la première ligne
                    tableListeEncaissement.setRowSelectionInterval(0, 0);
                }
            }

            @Override
            public void setAjoutDecaissement(ModeleListeDecaissement modeleListeDecaissement) {
                if (modeleListeDecaissement != null) {
                    int index = (modeleListeDecaissement.getRowCount() + 1);
                    int idExercice = dataTresorerie.getParametreTresorerie().getExercice().getId();
                    int idUtilisateur = dataTresorerie.getParametreTresorerie().getUtilisateur().getId();
                    Date date = new Date();
                    int id = -1;
                    int source = InterfaceDecaissement.SOURCE_CAISSE;
                    String reference = source + "DEC" + index;
                    double montant = 0;
                    int idMonnaie = dataTresorerie.getParametreTresorerie().getMonnaies().firstElement().getId();
                    String monnaie = dataTresorerie.getParametreTresorerie().getMonnaies().firstElement().getCode();
                    String beneficiaire = "";
                    String motif = "";
                    int idCharge = -1;
                    String charge = "";
                    int beta = InterfaceDecaissement.BETA_NOUVEAU;

                    modeleListeDecaissement.AjouterDecaissement(new Decaissement(id, source, reference, date, montant, idMonnaie, monnaie, beneficiaire, motif, idCharge, charge, idExercice, idUtilisateur, UtilObjet.getSignature(), beta));
                    //On sélectionne la première ligne
                    tableListeDecaissement.setRowSelectionInterval(0, 0);
                }
            }
        };

        setBoutons();
        setMenuContextuel();
    }

    public void activerBoutons(int selectedTab) {
        this.indexTabSelected = selectedTab;
        actualiserTotaux();
        actualiserBtExportTab();
        switch (indexTabSelected) {
            case 0://Tab Encaissement
                if (modeleListeEncaissement != null && tableListeEncaissement != null) {
                    InterfaceEncaissement enc = modeleListeEncaissement.getEncaissement(tableListeEncaissement.getSelectedRow());
                    if (enc != null) {
                        this.ecouteurClose.onActualiser("Encaiss...:" + UtilTresorerie.getDateFrancais(enc.getDate()) + " " + enc.getEffectuePar() + " " + UtilTresorerie.getMontantFrancais(enc.getMontant()) + ".", icones.getClient_01());
                    }
                }
                break;
            case 1://Tab Decaissement
                if (modeleListeDecaissement != null && tableListeDecaissement != null) {
                    InterfaceDecaissement dec = modeleListeDecaissement.getDecaissement(tableListeDecaissement.getSelectedRow());
                    if (dec != null) {
                        this.ecouteurClose.onActualiser("Encaiss...:" + UtilTresorerie.getDateFrancais(dec.getDate()) + " " + dec.getBeneficiaire() + " " + UtilTresorerie.getMontantFrancais(dec.getMontant()) + ".", icones.getClient_01());
                    }
                }
                break;
        }
    }

    public void ajouter() {
        switch (indexTabSelected) {
            case 0: //Tab Encaissement
                this.ecouteurAjout.setAjoutEncaissement(modeleListeEncaissement);
                break;
            case 1: //Tab Decaissement
                this.ecouteurAjout.setAjoutDecaissement(modeleListeDecaissement);
                break;
        }
    }

    public void supprimer() {
        switch (indexTabSelected) {
            case 0: //Tab Encaissement
                modeleListeEncaissement.SupprimerEncaissement(tableListeEncaissement.getSelectedRow(), new EcouteurSuppressionElement() {
                    @Override
                    public void onSuppressionConfirmee(int idElement, long signature) {
                        if (ecouteurTresorerie != null) {
                            ecouteurTresorerie.onDetruitElement(idElement, indexTabSelected, signature);
                        }
                    }
                });
                break;
            case 1: //Tab Decaissement
                modeleListeDecaissement.SupprimerDecaissement(tableListeDecaissement.getSelectedRow(), new EcouteurSuppressionElement() {
                    @Override
                    public void onSuppressionConfirmee(int idElement, long signature) {
                        if (ecouteurTresorerie != null) {
                            ecouteurTresorerie.onDetruitElement(idElement, indexTabSelected, signature);
                        }
                    }
                });
                break;
        }
    }

    public void vider() {
        this.ecouteurClose.onActualiser("Vidé!", icones.getInfos_01());
        switch (indexTabSelected) {
            case 0: //Tab Encaissement
                modeleListeEncaissement.viderListe();
                break;
            case 1: //Tab Decaissement
                modeleListeDecaissement.viderListe();
                break;
        }

    }

    private void setIconesTabs() {
        this.tabPrincipal.setIconAt(0, icones.getEntrer_01());  //Encaissement
        this.tabPrincipal.setIconAt(1, icones.getSortie_01());  //Decaissement
        this.labTotauxEncaissement.setIcon(icones.getNombre_01());
        this.labTotauxDecaissement.setIcon(icones.getNombre_01());
        this.labTotauxSolde.setIcon(icones.getNombre_01());
    }

    private void setBoutons() {
        btAjouter = new Bouton(12, "Ajouter", "Ajouter un element", false, icones.getAjouter_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                ajouter();
            }
        });

        btSupprimer = new Bouton(12, "Supprimer", "Supprimer", false, icones.getSupprimer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                supprimer();
            }
        });

        btEnregistrer = new Bouton(12, "Enregistrer", "Enregistrer", false, icones.getEnregistrer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                enregistrer();
            }
        });
        btEnregistrer.setGras(true);

        btVider = new Bouton(12, "Vider", "Vider la liste actuelle", false, icones.getAnnuler_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                vider();
            }
        });

        btImprimer = new Bouton(12, "Imprimer", "Imprimer", false, icones.getImprimer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                imprimer();
            }
        });

        btFermer = new Bouton(12, "Fermer", "Fermer cette fenêtre", false, icones.getFermer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                fermer();
            }
        });

        btPDF = new Bouton(12, "Exp. Tout", "Tout exporter", false, icones.getPDF_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                typeExport = -1;
                exporterPDF();
            }
        });

        btPDFSynth = new Bouton(12, "Exporter", "Exporter cette sélection", false, icones.getPDF_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                typeExport = indexTabSelected;
                exporterPDF();
            }
        });

        btActualiser = new Bouton(12, "Actualiser", "Actualiser", false, icones.getSynchroniser_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                actualiser();
            }
        });

        btEdition = new Bouton(12, "Edition", "", true, icones.getModifier_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                setEditionMode();
            }
        });

        //Il faut respecter les droits d'accès attribué à l'utilisateur actuel!
        bOutils = new BarreOutils(barreOutils);
        if (dataTresorerie.getParametreTresorerie().getUtilisateur() != null) {
            Utilisateur user = dataTresorerie.getParametreTresorerie().getUtilisateur();
            
            if (user.getDroitTresorerie() == InterfaceUtilisateur.DROIT_CONTROLER) {
                bOutils.AjouterBouton(btEnregistrer);
                bOutils.AjouterBouton(btAjouter);
                bOutils.AjouterBouton(btEdition);
                bOutils.AjouterSeparateur();
                bOutils.AjouterBouton(btSupprimer);
                bOutils.AjouterBouton(btVider);
            }
            bOutils.AjouterBouton(btActualiser);
            bOutils.AjouterSeparateur();
            bOutils.AjouterBouton(btImprimer);
            bOutils.AjouterBouton(btPDF);
            bOutils.AjouterBouton(btPDFSynth);
            bOutils.AjouterSeparateur();
            bOutils.AjouterBouton(btFermer);
        }
    }

    private void setMenuContextuel() {
        mAjouter = new RubriqueSimple("Ajouter", 12, false, icones.getAjouter_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                ajouter();
            }
        });

        mSupprimer = new RubriqueSimple("Supprimer", 12, false, icones.getSupprimer_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                supprimer();
            }
        });

        mEnregistrer = new RubriqueSimple("Enregistrer", 12, true, icones.getEnregistrer_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                enregistrer();
            }
        });

        mVider = new RubriqueSimple("Vider", 12, false, icones.getAnnuler_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                vider();
            }
        });

        mImprimer = new RubriqueSimple("Imprimer", 12, false, icones.getImprimer_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                imprimer();
            }
        });

        mFermer = new RubriqueSimple("Fermer", 12, false, icones.getFermer_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                fermer();
            }
        });

        mPDF = new RubriqueSimple("Exporter tout", 12, false, icones.getPDF_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                typeExport = -1;
                exporterPDF();
            }
        });

        mActualiser = new RubriqueSimple("Actualiser", 12, false, icones.getSynchroniser_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                actualiser();
            }
        });

        mPDFSynth = new RubriqueSimple("Exporter", 12, true, icones.getPDF_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                typeExport = indexTabSelected;
                exporterPDF();
            }
        });

        mEdition = new RubriqueSimple("Editer", 12, false, icones.getModifier_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                setEditionMode();
            }
        });

        //Il faut respecter les droits d'accès attribué à l'utilisateur actuel!
        menuContextuel = new MenuContextuel();
        if (dataTresorerie.getParametreTresorerie().getUtilisateur() != null) {
            Utilisateur user = dataTresorerie.getParametreTresorerie().getUtilisateur();

            if (user.getDroitTresorerie() == InterfaceUtilisateur.DROIT_CONTROLER) {
                menuContextuel.Ajouter(mEnregistrer);
                menuContextuel.Ajouter(mAjouter);
                menuContextuel.Ajouter(mEdition);
                menuContextuel.Ajouter(new JPopupMenu.Separator());
                menuContextuel.Ajouter(mSupprimer);
                menuContextuel.Ajouter(mVider);
            }
            menuContextuel.Ajouter(mActualiser);
            menuContextuel.Ajouter(new JPopupMenu.Separator());
            menuContextuel.Ajouter(mImprimer);
            menuContextuel.Ajouter(mPDF);
            menuContextuel.Ajouter(mPDFSynth);
            menuContextuel.Ajouter(new JPopupMenu.Separator());
            menuContextuel.Ajouter(mFermer);
        }
    }

    private boolean mustBeSaved() {
        boolean rep = false;
        //On vérifie dans la liste d'encaissements
        for (InterfaceEncaissement Ienc : this.modeleListeEncaissement.getListeData()) {
            if (Ienc.getBeta() == InterfaceEncaissement.BETA_MODIFIE || Ienc.getBeta() == InterfaceEncaissement.BETA_NOUVEAU) {
                rep = true;
            }
        }

        //On vérifie aussi dans la liste de decaissements
        for (InterfaceDecaissement Idec : this.modeleListeDecaissement.getListeData()) {
            if (Idec.getBeta() == InterfaceDecaissement.BETA_MODIFIE || Idec.getBeta() == InterfaceDecaissement.BETA_NOUVEAU) {
                rep = true;
            }
        }
        return rep;
    }

    public void fermer() {
        //Vérifier s'il n'y a rien à enregistrer
        if (mustBeSaved() == true) {
            int dialogResult = JOptionPane.showConfirmDialog(this, "Voulez-vous enregistrer les modifications et/ou ajouts apportés à ces données?", "Avertissement", JOptionPane.YES_NO_CANCEL_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                this.ecouteurTresorerie.onEnregistre(getSortieTresorerie(btEnregistrer, mEnregistrer));
                this.ecouteurClose.onFermer();
            } else if (dialogResult == JOptionPane.NO_OPTION) {
                this.ecouteurClose.onFermer();
            }
        } else {
            int dialogResult = JOptionPane.showConfirmDialog(this, "Etes-vous sûr de vouloir fermer cette fenêtre?", "Avertissement", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                this.ecouteurClose.onFermer();
            }
        }
    }

    public void imprimer() {
        int dialogResult = JOptionPane.showConfirmDialog(this, "Etes-vous sûr de vouloir imprimer ce document?", "Avertissement", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            try {
                SortiesTresorerie sortie = getSortieTresorerie(btImprimer, mImprimer);
                DocumentPDFTres documentPDF = new DocumentPDFTres(this, DocumentPDFTres.ACTION_IMPRIMER, sortie);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ParametreTresorerie getParametreTresorerie() {
        return dataTresorerie.getParametreTresorerie();
    }

    public String getNomfichierPreuve() {
        return "TresorerieS2B.pdf";
    }

    private SortiesTresorerie getSortieTresorerie(Bouton boutonDeclencheur, RubriqueSimple rubriqueDeclencheur) {
        EcouteurEnregistrement ecouteurEnregistrement = new EcouteurEnregistrement() {
            @Override
            public void onDone(String message) {
                ecouteurClose.onActualiser(message, icones.getAimer_01());
                if (boutonDeclencheur != null) {
                    boutonDeclencheur.appliquerDroitAccessDynamique(true);
                }
                if (rubriqueDeclencheur != null) {
                    rubriqueDeclencheur.appliquerDroitAccessDynamique(true);
                }

                //On redessine les tableaux afin que les couleurs se réinitialisent / Tout redevient noire
                if (modeleListeEncaissement != null) {
                    modeleListeEncaissement.redessinerTable();
                }
                if (modeleListeDecaissement != null) {
                    modeleListeDecaissement.redessinerTable();
                }
            }

            @Override
            public void onError(String message) {
                ecouteurClose.onActualiser(message, icones.getAlert_01());
                if (boutonDeclencheur != null) {
                    boutonDeclencheur.appliquerDroitAccessDynamique(true);
                }
                if (rubriqueDeclencheur != null) {
                    rubriqueDeclencheur.appliquerDroitAccessDynamique(true);
                }
            }

            @Override
            public void onUploading(String message) {
                ecouteurClose.onActualiser(message, icones.getInfos_01());
                if (boutonDeclencheur != null) {
                    boutonDeclencheur.appliquerDroitAccessDynamique(false);
                }
                if (rubriqueDeclencheur != null) {
                    rubriqueDeclencheur.appliquerDroitAccessDynamique(false);
                }
            }
        };
        SortiesTresorerie sortieEA = new SortiesTresorerie(ecouteurEnregistrement, this.modeleListeDecaissement.getListeData(), this.modeleListeEncaissement.getListeData());
        return sortieEA;
    }

    public void enregistrer() {
        if (this.ecouteurTresorerie != null) {
            btEnregistrer.setForeground(Color.black);
            mEnregistrer.setCouleur(Color.BLACK);

            SortiesTresorerie sortie = getSortieTresorerie(btEnregistrer, mEnregistrer);
            this.ecouteurTresorerie.onEnregistre(sortie);

            actualiserEditeur();
        }
    }

    public void exporterPDF() {
        int dialogResult = JOptionPane.showConfirmDialog(this, "Voulez-vous les exporter dans un fichier PDF?", "Avertissement", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            try {
                SortiesTresorerie sortie = getSortieTresorerie(btPDF, mPDF);
                DocumentPDFTres docpdf = new DocumentPDFTres(this, DocumentPDFTres.ACTION_OUVRIR, sortie);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public EcouteurActualisationTresorerie getEcouteurActualisationTresorerie() {
        return ecouteurActualisationTresorerie;
    }

    public void setEcouteurActualisationTresorerie(EcouteurActualisationTresorerie ecouteurActualisationTresorerie) {
        this.ecouteurActualisationTresorerie = ecouteurActualisationTresorerie;
    }

    public void actualiser() {
        if (indexTabSelected == 0) {
            if (navigateurPagesEncaissement != null) {
                navigateurPagesEncaissement.reload();
            }
        } else {
            if (navigateurPagesDecaissement != null) {
                navigateurPagesDecaissement.reload();
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        barreOutils = new javax.swing.JToolBar();
        jButton5 = new javax.swing.JButton();
        tabPrincipal = new javax.swing.JTabbedPane();
        scrollListeEncaissement = new javax.swing.JScrollPane();
        tableListeEncaissement = new javax.swing.JTable();
        scrollListeDecaissement = new javax.swing.JScrollPane();
        tableListeDecaissement = new javax.swing.JTable();
        labInfos = new javax.swing.JLabel();
        panelTotaux = new javax.swing.JPanel();
        combototMonnaie = new javax.swing.JComboBox<>();
        labTotauxEncaissement = new javax.swing.JLabel();
        labTauxDeChange = new javax.swing.JLabel();
        labTotauxDecaissement = new javax.swing.JLabel();
        labTotauxSolde = new javax.swing.JLabel();
        navigateurPagesEncaissement = new Source.UI.NavigateurPages();
        navigateurPagesDecaissement = new Source.UI.NavigateurPages();

        setBackground(new java.awt.Color(255, 255, 255));

        barreOutils.setBackground(new java.awt.Color(255, 255, 255));
        barreOutils.setFloatable(false);
        barreOutils.setRollover(true);
        barreOutils.setAutoscrolls(true);
        barreOutils.setPreferredSize(new java.awt.Dimension(59, 61));

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture02.png"))); // NOI18N
        jButton5.setText("Ajouter");
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        barreOutils.add(jButton5);

        tabPrincipal.setBackground(new java.awt.Color(255, 255, 255));
        tabPrincipal.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabPrincipalStateChanged(evt);
            }
        });

        scrollListeEncaissement.setBackground(new java.awt.Color(255, 255, 255));
        scrollListeEncaissement.setAutoscrolls(true);
        scrollListeEncaissement.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollListeEncaissementMouseClicked(evt);
            }
        });

        tableListeEncaissement.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Article", "Qunatité", "Unités", "Prix Unitaire HT", "Tva %", "Tva", "Total TTC"
            }
        ));
        tableListeEncaissement.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                tableListeEncaissementMouseDragged(evt);
            }
        });
        tableListeEncaissement.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableListeEncaissementMouseClicked(evt);
            }
        });
        tableListeEncaissement.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tableListeEncaissementKeyReleased(evt);
            }
        });
        scrollListeEncaissement.setViewportView(tableListeEncaissement);

        tabPrincipal.addTab("Encaissements", scrollListeEncaissement);

        scrollListeDecaissement.setBackground(new java.awt.Color(255, 255, 255));
        scrollListeDecaissement.setAutoscrolls(true);
        scrollListeDecaissement.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                scrollListeDecaissementMouseClicked(evt);
            }
        });

        tableListeDecaissement.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Article", "Qunatité", "Unités", "Prix Unitaire HT", "Tva %", "Tva", "Total TTC"
            }
        ));
        tableListeDecaissement.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                tableListeDecaissementMouseDragged(evt);
            }
        });
        tableListeDecaissement.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableListeDecaissementMouseClicked(evt);
            }
        });
        tableListeDecaissement.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tableListeDecaissementKeyReleased(evt);
            }
        });
        scrollListeDecaissement.setViewportView(tableListeDecaissement);

        tabPrincipal.addTab("Decaissements", scrollListeDecaissement);

        labInfos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        labInfos.setText("Prêt.");

        panelTotaux.setBackground(new java.awt.Color(255, 255, 255));
        panelTotaux.setBorder(javax.swing.BorderFactory.createTitledBorder("Total"));

        combototMonnaie.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        combototMonnaie.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                combototMonnaieItemStateChanged(evt);
            }
        });

        labTotauxEncaissement.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labTotauxEncaissement.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        labTotauxEncaissement.setText("Total : 0000000000 $ ");

        labTauxDeChange.setFont(new java.awt.Font("Tahoma", 2, 10)); // NOI18N
        labTauxDeChange.setForeground(new java.awt.Color(51, 51, 255));
        labTauxDeChange.setText("Taux");

        labTotauxDecaissement.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labTotauxDecaissement.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        labTotauxDecaissement.setText("Total : 0000000000 $ ");

        labTotauxSolde.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labTotauxSolde.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        labTotauxSolde.setText("Total : 0000000000 $ ");

        javax.swing.GroupLayout panelTotauxLayout = new javax.swing.GroupLayout(panelTotaux);
        panelTotaux.setLayout(panelTotauxLayout);
        panelTotauxLayout.setHorizontalGroup(
            panelTotauxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTotauxLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTotauxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labTauxDeChange, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelTotauxLayout.createSequentialGroup()
                        .addComponent(combototMonnaie, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(labTotauxEncaissement, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labTotauxDecaissement, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labTotauxSolde, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelTotauxLayout.setVerticalGroup(
            panelTotauxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTotauxLayout.createSequentialGroup()
                .addComponent(combototMonnaie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(labTotauxEncaissement)
                .addGap(0, 0, 0)
                .addComponent(labTotauxDecaissement)
                .addGap(0, 0, 0)
                .addComponent(labTotauxSolde)
                .addGap(0, 0, 0)
                .addComponent(labTauxDeChange)
                .addGap(0, 5, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(barreOutils, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tabPrincipal)
            .addComponent(panelTotaux, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labInfos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(navigateurPagesEncaissement, javax.swing.GroupLayout.DEFAULT_SIZE, 671, Short.MAX_VALUE)
            .addComponent(navigateurPagesDecaissement, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(barreOutils, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(navigateurPagesEncaissement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(navigateurPagesDecaissement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(tabPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(panelTotaux, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labInfos)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tableListeEncaissementMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListeEncaissementMouseClicked
        // TODO add your handling code here:
        //ecouterMenContA(evt, 0);
    }//GEN-LAST:event_tableListeEncaissementMouseClicked

    private void scrollListeEncaissementMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollListeEncaissementMouseClicked
        // TODO add your handling code here:
        //ecouterMenContA(evt, 0);
    }//GEN-LAST:event_scrollListeEncaissementMouseClicked

    private void tableListeDecaissementMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListeDecaissementMouseClicked
        // TODO add your handling code here:
        //ecouterMenContA(evt, 1);
    }//GEN-LAST:event_tableListeDecaissementMouseClicked

    private void scrollListeDecaissementMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollListeDecaissementMouseClicked
        // TODO add your handling code here:
        //ecouterMenContA(evt, 1);
    }//GEN-LAST:event_scrollListeDecaissementMouseClicked

    private void tabPrincipalStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabPrincipalStateChanged
        // TODO add your handling code here:
        activerBoutons(((JTabbedPane) evt.getSource()).getSelectedIndex());
    }//GEN-LAST:event_tabPrincipalStateChanged

    private void tableListeEncaissementKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableListeEncaissementKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_tableListeEncaissementKeyReleased

    private void tableListeDecaissementKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableListeDecaissementKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_tableListeDecaissementKeyReleased

    private void combototMonnaieItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_combototMonnaieItemStateChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_combototMonnaieItemStateChanged

    private void tableListeEncaissementMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListeEncaissementMouseDragged
        // TODO add your handling code here:

    }//GEN-LAST:event_tableListeEncaissementMouseDragged

    private void tableListeDecaissementMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListeDecaissementMouseDragged
        // TODO add your handling code here:

    }//GEN-LAST:event_tableListeDecaissementMouseDragged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barreOutils;
    private javax.swing.JComboBox<String> combototMonnaie;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel labInfos;
    private javax.swing.JLabel labTauxDeChange;
    private javax.swing.JLabel labTotauxDecaissement;
    private javax.swing.JLabel labTotauxEncaissement;
    private javax.swing.JLabel labTotauxSolde;
    private Source.UI.NavigateurPages navigateurPagesDecaissement;
    private Source.UI.NavigateurPages navigateurPagesEncaissement;
    private javax.swing.JPanel panelTotaux;
    private javax.swing.JScrollPane scrollListeDecaissement;
    private javax.swing.JScrollPane scrollListeEncaissement;
    public javax.swing.JTabbedPane tabPrincipal;
    private javax.swing.JTable tableListeDecaissement;
    private javax.swing.JTable tableListeEncaissement;
    // End of variables declaration//GEN-END:variables
}
