/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SOURCES.GenerateurPDF_Tresorerie;

import SOURCES.UI_Tresorerie.PanelTresorerie;
import SOURCES.Utilitaires_Tresorerie.SortiesTresorerie;
import SOURCES.Utilitaires_Tresorerie.UtilTresorerie;
import Source.Interface.InterfaceCharge;
import Source.Interface.InterfaceDecaissement;
import Source.Interface.InterfaceEncaissement;
import Source.Interface.InterfaceMonnaie;
import Source.Interface.InterfaceRevenu;
import Source.Objet.Decaissement;
import Source.Objet.Encaissement;
import Source.Objet.Entreprise;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author Gateway
 */
public class DocumentPDFTres extends PdfPageEventHelper {

    private Document document = new Document(PageSize.A4.rotate());//Avant: new Document(PageSize.A4);
    private Font Font_Titre1 = null;
    private Font Font_Titre2 = null;
    private Font Font_Titre3 = null;
    private Font Font_TexteSimple = null;
    private Font Font_TexteSimple_petit, Font_TexteSimple_petit_Gras = null;
    private Font Font_TexteSimple_Gras = null;
    private Font Font_TexteSimple_Italique = null;
    private Font Font_TexteSimple_Gras_Italique = null;
    public static final int ACTION_IMPRIMER = 0;
    public static final int ACTION_OUVRIR = 1;
    private SortiesTresorerie sortiesTresorerie = null;
    private PanelTresorerie gestionnaireTrsorerie;
    private String monnaie = "";

