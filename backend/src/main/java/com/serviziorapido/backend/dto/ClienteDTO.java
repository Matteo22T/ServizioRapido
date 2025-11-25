package com.serviziorapido.backend.dto;

public class ClienteDTO extends UtenteDTO {
    private String indirizzo;

    public ClienteDTO() {}

    // Getter e Setter
    public String getIndirizzo() { return indirizzo; }
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }
}