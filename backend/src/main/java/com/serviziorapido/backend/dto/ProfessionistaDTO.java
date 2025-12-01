package com.serviziorapido.backend.dto;

import com.serviziorapido.backend.model.CategoriaRichiesta;

public class ProfessionistaDTO extends UtenteDTO {
    private String biografia;
    private CategoriaRichiesta specializzazione;

    public ProfessionistaDTO() {}

    // Getter e Setter
    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }
    public CategoriaRichiesta getSpecializzazione() { return specializzazione; }
    public void setSpecializzazione(CategoriaRichiesta specializzazione) { this.specializzazione = specializzazione; }
}