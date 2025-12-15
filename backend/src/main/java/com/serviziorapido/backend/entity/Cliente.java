package com.serviziorapido.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "cliente")
@PrimaryKeyJoinColumn(name = "id_utente")
public class Cliente extends Utente {

    private String indirizzo;

    @OneToMany(mappedBy = "clientePubblicante")
    @JsonIgnore
    private List<RichiestaServizio> richiestePubblicate;

    // Getters e Setters
    public String getIndirizzo() { return indirizzo; }
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }
}