package com.serviziorapido.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "notifica")
public class Notifica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notifica")
    private Long idNotifica;

    @Column(name = "messaggio", nullable = false, columnDefinition = "TEXT")
    private String messaggio;

    @Enumerated(EnumType.STRING)
    @Column(name = "stato_invio")
    private StatoNotifica statoNotifica;

    @Column(name = "destinatario", nullable = false)
    private Long destinatarioId;


    public Notifica() {
    }

    public Notifica(String messaggio, StatoNotifica statoInvio, Long destinatarioId) {
        this.messaggio = messaggio;
        this.statoNotifica = statoInvio;
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

    public StatoNotifica getStatoNotifica() {
        return statoNotifica;
    }

    public void setStatoNotifica(StatoNotifica  statoNotifica) {
        this.statoNotifica= statoNotifica;
    }

    public Long getDestinatarioId() {
        return destinatarioId;
    }

    public void setDestinatarioId(Long destinatarioId) {
        this.destinatarioId = destinatarioId;
    }
}