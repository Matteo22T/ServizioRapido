package com.serviziorapido.backend.service;
import com.serviziorapido.backend.entity.Notifica;
import com.serviziorapido.backend.entity.StatoNotifica;
import com.serviziorapido.backend.repository.NotificaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificaService {
    private final NotificaRepository notificaRepository;

    @Autowired
    public NotificaService(NotificaRepository notificaRepository) {
        this.notificaRepository = notificaRepository;
    }

    public Notifica inviaNotifica(Long destinatarioId, String messaggio) {
        Notifica notifica = new Notifica();
        notifica.setDestinatarioId(destinatarioId);
        notifica.setMessaggio(messaggio);
        notifica.setStatoNotifica(StatoNotifica.INVIATO);
        return notificaRepository.save(notifica);
    }
    public void eliminaNotifica(Long id) {
        notificaRepository.deleteById(id);
    }

    //Recupera le notifiche per un utente specifico
    public List<Notifica> getNotifichePerUtente(Long idUtente) {
        return notificaRepository.findByDestinatarioId(idUtente);
    }



}
