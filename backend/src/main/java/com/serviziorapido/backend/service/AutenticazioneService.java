package com.serviziorapido.backend.service;

import com.serviziorapido.backend.model.*;
import com.serviziorapido.backend.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AutenticazioneService {

    @Autowired
    private UtenteRepository utenteRepo;

    public Utente autenticaUtente(String email, String password) {
        Optional<Utente> utente = utenteRepo.findByEmail(email);

        System.out.println(utente.isPresent());
        System.out.println("Credenziali: " + email + " - " + password + " - " + utente.get().getPassword() + " - " + utente.get().getEmail());

        if (utente.isPresent() && utente.get().getPassword().equals(password)) {
            return utente.get();
        }
        return null; // Login fallito
    }

    public Utente registraUtente(Utente utente) {
        if (utenteRepo.existsByEmail(utente.getEmail())) {
            throw new RuntimeException("Email già registrata!");
        }
        return utenteRepo.save(utente);
    }
}