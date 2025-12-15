package com.serviziorapido.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;

@Entity
@Table(name = "proposta_servizio")
public class PropostaServizio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProposta;

    @Column(columnDefinition = "TEXT")
    private String dettagli;

    private BigDecimal prezzo;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM) // <--- AGGIUNGI QUESTO!
    private StatoProposta statoProposta;

    @ManyToOne
    @JoinColumn(name = "id_richiesta", nullable = false)
    private RichiestaServizio richiestaRiferimento;

    @ManyToOne
    @JoinColumn(name = "id_professionista", nullable = false)
    private Professionista professionistaMittente;

    // Getters e Setters essenziali
    public Long getIdProposta() { return idProposta; }
    public void setIdProposta(Long idProposta) { this.idProposta = idProposta; }

    public String getDettagli() { return dettagli; }
    public void setDettagli(String dettagli) { this.dettagli = dettagli; }

    public BigDecimal getPrezzo() { return prezzo; }
    public void setPrezzo(BigDecimal prezzo) { this.prezzo = prezzo; }

    public StatoProposta getStatoProposta() { return statoProposta; }
    public void setStatoProposta(StatoProposta statoProposta) { this.statoProposta = statoProposta; }

    public RichiestaServizio getRichiestaRiferimento() { return richiestaRiferimento; }
    public void setRichiestaRiferimento(RichiestaServizio richiestaRiferimento) { this.richiestaRiferimento = richiestaRiferimento; }

    public Professionista getProfessionistaMittente() { return professionistaMittente; }
    public void setProfessionistaMittente(Professionista professionistaMittente) { this.professionistaMittente = professionistaMittente; }
}