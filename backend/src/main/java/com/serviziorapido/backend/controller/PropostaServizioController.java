package com.serviziorapido.backend.controller;

import com.serviziorapido.backend.model.PropostaServizio;
import com.serviziorapido.backend.service.PropostaServizioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/proposte")
@CrossOrigin(origins = "http://localhost:4200")
public class PropostaServizioController {
    @Autowired
    private PropostaServizioService propostaServizioService;

    @PostMapping
    public ResponseEntity<PropostaServizio> pubblicaProposta(@RequestBody PropostaServizio proposta){
        return ResponseEntity.ok(propostaServizioService.pubblicaProposta(proposta));
    }

    @PutMapping("/modificate/{id}")
    public ResponseEntity<?> modificaProposta(@PathVariable Long id,@RequestBody PropostaServizio proposta){
        try { //metto il ? (jolly) per non avere per forza un tipo di ritorno: utile per eccezioni
            return ResponseEntity.ok(propostaServizioService.modificaProposta(id, proposta));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/eliminate/{id}")
    public ResponseEntity<?> eliminaProposta(@PathVariable Long id){
        try {
            propostaServizioService.eliminaProposta(id);
            return ResponseEntity.ok("Proposta eliminata con successo");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/proposte/{idProfessionista}")
    public ResponseEntity<List<PropostaServizio>> vediProposte(@PathVariable Long idRichiesta){
        return ResponseEntity.ok(propostaServizioService.getProposteProfessionista(idRichiesta));
    }

    @GetMapping("/richiesta/{idRichiesta}")
    public ResponseEntity<List<PropostaServizio>> vediProposteDiUnaRichiesta(@PathVariable Long idRichiesta){
        return ResponseEntity.ok(propostaServizioService.getPropostePerRichiesta(idRichiesta));
    }

    }

