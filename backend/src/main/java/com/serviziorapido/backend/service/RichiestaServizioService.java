package com.serviziorapido.backend.service;

import com.serviziorapido.backend.model.*;
import com.serviziorapido.backend.repository.RichiestaServizioRepository;
import com.serviziorapido.backend.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RichiestaServizioService {

    @Autowired
    private RichiestaServizioRepository richiestaRepo;

    @Autowired
    private UtenteRepository utenteRepo;

    // 1. INIETTA IL NOTIFICA SERVICE
    @Autowired
    private NotificaService notificaService;

    public RichiestaServizio creaRichiesta(RichiestaServizio richiesta){

        Long idCliente = richiesta.getClientePubblicante().getIdUtente();

         Cliente clienteVero = (Cliente) utenteRepo.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente non trovato con ID: " + idCliente));

        richiesta.setClientePubblicante(clienteVero);

        richiesta.setStatoRichiesta(StatoRichiesta.APERTA);
        return richiestaRepo.save(richiesta);
    }

    public List<RichiestaServizio> getRichiesteCompatibili(Long idProfessionista) {

        // 1. Recupero il Professionista dal DB
        Professionista prof = (Professionista) utenteRepo.findById(idProfessionista)
                .orElseThrow(() -> new RuntimeException("Professionista non trovato"));

        // 2. Leggo la sua specializzazione (es. IDRAULICO)
        CategoriaRichiesta specializzazione = prof.getSpecializzazione();

        // 3. Cerco nel DB solo le richieste APERTE + IDRAULICO
        return richiestaRepo.findByStatoRichiestaAndCategoria(StatoRichiesta.APERTA, specializzazione);
    }

    public List<RichiestaServizio> getRichiesteDelCliente(Long idCliente) {
        return richiestaRepo.findByClientePubblicante_IdUtente(idCliente);
    }

    @Transactional //atomicità dell'operazione
    public void annullaRichiesta(Long idRichiesta) {
        RichiestaServizio richiesta = richiestaRepo.findById(idRichiesta)
                .orElseThrow(() -> new RuntimeException("Richiesta non trovata con ID: " + idRichiesta));
        if (richiesta.getProposteRicevute() != null && !richiesta.getProposteRicevute().isEmpty()) {
            for (PropostaServizio proposta : richiesta.getProposteRicevute()) {
                Long idProfessionista = proposta.getProfessionistaMittente().getIdUtente();
                String messaggio = "La richiesta '" + richiesta.getDettagli() +
                        "' per cui avevi inviato una proposta è stata annullata dal cliente.";
                notificaService.inviaNotifica(idProfessionista, messaggio);
            }
        }
        richiestaRepo.deleteById(idRichiesta);
    }

}
