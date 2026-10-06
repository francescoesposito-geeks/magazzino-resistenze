package it.francesco.magazzino.services;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.francesco.database.ArticoliDbUtil;
import it.francesco.database.LocazioniDbUtil;
import it.francesco.database.MovimentoDbUtil;
import it.francesco.database.WArticoliPresentiDbUtil;
import it.francesco.database.WOccupazioneLocazioneDbUtil;
import it.francesco.exceptions.DataLayerException;
import it.francesco.magazzino.services.dto.LocationInfo;
import it.francesco.magazzino.services.dto.Missione;
import it.francesco.models.Articolo;
import it.francesco.models.LocationNumber;
import it.francesco.models.Locazioni;
import it.francesco.models.MissioniE;
import it.francesco.models.MissioniU;
import it.francesco.models.Movimento;
import it.francesco.models.ViewOccupazioneLocazioni;

@Service
public class BarcodeService {

    /** Esito di un'operazione: il controller lo traduce nella risposta HTTP. */
    public enum Esito {
        OK(""),
        BARCODE_GIA_REGISTRATO("Esiste già un articolo con questo barcode"),
        MOVIMENTO_NON_TROVATO("Movimento non trovato"),
        LOCAZIONE_ERRATA("La locazione indicata non corrisponde al movimento"),
        ENTRATA_GIA_CONFERMATA("Entrata già confermata"),
        ENTRATA_NON_CONFERMATA("Entrata non ancora confermata"),
        PRELIEVO_GIA_ESEGUITO("Prelievo già eseguito"),
        PRELIEVO_NON_ESEGUITO("Prelievo non ancora eseguito"),
        USCITA_GIA_CONFERMATA("Uscita già confermata");

        private final String messaggio;

        Esito(String messaggio) {
            this.messaggio = messaggio;
        }

        public String getMessaggio() {
            return messaggio;
        }
    }

    /** Nessuna locazione libera disponibile per un nuovo ingresso. */
    public static class MagazzinoPienoException extends Exception {
        public MagazzinoPienoException() {
            super("Nessuna locazione libera disponibile");
        }
    }

    private static final int NUM_PEZZI_MAX_DEFAULT = 10;

    @Autowired
    private ArticoliDbUtil articoliDbUtil;
    @Autowired
    private WOccupazioneLocazioneDbUtil wLocationDisponibiliDbUtil;
    @Autowired
    private WArticoliPresentiDbUtil wArticoliPresentiDbUtil;
    @Autowired
    private MovimentoDbUtil movimentoDbUtil;
    @Autowired
    private LocazioniDbUtil locazioniDbUtil;

    public Esito aggiungiBarcode(String barcode, String descrizione) throws DataLayerException {
        if (articoliDbUtil.recuperaArticoloConBarcode(barcode).getIdArticolo() != 0) {
            return Esito.BARCODE_GIA_REGISTRATO;
        }

        Articolo articolo = new Articolo();
        articolo.setBarcode(barcode);
        articolo.setDescrizione(descrizione);
        articolo.setNumPezziMax(NUM_PEZZI_MAX_DEFAULT);

        Articolo salvato = articoliDbUtil.salvaArticolo(articolo);
        if (salvato.getIdArticolo() == 0) {
            throw new DataLayerException();
        }
        return Esito.OK;
    }

