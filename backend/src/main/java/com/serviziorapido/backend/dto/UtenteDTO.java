package com.serviziorapido.backend.dto;

public class UtenteDTO {
    private Long idUtente;
    private String nome;
    private String cognome;
    private String email;
    private String telefono;
    private String ruolo; // "CLIENTE" o "PROFESSIONISTA"

    // Costruttore vuoto e con campi
    public UtenteDTO() {}

    public UtenteDTO(Long id, String nome, String cognome, String email, String telefono, String ruolo) {
        this.idUtente = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.telefono = telefono;
        this.ruolo = ruolo;
    }

    // Getter e Setter (generali con IDE)
    public Long getId() { return idUtente; }
    public void setId(Long id) { this.idUtente = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getRuolo() { return ruolo; }
    public void setRuolo(String ruolo) { this.ruolo = ruolo; }
}