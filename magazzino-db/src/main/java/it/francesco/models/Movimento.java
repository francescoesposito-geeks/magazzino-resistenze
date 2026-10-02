package it.francesco.models;

import java.time.LocalDateTime;

public class Movimento {

    private int idMovimento;
    private boolean confermatoE;
    private boolean confermatoU;
    private LocalDateTime dataOraE;
    private LocalDateTime dataOraU;
    private int idArticolo;
    private int idLocazione;
    private int codiceInterno;

    public Movimento(int idMovimento,
            boolean confermatoE,
            boolean confermatoU,
            LocalDateTime dataOraE,
            LocalDateTime dataOraU,
            int idArticolo,
            int idLocazione,
            int codiceInterno

    ) {
        this.idMovimento = idMovimento;
        this.confermatoE = confermatoE;
        this.confermatoU = confermatoU;
        this.dataOraE = dataOraE;
        this.dataOraU = dataOraU;
        this.idArticolo = idArticolo;
        this.idLocazione = idLocazione;
        this.codiceInterno = codiceInterno;

    }

    public Movimento() {
        this.idMovimento = 0;
        this.confermatoE = false;
        this.confermatoU = false;
        this.dataOraE = null;
        this.dataOraU = null;
        this.idArticolo = 0;
        this.idLocazione = 0;
        this.codiceInterno = 0;
    }

    public int getIdMovimento() {
        return idMovimento;
    }

    public boolean isConfermatoE() {
        return confermatoE;
    }

    public boolean isConfermatoU() {
        return confermatoU;
    }

    public LocalDateTime getDataOraE() {
        return dataOraE;
    }

    public LocalDateTime getDataOraU() {
        return dataOraU;
    }

    public int getIdArticolo() {
        return idArticolo;
    }

    public int getIdLocazione() {
        return idLocazione;
    }
    public int getCodiceInterno() {
        return codiceInterno;
    }

    public void setIdMovimento(int idMovimento) {
        this.idMovimento = idMovimento;
    }

    public void setConfermatoE(boolean confermatoE) {
        this.confermatoE = confermatoE;
    }

    public void setConfermatoU(boolean confermatoU) {
        this.confermatoU = confermatoU;
    }

    public void setDataOraE(LocalDateTime dataOraE) {
        this.dataOraE = dataOraE;
    }

    public void setDataOraU(LocalDateTime dataOraU) {
        this.dataOraU = dataOraU;
    }

    public void setIdArticolo(int idArticolo) {
        this.idArticolo = idArticolo;
    }

    public void setIdLocazione(int idLocazione) {
        this.idLocazione = idLocazione;
    }
    public void setCodiceInterno(int codiceInterno) {
        this.codiceInterno = codiceInterno;
    }
}
