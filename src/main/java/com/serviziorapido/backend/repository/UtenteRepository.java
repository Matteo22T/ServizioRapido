package com.serviziorapido.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.serviziorapido.backend.model.Utente;
import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Long> {

    Optional<Utente> findByEmail(String email);

    Boolean existsByEmail(String email);
}
