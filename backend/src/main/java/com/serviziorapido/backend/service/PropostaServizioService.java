package com.serviziorapido.backend.service;

import com.serviziorapido.backend.model.PropostaServizio;
import com.serviziorapido.backend.model.RichiestaServizio;
import com.serviziorapido.backend.model.StatoProposta;
import com.serviziorapido.backend.model.StatoRichiesta;
import com.serviziorapido.backend.repository.PropostaServizioRepository;
import com.serviziorapido.backend.repository.RichiestaServizioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.annotation.JsonAppend;

import java.util.List;

@Service
public class PropostaServizioService {

    @Autowired
    private PropostaServizioRepository propostaRepo;
    @Autowired
    private RichiestaServizioRepository richiestaRepo;
    @Autowired
    private NotificaService notificaService;



    public PropostaServizio pubblicaProposta(PropostaServizio proposta){
        Long idRichiesta = proposta.getRichiestaRiferimento().getIdRichiesta();
        Long idProfessionista = proposta.getProfessionistaMittente().getIdUtente();

        // 2. CONTROLLO DUPLICATI: Chiami il metodo che abbiamo creato nel Repository
        List<PropostaServizio> propostePrecedenti = propostaRepo.findByRichiestaRiferimento_IdRichiestaAndProfessionistaMittente_IdUtente(idRichiesta, idProfessionista);
        for (PropostaServizio p : propostePrecedenti) {
            if (p.getStatoProposta() == StatoProposta.INVIATA || p.getStatoProposta() == StatoProposta.ACCETTATA) {
                // Se ne trovi anche solo una attiva, blocchi tutto
                throw new RuntimeException("HAI_GIA_PROPOSTO");
            }
        }
        RichiestaServizio richiestaReale= richiestaRepo.findById(proposta.getRichiestaRiferimento().getIdRichiesta()). //funziona a pipeline
                orElseThrow(() -> new RuntimeException("Richiesta non trovata"));

        if (richiestaReale.getStatoRichiesta() != StatoRichiesta.APERTA) { //LANCIO ECCEZIONE SE LO STATO DELLA RICHIESTA NON E' APERTA
            throw new RuntimeException("Non puoi inviare una proposta: la richiesta non è APERTA.");
        }
        proposta.setStatoProposta(StatoProposta.INVIATA); // altrimenti setto lo stato della proposta ad inviata
        PropostaServizio nuovaProposta = propostaRepo.save(proposta);
        String nomeProf = proposta.getProfessionistaMittente().getNome();
        String cognomeProf = proposta.getProfessionistaMittente().getCognome();
        Long idCliente = proposta.getRichiestaRiferimento().getClientePubblicante().getIdUtente();
        String messaggio = "Il professionista " + nomeProf + " " + cognomeProf +
                " ha inviato una proposta per la tua richiesta: " +
                richiestaReale.getDettagli();
        notificaService.inviaNotifica(idCliente, messaggio);
        return nuovaProposta;
    }


    public PropostaServizio modificaProposta(Long idProposta,PropostaServizio nuovaDatiProposta){
        PropostaServizio proposta = propostaRepo.findById(idProposta).get();
        if (proposta.getStatoProposta() != StatoProposta.INVIATA) {
            throw new RuntimeException("Proposta non modificabile");
        }
        proposta.setDettagli(nuovaDatiProposta.getDettagli());
        proposta.setPrezzo(nuovaDatiProposta.getPrezzo());
        PropostaServizio aggiornata = propostaRepo.save(proposta);
        String nomeProf = proposta.getProfessionistaMittente().getNome();
        String cognomeProf = proposta.getProfessionistaMittente().getCognome();
        Long idCliente = proposta.getRichiestaRiferimento().getClientePubblicante().getIdUtente();
        String messaggio = "La proposta per la richiesta '" + proposta.getRichiestaRiferimento().getDettagli() + "' è stata modificata "+ "dal professionista "+ nomeProf + " " + cognomeProf;
        notificaService.inviaNotifica(idCliente, messaggio);
        return aggiornata;
    }

    @Transactional
    public void eliminaProposta(Long idProposta) {
        // Recuperiamo la proposta PRIMA di cancellarla per sapere chi notificare
        PropostaServizio proposta = propostaRepo.findById(idProposta)
                .orElseThrow(() -> new RuntimeException("Proposta non trovata"));
        Long idCliente = proposta.getRichiestaRiferimento().getClientePubblicante().getIdUtente();
        String nomeProf = proposta.getProfessionistaMittente().getNome();
        String cognomeProf = proposta.getProfessionistaMittente().getCognome();
        String messaggio = "Il professionista " + nomeProf+ " "+ cognomeProf+ "ha ritirato la proposta per la richiesta: " + proposta.getRichiestaRiferimento().getDettagli();
        notificaService.inviaNotifica(idCliente, messaggio);

        propostaRepo.deleteById(idProposta);
    }

    public List<PropostaServizio> getProposteProfessionista(Long idProfessionista) {
        return propostaRepo.findByProfessionistaMittente_IdUtente(idProfessionista);
     }

    public List<PropostaServizio> getPropostePerRichiesta(Long idRichiesta){
        return propostaRepo.findByRichiestaRiferimento_IdRichiesta(idRichiesta);
    }

    @Transactional
    public PropostaServizio accettaProposta(Long idProposta, Long idRichiesta){
        PropostaServizio propostaScelta = propostaRepo.findById(idProposta).get();
        RichiestaServizio richiesta = richiestaRepo.findById(idRichiesta).get();
        richiesta.setStatoRichiesta(StatoRichiesta.IN_LAVORAZIONE);
        propostaScelta.setStatoProposta(StatoProposta.ACCETTATA);
        richiesta.setPropostaAccettata(propostaScelta);

        // 2. Rifiuto automatico delle altre proposte
        List<PropostaServizio> tutteLeProposte = propostaRepo.findByRichiestaRiferimento_IdRichiesta(idRichiesta);
        for (PropostaServizio p : tutteLeProposte) {
            if (!p.getIdProposta().equals(idProposta)) {
                p.setStatoProposta(StatoProposta.RIFIUTATA);
                propostaRepo.save(p);
                //Notifica anche agli scartati.
                Long idProfScartato = p.getProfessionistaMittente().getIdUtente();
                String messaggioScartato = "La tua proposta per la richiesta '" + richiesta.getDettagli() +
                        "' non è stata accettata. Il cliente ha assegnato il lavoro a un altro professionista.";

                notificaService.inviaNotifica(idProfScartato, messaggioScartato);
            }
        }
        richiestaRepo.save(richiesta);
        PropostaServizio salvata = propostaRepo.save(propostaScelta);

        Long idProf = propostaScelta.getProfessionistaMittente().getIdUtente();
        String messaggio = "Congratulazioni! La tua proposta per '" + richiesta.getDettagli() + "' è stata ACCETTATA.";
        notificaService.inviaNotifica(idProf, messaggio);
        return salvata;
    }

    public PropostaServizio rifiutaProposta(Long idProposta){
        PropostaServizio proposta = propostaRepo.findById(idProposta).get();
        proposta.setStatoProposta(StatoProposta.RIFIUTATA);
        PropostaServizio salvata = propostaRepo.save(proposta);

        Long idProf = proposta.getProfessionistaMittente().getIdUtente();
        String messaggio = "La tua proposta per la richiesta '" + proposta.getRichiestaRiferimento().getDettagli() + "' è stata rifiutata.";
        notificaService.inviaNotifica(idProf, messaggio);

        return salvata;
    }
}
