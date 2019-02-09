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
import SOURCES.EditeursTable.EditeurRevenu;
import SOURCES.Interface.InterfaceDecaissement;
import SOURCES.Interface.InterfaceEncaissement;
import SOURCES.Interface.InterfaceEntreprise;
import SOURCES.ModelsTable.ModeleListeDecaissement;
import SOURCES.ModelsTable.ModeleListeEncaissement;
import SOURCES.RendusTables.RenduTableDecaissement;
import SOURCES.RendusTables.RenduTableEncaissement;
import SOURCES.Utilitaires.DonneesTresorerie;
import SOURCES.Utilitaires.ParametreTresorerie;
import SOURCES.Utilitaires.SortiesTresorerie;
import SOURCES.Utilitaires.Util;
import SOURCES.Utilitaires.XX_Decaissement;
import SOURCES.Utilitaires.XX_Encaissement;
import java.awt.event.MouseEvent;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
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

    public Panel(JTabbedPane parent, DonneesTresorerie donneesTresorerie, ParametreTresorerie parametreTresorerie, EcouteurTresorerie ecouteurTresorerie) {
        this.initComponents();
        this.parent = parent;
        this.init();
        this.donneesTresorerie = donneesTresorerie;
        this.parametreTresorerie = parametreTresorerie;
        this.ecouteurTresorerie = ecouteurTresorerie;

        //Initialisaterus
        parametrerTableEncaissement();
        parametrerTableDecaissement();
        setIconesTabs();
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
        return this.chSexe.getSelectedItem() + "";
    }

    public String getCritereClasse() {
        return this.chClasse.getSelectedItem() + "";
    }

    public String getCritereStatus() {
        return this.chStatus.getSelectedItem() + "";
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
        TableColumn col_No = this.tableListeEncaissement.getColumnModel().getColumn(0);
        col_No.setPreferredWidth(40);
        col_No.setMaxWidth(40);

        TableColumn colDate = this.tableListeEncaissement.getColumnModel().getColumn(1);
        colDate.setPreferredWidth(150);

        TableColumn colSourc = this.tableListeEncaissement.getColumnModel().getColumn(2);
        colSourc.setPreferredWidth(150);

        TableColumn colRef = this.tableListeEncaissement.getColumnModel().getColumn(3);
        colRef.setPreferredWidth(150);

        TableColumn colMot = this.tableListeEncaissement.getColumnModel().getColumn(4);
        colMot.setPreferredWidth(140);
        colMot.setMaxWidth(140);

        TableColumn colNat = this.tableListeEncaissement.getColumnModel().getColumn(5);
        colNat.setCellEditor(new EditeurRevenu(parametreTresorerie));
        colNat.setPreferredWidth(90);
        colNat.setMaxWidth(90);

        TableColumn colBenn = this.tableListeEncaissement.getColumnModel().getColumn(6);
        colBenn.setPreferredWidth(150);

        TableColumn colMont = this.tableListeEncaissement.getColumnModel().getColumn(7);
        colMont.setPreferredWidth(120);

        TableColumn colMonn = this.tableListeEncaissement.getColumnModel().getColumn(8);
        colMonn.setPreferredWidth(120);
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
        TableColumn col_No = this.tableListeEncaissement.getColumnModel().getColumn(0);
        col_No.setPreferredWidth(40);
        col_No.setMaxWidth(40);

        TableColumn colDate = this.tableListeEncaissement.getColumnModel().getColumn(1);
        colDate.setPreferredWidth(150);

        TableColumn colDest = this.tableListeEncaissement.getColumnModel().getColumn(2);
        colDest.setPreferredWidth(150);

        TableColumn colRef = this.tableListeEncaissement.getColumnModel().getColumn(3);
        colRef.setPreferredWidth(150);

        TableColumn colMot = this.tableListeEncaissement.getColumnModel().getColumn(4);
        colMot.setPreferredWidth(140);
        colMot.setMaxWidth(140);

        TableColumn colNat = this.tableListeEncaissement.getColumnModel().getColumn(5);
        colNat.setCellEditor(new EditeurRevenu(parametreTresorerie));
        colNat.setPreferredWidth(90);
        colNat.setMaxWidth(90);

        TableColumn colEff = this.tableListeEncaissement.getColumnModel().getColumn(6);
        colEff.setPreferredWidth(150);

        TableColumn colMont = this.tableListeEncaissement.getColumnModel().getColumn(7);
        colMont.setPreferredWidth(120);

        TableColumn colMonn = this.tableListeEncaissement.getColumnModel().getColumn(8);
        colMonn.setPreferredWidth(120);
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
        this.icones = new Icones();
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
                    int idMonnaie = -1;
                    String monnaie = "";
                    String effectuePar = "";
                    String motif = "";
                    int idRevenu = -1;
                    String revenu = "";
                    int beta = InterfaceEncaissement.BETA_NOUVEAU;

                    modeleListeEncaissement.AjouterEncaissement(new XX_Encaissement(id, dest, reference, date, montant, idMonnaie, monnaie, effectuePar, motif, idRevenu, revenu, beta));
                    //On sélectionne la première ligne
                    tableListeEncaissement.setRowSelectionAllowed(true);
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
                    int idMonnaie = -1;
                    String monnaie = "";
                    String beneficiaire = "";
                    String motif = "";
                    int idCharge = -1;
                    String charge = "";
                    int beta = InterfaceDecaissement.BETA_NOUVEAU;

                    modeleListeDecaissement.AjouterDecaissement(new XX_Decaissement(id, source, reference, date, montant, idMonnaie, monnaie, beneficiaire, motif, idCharge, charge, beta));
                    //On sélectionne la première ligne
                    tableListeDecaissement.setRowSelectionAllowed(true);
                    tableListeDecaissement.setRowSelectionInterval(0, 0);
                }
            }
        };

        setBoutons();
        setMenuContextuel();
    }

    public void activerBoutons(int selectedTab) {
        this.indexTabSelected = selectedTab;
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
        chClasse.setSelectedIndex(0);
        chSexe.setSelectedIndex(0);
        chStatus.setSelectedIndex(0);

        if (btCriteres.isSelected() == true) {
            chClasse.setVisible(true);
            chSexe.setVisible(true);
            chStatus.setVisible(true);
            btCriteres.setText("Critères [-]");
        } else {
            chClasse.setVisible(false);
            chSexe.setVisible(false);
            chStatus.setVisible(false);
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
        chClasse = new javax.swing.JComboBox<>();
        chSexe = new javax.swing.JComboBox<>();
        chStatus = new javax.swing.JComboBox<>();
        btCriteres = new javax.swing.JToggleButton();

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
        tableListeEncaissement.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableListeEncaissementMouseClicked(evt);
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
        tableListeDecaissement.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableListeDecaissementMouseClicked(evt);
            }
        });
        scrollListeDecaissement.setViewportView(tableListeDecaissement);

        tabPrincipal.addTab("Decaissements", scrollListeDecaissement);

        labInfos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        labInfos.setText("Prêt.");

        chRecherche.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        chRecherche.setTextInitial("Recherche");

        chClasse.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        chSexe.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "TOUT GENRE", "MASCULIN", "FEMININ" }));

        chStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "TOUT STATUS", "EL. ACTIF", "EL. INACTIF" }));

        btCriteres.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMG/Facture01.png"))); // NOI18N
        btCriteres.setText("Critères++");
        btCriteres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCriteresActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(barreOutils, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(tabPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labInfos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(chClasse, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chSexe, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chStatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(chRecherche, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btCriteres)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(barreOutils, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chRecherche, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btCriteres))
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chClasse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chSexe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToolBar barreOutils;
    private javax.swing.JToggleButton btCriteres;
    private javax.swing.JComboBox<String> chClasse;
    private UI.JS2bTextField chRecherche;
    private javax.swing.JComboBox<String> chSexe;
    private javax.swing.JComboBox<String> chStatus;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel labInfos;
    private javax.swing.JScrollPane scrollListeDecaissement;
    private javax.swing.JScrollPane scrollListeEncaissement;
    private javax.swing.JTabbedPane tabPrincipal;
    private javax.swing.JTable tableListeDecaissement;
    private javax.swing.JTable tableListeEncaissement;
    // End of variables declaration//GEN-END:variables
}
