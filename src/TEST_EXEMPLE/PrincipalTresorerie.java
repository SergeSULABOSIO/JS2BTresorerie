/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TEST_EXEMPLE;

import ICONES.Icones;
import SOURCES.CallBack_Tresorerie.EcouteurTresorerie;
import SOURCES.UI_Tresorerie.PanelTresorerie;
import SOURCES.Utilitaires_Tresorerie.DataTresorerie;
import SOURCES.Utilitaires_Tresorerie.DonneesTresorerie;
import SOURCES.Utilitaires_Tresorerie.ParametreTresorerie;
import SOURCES.Utilitaires_Tresorerie.SortiesTresorerie;
import SOURCES.Utilitaires_Tresorerie.UtilTresorerie;
import Source.Callbacks.ConstructeurCriteres;
import Source.Callbacks.EcouteurFreemium;
import Source.Callbacks.EcouteurNavigateurPages;
import Source.Interface.InterfaceDecaissement;
import Source.Interface.InterfaceEncaissement;
import Source.Interface.InterfaceMonnaie;
import Source.Interface.InterfaceRevenu;
import Source.Interface.InterfaceUtilisateur;
import Source.Objet.Charge;
import Source.Objet.CouleurBasique;
import Source.Objet.Decaissement;
import Source.Objet.Encaissement;
import Source.Objet.Entreprise;
import Source.Objet.Annee;
import Source.Objet.Monnaie;
import Source.Objet.Revenu;
import Source.Objet.UtilObjet;
import Source.Objet.Utilisateur;
import Source.UI.NavigateurPages;
import Sources.CHAMP_LOCAL;
import Sources.PROPRIETE;
import Sources.UI.JS2BPanelPropriete;
import static java.lang.Thread.sleep;
import java.util.Date;
import java.util.Vector;
import Source.Interface.InterfaceAnnee;

/**
 *
 * @author HP Pavilion
 */
public class PrincipalTresorerie extends javax.swing.JFrame {

    /**
     * Creates new form TEST_Principal
     */
    public PanelTresorerie panelTresorerie = null;
    public ParametreTresorerie parametreTresorerie = null;
    public DonneesTresorerie donneesTresorerie = null;
    public Vector<Monnaie> monnaies = new Vector<>();
    public Vector<Revenu> revenus = new Vector<>();
    public Vector<Charge> charges = new Vector<>();

    public Entreprise entreprise = new Entreprise(1, "ECOLE CARESIENNE DE KINSHASA", "7e Rue Limeté Industrielle, Kinshasa/RDC", "+243844803514", "infos@cartesien.org", "wwww.cartesien.org", "logo.png", "RCCM/KD/CD/4513", "IDN00111454", "IMP00124100", "Equity Bank Congo SA", "AIB RDC Sarl", "000000121212400", "IBANNN0012", "SWIFTCDK");
    public Annee exercice = new Annee(12, entreprise.getId(), 1, "Année Scolaire 2019-2020", new Date(), UtilTresorerie.getDate_AjouterAnnee(new Date(), 1), UtilObjet.getSignature(), InterfaceAnnee.BETA_EXISTANT);
    public Utilisateur utilisateur = new Utilisateur(1, entreprise.getId(), "SULA", "BOSIO", "SERGE", "sulabosiog@gmail.com", "abc", InterfaceUtilisateur.TYPE_ADMIN, UtilTresorerie.generateSignature(), InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.DROIT_CONTROLER, InterfaceUtilisateur.BETA_EXISTANT);
    public Monnaie monnaie_CDF = new Monnaie(21, entreprise.getId(), utilisateur.getId(), exercice.getId(), "Francs Congolais", "Fc", InterfaceMonnaie.NATURE_MONNAIE_LOCALE, 1, UtilTresorerie.generateSignature(), InterfaceMonnaie.BETA_EXISTANT);
    public Monnaie monnaie_USD = new Monnaie(20, entreprise.getId(), utilisateur.getId(), exercice.getId(), "Dollars Américains", "$", InterfaceMonnaie.NATURE_MONNAIE_ETRANGERE, 1620, UtilTresorerie.generateSignature(), InterfaceMonnaie.BETA_EXISTANT);

    public Vector<Encaissement> listeEncaissements = new Vector<>();
    public Vector<Decaissement> listeDecaissements = new Vector<>();

    public Icones icones;

    public PrincipalTresorerie() {
        initComponents();
        icones = new Icones();
        this.setIconImage(icones.getAdresse_03().getImage());
        //TEst
    }