    /**
     * Assegna una locazione a una nuova confezione e registra il movimento di entrata.
     * synchronized evita che due ingressi contemporanei ricevano la stessa locazione
     * oltre la capienza.
     *
     * @return la locazione assegnata, oppure null se il barcode non corrisponde a nessun articolo
     */
    public synchronized LocationInfo trovaLocazione(String barcode)
            throws DataLayerException, MagazzinoPienoException {

        Articolo articolo = articoliDbUtil.recuperaArticoloConBarcode(barcode);
        if (articolo.getIdArticolo() == 0) {
            return null;
        }

        int idLocazione;
        LocationNumber locazioneConArticolo = wArticoliPresentiDbUtil.getIdLocazione(articolo.getIdArticolo());

        if (locazioneConArticolo != null && locazioneConArticolo.getNumeroPezzi() < articolo.getNumPezziMax()) {
            idLocazione = locazioneConArticolo.getIdLocazione();
        } else {
            ArrayList<ViewOccupazioneLocazioni> locazioniLibere = wLocationDisponibiliDbUtil.getOccupazioneLocazioni();
            if (locazioniLibere == null) {
                throw new DataLayerException();
            }
            if (locazioniLibere.isEmpty()) {
                throw new MagazzinoPienoException();
            }
            idLocazione = locazioniLibere.get(0).getIdLocazione();
        }

        Movimento movimento = new Movimento();
        movimento.setIdLocazione(idLocazione);
        movimento.setIdArticolo(articolo.getIdArticolo());
        movimento = movimentoDbUtil.salvaMovimento(movimento);

        Locazioni locazione = locazioniDbUtil.recuperLocazioneConId(idLocazione);
        if (locazione == null) {
            throw new DataLayerException();
        }

        return new LocationInfo(idLocazione, locazione.getCorsia(), locazione.getColonna(),
                locazione.getRipiano(), movimento.getIdMovimento(), movimento.getCodiceInterno());
    }

    public Esito confermaMovimentoE(int codiceInterno, int idLocazione) throws DataLayerException {
        Movimento movimento = movimentoDbUtil.cercaMovimentoPerCodiceInterno(codiceInterno);
        if (movimento == null) {
            return Esito.MOVIMENTO_NON_TROVATO;
        }
        if (movimento.getIdLocazione() != idLocazione) {
            return Esito.LOCAZIONE_ERRATA;
        }
        if (movimento.isConfermatoE()) {
            return Esito.ENTRATA_GIA_CONFERMATA;
        }

        movimento.setConfermatoE(true);
        movimento.setDataOraE(LocalDateTime.now());
        movimentoDbUtil.modificaMovimentoConId(movimento);
        return Esito.OK;
    }

    public Esito eseguiUscita(int codiceInterno) throws DataLayerException {
        Movimento movimento = movimentoDbUtil.cercaMovimentoPerCodiceInterno(codiceInterno);
        if (movimento == null) {
            return Esito.MOVIMENTO_NON_TROVATO;
        }
        if (!movimento.isConfermatoE()) {
            return Esito.ENTRATA_NON_CONFERMATA;
        }
        if (movimento.getDataOraU() != null) {
            return Esito.PRELIEVO_GIA_ESEGUITO;
        }

        movimento.setDataOraU(LocalDateTime.now());
        movimentoDbUtil.modificaMovimentoConId(movimento);
        return Esito.OK;
    }

    public Esito confermaMovimentoU(int codiceInterno, int idLocazione) throws DataLayerException {
        Movimento movimento = movimentoDbUtil.cercaMovimentoPerCodiceInterno(codiceInterno);
        if (movimento == null) {
            return Esito.MOVIMENTO_NON_TROVATO;
        }
        if (movimento.getIdLocazione() != idLocazione) {
            return Esito.LOCAZIONE_ERRATA;
        }
        if (movimento.getDataOraU() == null) {
            return Esito.PRELIEVO_NON_ESEGUITO;
        }
        if (movimento.isConfermatoU()) {
            return Esito.USCITA_GIA_CONFERMATA;
        }

        movimento.setConfermatoU(true);
        movimentoDbUtil.modificaMovimentoConId(movimento);
        return Esito.OK;
    }

    public ArrayList<Missione> getMissioni(boolean entrata) throws DataLayerException {
        ArrayList<Missione> risultato = new ArrayList<>();

        if (entrata) {
            for (MissioniE m : movimentoDbUtil.missioneEntrata()) {
                risultato.add(new Missione(m.getBarcode(), m.getDescrizione(), m.getCodiceInterno(),
                        m.getIdLocazione(), m.getCorsia(), m.getColonna(), m.getRipiano()));
            }
        } else {
            for (MissioniU m : movimentoDbUtil.missioneUscita()) {
                risultato.add(new Missione(m.getBarcode(), m.getDescrizione(), m.getCodiceInterno(),
                        m.getIdLocazione(), m.getCorsia(), m.getColonna(), m.getRipiano()));
            }
        }
        return risultato;
    }
}