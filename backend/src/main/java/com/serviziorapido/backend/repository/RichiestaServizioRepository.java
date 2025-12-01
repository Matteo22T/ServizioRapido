package com.serviziorapido.backend.repository;

import com.serviziorapido.backend.model.CategoriaRichiesta;
import com.serviziorapido.backend.model.RichiestaServizio;
import com.serviziorapido.backend.model.StatoRichiesta; // Assicurati di importare il tuo Enum
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RichiestaServizioRepository extends JpaRepository<RichiestaServizio, Long> {

    List<RichiestaServizio> findByStatoRichiesta(StatoRichiesta stato);

    List<RichiestaServizio> findByStatoRichiestaAndCategoria(StatoRichiesta stato, CategoriaRichiesta categoria);

    List<RichiestaServizio> findByClientePubblicante_IdUtente(Long idCliente);
}