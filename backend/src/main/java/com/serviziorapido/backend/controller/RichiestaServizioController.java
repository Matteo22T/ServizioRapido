package com.serviziorapido.backend.controller;

import com.serviziorapido.backend.model.RichiestaServizio;
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

    @GetMapping("/aperte")
    public ResponseEntity<List<RichiestaServizio>> getAperte (){
        return ResponseEntity.ok(richiestaService.getRichiesteAperte());
    }

    @GetMapping("/mie/{idCliente}")
    public ResponseEntity<List<RichiestaServizio>> getMieRichieste (@PathVariable Long idCliente){
        return ResponseEntity.ok(richiestaService.getRichiesteDelCliente(idCliente));
    }
}
