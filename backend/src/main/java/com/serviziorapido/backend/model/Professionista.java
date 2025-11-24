package com.serviziorapido.backend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "professionista")
@PrimaryKeyJoinColumn(name = "id_utente")
public class Professionista extends Utente {

    private String biografia;
    private String specializzazione;

    @OneToMany(mappedBy = "professionistaMittente")
    private List<PropostaServizio> proposteInviate;

    // Getters e Setters
    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }

    public String getSpecializzazione() { return specializzazione; }
    public void setSpecializzazione(String specializzazione) { this.specializzazione = specializzazione; }
}