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

@Service
public class BarcodeService {

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

    public void aggiungiBarcode(String barcode, String descrizione) {
        Articolo articolo = new Articolo();
        articolo.setBarcode(barcode);
        articolo.setDescrizione(descrizione);
        articolo.setNumPezziMax(10);
        articoliDbUtil.salvaArticolo(articolo);

    }

    public LocationInfo trovaLocazione(String barcode) throws DataLayerException {
        Articolo articolo = articoliDbUtil.recuperaArticoloConBarcode(barcode);
        int idLocazione = 0;
        if (articolo.getIdArticolo() == 0) {

            return null;
        }
        LocationNumber locationNumber = wArticoliPresentiDbUtil.getIdLocazione(articolo.getIdArticolo());
        if (locationNumber != null) {
            idLocazione = locationNumber.getIdLocazione();
            if (locationNumber.getNumeroPezzi() < articolo.getNumPezziMax()) {

                idLocazione = locationNumber.getIdLocazione();

            } else {
                idLocazione = wLocationDisponibiliDbUtil.getOccupazioneLocazioni().get(0).getIdLocazione();
            }
        } else {
            idLocazione = wLocationDisponibiliDbUtil.getOccupazioneLocazioni().get(0).getIdLocazione();
        }

        Movimento movimento = new Movimento();
        movimento.setIdLocazione(idLocazione);
        movimento.setIdArticolo(articolo.getIdArticolo());
        movimento = movimentoDbUtil.salvaMovimento(movimento);

        Locazioni locazione = locazioniDbUtil.recuperLocazioneConId(idLocazione);
        LocationInfo locationInfo = new LocationInfo(idLocazione, locazione.getCorsia(), locazione.getColonna(),
                locazione.getRipiano(), movimento.getIdMovimento(), movimento.getCodiceInterno());
        return locationInfo;

    }

    public boolean confermaMovimentoE(int codiceInterno, int idLocazione) throws DataLayerException {
        Movimento movimento = movimentoDbUtil.cercaMovimentoPerCodiceInterno(codiceInterno);

        if (movimento.getIdLocazione() != idLocazione) {
            return false;
        }
        movimento.setConfermatoE(true);
        movimento.setDataOraE(LocalDateTime.now());
        movimentoDbUtil.modificaMovimentoConId(movimento);
        return true;

    }

    public boolean confermaMovimentoU(int codiceInterno, int idLocazione) throws DataLayerException {
        Movimento movimento = movimentoDbUtil.cercaMovimentoPerCodiceInterno(codiceInterno);
         if (movimento.getIdLocazione() != idLocazione) {
            return false;
        }
        movimento.setDataOraU(LocalDateTime.now());
        movimento.setConfermatoU(true);
        movimentoDbUtil.modificaMovimentoConId(movimento);

        return true;

    }

    public Movimento eseguiUscita(int codiceInterno) throws DataLayerException {
        Movimento movimento = movimentoDbUtil.cercaMovimentoPerCodiceInterno(codiceInterno);
        movimento.setDataOraU(LocalDateTime.now());
        movimentoDbUtil.modificaMovimentoConId(movimento);

        return movimento;

    }

    public ArrayList<Missione> getMissioni(boolean entrataUscita) throws DataLayerException {
        ArrayList<Missione> response = new ArrayList<>();

        if (entrataUscita == true) {
            ArrayList<MissioniE> missioniEntrata = movimentoDbUtil.missioneEntrata();
            for (MissioniE missioneEntrata : missioniEntrata) {
                Missione missione = new Missione(missioneEntrata.getBarcode(),
                        missioneEntrata.getDescrizione(),
                        missioneEntrata.getCodiceInterno(),
                        missioneEntrata.getIdLocazione(),
                        missioneEntrata.getCorsia(),
                        missioneEntrata.getColonna(),
                        missioneEntrata.getRipiano());

                response.add(missione);

            }
        } else {
            ArrayList<MissioniU> missioniUscita = movimentoDbUtil.missioneUscita();
            for (MissioniU missioneUscita : missioniUscita) {
                Missione missione = new Missione(missioneUscita.getBarcode(),
                        missioneUscita.getDescrizione(),
                        missioneUscita.getCodiceInterno(),
                        missioneUscita.getIdLocazione(),
                        missioneUscita.getCorsia(),
                        missioneUscita.getColonna(),
                        missioneUscita.getRipiano());

                response.add(missione);

            }

        }

        return response;

    }

}
