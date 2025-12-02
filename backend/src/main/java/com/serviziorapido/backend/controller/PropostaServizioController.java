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

    @PutMapping("/modifica/{id}")
    public ResponseEntity<?> modificaProposta(@PathVariable Long id,@RequestBody PropostaServizio proposta){
        try {
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

    @GetMapping("professionista/{idProfessionista}")
    public ResponseEntity<List<PropostaServizio>> vediProposte(@PathVariable Long idProfessionista){
        return ResponseEntity.ok(propostaServizioService.getProposteProfessionista(idProfessionista));
    }

    @GetMapping("/richiesta/{idRichiesta}")
    public ResponseEntity<List<PropostaServizio>> vediProposteDiUnaRichiesta(@PathVariable Long idRichiesta){
        return ResponseEntity.ok(propostaServizioService.getPropostePerRichiesta(idRichiesta));
    }

    @PutMapping("/accetta/{idProposta}/{idRichiesta}")
    public  ResponseEntity<?> accettaProposta(@PathVariable Long idProposta,@PathVariable Long idRichiesta)
    {
        try {
            return ResponseEntity.ok(propostaServizioService.accettaProposta(idProposta,idRichiesta));
            } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/rifiuta/{idProposta}")
    public ResponseEntity<?> rifiutaProposta(@PathVariable Long idProposta){
        try {
            return ResponseEntity.ok(propostaServizioService.rifiutaProposta(idProposta));
            } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    }

