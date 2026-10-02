package it.francesco.models;

import java.time.LocalDate;

public class MissioniU {
    private String barcode;
    private String descrizione;
    private int codiceInterno;
    private int idLocazione;
    private String corsia;
    private String colonna;
    private String ripiano;

    public MissioniU(String barcode,
            String descrizione,
            int codiceInterno,
            int idLocazione,
            String corsia,
            String colonna,
            String ripiano) {
        this.barcode = barcode;
        this.descrizione = descrizione;
        this.codiceInterno = codiceInterno;
        this.idLocazione = idLocazione;
        this.corsia = corsia;
        this.colonna = colonna;
        this.ripiano = ripiano;

    }

    public String getBarcode() {
        return this.barcode;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public int getCodiceInterno() {
        return this.codiceInterno;
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

    public void setbarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setCodiceInterno(int idLocazione) {
        this.codiceInterno = codiceInterno;
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
