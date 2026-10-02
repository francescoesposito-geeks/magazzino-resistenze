package it.francesco.models;

public class ViewOccupazioneLocazioni {
    private int idLocazione;
    private String corsia;
    private String colonna;
    private String ripiano;
    private int numeroPezzi;
    private int idArticolo;

    public ViewOccupazioneLocazioni(int idLocazione, String corsia, String colonna, String ripiano, int numeroPezzi,
            int idArticolo) {
        this.idLocazione = idLocazione;
        this.corsia = corsia;
        this.colonna = colonna;
        this.ripiano = ripiano;
        this.numeroPezzi = numeroPezzi;
        this.idArticolo = idArticolo;
    }

    public ViewOccupazioneLocazioni() {
        this.idLocazione = 0;
        this.corsia = null;
        this.colonna = null;
        this.ripiano = null;
        this.numeroPezzi = 0;
        this.idArticolo = 0;

    }

    public int getIdLocazione() {
        return idLocazione;
    }

    public String getCorsia() {
        return corsia;
    }

    public String getColonna() {
        return colonna;
    }

    public String getRipiano() {
        return ripiano;
    }

    public int getNumeroPezzi() {
        return numeroPezzi;
    }

    public int getIdArticolo() {
        return idArticolo;
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

    public void setNumeroPezzi(int numeroPezzi) {
        this.numeroPezzi = numeroPezzi;
    }

    public void setIdArticolo(int idArticolo) {
        this.idArticolo = idArticolo;
    }
}
