package it.francesco.models;

/**
 * Confezione con ingresso confermato e uscita non ancora confermata:
 * è una "missione di uscita" da completare per l'operatore.
 * Oggetto di sola lettura, popolato dai dati del database.
 */
public class MissioniU {

    private final String barcode;
    private final String descrizione;
    private final int codiceInterno;
    private final int idLocazione;
    private final String corsia;
    private final String colonna;
    private final String ripiano;

    public MissioniU(String barcode, String descrizione, int codiceInterno, int idLocazione,
            String corsia, String colonna, String ripiano) {
        this.barcode = barcode;
        this.descrizione = descrizione;
        this.codiceInterno = codiceInterno;
        this.idLocazione = idLocazione;
        this.corsia = corsia;
        this.colonna = colonna;
        this.ripiano = ripiano;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public int getCodiceInterno() {
        return codiceInterno;
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
}