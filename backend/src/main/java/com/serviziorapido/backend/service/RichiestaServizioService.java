package com.serviziorapido.backend.service;

import com.serviziorapido.backend.entity.*;
import com.serviziorapido.backend.event.TipoEventoRichiesta;
import com.serviziorapido.backend.observer_pattern.Subject;
import com.serviziorapido.backend.repository.RichiestaServizioRepository;
import com.serviziorapido.backend.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RichiestaServizioService extends Subject {

    @Autowired
    private RichiestaServizioRepository richiestaRepo;

    @Autowired
    private UtenteRepository utenteRepo;

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

        Professionista prof = (Professionista) utenteRepo.findById(idProfessionista)
                .orElseThrow(() -> new RuntimeException("Professionista non trovato"));

        CategoriaRichiesta specializzazione = prof.getSpecializzazione();

        return richiestaRepo.findByStatoRichiestaAndCategoria(
                StatoRichiesta.APERTA,
                specializzazione,
                idProfessionista
        );    }

    public List<RichiestaServizio> getRichiesteDelCliente(Long idCliente) {
        return richiestaRepo.findByClientePubblicante_IdUtente(idCliente);
    }

    @Transactional
    public void annullaRichiesta(Long idRichiesta) {
        RichiestaServizio richiesta = richiestaRepo.findById(idRichiesta)
                .orElseThrow(() -> new RuntimeException("Richiesta non trovata con ID: " + idRichiesta));

        notifyObservers(richiesta, TipoEventoRichiesta.ANNULLATA);

        richiestaRepo.deleteById(idRichiesta);
    }

    public RichiestaServizio completaRichiesta(Long idRichiesta) {
        RichiestaServizio richiesta = richiestaRepo.findById(idRichiesta)
                .orElseThrow(() -> new RuntimeException("Richiesta non trovata"));

        if (richiesta.getStatoRichiesta() != StatoRichiesta.IN_LAVORAZIONE) {
            throw new RuntimeException("Impossibile completare: la richiesta non è in lavorazione.");
        }

        richiesta.setStatoRichiesta(StatoRichiesta.COMPLETATA);
        return richiestaRepo.save(richiesta);
    }
}
