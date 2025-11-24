package com.serviziorapido.backend.repository;

import com.serviziorapido.backend.model.PropostaServizio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PropostaServizioRepository extends JpaRepository<PropostaServizio, Long> {

    List<PropostaServizio> findByRichiestaRiferimento_IdRichiesta(Long idRichiesta);

    List<PropostaServizio> findByProfessionistaMittente_IdUtente(Long idProfessionista);
}