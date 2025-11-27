package com.serviziorapido.backend.service;

import com.serviziorapido.backend.model.PropostaServizio;
import com.serviziorapido.backend.model.RichiestaServizio;
import com.serviziorapido.backend.model.StatoProposta;
import com.serviziorapido.backend.model.StatoRichiesta;
import com.serviziorapido.backend.repository.PropostaServizioRepository;
import com.serviziorapido.backend.repository.RichiestaServizioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.annotation.JsonAppend;

import java.util.List;

@Service
public class PropostaServizioService {

    @Autowired
    private PropostaServizioRepository propostaRepo;
    @Autowired
    private RichiestaServizioRepository richiestaRepo;

    public PropostaServizio pubblicaProposta(PropostaServizio proposta){
        RichiestaServizio richiestaReale= richiestaRepo.findById(proposta.getRichiestaRiferimento().getIdRichiesta()). //funziona a pipeline
                orElseThrow(() -> new RuntimeException("Richiesta non trovata"));

        if (richiestaReale.getStatoRichiesta() != StatoRichiesta.APERTA) { //LANCIO ECCEZIONE SE LO STATO DELLA RICHIESTA NON E' APERTA
            throw new RuntimeException("Non puoi inviare una proposta: la richiesta non è APERTA.");
        }
        proposta.setStatoProposta(StatoProposta.INVIATA); // altrimenti setto lo stato della proposta ad inviata
        return propostaRepo.save(proposta);
    }
    public PropostaServizio modificaProposta(Long idProposta,PropostaServizio nuovaDatiProposta){
        PropostaServizio proposta = propostaRepo.findById(idProposta).get();
        if (proposta.getStatoProposta() != StatoProposta.INVIATA) {
            throw new RuntimeException("Proposta non modificabile");
        }
        proposta.setDettagli(nuovaDatiProposta.getDettagli());
        proposta.setPrezzo(nuovaDatiProposta.getPrezzo());
        return propostaRepo.save(proposta);
    }

    public void eliminaProposta(Long idProposta) {
        propostaRepo.deleteById(idProposta);
    }

    public List<PropostaServizio> getProposteProfessionista(Long idProfessionista) {
        return propostaRepo.findByProfessionistaMittente_IdUtente(idProfessionista);
     }

    public List<PropostaServizio> getPropostePerRichiesta(Long idRichiesta){
        return propostaRepo.findByRichiestaRiferimento_IdRichiesta(idRichiesta);
    }

    public PropostaServizio accettaProposta(Long idProposta, Long idRichiesta){
        PropostaServizio proposta = propostaRepo.findById(idProposta).get();
        RichiestaServizio richiesta = richiestaRepo.findById(idRichiesta).get();
        richiesta.setStatoRichiesta(StatoRichiesta.IN_LAVORAZIONE);
        proposta.setStatoProposta(StatoProposta.ACCETTATA);
        richiesta.setPropostaAccettata(proposta);
        richiestaRepo.save(richiesta);
        return propostaRepo.save(proposta);
    }

    public PropostaServizio rifiutaProposta(Long idProposta){
        PropostaServizio proposta = propostaRepo.findById(idProposta).get();
        proposta.setStatoProposta(StatoProposta.RIFIUTATA);
        return propostaRepo.save(proposta);
    }
}
