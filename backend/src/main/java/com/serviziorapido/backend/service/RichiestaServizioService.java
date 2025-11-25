package com.serviziorapido.backend.service;

import com.serviziorapido.backend.model.RichiestaServizio;
import com.serviziorapido.backend.model.StatoRichiesta;
import com.serviziorapido.backend.repository.RichiestaServizioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RichiestaServizioService {

    @Autowired
    private RichiestaServizioRepository richiestaRepo;

    public RichiestaServizio creaRichiesta(RichiestaServizio richiesta){

        richiesta.setStatoRichiesta(StatoRichiesta.APERTA);
        return richiestaRepo.save(richiesta);
    }

    public List<RichiestaServizio> getRichiesteAperte() {
        return richiestaRepo.findByStatoRichiesta(StatoRichiesta.APERTA);
    }

    public List<RichiestaServizio> getRichiesteDelCliente(Long idCliente) {
        return richiestaRepo.findByClientePubblicante_IdUtente(idCliente);
    }

}
