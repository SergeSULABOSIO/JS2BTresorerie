/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.UI;

import SOURCES.Utilitaires.MoteurRecherche;
import BEAN_BARRE_OUTILS.BarreOutils;
import BEAN_BARRE_OUTILS.Bouton;
import BEAN_BARRE_OUTILS.BoutonListener;
import BEAN_MenuContextuel.MenuContextuel;
import BEAN_MenuContextuel.RubriqueListener;
import BEAN_MenuContextuel.RubriqueSimple;
import ICONES.Icones;
import SOURCES.CallBack.EcouteurAjout;
import SOURCES.CallBack.EcouteurEnregistrement;
import SOURCES.CallBack.EcouteurTresorerie;
import SOURCES.CallBack.EcouteurUpdateClose;
import SOURCES.CallBack.EcouteurValeursChangees;
import SOURCES.EditeursTable.EditeurCharge;
import SOURCES.EditeursTable.EditeurDate;
import SOURCES.EditeursTable.EditeurDestination;
import SOURCES.EditeursTable.EditeurMonnaie;
import SOURCES.EditeursTable.EditeurRevenu;
import SOURCES.EditeursTable.EditeurSource;
import SOURCES.Interface.InterfaceDecaissement;
import SOURCES.Interface.InterfaceEncaissement;
import SOURCES.Interface.InterfaceEntreprise;
import SOURCES.Interface.InterfaceMonnaie;
import SOURCES.ModelsTable.ModeleListeDecaissement;
import SOURCES.ModelsTable.ModeleListeEncaissement;
import SOURCES.RenduComboBox.RenduCombo;
import SOURCES.RendusTables.RenduTableDecaissement;
import SOURCES.RendusTables.RenduTableEncaissement;
import SOURCES.Utilitaires.DonneesTresorerie;
import SOURCES.Utilitaires.ParametreTresorerie;
import SOURCES.Utilitaires.SortiesTresorerie;
import SOURCES.Utilitaires.Util;
import SOURCES.Utilitaires.XX_Decaissement;
import SOURCES.Utilitaires.XX_Encaissement;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

/**
 *
 * @author HP Pavilion
 */
public class Panel extends javax.swing.JPanel {

    /**
     * Creates new form Panel
     */
    public int indexTabSelected = 0;
    private Icones icones = null;
    private final JTabbedPane parent;
    private Panel moi = null;
    private EcouteurUpdateClose ecouteurClose = null;
    private EcouteurAjout ecouteurAjout = null;
    private Bouton btEnregistrer, btAjouter, btSupprimer, btVider, btImprimer, btPDF, btFermer, btActualiser;
    private RubriqueSimple mEnregistrer, mAjouter, mSupprimer, mVider, mImprimer, mPDF, mFermer, mActualiser;
    private MenuContextuel menuContextuel = null;
    private BarreOutils bOutils = null;
    private EcouteurTresorerie ecouteurTresorerie = null;

    private ModeleListeEncaissement modeleListeEncaissement;
    private ModeleListeDecaissement modeleListeDecaissement;
    private MoteurRecherche gestionnaireRecherche;
    
    public DonneesTresorerie donneesTresorerie;
    public ParametreTresorerie parametreTresorerie;
    public double totalEncaissement = 0;
    public double totalDecaissement = 0;

    public Panel(JTabbedPane parent, DonneesTresorerie donneesTresorerie, ParametreTresorerie parametreTresorerie, EcouteurTresorerie ecouteurTresorerie) {
        this.initComponents();
        this.icones = new Icones();
        this.parent = parent;
        this.init();
        this.donneesTresorerie = donneesTresorerie;
        this.parametreTresorerie = parametreTresorerie;
        this.ecouteurTresorerie = ecouteurTresorerie;

        //Initialisaterus
        parametrerTableEncaissement();
        parametrerTableDecaissement();
        setIconesTabs();

        initMonnaieTotaux();
        //actualiserTotaux("Panel");
        actualiserTotalDecaissement();
        actualiserTotalEncaissement();
        activerCriteres();
        
        initCombos();
        activerMoteurRecherche();
    }
    
    
    private void initCombos() {
        /*
        //Les sexes
        chMonnaie.removeAllItems();
        chMonnaie.addItem("MASCULIN & FEMININ");
        chMonnaie.addItem("MASCULIN");
        chMonnaie.addItem("FEMININ");
        chMonnaie.setRenderer(new RenduCombo(icones.getClient_01()));

        //Les status
        chRevenu.removeAllItems();
        chRevenu.addItem("REGULIER(E) & EXCLU(E)");
        chRevenu.addItem("REGULIER(E)");
        chRevenu.addItem("EXCLU(E)");
        chRevenu.setRenderer(new RenduCombo(icones.getAimer_01()));

        //Les calsses
        chSource.removeAllItems();
        chSource.addItem("TOUTES LES CLASSES");
        if (this.parametreInscription.getListeClasses() != null) {
            for (InterfaceClasse iClasse : this.parametreInscription.getListeClasses()) {
                chSource.addItem(iClasse.getNom() + "");
            }
        }
        chSource.setRenderer(new RenduCombo(icones.getClasse_01()));

        chRecherche.setTextInitial("Recherche : Saisissez votre mot clé ici, puis tapez ENTER");
        activerCriteres();
        
        
        */
        
        
        
        
        
    }
    
