package efrei.moutte.moutte_project.models;

import java.util.List;

public class Annonce {

    private String objectID;
    private String id_annonce;
    private String source;
    private String type;
    private int prix;
    private String cp;
    private String ville;
    private int surface;
    private int prixm2;
    private float rendement;
    private float loyer;
    private int investissement;
    private String transaction;
    private String description;
    private int nbpieces;
    private boolean neuf;
    private String permalien;
    private String img_url;
    private String travaux;
    private String montant;
    private List<String> locations;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }



    public Annonce() {
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getId_annonce() {
        return id_annonce;
    }

    public void setId_annonce(String id_annonce) {
        this.id_annonce = id_annonce;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public int getSurface() {
        return surface;
    }

    public void setSurface(int surface) {
        this.surface = surface;
    }

    public int getPrixm2() {
        return prixm2;
    }

    public void setPrixm2(int prixm2) {
        this.prixm2 = prixm2;
    }

    public float getRendement() {
        return rendement;
    }

    public void setRendement(float rendement) {
        this.rendement = rendement;
    }

    public float getLoyer() {
        return loyer;
    }

    public void setLoyer(float loyer) {
        this.loyer = loyer;
    }

    public int getInvestissement() {
        return investissement;
    }

    public void setInvestissement(int investissement) {
        this.investissement = investissement;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNbpieces() {
        return nbpieces;
    }

    public void setNbpieces(int nbpieces) {
        this.nbpieces = nbpieces;
    }

    public boolean isNeuf() {
        return neuf;
    }

    public void setNeuf(boolean neuf) {
        this.neuf = neuf;
    }

    public String getPermalien() {
        return permalien;
    }

    public void setPermalien(String permalien) {
        this.permalien = permalien;
    }

    public void setTravaux(String travaux) {
        this.travaux = travaux;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public String getTravaux() {
        return travaux;
    }

    public String getMontant() {
        return montant;
    }

    public List<String> getLocations() {
        return locations;
    }
}
