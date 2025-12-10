package com.serviziorapido.backend.controller;

import com.serviziorapido.backend.dto.NotificaDTO;
import com.serviziorapido.backend.model.Notifica;
import com.serviziorapido.backend.service.NotificaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/notifiche")
@CrossOrigin(origins = "http://localhost:4200")
public class NotificaController {
    private NotificaService notificaService;

    @Autowired
    public NotificaController(NotificaService notificaService) {
        this.notificaService = notificaService;
    }

    //End-point per ottenere le notifiche per un utente specifico
    @GetMapping("/utente/{idUtente}")
    public ResponseEntity<List<NotificaDTO>> getNotifichePerUtente(@PathVariable Long idUtente) {

        List<Notifica> notificheEntity = notificaService.getNotifichePerUtente(idUtente);
        List<NotificaDTO> notificheDTO = new ArrayList<>();

        for (Notifica entity : notificheEntity) {
            NotificaDTO dto = new NotificaDTO();

            // Copio i dati semplici
            dto.setIdNotifica(entity.getIdNotifica());
            dto.setMessaggio(entity.getMessaggio());
            dto.setDestinatarioId(entity.getDestinatarioId());
            if (entity.getStatoNotifica() != null) {
                dto.setStatoInvio(entity.getStatoNotifica().name());
            }

            notificheDTO.add(dto);
        }
        return new ResponseEntity<>(notificheDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void eliminaNotifica(@PathVariable Long id) {
        notificaService.eliminaNotifica(id);
    }

}