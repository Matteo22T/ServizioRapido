package com.serviziorapido.backend.service;

import com.serviziorapido.backend.entity.*;
import com.serviziorapido.backend.event.TipoEventoProposta;
import com.serviziorapido.backend.observer_pattern.Observer;
import com.serviziorapido.backend.observer_pattern.Subject;
import com.serviziorapido.backend.repository.PropostaServizioRepository;
import com.serviziorapido.backend.repository.RichiestaServizioRepository;
import com.serviziorapido.backend.repository.UtenteRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PropostaServizioService extends Subject {

    @Autowired
    private PropostaServizioRepository propostaRepo;
    @Autowired
    private RichiestaServizioRepository richiestaRepo;

    @Autowired
    private UtenteRepository utenteRepo;

    @Autowired
    private List<Observer> listaOsservatoriDisponibili;

    @PostConstruct
    public void init() {
          for (Observer obs : listaOsservatoriDisponibili) {
            this.attach(obs);
        }
    }



    public PropostaServizio pubblicaProposta(PropostaServizio proposta) {
        RichiestaServizio richiesta = richiestaRepo.findById(proposta.getRichiestaRiferimento().getIdRichiesta())
                .orElseThrow(() -> new RuntimeException("Richiesta non trovata"));




        proposta.setStatoProposta(StatoProposta.INVIATA);
        PropostaServizio nuovaProposta = propostaRepo.save(proposta);

        proposta.setRichiestaRiferimento(richiesta);

        notifyObservers(nuovaProposta, TipoEventoProposta.INVIATA);

        return nuovaProposta;
    }

    public PropostaServizio modificaProposta(Long idProposta, PropostaServizio nuovaDatiProposta) {
        PropostaServizio proposta = propostaRepo.findById(idProposta)
                .orElseThrow(() -> new RuntimeException("Proposta non trovata"));

        if (proposta.getStatoProposta() != StatoProposta.INVIATA) {
            throw new RuntimeException("Impossibile modificare: la proposta non è in stato INVIATA.");
        }

        proposta.setDettagli(nuovaDatiProposta.getDettagli());
        proposta.setPrezzo(nuovaDatiProposta.getPrezzo());

        PropostaServizio propostaAggiornata = propostaRepo.save(proposta);

         notifyObservers(propostaAggiornata, TipoEventoProposta.MODIFICATA);

        return propostaAggiornata;
    }

    @Transactional
    public void eliminaProposta(Long idProposta) {
        PropostaServizio proposta = propostaRepo.findById(idProposta)
                .orElseThrow(() -> new RuntimeException("Proposta non trovata"));

        // NOTA: Qui notifico PRIMA di cancellare, altrimenti l'observer non ha i dati
        // per costruire il messaggio (es. il nome del professionista)
        notifyObservers(proposta, TipoEventoProposta.ELIMINATA);

        propostaRepo.deleteById(idProposta);
    }

    public List<PropostaServizio> getProposteProfessionista(Long idProfessionista) {
        return propostaRepo.findByProfessionistaMittente_IdUtente(idProfessionista, StatoRichiesta.COMPLETATA);     }

    public List<PropostaServizio> getPropostePerRichiesta(Long idRichiesta){
        return propostaRepo.findByRichiestaRiferimento_IdRichiesta(idRichiesta);
    }

    @Transactional
    public PropostaServizio accettaProposta(Long idProposta, Long idRichiesta) {
        PropostaServizio propostaScelta = propostaRepo.findById(idProposta)
                .orElseThrow(() -> new RuntimeException("Proposta non trovata"));
        RichiestaServizio richiesta = richiestaRepo.findById(idRichiesta)
                .orElseThrow(() -> new RuntimeException("Richiesta non trovata"));

        richiesta.setStatoRichiesta(StatoRichiesta.IN_LAVORAZIONE);
        propostaScelta.setStatoProposta(StatoProposta.ACCETTATA);
        richiesta.setPropostaAccettata(propostaScelta);

        // Gestione proposte scartate
        List<PropostaServizio> tutteLeProposte = propostaRepo.findByRichiestaRiferimento_IdRichiesta(idRichiesta);
        for (PropostaServizio p : tutteLeProposte) {
            if (!p.getIdProposta().equals(idProposta)) {
                p.setStatoProposta(StatoProposta.RIFIUTATA);
                propostaRepo.save(p);
                // Notifica SCARTATA
                notifyObservers(p, TipoEventoProposta.SCARTATA);
            }
        }

        richiestaRepo.save(richiesta);
        PropostaServizio salvata = propostaRepo.save(propostaScelta);

        // Notifica ACCETTATA
        notifyObservers(salvata, TipoEventoProposta.ACCETTATA);

        return salvata;
    }

    public PropostaServizio rifiutaProposta(Long idProposta) {
        // 1. Recupero la proposta
        PropostaServizio proposta = propostaRepo.findById(idProposta)
                .orElseThrow(() -> new RuntimeException("Proposta non trovata"));

        // 2. Cambio lo stato
        proposta.setStatoProposta(StatoProposta.RIFIUTATA);

        // 3. Salvo su Database
        PropostaServizio propostaRifiutata = propostaRepo.save(proposta);

        // 4. NOTIFY: Avviso gli observer (il professionista saprà di essere stato rifiutato)
        notifyObservers(propostaRifiutata, TipoEventoProposta.RIFIUTATA);

        return propostaRifiutata;
    }
}
