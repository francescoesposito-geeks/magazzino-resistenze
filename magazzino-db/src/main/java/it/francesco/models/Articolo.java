package it.francesco.models;

public class Articolo {
    private int idArticolo;
    private String descrizione;
    private int numPezziMax;
    private String barCode;

    public Articolo(int idArticolo, String descrizione, int numPezziMax, String barCode) {
        this.idArticolo = idArticolo;
        this.descrizione = descrizione;
        this.numPezziMax = numPezziMax;
        this.barCode = barCode;
    }

    public Articolo() {
        this.idArticolo = 0;
        this.descrizione = null;
        this.numPezziMax = 0;
        this.barCode = null;

    }

    @Override
    public String toString() {
        return idArticolo + " " + descrizione + " " + numPezziMax + " " + barCode;
    }


    public int getIdArticolo() {
        return this.idArticolo;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public int getNumPezziMax() {
        return this.numPezziMax;
    }

    public String getBarcode() {
        return this.barCode;
    }

    public void setIdArticolo(int idArticolo) {
        this.idArticolo = idArticolo;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setNumPezziMax(int numPezziMax) {
        this.numPezziMax = numPezziMax;
    }

    public void setBarcode(String barcode) {
        this.barCode = barcode;
    }


}