    public DocumentPDFTres(PanelTresorerie panel, int action, SortiesTresorerie sortiesTresorerie) {
        try {
            init(panel, action, sortiesTresorerie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(PanelTresorerie panel, int action, SortiesTresorerie sortiesTresorerie) {
        this.gestionnaireTrsorerie = panel;
        this.sortiesTresorerie = sortiesTresorerie;
        parametre_initialisation_fichier();
        parametre_construire_fichier();
        if (action == ACTION_OUVRIR) {
            parametres_ouvrir_fichier();
        } else if (action == ACTION_IMPRIMER) {
            parametres_imprimer_fichier();
        }
    }

    private void parametre_initialisation_fichier() {
        //Les titres du document
        this.Font_Titre1 = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD, BaseColor.DARK_GRAY);
        this.Font_Titre2 = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK);
        this.Font_Titre3 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK);

        //Les textes simples
        this.Font_TexteSimple = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL, BaseColor.BLACK);
        this.Font_TexteSimple_petit = new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.NORMAL, BaseColor.BLACK);
        this.Font_TexteSimple_petit_Gras = new Font(Font.FontFamily.TIMES_ROMAN, 7, Font.BOLD, BaseColor.BLACK);
        this.Font_TexteSimple_Gras = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK);
        this.Font_TexteSimple_Italique = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK);
        this.Font_TexteSimple_Gras_Italique = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLDITALIC, BaseColor.BLACK);
    }

    private void parametre_construire_fichier() {
        try {
            String nomFichier = gestionnaireTrsorerie.getNomfichierPreuve();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(nomFichier));
            writer.setPageEvent(new MarqueS2BTres());
            this.document.open();
            setDonneesBibliographiques();
            setContenuDeLaPage();
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(gestionnaireTrsorerie, "Impossible d'effectuer cette opération.\nAssurez vous qu'aucun fichier du même nom ne soit actuellement ouvert.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void parametres_ouvrir_fichier() {
        String nomFichier = "Annee_S2B.pdf";
        if (this.gestionnaireTrsorerie != null) {
            nomFichier = gestionnaireTrsorerie.getNomfichierPreuve();
        }
        File fic = new File(nomFichier);
        if (fic.exists() == true) {
            try {
                Desktop.getDesktop().open(fic);
                if (sortiesTresorerie != null) {
                    sortiesTresorerie.getEcouteurEnregistrement().onDone("PDF ouvert avec succès!");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                String message = "Impossible d'ouvrir le fichier !";
                JOptionPane.showMessageDialog(gestionnaireTrsorerie, message, "Erreur", JOptionPane.ERROR_MESSAGE);
                if (sortiesTresorerie != null) {
                    sortiesTresorerie.getEcouteurEnregistrement().onError(message);
                }
            }
        }
    }

    private void parametres_imprimer_fichier() {
        String nomFichier = "FicheElevesS2B.pdf";
        if (gestionnaireTrsorerie != null) {
            nomFichier = gestionnaireTrsorerie.getNomfichierPreuve();
        }
        File fic = new File(nomFichier);
        if (fic.exists() == true) {
            try {
                Desktop.getDesktop().print(fic);
                if (sortiesTresorerie != null) {
                    sortiesTresorerie.getEcouteurEnregistrement().onDone("Impression effectuée avec succès!");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                String message = "Impossible d'imprimer les données ";
                JOptionPane.showMessageDialog(gestionnaireTrsorerie, message, "Erreur", JOptionPane.ERROR_MESSAGE);
                if (sortiesTresorerie != null) {
                    sortiesTresorerie.getEcouteurEnregistrement().onError(message);
                }
            }
        }
    }

    private void setDonneesBibliographiques() {
        this.document.addTitle("Document généré par JS2BTresorerie");
        this.document.addSubject("Etat");
        this.document.addKeywords("Java, PDF, Tresorerie");
        this.document.addAuthor("S2B. Simple.Intuitif");
        this.document.addCreator("SULA BOSIO Serge, S2B, sulabosiog@gmail.com");
    }

    private void ajouterLigne(int number) throws Exception {
        Paragraph paragraphe = new Paragraph();
        for (int i = 0; i < number; i++) {
            paragraphe.add(new Paragraph(" "));
        }
        this.document.add(paragraphe);
    }

    private void setTitreEtDateDocument() throws Exception {
        Paragraph preface = new Paragraph();
        String titre = gestionnaireTrsorerie.getTitreDoc() + "";

        if (gestionnaireTrsorerie != null) {
            preface.add(getParagraphe("Date: " + UtilTresorerie.getDateFrancais(gestionnaireTrsorerie.getDateDocument()), Font_Titre3, Element.ALIGN_RIGHT));
            preface.add(getParagraphe(titre, Font_Titre1, Element.ALIGN_CENTER));
        } else {
            preface.add(getParagraphe("Date: " + UtilTresorerie.getDateFrancais(new Date()), Font_Titre3, Element.ALIGN_RIGHT));
            preface.add(getParagraphe("Facture n°XXXXXXXXX/2018", Font_Titre1, Element.ALIGN_CENTER));
        }
        this.document.add(preface);
    }

    private void setSignataire() throws Exception {
        if (gestionnaireTrsorerie != null) {
            this.document.add(getParagraphe(""
                    + "Produit par " + gestionnaireTrsorerie.getNomUtilisateur() + "\n"
                    + "Validé par :..............................................\n\n"
                    + "Signature", Font_TexteSimple, Element.ALIGN_RIGHT));
        } else {
            this.document.add(getParagraphe(""
                    + "Produit par Serge SULA BOSIO\n"
                    + "Validé par :..............................................\n\n"
                    + "Signature", Font_TexteSimple, Element.ALIGN_RIGHT));
        }

    }

    private void setBasDePage() throws Exception {
        if (gestionnaireTrsorerie != null) {
            Entreprise entreprise = gestionnaireTrsorerie.getEntreprise();
            if (entreprise != null) {
                this.document.add(getParagraphe(entreprise.getNom() + "\n" + entreprise.getAdresse() + " | " + entreprise.getTelephone() + " | " + entreprise.getEmail() + " | " + entreprise.getSiteWeb(), Font_TexteSimple, Element.ALIGN_CENTER));
            } else {
                addDefaultEntreprise();
            }
        } else {
            addDefaultEntreprise();
        }
    }

    private void addDefaultEntreprise() throws Exception {
        this.document.add(getParagraphe(""
                + "UAP RDC Sarl. Courtier d’Assurances n°0189\n"
                + "Prins van Luikschool, Av de la Gombe, Gombe, Kinshasa, DRC | (+243) 975 33 88 33 | info@aib-brokers.com", Font_TexteSimple, Element.ALIGN_CENTER));

    }

    private Paragraph getParagraphe(String texte, Font font, int alignement) {
        Paragraph par = new Paragraph(texte, font);
        par.setAlignment(alignement);
        return par;
    }

    private Phrase getPhrase(String texte, Font font) {
        Phrase phrase = new Phrase(texte, font);
        return phrase;
    }

    private void setLogoEtDetailsEntreprise() {
        try {
            PdfPTable tableauEnteteFacture = new PdfPTable(2);
            int[] dimensionsWidthHeight = {320, 1460};
            tableauEnteteFacture.setWidths(dimensionsWidthHeight);
            tableauEnteteFacture.setHorizontalAlignment(Element.ALIGN_LEFT);

            //CELLULE DU LOGO DE L'ENTREPRISE
            PdfPCell celluleLogoEntreprise = null;
            String logo = "";
            if (gestionnaireTrsorerie != null) {
                logo = this.gestionnaireTrsorerie.getDataTresorerie().getParametreTresorerie().getEntreprise().getLogo();
                System.out.println("Fic logo: " + logo);
            }
            File ficLogo = new File(new File(logo).getName());
            System.out.println("Fichier Logo: " + ficLogo.getAbsolutePath());
            if (ficLogo.exists() == true) {
                System.out.println("Fichier Logo: " + ficLogo.getAbsolutePath() + " - Trouvé!");
                //Chargement du logo et redimensionnement afin que celui-ci convienne dans l'espace qui lui est accordé
                Image Imglogo = Image.getInstance(ficLogo.getName());
                Imglogo.scaleAbsoluteWidth(70);
                Imglogo.scaleAbsoluteHeight(70);
                celluleLogoEntreprise = new PdfPCell(Imglogo);
            } else {
                celluleLogoEntreprise = new PdfPCell();
            }
            celluleLogoEntreprise.setPadding(2);
            celluleLogoEntreprise.setBorderWidth(0);
            celluleLogoEntreprise.setBorderColor(BaseColor.BLACK);
            tableauEnteteFacture.addCell(celluleLogoEntreprise);

            //CELLULE DES DETAILS SUR L'ENTREPRISE - TEXTE (Nom, Adresse, Téléphone, Email, etc)
            PdfPCell celluleDetailsEntreprise = new PdfPCell();
            celluleDetailsEntreprise.setPadding(2);
            celluleDetailsEntreprise.setPaddingLeft(5);
            celluleDetailsEntreprise.setBorderWidth(0);
            celluleDetailsEntreprise.setBorderWidthLeft(1);
            celluleDetailsEntreprise.setBorderColor(BaseColor.BLACK);
            celluleDetailsEntreprise.setHorizontalAlignment(Element.ALIGN_TOP);

            if (gestionnaireTrsorerie != null) {
                Entreprise entreprise = gestionnaireTrsorerie.getEntreprise();
                if (entreprise != null) {
                    celluleDetailsEntreprise.addElement(getParagraphe(entreprise.getNom(), Font_Titre2, Element.ALIGN_LEFT));
                    celluleDetailsEntreprise.addElement(getParagraphe(entreprise.getAdresse(), Font_TexteSimple_petit, Element.ALIGN_LEFT));
                    celluleDetailsEntreprise.addElement(getParagraphe(entreprise.getSiteWeb() + " | " + entreprise.getEmail() + " | " + entreprise.getTelephone(), Font_TexteSimple_petit, Element.ALIGN_LEFT));
                    celluleDetailsEntreprise.addElement(getParagraphe("RCC : " + entreprise.getRccm() + "\nID. NAT : " + entreprise.getIdnat() + "\nNIF : " + entreprise.getNumeroImpot(), Font_TexteSimple_petit, Element.ALIGN_LEFT));
                }
            } else {
                celluleDetailsEntreprise.addElement(getParagraphe("UAP RDC Sarl, Courtier d'Assurances n°0189", Font_Titre2, Element.ALIGN_LEFT));
                celluleDetailsEntreprise.addElement(getParagraphe("Avenue de la Gombe, Kinshasa/Gombe", Font_TexteSimple_petit, Element.ALIGN_LEFT));
                celluleDetailsEntreprise.addElement(getParagraphe("https://www.aib-brokers.com | info@aib-brokers.com | (+243)84 480 35 14 - (+243)82 87 27 706", Font_TexteSimple_petit, Element.ALIGN_LEFT));
                celluleDetailsEntreprise.addElement(getParagraphe("RCC : CDF/KIN/2015-1245\nID. NAT : 0112487789\nNIF : 012245", Font_TexteSimple_petit, Element.ALIGN_LEFT));
            }
            tableauEnteteFacture.addCell(celluleDetailsEntreprise);

            //On insère le le tableau entete (logo et détails de l'entreprise) dans la page
            document.add(tableauEnteteFacture);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PdfPCell getCelluleTableau(String texte, float BorderWidth, BaseColor background, BaseColor textColor, int alignement, Font font) {
        PdfPCell cellule = new PdfPCell();
        cellule.setBorderWidth(BorderWidth);
        if (background != null) {
            cellule.setBackgroundColor(background);
        } else {
            cellule.setBackgroundColor(BaseColor.WHITE);
        }
        if (textColor != null) {
            font.setColor(textColor);
        } else {
            font.setColor(BaseColor.BLACK);
        }
        cellule.setHorizontalAlignment(alignement);
        cellule.setPhrase(getPhrase(texte, font));
        return cellule;
    }

    private PdfPTable getTableau(float totalWidth, String[] titres, int[] widths, int alignement, float borderWidth) {
        try {
            PdfPTable tableau = new PdfPTable(widths.length);
            if (totalWidth != -1) {
                tableau.setTotalWidth(totalWidth);
            } else {
                tableau.setTotalWidth(PageSize.A4.getWidth() - 72);
            }
            tableau.setLockedWidth(true);
            tableau.setWidths(widths);
            tableau.setHorizontalAlignment(alignement);
            if (titres != null) {
                tableau.setSpacingBefore(3);
                for (String titre : titres) {
                    tableau.addCell(getCelluleTableau(titre, borderWidth, BaseColor.LIGHT_GRAY, null, Element.ALIGN_CENTER, Font_TexteSimple_Gras));
                }
            }

            return tableau;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getRevenu(int idRevenu) {
        for (InterfaceRevenu Irev : gestionnaireTrsorerie.getParametreTresorerie().getRevenus()) {
            if (idRevenu == Irev.getId()) {
                return UtilTresorerie.getTexteCourt(Irev.getNom(), 15);
            }
        }
        return "";
    }

    private String getCharge(int idCharge) {
        for (InterfaceCharge Ich : gestionnaireTrsorerie.getParametreTresorerie().getCharges()) {
            if (idCharge == Ich.getId()) {
                return Ich.getNom();
            }
        }
        return "";
    }

    private String getMonnaie(int idMonnaie) {
        for (InterfaceMonnaie Ich : gestionnaireTrsorerie.getParametreTresorerie().getMonnaies()) {
            if (idMonnaie == Ich.getId()) {
                return Ich.getCode();
            }
        }
        return "";
    }

    private String getDestinationSource(int id) {
        if (id == InterfaceEncaissement.DESTINATION_BANQUE) {
            return "BANQUE";
        } else {
            return "CAISSE";
        }
    }

    private void setTableauEncaissement() {
        /* */
        //{"N°", "Date", "Destination", "Reference", "Motif", "Nature", "Effectué par", "Montant", "Monnaie"}
        try {
            if (sortiesTresorerie != null) {
                gestionnaireTrsorerie.actualiserTotalEncaissement();
                String total = gestionnaireTrsorerie.getMonnaieOutput() + " " + UtilTresorerie.getMontantFrancais(gestionnaireTrsorerie.getTotalEncaissement());
                document.add(getParagraphe("LISTE D'ENCAISSEMENTS", Font_TexteSimple_Gras_Italique, Element.ALIGN_LEFT));
                document.add(getParagraphe(gestionnaireTrsorerie.getNavigateurPagesEncaissement().toStringCriteres(), Font_TexteSimple_petit, Element.ALIGN_LEFT));

                PdfPTable tableEncaissements = getTableau(
                        780f,//-1
                        new String[]{"N°", "Date", "Destination", "Référence", "Motif", "Nature", "Effectué par", "Montant"},
                        new int[]{30, 120, 100, 100, 300, 200, 200, 150},
                        Element.ALIGN_CENTER,
                        0.2f
                );
                Vector<Encaissement> listeEncaissements = sortiesTresorerie.getListeEncaissements();
                int iEnc = 0;
                for (InterfaceEncaissement Ienc : listeEncaissements) {
                    //écriture dans chaque cellule de la ligne
                    tableEncaissements.addCell(getCelluleTableau("" + (iEnc + 1), 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
                    tableEncaissements.addCell(getCelluleTableau(UtilTresorerie.getDateFrancais(Ienc.getDate()), 0.2f, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
                    tableEncaissements.addCell(getCelluleTableau(getDestinationSource(Ienc.getDestination()), 0.2f, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
                    tableEncaissements.addCell(getCelluleTableau(Ienc.getReference(), 0.2f, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
                    tableEncaissements.addCell(getCelluleTableau(UtilTresorerie.getTexteCourt(Ienc.getMotif(), 30), 0.2f, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
                    tableEncaissements.addCell(getCelluleTableau(getRevenu(Ienc.getIdRevenu()), 0.2f, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
                    tableEncaissements.addCell(getCelluleTableau(UtilTresorerie.getTexteCourt(Ienc.getEffectuePar(), 20), 0.2f, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
                    tableEncaissements.addCell(getCelluleTableau(getMonnaie(Ienc.getIdMonnaie()) + " " + UtilTresorerie.getMontantFrancais(Ienc.getMontant()), 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
                    //incrémentaion
                    iEnc++;
                }

                //La dernière ligne du table
                setDerniereLigne(tableEncaissements, total);
                document.add(tableEncaissements);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTableauDecaissement() {
        /* */
        //{"N°", "Date", "Source", "Reference", "Motif", "Nature", "Bénéficiaire", "Montant", "Monnaie"}
        try {
            if (sortiesTresorerie != null) {
                gestionnaireTrsorerie.actualiserTotalDecaissement();
                String total = gestionnaireTrsorerie.getMonnaieOutput() + " " + UtilTresorerie.getMontantFrancais(gestionnaireTrsorerie.getTotalDecaissement());
                document.add(getParagraphe("LISTE DES DECAISSEMENTS", Font_TexteSimple_Gras_Italique, Element.ALIGN_LEFT));
                document.add(getParagraphe(gestionnaireTrsorerie.getNavigateurPagesDecaissement().toStringCriteres(), Font_TexteSimple_petit, Element.ALIGN_LEFT));

                PdfPTable tableDecaissements = getTableau(
                        780f,//-1
                        new String[]{"N°", "Date", "Source", "Référence", "Motif", "Nature", "Bénéficiaire", "Montant"},
                        new int[]{30, 120, 100, 100, 300, 200, 200, 150},
                        Element.ALIGN_CENTER,
                        0.2f
                );
                Vector<Decaissement> listeDecaissements = sortiesTresorerie.getListeDecaissements();
                int iEnc = 0;
                for (InterfaceDecaissement Ienc : listeDecaissements) {
                    //écriture dans chaque cellule de la ligne
                    tableDecaissements.addCell(getCelluleTableau("" + (iEnc + 1), 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
                    tableDecaissements.addCell(getCelluleTableau(UtilTresorerie.getDateFrancais(Ienc.getDate()), 0.2f, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
                    tableDecaissements.addCell(getCelluleTableau(getDestinationSource(Ienc.getSource()), 0.2f, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
                    tableDecaissements.addCell(getCelluleTableau(Ienc.getReference(), 0.2f, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
                    tableDecaissements.addCell(getCelluleTableau(UtilTresorerie.getTexteCourt(Ienc.getMotif(), 30), 0.2f, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
                    tableDecaissements.addCell(getCelluleTableau(getCharge(Ienc.getIdCharge()), 0.2f, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
                    tableDecaissements.addCell(getCelluleTableau(UtilTresorerie.getTexteCourt(Ienc.getBeneficiaire(), 20), 0.2f, BaseColor.WHITE, null, Element.ALIGN_LEFT, Font_TexteSimple));
                    tableDecaissements.addCell(getCelluleTableau(getMonnaie(Ienc.getIdMonnaie()) + " " + UtilTresorerie.getMontantFrancais(Ienc.getMontant()), 0.2f, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
                    //incrémentaion
                    iEnc++;
                }

                //La dernière ligne du table
                setDerniereLigne(tableDecaissements, total);
                document.add(tableDecaissements);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDerniereLigne(PdfPTable table, String total) {
        table.addCell(getCelluleTableau("", 0, BaseColor.LIGHT_GRAY, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));
        table.addCell(getCelluleTableau("Total", 0, BaseColor.LIGHT_GRAY, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));
        table.addCell(getCelluleTableau("", 0, BaseColor.LIGHT_GRAY, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));
        table.addCell(getCelluleTableau("", 0, BaseColor.LIGHT_GRAY, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));
        table.addCell(getCelluleTableau("", 0, BaseColor.LIGHT_GRAY, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));
        table.addCell(getCelluleTableau("", 0, BaseColor.LIGHT_GRAY, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));
        table.addCell(getCelluleTableau("", 0, BaseColor.LIGHT_GRAY, null, Element.ALIGN_LEFT, Font_TexteSimple_Gras));
        table.addCell(getCelluleTableau(total, 0.2f, BaseColor.LIGHT_GRAY, null, Element.ALIGN_RIGHT, Font_TexteSimple_Gras));
    }

    private void setLigneSeparateur() {
        try {
            Chunk linebreak = new Chunk(new DottedLineSeparator());
            document.add(linebreak);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTableauSynthese() {
        try {
            Paragraph paragraphe = new Paragraph();
            PdfPTable tableSynthese = getTableau(
                    160f,
                    new String[]{"Synthèse", ""},
                    new int[]{100, 150},
                    Element.ALIGN_RIGHT,
                    0
            );
            if (gestionnaireTrsorerie != null) {
                paragraphe.add(getParagraphe(gestionnaireTrsorerie.getTauxChange(), Font_TexteSimple_petit, Element.ALIGN_RIGHT));
                double totEncaissements = gestionnaireTrsorerie.getTotalEncaissement();
                double totDecaisssments = gestionnaireTrsorerie.getTotalDecaissement();
                setLignesTabSynthese(tableSynthese, 0, gestionnaireTrsorerie.getMonnaieOutput(), totEncaissements, totDecaisssments);
            } else {
                setLignesTabSynthese(tableSynthese, 0, "$", 1000, 800);
            }
            document.add(tableSynthese);
            document.add(paragraphe);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setLignesTabSynthese(PdfPTable tableau, float borderwidth, String monnaie, double totalEncaissement, double totalDecaissement) {
        //Total Encaissement
        tableau.addCell(getCelluleTableau("Encaissements:", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau(UtilTresorerie.getMontantFrancais(totalEncaissement) + " " + monnaie, borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
        //Total Decaissement
        tableau.addCell(getCelluleTableau("Décaissements:", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
        tableau.addCell(getCelluleTableau(UtilTresorerie.getMontantFrancais(totalDecaissement) + " " + monnaie, borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple));
        //Total Solde
        tableau.addCell(getCelluleTableau("Solde:", borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_Gras));
        tableau.addCell(getCelluleTableau(UtilTresorerie.getMontantFrancais(totalEncaissement - totalDecaissement) + " " + monnaie, borderwidth, BaseColor.WHITE, null, Element.ALIGN_RIGHT, Font_TexteSimple_Gras));
    }

    private void setContenuDeLaPage() throws Exception {
        if (sortiesTresorerie != null) {
            sortiesTresorerie.getEcouteurEnregistrement().onUploading("Construction du contenu...");
        }
        setLogoEtDetailsEntreprise();
        setTitreEtDateDocument();
        //Corps
        if (gestionnaireTrsorerie.getTypeExportation() == 0) {
            //Lorsqu'il s'agit des encaissements
            setTableauEncaissement();
        } else if (gestionnaireTrsorerie.getTypeExportation() == 1) {
            //Lorsqu'il s'agit des decaissements
            setTableauDecaissement();
        } else {
            setTableauSynthese();
            setTableauEncaissement();
            ajouterLigne(1);
            setTableauDecaissement();
        }
        //Fin du corps
        ajouterLigne(1);
        setSignataire();
        setBasDePage();
        if (sortiesTresorerie != null) {
            sortiesTresorerie.getEcouteurEnregistrement().onUploading("Finalisation...");
        }
    }

    public static void main(String[] a) {
        //Exemple
        DocumentPDFTres docpdf = new DocumentPDFTres(null, ACTION_OUVRIR, null);
    }

}















