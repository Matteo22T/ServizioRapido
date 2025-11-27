package com.serviziorapido.backend.service;

import com.serviziorapido.backend.model.Cliente;
import com.serviziorapido.backend.model.RichiestaServizio;
import com.serviziorapido.backend.model.StatoRichiesta;
import com.serviziorapido.backend.repository.RichiestaServizioRepository;
import com.serviziorapido.backend.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RichiestaServizioService {

    @Autowired
    private RichiestaServizioRepository richiestaRepo;

    @Autowired
    private UtenteRepository utenteRepo;

    public RichiestaServizio creaRichiesta(RichiestaServizio richiesta){

        Long idCliente = richiesta.getClientePubblicante().getIdUtente();

         Cliente clienteVero = (Cliente) utenteRepo.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente non trovato con ID: " + idCliente));

        richiesta.setClientePubblicante(clienteVero);

        richiesta.setStatoRichiesta(StatoRichiesta.APERTA);
        return richiestaRepo.save(richiesta);
    }

    public List<RichiestaServizio> getRichiesteAperte() {
        return richiestaRepo.findByStatoRichiesta(StatoRichiesta.APERTA);
    }

    public List<RichiestaServizio> getRichiesteDelCliente(Long idCliente) {
        return richiestaRepo.findByClientePubblicante_IdUtente(idCliente);
    }

    public void annullaRichiesta(Long idRichiesta){
        richiestaRepo.deleteById(idRichiesta);
    }

}