    private void chercherEncaissements(String motCle, int taillePage, JS2BPanelPropriete criteresAvances) {
        int index = 0;
        for (Encaissement ee : listeEncaissements) {
            if (index == taillePage) {
                break;
            }
            boolean checkCritere = checkCriteresEncaissements(motCle, ee, criteresAvances);
            index++;
        }
    }

    private void chercherDecaissements(String motCle, int taillePage, JS2BPanelPropriete criteresAvances) {
        int index = 0;
        for (Decaissement ee : listeDecaissements) {
            if (index == taillePage) {
                break;
            }
            boolean checkCritere = checkCriteresDecaissements(motCle, ee, criteresAvances);
            index++;
        }
    }

    public boolean checkCriteresEncaissements(String motCle, Object data, JS2BPanelPropriete jsbpp) {
        Encaissement encaissement = (Encaissement) data;
        boolean repMotCle = panelTresorerie.search_verifier_motcle_encaiss(encaissement, motCle);
        if (repMotCle == false) {
            return false;
        }
        boolean repMonnaie = false;
        boolean repRevenu = false;
        boolean repDestination = false;
        if (jsbpp != null) {
            PROPRIETE propDateA = jsbpp.getPropriete("A partir du");
            PROPRIETE propDateB = jsbpp.getPropriete("Jusqu'au");
            boolean repPeriode = panelTresorerie.search_verifier_periode_encaiss(encaissement, (Date) propDateA.getValeurSelectionne(), (Date) propDateB.getValeurSelectionne());
            if (repPeriode == false) {
                return false;
            }
            PROPRIETE propMonnaie = jsbpp.getPropriete("Monnaie");
            int idMonnaie = panelTresorerie.getIdMonnaie(propMonnaie.getValeurSelectionne() + "");
            if (idMonnaie == -1) {
                repMonnaie = true;
            } else {
                repMonnaie = (idMonnaie == encaissement.getIdMonnaie());
            }
            PROPRIETE propRevenu = jsbpp.getPropriete("Type de revenu");
            int idRevenu = panelTresorerie.getIdRevenu(propRevenu.getValeurSelectionne() + "");
            if (idRevenu == -1) {
                repRevenu = true;
            } else {
                repRevenu = (idRevenu == encaissement.getIdRevenu());
            }
            PROPRIETE propDestination = jsbpp.getPropriete("Destination des fonds");
            int idDestination = panelTresorerie.getDestination(propDestination.getValeurSelectionne() + "");
            if (idDestination == -1) {
                repDestination = true;
            } else {
                repDestination = (idDestination == encaissement.getDestination());
            }
        } else {
            repMonnaie = true;
            repRevenu = true;
            repDestination = true;
        }
        if (repMotCle == true && repMonnaie == true && repRevenu == true && repDestination == true) {
            panelTresorerie.setDonneesEncaissement(encaissement);
            return true;
        } else {
            panelTresorerie.setDonneesEncaissement(null);
            return false;
        }
    }

