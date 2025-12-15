package com.serviziorapido.backend.controller;

import com.serviziorapido.backend.dto.ClienteDTO;
import com.serviziorapido.backend.dto.ProfessionistaDTO;
import com.serviziorapido.backend.dto.RegisterDTO;
import com.serviziorapido.backend.entity.*;
import com.serviziorapido.backend.factory.UtenteFactory;
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

    @Autowired
    private Map<String, UtenteFactory> factoryMap;

    @PostMapping("/login")
    public ResponseEntity<?> autenticaUtente(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");

        Utente utente = authService.autenticaUtente(email, password);

        if (utente == null) {
            return ResponseEntity.status(401).body("Credenziali non valide");
        }

        // Conversione Manuale in DTO
        if (utente instanceof Cliente) {
            return ResponseEntity.ok(convertiInClienteDTO((Cliente) utente));
        } else if (utente instanceof Professionista) {
            return ResponseEntity.ok(convertiInProfessionistaDTO((Professionista) utente));
        }

        return ResponseEntity.status(500).body("Tipo utente sconosciuto");
    }

    @PostMapping("/register")
    public ResponseEntity<?> registraCliente(@RequestBody RegisterDTO registerDTO) {
        try {
            UtenteFactory factory = factoryMap.get(registerDTO.getTipoProfilo());
            if (factory == null) {
                return ResponseEntity.badRequest().body("Tipo profilo non supportato: " + registerDTO.getTipoProfilo());
            }

            Utente nuovoUtente = factory.creaUtente(registerDTO);

            Utente salvato = authService.registraUtente(nuovoUtente);

            return ResponseEntity.ok("Registrazione avvenuta con successo per: " + salvato.getEmail());        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }




    @PostMapping("/login/recupero-password")
    public ResponseEntity<?> richiediReset(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        authService.avviaRecuperoPassword(email);
        // Rispondiamo sempre OK per sicurezza (privacy), anche se la mail non esiste
        return ResponseEntity.ok("Se l'email esiste, riceverai le istruzioni.");
    }

    @PostMapping("/login/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        String nuovaPassword = payload.get("password");

        boolean successo = authService.completaRecuperoPassword(token, nuovaPassword);

        if (successo) {
            return ResponseEntity.ok("Password aggiornata con successo!");
        } else {
            return ResponseEntity.badRequest().body("Token non valido o scaduto.");
        }
    }




    private ClienteDTO convertiInClienteDTO(Cliente c) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(c.getIdUtente());
        dto.setNome(c.getNome());
        dto.setCognome(c.getCognome());
        dto.setEmail(c.getEmail());
        dto.setTelefono(c.getTelefono());
        dto.setRuolo("CLIENTE"); // Campo utile per il frontend!
        dto.setIndirizzo(c.getIndirizzo());
        return dto;
    }

    private ProfessionistaDTO convertiInProfessionistaDTO(Professionista p) {
        ProfessionistaDTO dto = new ProfessionistaDTO();
        dto.setId(p.getIdUtente());
        dto.setNome(p.getNome());
        dto.setCognome(p.getCognome());
        dto.setEmail(p.getEmail());
        dto.setTelefono(p.getTelefono());
        dto.setRuolo("PROFESSIONISTA");
        dto.setBiografia(p.getBiografia());
        dto.setSpecializzazione(p.getSpecializzazione());
        return dto;
    }
}