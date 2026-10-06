package it.francesco.magazzino.controllers;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public ResponseEntity<Resource> index() {
        Resource resource = new ClassPathResource("static/index.html");
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(resource);
    }

    @GetMapping("/inserisciArticoli.html")
    public ResponseEntity<Resource> inserisciArticoli() {
        Resource resource = new ClassPathResource("static/inserisciArticoli.html");
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(resource);
    }

    @GetMapping("/inserisciBarcode.html")
    public ResponseEntity<Resource> inserisciBarcode() {
        Resource resource = new ClassPathResource("static/inserisciBarcode.html");
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(resource);
    }

    @GetMapping("/confermaIngresso.html")
    public ResponseEntity<Resource> confermaIngresso() {
        Resource resource = new ClassPathResource("static/confermaIngresso.html");
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(resource);
    }

    @GetMapping("/confermaUscita.html")
    public ResponseEntity<Resource> confermaUscita() {
        Resource resource = new ClassPathResource("static/confermaUscita.html");
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(resource);
    }

    @GetMapping("/prelevaArticolo.html")
    public ResponseEntity<Resource> prelevaArticolo() {
        Resource resource = new ClassPathResource("static/prelevaArticolo.html");
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(resource);
    }
}