package com.serviziorapido.backend.service;

import com.serviziorapido.backend.model.*;
import com.serviziorapido.backend.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.UUID;
import java.time.LocalDateTime;

@Service
public class AutenticazioneService {

    private static final String EMAIL_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";


    @Autowired
    private UtenteRepository utenteRepo;

    public Utente autenticaUtente(String email, String password) {
        Optional<Utente> utente = utenteRepo.findByEmail(email);
        if (utente.isPresent() && utente.get().getPassword().equals(password) ) {
            return utente.get();
        }
        return null; // Login fallito
    }

    public Utente registraUtente(Utente utente) {
        verificaInformazioni(utente);
        return utenteRepo.save(utente);
    }


    public boolean verificaInformazioni(Utente utente){
        if (utente.getEmail() == null || !Pattern.matches(EMAIL_PATTERN, utente.getEmail())) {
            throw new RuntimeException("Formato email non valido!");
        }

        // 2. Controllo requisiti Password
        if (utente.getPassword() == null || !Pattern.matches(PASSWORD_PATTERN, utente.getPassword())) {
            throw new RuntimeException("La password deve avere almeno 8 caratteri, una maiuscola, una minuscola, un numero e un carattere speciale (@#$%^&+=!).");
        }

        // 3. Controllo duplicati
        if (utenteRepo.existsByEmail(utente.getEmail())) {
            throw new RuntimeException("Email già registrata!");
        }

        return true;
    }


    public void avviaRecuperoPassword(String email) {
        Optional<Utente> utenteOpt = utenteRepo.findByEmail(email);

        if (utenteOpt.isEmpty()) {
            // Per sicurezza non diciamo se l'email non esiste, ma qui logghiamo
            System.out.println("Tentativo di recupero per email inesistente: " + email);
            return;
        }

        Utente utente = utenteOpt.get();

        // Generiamo un token univoco
        String token = UUID.randomUUID().toString();
        utente.setResetToken(token);
        // Il token vale 1 ora
        utente.setResetTokenScadenza(LocalDateTime.now().plusHours(1));

        utenteRepo.save(utente);

        // SIMULAZIONE INVIO EMAIL (Guarda la console di IntelliJ!)
        System.out.println("------------------------------------------------");
        System.out.println("SIMULAZIONE EMAIL A: " + email);
        System.out.println("Ciao, clicca qui per resettare la password:");
        System.out.println("http://localhost:4200/reset-password?token=" + token);
        System.out.println("------------------------------------------------");
    }

    // STEP 2: L'utente invia la nuova password col token
    public boolean completaRecuperoPassword(String token, String nuovaPassword) {
        // Cerca l'utente col token (dovresti aggiungere findByResetToken nel repo, o farlo a mano)
        // Per semplicità qui facciamo una scansione veloce o aggiungi il metodo nel Repo
        // Opzione migliore: Aggiungi Optional<Utente> findByResetToken(String token) nel Repository

        // Supponiamo tu abbia aggiunto il metodo nel repository (vedi sotto)
        Optional<Utente> utenteOpt = utenteRepo.findByResetToken(token);

        if (utenteOpt.isEmpty()) return false;

        Utente utente = utenteOpt.get();

        // Controllo scadenza
        if (utente.getResetTokenScadenza().isBefore(LocalDateTime.now())) {
            return false; // Token scaduto
        }

        // Aggiorno password e pulisco il token
        utente.setPassword(nuovaPassword);
        utente.setResetToken(null);
        utente.setResetTokenScadenza(null);

        utenteRepo.save(utente);
        return true;
    }
}