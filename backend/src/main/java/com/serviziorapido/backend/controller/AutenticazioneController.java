package com.serviziorapido.backend.controller;

import com.serviziorapido.backend.dto.ClienteDTO;
import com.serviziorapido.backend.dto.ProfessionistaDTO;
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

    @PostMapping("/register/cliente")
    public ResponseEntity<?> registraCliente(@RequestBody Cliente cliente) {
        try {
            Utente salvato = authService.registraUtente(cliente);
            // Restituisci il DTO, non l'Entity con la password!
            return ResponseEntity.ok(convertiInClienteDTO((Cliente) salvato));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register/professionista")
    public ResponseEntity<?> registraProfessionista(@RequestBody Professionista professionista) {
        try {
            Utente salvato = authService.registraUtente(professionista);
            return ResponseEntity.ok(convertiInProfessionistaDTO((Professionista) salvato));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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