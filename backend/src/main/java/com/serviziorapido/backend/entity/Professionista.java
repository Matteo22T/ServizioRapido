package com.serviziorapido.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "professionista")
@PrimaryKeyJoinColumn(name = "id_utente")
public class Professionista extends Utente {

    private String biografia;

    @Enumerated(EnumType.STRING)
    private CategoriaRichiesta specializzazione;

    @OneToMany(mappedBy = "professionistaMittente")
    @JsonIgnore
    private List<PropostaServizio> proposteInviate;

    // Getters e Setters
    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }

    public CategoriaRichiesta getSpecializzazione() { return specializzazione; }
    public void setSpecializzazione(CategoriaRichiesta specializzazione) { this.specializzazione = specializzazione; }
}