    public boolean checkCriteresDecaissements(String motCle, Object data, JS2BPanelPropriete jsbpp) {
        Decaissement decaissement = (Decaissement) data;
        boolean repMotCle = panelTresorerie.search_verifier_motcle_decaiss(decaissement, motCle);
        if (repMotCle == false) {
            return false;
        }
        boolean repMonnaie = false;
        boolean repCharge = false;
        boolean repSource = false;
        if (jsbpp != null) {
            PROPRIETE propDateA = jsbpp.getPropriete("A partir du");
            PROPRIETE propDateB = jsbpp.getPropriete("Jusqu'au");
            boolean repPeriode = panelTresorerie.search_verifier_periode_decaiss(decaissement, (Date) propDateA.getValeurSelectionne(), (Date) propDateB.getValeurSelectionne());
            if (repPeriode == false) {
                return false;
            }
            PROPRIETE propMonnaie = jsbpp.getPropriete("Monnaie");
            int idMonnaie = panelTresorerie.getIdMonnaie(propMonnaie.getValeurSelectionne() + "");
            if (idMonnaie == -1) {
                repMonnaie = true;
            } else {
                repMonnaie = (idMonnaie == decaissement.getIdMonnaie());
            }
            PROPRIETE propRevenu = jsbpp.getPropriete("Type de charge");
            int idCharge = panelTresorerie.getIdCharge(propRevenu.getValeurSelectionne() + "");
            if (idCharge == -1) {
                repCharge = true;
            } else {
                repCharge = (idCharge == decaissement.getIdCharge());
            }
            PROPRIETE propDestination = jsbpp.getPropriete("Source des fonds");
            int idSource = panelTresorerie.getDestination(propDestination.getValeurSelectionne() + "");
            if (idSource == -1) {
                repSource = true;
            } else {
                repSource = (idSource == decaissement.getSource());
            }
        } else {
            repMonnaie = true;
            repCharge = true;
            repSource = true;
        }
        if (repMotCle == true && repMonnaie == true && repCharge == true && repSource == true) {
            panelTresorerie.setDonneesDecaissement(decaissement);
            return true;
        } else {
            panelTresorerie.setDonneesDecaissement(null);
            return false;
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

        jButton1 = new javax.swing.JButton();
        tabPrincipal = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Ouvrir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addContainerGap(622, Short.MAX_VALUE))
            .addComponent(tabPrincipal)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 472, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        ouvrir();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PrincipalTresorerie.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PrincipalTresorerie.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PrincipalTresorerie.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PrincipalTresorerie.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PrincipalTresorerie().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JTabbedPane tabPrincipal;
    // End of variables declaration//GEN-END:variables

    private void ouvrir() {
        initParametres();
        initDonnees();

        if (panelTresorerie != null) {
            NavigateurPages navigateurEncaissements = panelTresorerie.getNavigateurPagesEncaissement();
            navigateurEncaissements.initialiser(this, new EcouteurNavigateurPages() {
                @Override
                public void onRecharge(String motCle, int pageActuelle, int taillePage, JS2BPanelPropriete criteresAvances) {
                    new Thread() {
                        public void run() {
                            //System.out.println("RECHARGE ENCAISSEMENT...");
                            navigateurEncaissements.setInfos(100, 10);
                            navigateurEncaissements.patienter(true, "Chargement...");
                            panelTresorerie.reiniliserEncaissements();
                            chercherEncaissements(motCle, taillePage, criteresAvances);
                        }
                    }.start();
                }
            }, new ConstructeurCriteres() {
                @Override
                public JS2BPanelPropriete onInitialise() {
                    JS2BPanelPropriete panProp = new JS2BPanelPropriete(icones.getFiltrer_01(), "Critères avancés", true);
                    panProp.viderListe();

                    panProp.AjouterPropriete(new CHAMP_LOCAL(icones.getCalendrier_01(), "A partir du", "du", null, UtilTresorerie.getDate_CeMatin(new Date()), PROPRIETE.TYPE_CHOIX_DATE), 0);
                    panProp.AjouterPropriete(new CHAMP_LOCAL(icones.getCalendrier_01(), "Jusqu'au", "Au", null, UtilTresorerie.getDate_ZeroHeure(new Date()), PROPRIETE.TYPE_CHOIX_DATE), 0);

                    //Critres Monnaie
                    Vector listeMonnaies = new Vector();
                    listeMonnaies.add("TOUTES");
                    for (Monnaie cl : panelTresorerie.getParametreTresorerie().getMonnaies()) {
                        listeMonnaies.add(cl.getNom());
                    }
                    panProp.AjouterPropriete(new CHAMP_LOCAL(icones.getTaxes_01(), "Monnaie", "Monnaie", listeMonnaies, "", PROPRIETE.TYPE_CHOIX_LISTE), 0);

                    //Critres Revenu
                    Vector listeRevenus = new Vector();
                    listeRevenus.add("TOUS");
                    for (Revenu per : panelTresorerie.getParametreTresorerie().getRevenus()) {
                        listeRevenus.add(per.getNom());
                    }
                    panProp.AjouterPropriete(new CHAMP_LOCAL(icones.getCalendrier_01(), "Type de revenu", "Revenu", listeRevenus, "", PROPRIETE.TYPE_CHOIX_LISTE), 0);

                    Vector listeDestinations = new Vector();
                    listeDestinations.add("TOUTES");
                    listeDestinations.add("BANQUE");
                    listeDestinations.add("CAISSE");

                    panProp.AjouterPropriete(new CHAMP_LOCAL(icones.getRecette_01(), "Destination des fonds", "Destination", listeDestinations, "", PROPRIETE.TYPE_CHOIX_LISTE), 0);

                    return panProp;
                }
            });

            NavigateurPages navigateurDecaissements = panelTresorerie.getNavigateurPagesDecaissement();
            navigateurDecaissements.initialiser(this, new EcouteurNavigateurPages() {
                @Override
                public void onRecharge(String motCle, int pageActuelle, int taillePage, JS2BPanelPropriete criteresAvances) {
                    new Thread() {
                        public void run() {
                            //System.out.println("RECHARGE DECAISSEMENT...");
                            navigateurDecaissements.setInfos(100, 10);
                            navigateurDecaissements.patienter(true, "Chargement...");
                            panelTresorerie.reiniliserDecaissements();
                            chercherDecaissements(motCle, taillePage, criteresAvances);
                        }
                    }.start();
                }
            }, new ConstructeurCriteres() {
                @Override
                public JS2BPanelPropriete onInitialise() {
                    JS2BPanelPropriete panProp = new JS2BPanelPropriete(icones.getFiltrer_01(), "Critères avancés", true);
                    panProp.viderListe();

                    //Critres date A
                    panProp.AjouterPropriete(new CHAMP_LOCAL(icones.getCalendrier_01(), "A partir du", "du", null, UtilTresorerie.getDate_CeMatin(new Date()), PROPRIETE.TYPE_CHOIX_DATE), 0);

                    //Critres date B
                    panProp.AjouterPropriete(new CHAMP_LOCAL(icones.getCalendrier_01(), "Jusqu'au", "Au", null, UtilTresorerie.getDate_ZeroHeure(new Date()), PROPRIETE.TYPE_CHOIX_DATE), 0);

                    //Critres Monnaie
                    Vector listeMonnaies = new Vector();
                    listeMonnaies.add("TOUTES");
                    for (Monnaie cl : panelTresorerie.getParametreTresorerie().getMonnaies()) {
                        listeMonnaies.add(cl.getNom());
                    }
                    panProp.AjouterPropriete(new CHAMP_LOCAL(icones.getTaxes_01(), "Monnaie", "Monnaie", listeMonnaies, "", PROPRIETE.TYPE_CHOIX_LISTE), 0);

                    //Critres Revenu
                    Vector listeRevenus = new Vector();
                    listeRevenus.add("TOUS");
                    for (Charge per : panelTresorerie.getParametreTresorerie().getCharges()) {
                        listeRevenus.add(per.getNom());
                    }
                    panProp.AjouterPropriete(new CHAMP_LOCAL(icones.getCalendrier_01(), "Type de charge", "Charge", listeRevenus, "", PROPRIETE.TYPE_CHOIX_LISTE), 0);

                    Vector listeDestinations = new Vector();
                    listeDestinations.add("TOUTES");
                    listeDestinations.add("BANQUE");
                    listeDestinations.add("CAISSE");

                    panProp.AjouterPropriete(new CHAMP_LOCAL(icones.getRecette_01(), "Source des fonds", "Source", listeDestinations, "", PROPRIETE.TYPE_CHOIX_LISTE), 0);

                    return panProp;
                }
            });

            tabPrincipal.addTab("Tresorerie (Enc & Dec)", panelTresorerie);
            tabPrincipal.setSelectedComponent(panelTresorerie);

            navigateurEncaissements.reload();
            navigateurDecaissements.reload();
        }
    }

    private void initDonnees() {
        listeEncaissements.removeAllElements();
        listeEncaissements.addElement(new Encaissement(1, InterfaceEncaissement.DESTINATION_BANQUE, "0014578BN", new Date(), 100, monnaie_USD.getId(), monnaie_USD.getCode(), utilisateur.getNom(), "Loyer restaut YZ", 2, "Loyer restau", exercice.getId(), utilisateur.getId(), UtilObjet.getSignature(), InterfaceRevenu.BETA_EXISTANT));
        listeEncaissements.addElement(new Encaissement(-100, InterfaceEncaissement.DESTINATION_BANQUE, "0014578NNN", new Date(), 100, monnaie_USD.getId(), monnaie_USD.getCode(), utilisateur.getNom(), "Non editable", 2, "Loyer restau", exercice.getId(), utilisateur.getId(), UtilObjet.getSignature(), InterfaceRevenu.BETA_EXISTANT));

        listeDecaissements.removeAllElements();
        listeDecaissements.addElement(new Decaissement(1, InterfaceDecaissement.SOURCE_BANQUE, "B0014578O", new Date(), 100, monnaie_USD.getId(), monnaie_USD.getCode(), utilisateur.getNom(), "Autres", 2, "Loyer restau", exercice.getId(), utilisateur.getId(), UtilObjet.getSignature(), InterfaceRevenu.BETA_EXISTANT));
    }

    private void initParametres() {
        ParametreTresorerie parametresTresorerie = getParametresTresorerie();

        panelTresorerie = new PanelTresorerie(new EcouteurFreemium() {
            @Override
            public boolean onVerifie() {
                return true;
            }

            @Override
            public boolean onVerifieNombre(String nomTable) {
                return true;
            }
        }, new CouleurBasique(), null, tabPrincipal, new DataTresorerie(parametresTresorerie), new EcouteurTresorerie() {
            @Override
            public void onDetruitElement(int idElement, int index, long signature) {
                System.out.println("DESTRUCTION DE L'ELEMENT " + idElement + ", TABLE INDICE " + index);
            }

            @Override
            public void onEnregistre(SortiesTresorerie sortiesTresorerie) {
                //Ce que le système devra faire lorsque l'on clique sur le bouton ENREGISTRER

                Thread th = new Thread() {
                    @Override
                    public void run() {
                        try {
                            sortiesTresorerie.getEcouteurEnregistrement().onUploading("Chargement...");
                            sleep(10);

                            sortiesTresorerie.getListeEncaissements().forEach((Oeleve) -> {
                                if (Oeleve.getBeta() == InterfaceEncaissement.BETA_MODIFIE || Oeleve.getBeta() == InterfaceEncaissement.BETA_NOUVEAU) {
                                    System.out.println(" * " + Oeleve.toString());

                                    //Après enregistrement
                                    Oeleve.setId(new Date().getSeconds());
                                    Oeleve.setBeta(InterfaceEncaissement.BETA_EXISTANT);
                                }
                            });

                            sortiesTresorerie.getListeDecaissements().forEach((Oeleve) -> {
                                if (Oeleve.getBeta() == InterfaceDecaissement.BETA_MODIFIE || Oeleve.getBeta() == InterfaceDecaissement.BETA_NOUVEAU) {
                                    System.out.println(" * " + Oeleve.toString() + " : ");

                                    //Après enregistrement
                                    Oeleve.setId(new Date().getSeconds());
                                    Oeleve.setBeta(InterfaceDecaissement.BETA_EXISTANT);
                                }
                            });

                            sortiesTresorerie.getEcouteurEnregistrement().onDone("Enregistré!");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                th.start();

            }

            @Override
            public void onClosed() {
                
            }

            @Override
            public boolean onCanDelete(int idElement, int index, long signature) {
                return true;
            }
        });

    }

    private ParametreTresorerie getParametresTresorerie() {
        //Les types des monnaies
        monnaies = new Vector<>();
        monnaies.addElement(monnaie_USD);
        monnaies.addElement(monnaie_CDF);

        //Les types des revenus
        revenus = new Vector<>();
        revenus.addElement(new Revenu(1, entreprise.getId(), utilisateur.getId(), exercice.getId(), monnaie_USD.getId(), monnaie_USD.getSignature(), "FRAIS SCOLAIRES", monnaie_USD.getCode(), 100000, UtilObjet.getSignature(), InterfaceEncaissement.BETA_EXISTANT));
        revenus.addElement(new Revenu(2, entreprise.getId(), utilisateur.getId(), exercice.getId(), monnaie_USD.getId(), monnaie_USD.getSignature(), "LOYERS RESTAURANT", monnaie_USD.getCode(), 10000, UtilObjet.getSignature(), InterfaceEncaissement.BETA_EXISTANT));

        //Les types des charges
        charges = new Vector<>();
        charges.addElement(new Charge(1, entreprise.getId(), utilisateur.getId(), exercice.getId(), "SALAIRE STAFF", 50000, monnaie_USD.getId(), monnaie_USD.getSignature(), monnaie_USD.getCode(), UtilObjet.getSignature(), InterfaceEncaissement.BETA_EXISTANT));
        charges.addElement(new Charge(2, entreprise.getId(), utilisateur.getId(), exercice.getId(), "ENERGIE", 50000, monnaie_USD.getId(), monnaie_USD.getSignature(), monnaie_USD.getCode(), UtilObjet.getSignature(), InterfaceEncaissement.BETA_EXISTANT));
        charges.addElement(new Charge(3, entreprise.getId(), utilisateur.getId(), exercice.getId(), "TRANSPORT", 50000, monnaie_USD.getId(), monnaie_USD.getSignature(), monnaie_USD.getCode(), UtilObjet.getSignature(), InterfaceEncaissement.BETA_EXISTANT));

        return new ParametreTresorerie(entreprise, exercice, utilisateur, monnaies, revenus, charges);
    }
}
