package com.serviziorapido.backend.service;

import com.serviziorapido.backend.entity.*;
import com.serviziorapido.backend.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
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
    private JavaMailSender mailSender;

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

        if (utente.getPassword() == null || !Pattern.matches(PASSWORD_PATTERN, utente.getPassword())) {
            throw new RuntimeException("La password deve avere almeno 8 caratteri, una maiuscola, una minuscola, un numero e un carattere speciale (@#$%^&+=!).");
        }

        if (utenteRepo.existsByEmail(utente.getEmail())) {
            throw new RuntimeException("Email già registrata!");
        }

        return true;
    }


    public void avviaRecuperoPassword(String email) {
        Optional<Utente> utenteOpt = utenteRepo.findByEmail(email);

        if (utenteOpt.isEmpty()) {
            System.out.println("Tentativo di recupero per email inesistente: " + email);
            return;
        }

        Utente utente = utenteOpt.get();

        String token = UUID.randomUUID().toString();
        utente.setResetToken(token);
        utente.setResetTokenScadenza(LocalDateTime.now().plusHours(1));

        utenteRepo.save(utente);

        SimpleMailMessage message = getSimpleMailMessage(email, token, utente);

        try {
            mailSender.send(message);
            System.out.println("Email di recupero inviata con successo a: " + email);
        } catch (Exception e) {
            System.err.println("Errore durante l'invio della mail: " + e.getMessage());
        }
    }

    private static SimpleMailMessage getSimpleMailMessage(String email, String token, Utente utente) {
        String linkReset = "http://localhost:4200/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ServizioRapido <webproject.unical@gmail.com>");
        message.setTo(email);
        message.setSubject("Reset Password - ServizioRapido");
        message.setText("Ciao " + utente.getNome() + ",\n\n" +
                "Hai richiesto il reset della password. Clicca sul link sottostante per procedere:\n\n" +
                linkReset + "\n\n" +
                "Il link scadrà tra 1 ora.\n" +
                "Se non sei stato tu, ignora questa email.");
        return message;
    }

    public boolean completaRecuperoPassword(String token, String nuovaPassword) {

        Optional<Utente> utenteOpt = utenteRepo.findByResetToken(token);

        if (utenteOpt.isEmpty()) return false;

        Utente utente = utenteOpt.get();

        if (utente.getResetTokenScadenza().isBefore(LocalDateTime.now())) {
            return false;
        }

        verificaInformazioni(utente);
        utente.setPassword(nuovaPassword);
        utente.setResetToken(null);
        utente.setResetTokenScadenza(null);

        utenteRepo.save(utente);
        return true;
    }
}