    private void activerMoteurRecherche() {
        
        /*
        gestionnaireRecherche = new MoteurRecherche(icones, chRecherche, ecouteurClose) {

            @Override
            public void chercher(String motcle) {
                //classe
                int idClasse = -1;
                for (InterfaceClasse iClasse : parametreInscription.getListeClasses()) {
                    if (iClasse.getNom().trim().equals(chSource.getSelectedItem() + "")) {
                        idClasse = iClasse.getId();
                        break;
                    }
                }

                //Sexe
                int sexe = -1;
                switch (chMonnaie.getSelectedIndex()) {
                    case 1:
                        sexe = InterfaceEleve.SEXE_MASCULIN;
                        break;
                    case 2:
                        sexe = InterfaceEleve.SEXE_FEMININ;
                        break;
                    default:
                        sexe = -1;
                        break;
                }

                //status
                int status = -1;
                switch (chRevenu.getSelectedIndex()) {
                    case 1:
                        status = InterfaceEleve.STATUS_ACTIF;
                        break;
                    case 2:
                        status = InterfaceEleve.STATUS_INACTIF;
                        break;
                    default:
                        status = -1;
                        break;
                }

                modeleListeEleve.chercher(motcle, idClasse, sexe, status);
                if (modeleListeAyantDroit != null) {
                    modeleListeAyantDroit.chercher(modeleListeEleve.getListeData());
                }
            }
        };
        
        */
        
    }

    public InterfaceEntreprise getEntreprise() {
        return this.parametreTresorerie.getEntreprise();
    }

    public int getIndexTabSelected() {
        return indexTabSelected;
    }

    public String getNomUtilisateur() {
        return this.parametreTresorerie.getNomUtilisateur();
    }

    public String getTitreDoc() {
        if (indexTabSelected == 0) {
            return "ENCAISSEMENTS";
        } else {
            return "DECAISSEMENTS";
        }
    }

    public Date getDateDocument() {
        return new Date();
    }

    public String getCritereSexe() {
        return this.chMonnaie.getSelectedItem() + "";
    }

    public String getCritereClasse() {
        return this.chSource.getSelectedItem() + "";
    }

    public String getCritereStatus() {
        return this.chRevenu.getSelectedItem() + "";
    }

    private void initMonnaieTotaux() {
        String labTaux = "Taux de change des monnaies enregistrées: ";
        InterfaceMonnaie monnaieLocal = null;
        combototMonnaie.removeAllItems();
        for (InterfaceMonnaie monnaie : this.parametreTresorerie.getMonnaies()) {
            combototMonnaie.addItem(monnaie.getCode() + " - " + monnaie.getNom());
            if (monnaie.getTauxMonnaieLocale() == 1) {
                monnaieLocal = monnaie;
            }
        }
        for (InterfaceMonnaie monnaie : this.parametreTresorerie.getMonnaies()) {
            if (monnaie != monnaieLocal) {
                labTaux += " 1 " + monnaie.getCode() + " = " + Util.getMontantFrancais(monnaie.getTauxMonnaieLocale()) + " " + monnaieLocal.getCode() + ", ";
            }
        }
        labTauxDeChange.setText(labTaux);
    }

    private InterfaceMonnaie getSelectedMonnaieTotaux() {
        for (InterfaceMonnaie monnaie : this.parametreTresorerie.getMonnaies()) {
            if ((monnaie.getCode() + " - " + monnaie.getNom()).equals(combototMonnaie.getSelectedItem() + "")) {
                return monnaie;
            }
        }
        return null;
    }

    private InterfaceMonnaie getMonnaie(int idMonnaie) {
        for (InterfaceMonnaie monnaie : this.parametreTresorerie.getMonnaies()) {
            if (monnaie.getId() == idMonnaie) {
                return monnaie;
            }
        }
        return null;
    }

