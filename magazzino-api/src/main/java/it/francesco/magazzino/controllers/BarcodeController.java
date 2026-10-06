package it.francesco.magazzino.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.francesco.exceptions.DataLayerException;
import it.francesco.magazzino.controllers.dto.ResponseGetLocation;
import it.francesco.magazzino.controllers.dto.ResponseMissione;
import it.francesco.magazzino.controllers.dto.RichiestaInserisciBarcode;
import it.francesco.magazzino.controllers.dto.RichiestaMovimentazioneBarcode;
import it.francesco.magazzino.services.BarcodeService;
import it.francesco.magazzino.services.BarcodeService.Esito;
import it.francesco.magazzino.services.BarcodeService.MagazzinoPienoException;
import it.francesco.magazzino.services.dto.LocationInfo;
import it.francesco.magazzino.services.dto.Missione;

@RestController
public class BarcodeController {

    private static final String ERRORE_INTERNO = "Errore interno del server, riprova più tardi";
    private static final int MAX_BARCODE = 45;
    private static final int MAX_DESCRIZIONE = 100;

    @Autowired
    private BarcodeService barcodeService;

    @PostMapping(value = "/barcode", consumes = "application/json")
    public ResponseEntity<String> aggiungiArticolo(@RequestBody RichiestaInserisciBarcode richiesta) {
        String barcode = richiesta.getBarcode() == null ? "" : richiesta.getBarcode().trim();
        String descrizione = richiesta.getDescrizione() == null ? "" : richiesta.getDescrizione().trim();

        if (barcode.isEmpty() || descrizione.isEmpty()) {
            return ResponseEntity.badRequest().body("Barcode e descrizione sono obbligatori");
        }
        if (barcode.length() > MAX_BARCODE || descrizione.length() > MAX_DESCRIZIONE) {
            return ResponseEntity.badRequest().body("Il barcode può avere al massimo " + MAX_BARCODE
                    + " caratteri e la descrizione " + MAX_DESCRIZIONE);
        }

        try {
            Esito esito = barcodeService.aggiungiBarcode(barcode, descrizione);
            if (esito != Esito.OK) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(esito.getMessaggio());
            }
            return ResponseEntity.status(HttpStatus.CREATED).body("Resistenza salvata");
        } catch (DataLayerException e) {
            return erroreInterno();
        }
    }

    @PostMapping(value = "/movimenti/entrata", consumes = "application/json")
    public ResponseEntity<?> eseguiEntrata(@RequestBody RichiestaMovimentazioneBarcode richiesta) {
        try {
            LocationInfo info = barcodeService.trovaLocazione(richiesta.getBarcode());
            if (info == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nessun articolo registrato con questo barcode");
            }
            return ResponseEntity.ok(new ResponseGetLocation(info.getIdLocazione(), info.getCorsia(),
                    info.getColonna(), info.getRipiano(), info.getIdMovimento(), info.getCodiceInterno()));
        } catch (MagazzinoPienoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (DataLayerException e) {
            return erroreInterno();
        }
    }

    @PutMapping("/movimenti/entrata/conferma")
    public ResponseEntity<String> confermaEntrata(@RequestParam("codiceInterno") int codiceInterno,
            @RequestParam("idLocazione") int idLocazione) {
        try {
            return risposta(barcodeService.confermaMovimentoE(codiceInterno, idLocazione), "Entrata confermata");
        } catch (DataLayerException e) {
            return erroreInterno();
        }
    }

    @GetMapping(value = "/movimenti/entrata", produces = "application/json")
    public ResponseEntity<ResponseMissione> recuperaMissioneEntrata() {
        return recuperaMissioni(true);
    }

    @PutMapping("/movimenti/uscita")
    public ResponseEntity<String> eseguiUscita(@RequestParam("codiceInterno") int codiceInterno) {
        try {
            return risposta(barcodeService.eseguiUscita(codiceInterno), "Prelievo eseguito");
        } catch (DataLayerException e) {
            return erroreInterno();
        }
    }

    @PutMapping("/movimenti/uscita/conferma")
    public ResponseEntity<String> confermaUscita(@RequestParam("codiceInterno") int codiceInterno,
            @RequestParam("idLocazione") int idLocazione) {
        try {
            return risposta(barcodeService.confermaMovimentoU(codiceInterno, idLocazione), "Uscita confermata");
        } catch (DataLayerException e) {
            return erroreInterno();
        }
    }

    @GetMapping(value = "/movimenti/uscita", produces = "application/json")
    public ResponseEntity<ResponseMissione> recuperaMissioneUscita() {
        return recuperaMissioni(false);
    }

    private ResponseEntity<ResponseMissione> recuperaMissioni(boolean entrata) {
        try {
            ArrayList<Missione> missioni = barcodeService.getMissioni(entrata);
            ResponseMissione risposta = new ResponseMissione();
            risposta.setMissioni(new ArrayList<>());
            for (Missione m : missioni) {
                risposta.getMissioni().add(new it.francesco.magazzino.controllers.dto.Missione(
                        m.getBarcode(), m.getDescrizione(), m.getCodiceInterno(), m.getIdLocazione(),
                        m.getCorsia(), m.getColonna(), m.getRipiano()));
            }
            risposta.setRecordTotali(missioni.size());
            return ResponseEntity.ok(risposta);
        } catch (DataLayerException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /** Traduce l'esito del service nella risposta HTTP corrispondente. */
    private ResponseEntity<String> risposta(Esito esito, String messaggioOk) {
        return switch (esito) {
            case OK -> ResponseEntity.ok(messaggioOk);
            case MOVIMENTO_NON_TROVATO -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(esito.getMessaggio());
            case LOCAZIONE_ERRATA -> ResponseEntity.badRequest().body(esito.getMessaggio());
            default -> ResponseEntity.status(HttpStatus.CONFLICT).body(esito.getMessaggio());
        };
    }

    private ResponseEntity<String> erroreInterno() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ERRORE_INTERNO);
    }
}