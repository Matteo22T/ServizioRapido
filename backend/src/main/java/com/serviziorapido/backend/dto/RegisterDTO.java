package com.serviziorapido.backend.dto;

public class RegisterDTO {
    // Campi comuni
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String telefono;
    private String tipoProfilo; // "CLIENTE" o "PROFESSIONISTA"

    // Campi specifici Cliente
    private String indirizzo;

    // Campi specifici Professionista
    private String biografia;
    private String specializzazione; // Riceviamo la stringa dell'Enum

    // Costruttore vuoto, Getters e Setters
    public RegisterDTO() {}

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getTipoProfilo() { return tipoProfilo; }
    public void setTipoProfilo(String tipoProfilo) { this.tipoProfilo = tipoProfilo; }
    public String getIndirizzo() { return indirizzo; }
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }
    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }
    public String getSpecializzazione() { return specializzazione; }
    public void setSpecializzazione(String specializzazione) { this.specializzazione = specializzazione; }
}