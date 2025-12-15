package com.serviziorapido.backend.repository;

import com.serviziorapido.backend.entity.CategoriaRichiesta;
import com.serviziorapido.backend.entity.RichiestaServizio;
import com.serviziorapido.backend.entity.StatoRichiesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface RichiestaServizioRepository extends JpaRepository<RichiestaServizio, Long> {

    List<RichiestaServizio> findByStatoRichiesta(StatoRichiesta stato);


    List<RichiestaServizio> findByClientePubblicante_IdUtente(Long idCliente);

    // --- NUOVO METODO FILTRATO ---
    // Seleziona le richieste che:
    // 1. Hanno lo stato specificato (APERTA)
    // 2. Hanno la categoria specificata
    // 3. Il cui ID NON è presente nella lista delle proposte fatte da QUESTO professionista
    @Query("SELECT r FROM RichiestaServizio r WHERE " +
            "r.statoRichiesta = :stato " +
            "AND r.categoria = :categoria " +
            "AND r.idRichiesta NOT IN " +
            "(SELECT p.richiestaRiferimento.idRichiesta FROM PropostaServizio p WHERE p.professionistaMittente.idUtente = :idProfessionista)")
    List<RichiestaServizio> findByStatoRichiestaAndCategoria(
            @Param("stato") StatoRichiesta stato,
            @Param("categoria") CategoriaRichiesta categoria,
            @Param("idProfessionista") Long idProfessionista
    );
}