    private double getMontant(InterfaceMonnaie ImonnaieOutput, InterfaceEncaissement intEncaiss) {
        if (intEncaiss != null && ImonnaieOutput != null) {
            if (ImonnaieOutput.getId() == intEncaiss.getIdMonnaie()) {
                return intEncaiss.getMontant();
            } else {
                InterfaceMonnaie ImonnaieOrigine = getMonnaie(intEncaiss.getIdMonnaie());
                double montMonLocal = intEncaiss.getMontant() * ImonnaieOrigine.getTauxMonnaieLocale();
                return (montMonLocal / ImonnaieOutput.getTauxMonnaieLocale());
            }
        } else {
            return 0;
        }
    }

    private double getMontant(InterfaceMonnaie ImonnaieOutput, InterfaceDecaissement intDecaiss) {
        if (intDecaiss != null && ImonnaieOutput != null) {
            if (ImonnaieOutput.getId() == intDecaiss.getIdMonnaie()) {
                return intDecaiss.getMontant();
            } else {
                InterfaceMonnaie ImonnaieOrigine = getMonnaie(intDecaiss.getIdMonnaie());
                double montMonLocal = intDecaiss.getMontant() * ImonnaieOrigine.getTauxMonnaieLocale();
                return (montMonLocal / ImonnaieOutput.getTauxMonnaieLocale());
            }
        } else {
            return 0;
        }
    }

    private void actualiserTotalEncaissement() {
        double totalListe = 0;
        InterfaceMonnaie ImonnaieOutput = null;
        if (modeleListeEncaissement != null) {
            ImonnaieOutput = getSelectedMonnaieTotaux();
            for (InterfaceEncaissement intEncaiss : modeleListeEncaissement.getListeData()) {
                totalListe += getMontant(ImonnaieOutput, intEncaiss);
            }
        }

        //Pour la selection
        int[] tabLignesSelected = tableListeEncaissement.getSelectedRows();
        double totalSel = 0;
        for (int i = 0; i < tabLignesSelected.length; i++) {
            if (tabLignesSelected[i] != -1) {
                if (modeleListeEncaissement != null) {
                    InterfaceEncaissement intEncaiss = modeleListeEncaissement.getEncaissement(tabLignesSelected[i]);
                    if (intEncaiss != null && ImonnaieOutput != null) {
                        //totalSel += intEncaiss.getMontant();
                        totalSel += getMontant(ImonnaieOutput, intEncaiss);
                    }
                }
            }
        }

        String monnaieOutput = "";
        if (ImonnaieOutput != null) {
            monnaieOutput = ImonnaieOutput.getCode();
        }
        String montantSelected = "";
        if (tabLignesSelected.length != 0) {
            montantSelected = "| Sélection [" + tabLignesSelected.length + "] : " + Util.getMontantFrancais(totalSel) + " " + monnaieOutput;
        }
        totalEncaissement = totalListe;
        String solde = "SOLDE: " + Util.getMontantFrancais((totalEncaissement - totalDecaissement)) + " " + monnaieOutput;
        labTotauxEncaissement.setText("ENCAISSEMENT: " + Util.getMontantFrancais(totalListe) + " " + monnaieOutput + " " + montantSelected);
        labTotauxSolde.setText(solde);
    }

    private void actualiserTotalDecaissement() {
        double totalListe = 0;
        InterfaceMonnaie ImonnaieOutput = null;
        if (modeleListeDecaissement != null) {
            ImonnaieOutput = getSelectedMonnaieTotaux();
            for (InterfaceDecaissement intDecaiss : modeleListeDecaissement.getListeData()) {
                totalListe += getMontant(ImonnaieOutput, intDecaiss);
            }
        }

        //Pour la selection
        int[] tabLignesSelected = tableListeDecaissement.getSelectedRows();
        double totalSel = 0;
        for (int i = 0; i < tabLignesSelected.length; i++) {
            if (tabLignesSelected[i] != -1) {
                if (modeleListeDecaissement != null) {
                    InterfaceDecaissement intDecaiss = modeleListeDecaissement.getDecaissement(tabLignesSelected[i]);
                    if (intDecaiss != null && ImonnaieOutput != null) {
                        //totalSel += intEncaiss.getMontant();
                        totalSel += getMontant(ImonnaieOutput, intDecaiss);
                    }
                }
            }
        }

        String monnaieOutput = "";
        if (ImonnaieOutput != null) {
            monnaieOutput = ImonnaieOutput.getCode();
        }
        String montantSelected = "";
        if (tabLignesSelected.length != 0) {
            montantSelected = "| Sélection [" + tabLignesSelected.length + "] : " + Util.getMontantFrancais(totalSel) + " " + monnaieOutput;
        }
        totalDecaissement = totalListe;
        String solde = "SOLDE: " + Util.getMontantFrancais((totalEncaissement - totalDecaissement)) + " " + monnaieOutput;
        labTotauxDecaissement.setText("DECAISSEMENT: " + Util.getMontantFrancais(totalListe) + " " + monnaieOutput + " " + montantSelected);
        labTotauxSolde.setText(solde);
    }

