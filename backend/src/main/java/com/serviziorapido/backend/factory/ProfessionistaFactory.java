package com.serviziorapido.backend.factory;

import com.serviziorapido.backend.dto.RegisterDTO;
import com.serviziorapido.backend.entity.CategoriaRichiesta;
import com.serviziorapido.backend.entity.Professionista;
import com.serviziorapido.backend.entity.Utente;
import org.springframework.stereotype.Component;

@Component("PROFESSIONISTA")
public class ProfessionistaFactory implements UtenteFactory {

    @Override
    public Utente creaUtente(RegisterDTO dto) {
        Professionista prof = new Professionista();
        // Set dati comuni
        prof.setNome(dto.getNome());
        prof.setCognome(dto.getCognome());
        prof.setEmail(dto.getEmail());
        prof.setPassword(dto.getPassword());
        prof.setTelefono(dto.getTelefono());

        // Set dati specifici
        prof.setBiografia(dto.getBiografia());

        // Conversione Enum
        if (dto.getSpecializzazione() != null) {
            try {
                prof.setSpecializzazione(CategoriaRichiesta.valueOf(dto.getSpecializzazione()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Specializzazione non valida: " + dto.getSpecializzazione());
            }
        }

        return prof;
    }
}