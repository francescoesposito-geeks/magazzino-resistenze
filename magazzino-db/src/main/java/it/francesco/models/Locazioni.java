package it.francesco.models;

public class Locazioni {
    private int idLocazione;
    private String corsia;
    private String colonna;
    private String ripiano;

    public Locazioni(int idLocazione, String corsia, String colonna, String ripiano) {

        this.idLocazione = idLocazione;
        this.corsia = corsia;
        this.colonna = colonna;
        this.ripiano = ripiano;

    }

    public Locazioni() {

        this.idLocazione = 0;
        this.corsia = null;
        this.colonna = null;
        this.ripiano = null;

    }

    public int getIdLocazione() {
        return this.idLocazione;
    }

    public String getCorsia() {
        return this.corsia;
    }

    public String getColonna() {
        return this.colonna;
    }

    public String getRipiano() {
        return this.ripiano;
    }

    public void setIdLocazione(int idLocazione) {
        this.idLocazione = idLocazione;
    }

    public void setCorsia(String corsia) {
        this.corsia = corsia;
    }

    public void setColonna(String colonna) {
        this.colonna = colonna;
    }

    public void setRipiano(String ripiano) {
        this.ripiano = ripiano;
    }

}