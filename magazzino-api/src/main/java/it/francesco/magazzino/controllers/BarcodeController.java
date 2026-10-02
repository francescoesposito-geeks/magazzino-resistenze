package it.francesco.magazzino.controllers;

import org.springframework.web.bind.annotation.RestController;

import it.francesco.exceptions.DataLayerException;
import it.francesco.magazzino.controllers.dto.ResponseGetLocation;
import it.francesco.magazzino.controllers.dto.ResponseMissione;
import it.francesco.magazzino.controllers.dto.RichiestaInserisciBarcode;
import it.francesco.magazzino.controllers.dto.RichiestaMovimentazioneBarcode;
import it.francesco.magazzino.services.BarcodeService;
import it.francesco.magazzino.services.dto.LocationInfo;
import it.francesco.magazzino.services.dto.Missione;
import it.francesco.models.MissioniE;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

//rende questo pojo rende capace di rispondere alle chiamate HTTP rest
@RestController
public class BarcodeController {

    //questa classe dipende da un altra classe che la trovi nel contesto
    @Autowired
    private BarcodeService barcodeService;

    //se applicazione è in ascolto su una porta ricevi su /barcode con verbo post esegui questo metodo
    @PostMapping(value = "/barcode", consumes = "application/json")
    public ResponseEntity<String> aggiungiArticolo(@RequestBody RichiestaInserisciBarcode entity) {

        barcodeService.aggiungiBarcode(entity.getBarcode(), entity.getDescrizione());
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @PostMapping(value = "/movimenti/entrata", consumes = "application/json")
    public ResponseEntity<ResponseGetLocation> eseguiEntrata(@RequestBody RichiestaMovimentazioneBarcode entity) {

        try {

            LocationInfo locationInfo = barcodeService.trovaLocazione(entity.getBarcode());
            if (locationInfo == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            ResponseGetLocation rgl = new ResponseGetLocation(locationInfo.getIdLocazione(), locationInfo.getCorsia(),
                    locationInfo.getColonna(), locationInfo.getRipiano(), locationInfo.getIdMovimento(),
                    locationInfo.getCodiceInterno());
            return new ResponseEntity<>(rgl, HttpStatus.OK);

        }

        catch (DataLayerException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "movimenti/entrata/conferma")
    public ResponseEntity<String> confermaEntrata(@RequestParam("codiceInterno") int codiceInterno,
            @RequestParam("idLocazione") int idLocazione) {

        try {
            boolean risultato = barcodeService.confermaMovimentoE(codiceInterno, idLocazione);
            if (risultato == false) {
                return new ResponseEntity<>("Movimento non trovato", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>("Entrata confermata", HttpStatus.OK);
        } catch (DataLayerException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "movimenti/entrata", produces = "application/json")
    public ResponseEntity<ResponseMissione> recuperaMissioneEntrata() {
        try {
            ArrayList<Missione> missioniEntrata = barcodeService.getMissioni(true);

            ResponseMissione response = new ResponseMissione();

            response.setMissioni(new ArrayList<>());
            for (Missione missioneEntrata : missioniEntrata) {
                it.francesco.magazzino.controllers.dto.Missione missione = new it.francesco.magazzino.controllers.dto.Missione(
                        missioneEntrata.getBarcode(),
                        missioneEntrata.getDescrizione(),
                        missioneEntrata.getCodiceInterno(),
                        missioneEntrata.getIdLocazione(),
                        missioneEntrata.getCorsia(),
                        missioneEntrata.getColonna(),
                        missioneEntrata.getRipiano());
                response.getMissioni().add(missione);
            }
            response.setRecordTotali(missioniEntrata.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataLayerException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "movimenti/uscita")
    public ResponseEntity<String> eseguiUscita(@RequestParam("codiceInterno") int codiceInterno) {
        try {
            barcodeService.eseguiUscita(codiceInterno);
            return new ResponseEntity<>("uscita eseguita", HttpStatus.OK);

        } catch (DataLayerException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "movimenti/uscita/conferma")
    public ResponseEntity<String> confermaUscita(@RequestParam("codiceInterno") int codiceInterno,
            @RequestParam("idLocazione") int idLocazione) {
        try {
            boolean result = barcodeService.confermaMovimentoU(codiceInterno, idLocazione);
            if (result == false) {
                return new ResponseEntity<>("Movimento non trovato", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>("Uscita confermata", HttpStatus.OK);
        } catch (DataLayerException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping(value = "movimenti/uscita", produces = "application/json")
    public ResponseEntity<ResponseMissione> recuperaMissioneUscita() {
        try {
            ArrayList<Missione> missioniUscita = barcodeService.getMissioni(false);

            ResponseMissione response = new ResponseMissione();

            response.setMissioni(new ArrayList<>());
            for (Missione missioneUscita : missioniUscita) {
                it.francesco.magazzino.controllers.dto.Missione missione = new it.francesco.magazzino.controllers.dto.Missione(
                        missioneUscita.getBarcode(),
                        missioneUscita.getDescrizione(),
                        missioneUscita.getCodiceInterno(),
                        missioneUscita.getIdLocazione(),
                        missioneUscita.getCorsia(),
                        missioneUscita.getColonna(),
                        missioneUscita.getRipiano());
                response.getMissioni().add(missione);
            }
            response.setRecordTotali(missioniUscita.size());
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (DataLayerException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}