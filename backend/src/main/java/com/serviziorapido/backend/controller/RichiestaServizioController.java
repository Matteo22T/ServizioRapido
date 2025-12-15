package com.serviziorapido.backend.controller;

import com.serviziorapido.backend.entity.RichiestaServizio;
import com.serviziorapido.backend.service.RichiestaServizioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/richieste")
@CrossOrigin(origins = "http://localhost:4200")
public class RichiestaServizioController {

    @Autowired
    private RichiestaServizioService richiestaService;

    @PostMapping
    public ResponseEntity<RichiestaServizio> pubblica (@RequestBody RichiestaServizio richiesta){
        return ResponseEntity.ok(richiestaService.creaRichiesta(richiesta));
    }

    @GetMapping("/compatibili/{idProfessionista}")
    public ResponseEntity<List<RichiestaServizio>> getCompatibili(@PathVariable Long idProfessionista) {
        return ResponseEntity.ok(richiestaService.getRichiesteCompatibili(idProfessionista));
    }

    @GetMapping("/mie/{idCliente}")
    public ResponseEntity<List<RichiestaServizio>> getMieRichieste (@PathVariable Long idCliente){
        System.out.println("invio richieste");
        return ResponseEntity.ok(richiestaService.getRichiesteDelCliente(idCliente));
    }

    @DeleteMapping("/eliminate/{id}")
    public ResponseEntity<?> annullaRichiesta(@PathVariable Long id){
        try {
            richiestaService.annullaRichiesta(id);
            return ResponseEntity.ok("Richiesta eliminata con successo");
        }
        catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/completa/{id}")
    public ResponseEntity<?> completaRichiesta(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(richiestaService.completaRichiesta(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