    private void actualiserTotaux(String methode) {
        if (indexTabSelected == 0) {//Encaissement
            System.out.println("actualiserTotaux - Encaissement - " + methode);
            actualiserTotalEncaissement();
        } else {
            System.out.println("actualiserTotaux - Decaissement - " + methode);
            actualiserTotalDecaissement();
        }
    }

    private void parametrerTableDecaissement() {
        this.modeleListeDecaissement = new ModeleListeDecaissement(scrollListeDecaissement, new EcouteurValeursChangees() {
            @Override
            public void onValeurChangee() {
                if (ecouteurClose != null) {
                    ecouteurClose.onActualiser(modeleListeDecaissement.getRowCount() + " élement(s).", icones.getClient_01());
                }
            }
        });

        //Parametrage du modele contenant les données de la table
        this.tableListeDecaissement.setModel(this.modeleListeDecaissement);

        if (this.donneesTresorerie != null) {
            if (this.donneesTresorerie.getDecaissements().size() != 0) {
                modeleListeDecaissement.setListeDecaissements(this.donneesTresorerie.getDecaissements());

            }
        }

        //Parametrage du rendu de la table
        this.tableListeDecaissement.setDefaultRenderer(Object.class, new RenduTableDecaissement(icones.getModifier_01(), this.modeleListeDecaissement, this.parametreTresorerie));
        this.tableListeDecaissement.setRowHeight(25);

        //{"N°", "Date", "Source", "Reference", "Motif", "Nature", "Bénéficiaire", "Montant", "Monnaie"};
        setTaille(this.tableListeDecaissement.getColumnModel().getColumn(0), 30, true, null);
        setTaille(this.tableListeDecaissement.getColumnModel().getColumn(1), 110, true, new EditeurDate());
        setTaille(this.tableListeDecaissement.getColumnModel().getColumn(2), 80, true, new EditeurSource());
        setTaille(this.tableListeDecaissement.getColumnModel().getColumn(3), 150, false, null);
        setTaille(this.tableListeDecaissement.getColumnModel().getColumn(4), 150, false, null);
        setTaille(this.tableListeDecaissement.getColumnModel().getColumn(5), 150, true, new EditeurCharge(parametreTresorerie));
        setTaille(this.tableListeDecaissement.getColumnModel().getColumn(6), 130, false, null);
        setTaille(this.tableListeDecaissement.getColumnModel().getColumn(7), 100, true, null);
        setTaille(this.tableListeDecaissement.getColumnModel().getColumn(8), 60, true, new EditeurMonnaie(parametreTresorerie));

        //On écoute les sélction
        tableListeDecaissement.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    actualiserTotaux("ecouterSelection - Table Decaissement");
                }
            }
        });
    }

    private void parametrerTableEncaissement() {
        this.modeleListeEncaissement = new ModeleListeEncaissement(scrollListeEncaissement, new EcouteurValeursChangees() {
            @Override
            public void onValeurChangee() {
                if (ecouteurClose != null) {
                    ecouteurClose.onActualiser(modeleListeEncaissement.getRowCount() + " élement(s).", icones.getClient_01());
                }
            }
        });

        //Parametrage du modele contenant les données de la table
        this.tableListeEncaissement.setModel(this.modeleListeEncaissement);

        if (this.donneesTresorerie != null) {
            if (this.donneesTresorerie.getEncaissements().size() != 0) {
                modeleListeEncaissement.setListeEncaissements(this.donneesTresorerie.getEncaissements());

            }
        }

        //Parametrage du rendu de la table
        this.tableListeEncaissement.setDefaultRenderer(Object.class, new RenduTableEncaissement(icones.getModifier_01(), this.modeleListeEncaissement, this.parametreTresorerie));
        this.tableListeEncaissement.setRowHeight(25);

        //{"N°", "Date", "Destination", "Reference", "Motif", "Nature", "Effectué par", "Montant", "Monnaie"};
        setTaille(this.tableListeEncaissement.getColumnModel().getColumn(0), 30, true, null);
        setTaille(this.tableListeEncaissement.getColumnModel().getColumn(1), 110, true, new EditeurDate());
        setTaille(this.tableListeEncaissement.getColumnModel().getColumn(2), 80, true, new EditeurDestination());
        setTaille(this.tableListeEncaissement.getColumnModel().getColumn(3), 150, false, null);
        setTaille(this.tableListeEncaissement.getColumnModel().getColumn(4), 150, false, null);
        setTaille(this.tableListeEncaissement.getColumnModel().getColumn(5), 150, true, new EditeurRevenu(parametreTresorerie));
        setTaille(this.tableListeEncaissement.getColumnModel().getColumn(6), 130, false, null);
        setTaille(this.tableListeEncaissement.getColumnModel().getColumn(7), 100, true, null);
        setTaille(this.tableListeEncaissement.getColumnModel().getColumn(8), 60, true, new EditeurMonnaie(parametreTresorerie));

        //On écoute les sélction
        tableListeEncaissement.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    actualiserTotaux("ecouterSelection - Table Encaissement");
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

    private void setBoutons() {
        btAjouter = new Bouton(12, "Ajouter", icones.getAjouter_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                ajouter();
            }
        });

        btSupprimer = new Bouton(12, "Supprimer", icones.getSupprimer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                supprimer();
            }
        });

        btEnregistrer = new Bouton(12, "Enregistrer", icones.getEnregistrer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                enregistrer();
            }
        });

        btVider = new Bouton(12, "Vider", icones.getAnnuler_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                vider();
            }
        });

        btImprimer = new Bouton(12, "Imprimer", icones.getImprimer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                imprimer();
            }
        });

        btFermer = new Bouton(12, "Fermer", icones.getFermer_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                fermer();
            }
        });

        btPDF = new Bouton(12, "Exp. en PDF", icones.getPDF_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                exporterPDF();
            }
        });

        btActualiser = new Bouton(12, "Actualiser", icones.getSynchroniser_02(), new BoutonListener() {
            @Override
            public void OnEcouteLeClick() {
                actualiser();
            }
        });

        bOutils = new BarreOutils(barreOutils);
        bOutils.AjouterBouton(btEnregistrer);
        bOutils.AjouterSeparateur();
        bOutils.AjouterBouton(btAjouter);
        bOutils.AjouterBouton(btSupprimer);
        bOutils.AjouterBouton(btVider);
        bOutils.AjouterBouton(btActualiser);
        bOutils.AjouterSeparateur();
        bOutils.AjouterBouton(btImprimer);
        bOutils.AjouterBouton(btPDF);
        bOutils.AjouterSeparateur();
        bOutils.AjouterBouton(btFermer);
    }

    private void ecouterMenContA(java.awt.event.MouseEvent evt, int tab) {
        if (evt.getButton() == MouseEvent.BUTTON3) {
            switch (tab) {
                case 0: //Tab Monnaie
                    menuContextuel.afficher(scrollListeEncaissement, evt.getX(), evt.getY());
                    break;
                case 1: //Tab classe
                    menuContextuel.afficher(scrollListeDecaissement, evt.getX(), evt.getY());
                    break;
            }
        }
        switch (tab) {
            case 0://Tab Encaissement
                InterfaceEncaissement enc = modeleListeEncaissement.getEncaissement(tableListeEncaissement.getSelectedRow());
                if (enc != null) {
                    this.ecouteurClose.onActualiser("Encaiss...:" + Util.getDateFrancais(enc.getDate()) + " " + enc.getEffectuePar() + " " + Util.getMontantFrancais(enc.getMontant()) + ".", icones.getClient_01());
                }
                break;
            case 1://Tab Decaissement
                InterfaceDecaissement dec = modeleListeDecaissement.getDecaissement(tableListeDecaissement.getSelectedRow());
                if (dec != null) {
                    this.ecouteurClose.onActualiser("Encaiss...:" + Util.getDateFrancais(dec.getDate()) + " " + dec.getBeneficiaire() + " " + Util.getMontantFrancais(dec.getMontant()) + ".", icones.getClient_01());
                }
                break;
        }
    }

    public void init() {
        this.moi = this;
        this.chRecherche.setIcon(icones.getChercher_01());
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

        this.ecouteurAjout = new EcouteurAjout() {
            @Override
            public void setAjoutEncaissement(ModeleListeEncaissement modeleListeEncaissement) {
                if (modeleListeEncaissement != null) {
                    int index = (modeleListeEncaissement.getRowCount() + 1);

                    Date date = new Date();
                    int id = -1;
                    int dest = InterfaceEncaissement.DESTINATION_CAISSE;
                    String reference = dest + "ENC" + index;
                    double montant = 0;
                    int idMonnaie = parametreTresorerie.getMonnaies().firstElement().getId();
                    String monnaie = parametreTresorerie.getMonnaies().firstElement().getCode();
                    String effectuePar = "";
                    String motif = "";
                    int idRevenu = -1;
                    String revenu = "";
                    int beta = InterfaceEncaissement.BETA_NOUVEAU;

                    modeleListeEncaissement.AjouterEncaissement(new XX_Encaissement(id, dest, reference, date, montant, idMonnaie, monnaie, effectuePar, motif, idRevenu, revenu, beta));
                    //On sélectionne la première ligne
                    tableListeEncaissement.setRowSelectionInterval(0, 0);
                }
            }

            @Override
            public void setAjoutDecaissement(ModeleListeDecaissement modeleListeDecaissement) {
                if (modeleListeDecaissement != null) {
                    int index = (modeleListeDecaissement.getRowCount() + 1);

                    Date date = new Date();
                    int id = -1;
                    int source = InterfaceDecaissement.SOURCE_CAISSE;
                    String reference = source + "DEC" + index;
                    double montant = 0;
                    int idMonnaie = parametreTresorerie.getMonnaies().firstElement().getId();
                    String monnaie = parametreTresorerie.getMonnaies().firstElement().getCode();
                    String beneficiaire = "";
                    String motif = "";
                    int idCharge = -1;
                    String charge = "";
                    int beta = InterfaceDecaissement.BETA_NOUVEAU;

                    modeleListeDecaissement.AjouterDecaissement(new XX_Decaissement(id, source, reference, date, montant, idMonnaie, monnaie, beneficiaire, motif, idCharge, charge, beta));
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
        actualiserTotaux("activerBoutons");
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
                modeleListeEncaissement.SupprimerEncaissement(tableListeEncaissement.getSelectedRow());
                break;
            case 1: //Tab Decaissement
                modeleListeDecaissement.SupprimerDecaissement(tableListeDecaissement.getSelectedRow());
                break;
        }
    }

    public void vider() {
        this.ecouteurClose.onActualiser("Vidé!", icones.getInfos_01());
        this.chRecherche.setText("");
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

    private void setMenuContextuel() {
        mAjouter = new RubriqueSimple("Ajouter", icones.getAjouter_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                ajouter();
            }
        });

        mSupprimer = new RubriqueSimple("Supprimer", icones.getSupprimer_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                supprimer();
            }
        });

        mEnregistrer = new RubriqueSimple("Enregistrer", icones.getEnregistrer_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                enregistrer();
            }
        });

        mVider = new RubriqueSimple("Vider", icones.getAnnuler_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                vider();
            }
        });

        mImprimer = new RubriqueSimple("Imprimer", icones.getImprimer_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                imprimer();
            }
        });

        mFermer = new RubriqueSimple("Fermer", icones.getFermer_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                fermer();
            }
        });

        mPDF = new RubriqueSimple("Export. PDF", icones.getPDF_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                exporterPDF();
            }
        });

        mActualiser = new RubriqueSimple("Actualiser", icones.getSynchroniser_01(), new RubriqueListener() {
            @Override
            public void OnEcouterLaSelection() {
                actualiser();
            }
        });

        menuContextuel = new MenuContextuel();
        menuContextuel.Ajouter(mEnregistrer);
        menuContextuel.Ajouter(new JPopupMenu.Separator());
        menuContextuel.Ajouter(mAjouter);
        menuContextuel.Ajouter(mSupprimer);
        menuContextuel.Ajouter(mVider);
        menuContextuel.Ajouter(mActualiser);
        menuContextuel.Ajouter(new JPopupMenu.Separator());
        menuContextuel.Ajouter(mImprimer);
        menuContextuel.Ajouter(mPDF);
        menuContextuel.Ajouter(new JPopupMenu.Separator());
        menuContextuel.Ajouter(mFermer);
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
                //DocumentPDF documentPDF = new DocumentPDF(this, DocumentPDF.ACTION_IMPRIMER, sortie);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
            SortiesTresorerie sortie = getSortieTresorerie(btEnregistrer, mEnregistrer);
            this.ecouteurTresorerie.onEnregistre(sortie);
        }
    }

    public void exporterPDF() {
        int dialogResult = JOptionPane.showConfirmDialog(this, "Voulez-vous les exporter dans un fichier PDF?", "Avertissement", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            try {
                SortiesTresorerie sortie = getSortieTresorerie(btPDF, mPDF);
                //DocumentPDF docpdf = new DocumentPDF(this, DocumentPDF.ACTION_OUVRIR, sortie);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void actualiser() {
        switch (indexTabSelected) {
            case 0: //Encaissements
                modeleListeEncaissement.actualiser();
                break;
            case 1: //Decaissements
                modeleListeDecaissement.actualiser();
                break;
        }
    }

    private void activerCriteres() {
        //System.out.println("btCriteres.isSelected() = " + btCriteres.isSelected());
        btCriteres.setIcon(icones.getParamètres_01());
        //On reinitialise les combo
        chSource.setSelectedIndex(0);
        chMonnaie.setSelectedIndex(0);
        chRevenu.setSelectedIndex(0);

        if (btCriteres.isSelected() == true) {
            panelCriteres_Encaissements.setVisible(true);
            btCriteres.setText("Critères [-]");
        } else {
            panelCriteres_Encaissements.setVisible(false);
            btCriteres.setText("Critères [+]");
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
        chRecherche = new UI.JS2bTextField();
        btCriteres = new javax.swing.JToggleButton();
        panelTotaux = new javax.swing.JPanel();
        combototMonnaie = new javax.swing.JComboBox<>();
        labTotauxEncaissement = new javax.swing.JLabel();
        labTauxDeChange = new javax.swing.JLabel();
        labTotauxDecaissement = new javax.swing.JLabel();
        labTotauxSolde = new javax.swing.JLabel();
        panelCriteres_Encaissements = new javax.swing.JPanel();
        chSource = new javax.swing.JComboBox<>();
        chMonnaie = new javax.swing.JComboBox<>();
        chRevenu = new javax.swing.JComboBox<>();
        chCharge = new javax.swing.JComboBox<>();
        chDestination = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        panelCriteres_Decaissements = new javax.swing.JPanel();
        chSource1 = new javax.swing.JComboBox<>();
        chMonnaie1 = new javax.swing.JComboBox<>();
        chRevenu1 = new javax.swing.JComboBox<>();
        chCharge1 = new javax.swing.JComboBox<>();
        chDestination1 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jDateChooser4 = new com.toedter.calendar.JDateChooser();

        setBackground(new java.awt.Color(255, 255, 255));

        barreOutils.setBackground(new java.awt.Color(255, 255, 255));
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

        chRecherche.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        chRecherche.setTextInitial("Recherche");

        btCriteres.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        btCriteres.setText("Critères++");
        btCriteres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCriteresActionPerformed(evt);
            }
        });

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labTotauxEncaissement)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labTotauxDecaissement)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labTotauxSolde)
                .addGap(5, 5, 5)
                .addComponent(labTauxDeChange)
                .addGap(0, 5, Short.MAX_VALUE))
        );

        panelCriteres_Encaissements.setBackground(new java.awt.Color(255, 255, 255));
        panelCriteres_Encaissements.setBorder(javax.swing.BorderFactory.createTitledBorder("Autres Critères de filtrage - Encaissements"));

        chSource.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SOURCE" }));

        chMonnaie.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "MONNAIE" }));

        chRevenu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "REVENU" }));

        chCharge.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CHARGE" }));

        chDestination.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DESTINATION" }));

        jLabel1.setText("Entre");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Et");

        jDateChooser1.setBackground(new java.awt.Color(255, 255, 255));

        jDateChooser2.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelCriteres_EncaissementsLayout = new javax.swing.GroupLayout(panelCriteres_Encaissements);
        panelCriteres_Encaissements.setLayout(panelCriteres_EncaissementsLayout);
        panelCriteres_EncaissementsLayout.setHorizontalGroup(
            panelCriteres_EncaissementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(panelCriteres_EncaissementsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCriteres_EncaissementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCriteres_EncaissementsLayout.createSequentialGroup()
                        .addComponent(chMonnaie, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chRevenu, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chCharge, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelCriteres_EncaissementsLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chSource, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chDestination, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelCriteres_EncaissementsLayout.setVerticalGroup(
            panelCriteres_EncaissementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCriteres_EncaissementsLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(panelCriteres_EncaissementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCriteres_EncaissementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelCriteres_EncaissementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chSource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(chDestination, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCriteres_EncaissementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chCharge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelCriteres_EncaissementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chMonnaie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(chRevenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelCriteres_Decaissements.setBackground(new java.awt.Color(255, 255, 255));
        panelCriteres_Decaissements.setBorder(javax.swing.BorderFactory.createTitledBorder("Autres Critères de filtrage - Décaissements"));

        chSource1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SOURCE" }));

        chMonnaie1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "MONNAIE" }));

        chRevenu1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "REVENU" }));

        chCharge1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CHARGE" }));

        chDestination1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DESTINATION" }));

        jLabel3.setText("Entre");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Et");

        jDateChooser3.setBackground(new java.awt.Color(255, 255, 255));

        jDateChooser4.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelCriteres_DecaissementsLayout = new javax.swing.GroupLayout(panelCriteres_Decaissements);
        panelCriteres_Decaissements.setLayout(panelCriteres_DecaissementsLayout);
        panelCriteres_DecaissementsLayout.setHorizontalGroup(
            panelCriteres_DecaissementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelCriteres_DecaissementsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCriteres_DecaissementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCriteres_DecaissementsLayout.createSequentialGroup()
                        .addComponent(chMonnaie1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chRevenu1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chCharge1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelCriteres_DecaissementsLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser4, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chSource1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chDestination1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelCriteres_DecaissementsLayout.setVerticalGroup(
            panelCriteres_DecaissementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCriteres_DecaissementsLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(panelCriteres_DecaissementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelCriteres_DecaissementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jDateChooser3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jDateChooser4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelCriteres_DecaissementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chSource1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(chDestination1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCriteres_DecaissementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chCharge1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelCriteres_DecaissementsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chMonnaie1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(chRevenu1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(barreOutils, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tabPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
            .addComponent(panelTotaux, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labInfos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(chRecherche, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btCriteres)))
                .addContainerGap())
            .addComponent(panelCriteres_Encaissements, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelCriteres_Decaissements, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(barreOutils, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chRecherche, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btCriteres))
                .addGap(5, 5, 5)
                .addComponent(panelCriteres_Encaissements, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(panelCriteres_Decaissements, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                .addGap(5, 5, 5)
                .addComponent(panelTotaux, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labInfos)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tableListeEncaissementMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListeEncaissementMouseClicked
        // TODO add your handling code here:
        ecouterMenContA(evt, 0);
    }//GEN-LAST:event_tableListeEncaissementMouseClicked

    private void scrollListeEncaissementMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollListeEncaissementMouseClicked
        // TODO add your handling code here:
        ecouterMenContA(evt, 0);
    }//GEN-LAST:event_scrollListeEncaissementMouseClicked

    private void tableListeDecaissementMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListeDecaissementMouseClicked
        // TODO add your handling code here:
        ecouterMenContA(evt, 1);
    }//GEN-LAST:event_tableListeDecaissementMouseClicked

    private void scrollListeDecaissementMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_scrollListeDecaissementMouseClicked
        // TODO add your handling code here:
        ecouterMenContA(evt, 1);
    }//GEN-LAST:event_scrollListeDecaissementMouseClicked

    private void tabPrincipalStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabPrincipalStateChanged
        // TODO add your handling code here:
        activerBoutons(((JTabbedPane) evt.getSource()).getSelectedIndex());
    }//GEN-LAST:event_tabPrincipalStateChanged

    private void btCriteresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCriteresActionPerformed
        // TODO add your handling code here:
        activerCriteres();
    }//GEN-LAST:event_btCriteresActionPerformed

    private void tableListeEncaissementKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableListeEncaissementKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_tableListeEncaissementKeyReleased

    private void tableListeDecaissementKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableListeDecaissementKeyReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_tableListeDecaissementKeyReleased

    private void combototMonnaieItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_combototMonnaieItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            actualiserTotalDecaissement();
            actualiserTotalEncaissement();
            actualiserTotaux("combototMonnaieItemStateChanged");
        }
    }//GEN-LAST:event_combototMonnaieItemStateChanged

    private void tableListeEncaissementMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListeEncaissementMouseDragged
        // TODO add your handling code here:

    }//GEN-LAST:event_tableListeEncaissementMouseDragged

    private void tableListeDecaissementMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableListeDecaissementMouseDragged
        // TODO add your handling code here:

    }//GEN-LAST:event_tableListeDecaissementMouseDragged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barreOutils;
    private javax.swing.JToggleButton btCriteres;
    private javax.swing.JComboBox<String> chCharge;
    private javax.swing.JComboBox<String> chCharge1;
    private javax.swing.JComboBox<String> chDestination;
    private javax.swing.JComboBox<String> chDestination1;
    private javax.swing.JComboBox<String> chMonnaie;
    private javax.swing.JComboBox<String> chMonnaie1;
    private UI.JS2bTextField chRecherche;
    private javax.swing.JComboBox<String> chRevenu;
    private javax.swing.JComboBox<String> chRevenu1;
    private javax.swing.JComboBox<String> chSource;
    private javax.swing.JComboBox<String> chSource1;
    private javax.swing.JComboBox<String> combototMonnaie;
    private javax.swing.JButton jButton5;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private com.toedter.calendar.JDateChooser jDateChooser4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel labInfos;
    private javax.swing.JLabel labTauxDeChange;
    private javax.swing.JLabel labTotauxDecaissement;
    private javax.swing.JLabel labTotauxEncaissement;
    private javax.swing.JLabel labTotauxSolde;
    private javax.swing.JPanel panelCriteres_Decaissements;
    private javax.swing.JPanel panelCriteres_Encaissements;
    private javax.swing.JPanel panelTotaux;
    private javax.swing.JScrollPane scrollListeDecaissement;
    private javax.swing.JScrollPane scrollListeEncaissement;
    private javax.swing.JTabbedPane tabPrincipal;
    private javax.swing.JTable tableListeDecaissement;
    private javax.swing.JTable tableListeEncaissement;
    // End of variables declaration//GEN-END:variables
}
