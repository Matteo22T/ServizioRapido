package com.serviziorapido.backend.controller;

import com.serviziorapido.backend.model.*;
import com.serviziorapido.backend.service.AutenticazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AutenticazioneController {

    @Autowired
    private AutenticazioneService authService;

    @PostMapping("/login")
    public ResponseEntity<?> autenticaUtente(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");

        Utente utente = authService.autenticaUtente(email, password);

        if (utente != null) {
            return ResponseEntity.ok(utente);
        } else {
            return ResponseEntity.status(401).body("Credenziali non valide");
        }
    }

    @PostMapping("/register/cliente")
    public ResponseEntity<?> registraCliente(@RequestBody Cliente cliente) {
        try {
            return ResponseEntity.ok(authService.registraUtente(cliente));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register/professionista")
    public ResponseEntity<?> registraProfessionista(@RequestBody Professionista professionista) {
        try {
            return ResponseEntity.ok(authService.registraUtente(professionista));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}