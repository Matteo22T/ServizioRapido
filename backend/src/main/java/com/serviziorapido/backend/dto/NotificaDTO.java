package com.serviziorapido.backend.dto;

import com.serviziorapido.backend.entity.StatoNotifica;

public class NotificaDTO {
    private Long idNotifica;
    private String messaggio;
    private String statoNotifica; // Uso String per facilità di lettura nel JSON
    private Long destinatarioId;

    public NotificaDTO() {
    }

    public NotificaDTO(Long idNotifica, String messaggio, String statoNotifica, Long destinatarioId) {
        this.idNotifica = idNotifica;
        this.messaggio = messaggio;
        this.statoNotifica = statoNotifica;
        this.destinatarioId = destinatarioId;
    }
    public Long getIdNotifica() {
        return idNotifica;
    }

    public void setIdNotifica(Long idNotifica) {
        this.idNotifica = idNotifica;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public String getStatoInvio() {
        return statoNotifica;
    }

    public void setStatoInvio(String statoNotifica) {
        this.statoNotifica= statoNotifica;
    }

    public Long getDestinatarioId() {
        return destinatarioId;
    }

    public void setDestinatarioId(Long destinatarioId) {
        this.destinatarioId = destinatarioId;
    }
}
