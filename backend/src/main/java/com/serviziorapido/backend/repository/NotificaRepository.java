package com.serviziorapido.backend.repository;

import com.serviziorapido.backend.entity.Notifica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface NotificaRepository extends JpaRepository<Notifica, Long> {
    List<Notifica> findByDestinatarioId(Long destinatarioId);
}
