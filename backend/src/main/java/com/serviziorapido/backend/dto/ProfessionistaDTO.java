package com.serviziorapido.backend.dto;

public class ProfessionistaDTO extends UtenteDTO {
    private String biografia;
    private String specializzazione;

    public ProfessionistaDTO() {}

    // Getter e Setter
    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }
    public String getSpecializzazione() { return specializzazione; }
    public void setSpecializzazione(String specializzazione) { this.specializzazione = specializzazione; }
}