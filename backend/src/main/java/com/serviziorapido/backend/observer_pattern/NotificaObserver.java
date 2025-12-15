package com.serviziorapido.backend.observer_pattern;

import com.serviziorapido.backend.entity.RichiestaServizio;
import com.serviziorapido.backend.event.TipoEventoRichiesta;
import com.serviziorapido.backend.observer_pattern.Observer;
import com.serviziorapido.backend.entity.PropostaServizio;
import com.serviziorapido.backend.event.TipoEventoProposta;
import com.serviziorapido.backend.service.NotificaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificaObserver implements Observer {

    @Autowired
    private NotificaService notificaService;

    @Override
    public void update(PropostaServizio proposta, TipoEventoProposta tipo) {
        String messaggio = "";
        Long idDestinatario = null;

        String nomeProf = proposta.getProfessionistaMittente().getNome();
        String cognomeProf = proposta.getProfessionistaMittente().getCognome();
        String titolo = proposta.getRichiestaRiferimento().getDettagli();

        switch (tipo) {
            case INVIATA:
                idDestinatario = proposta.getRichiestaRiferimento().getClientePubblicante().getIdUtente();
                messaggio = "Nuova proposta ricevuta per la Richiesta: " + titolo;
                break;

            case ACCETTATA:
                idDestinatario = proposta.getProfessionistaMittente().getIdUtente();
                messaggio = "La tua proposta per '" + titolo + "' è stata ACCETTATA!";
                break;

            case SCARTATA:
                idDestinatario = proposta.getProfessionistaMittente().getIdUtente();
                messaggio = "La tua proposta per '" + titolo + "' è stata scartata.";
                break;

            case RIFIUTATA:
                idDestinatario = proposta.getProfessionistaMittente().getIdUtente();
                messaggio = "La tua proposta per '" + titolo + "' è stata rifiutata dal cliente.";
                break;

            case MODIFICATA:
                idDestinatario = proposta.getRichiestaRiferimento().getClientePubblicante().getIdUtente();
                messaggio = "Proposta modificata da " + nomeProf +  " " + cognomeProf + " per: " + titolo;
                break;

            case ELIMINATA:
                idDestinatario = proposta.getRichiestaRiferimento().getClientePubblicante().getIdUtente();
                messaggio = "Proposta ritirata da " + nomeProf + " " + cognomeProf + " per: " + titolo;
                break;
        }

        if (idDestinatario != null && !messaggio.isEmpty()) {
            notificaService.inviaNotifica(idDestinatario, messaggio);
        }
    }

    public void update(RichiestaServizio richiesta, TipoEventoRichiesta tipo) {
        if (tipo == TipoEventoRichiesta.ANNULLATA) {
            // 1. Notifica all'utente che ha creato la richiesta (Cliente)
            Long idUtente = richiesta.getClientePubblicante().getIdUtente();
            String messaggioCliente = "Hai annullato con successo la richiesta: " + richiesta.getDettagli();
            notificaService.inviaNotifica(idUtente, messaggioCliente);

              if (richiesta.getProposteRicevute() != null && !richiesta.getProposteRicevute().isEmpty()) {

                for (PropostaServizio proposta : richiesta.getProposteRicevute()) {
                   Long idProfessionista = proposta.getProfessionistaMittente().getIdUtente();

                    String messaggioProf = "La richiesta '" + richiesta.getDettagli() +
                            "' per cui hai inviato una proposta è stata annullata dal cliente.";

                    notificaService.inviaNotifica(idProfessionista, messaggioProf);
                }
            }
        }
    }
}