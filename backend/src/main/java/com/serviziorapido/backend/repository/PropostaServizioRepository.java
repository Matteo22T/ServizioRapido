package com.serviziorapido.backend.repository;

import com.serviziorapido.backend.entity.PropostaServizio;
import com.serviziorapido.backend.entity.StatoRichiesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PropostaServizioRepository extends JpaRepository<PropostaServizio, Long> {

    List<PropostaServizio> findByRichiestaRiferimento_IdRichiesta(Long idRichiesta);


    @Query("SELECT p FROM PropostaServizio p WHERE " +
            "p.professionistaMittente.idUtente = :idProf " +
            "AND p.richiestaRiferimento.statoRichiesta != :statoEscluso")
    List<PropostaServizio> findByProfessionistaMittente_IdUtente(
            @Param("idProf") Long idProf,
            @Param("statoEscluso") StatoRichiesta statoEscluso
    );

    List<PropostaServizio> findByRichiestaRiferimento_IdRichiestaAndProfessionistaMittente_IdUtente(Long idRichiesta, Long idProfessionista);
}