package com.serviziorapido.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Table(name = "richiesta_servizio")
public class RichiestaServizio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRichiesta;

    @Column(columnDefinition = "TEXT")
    private String dettagli;

    private String indirizzo;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM) // <--- AGGIUNGI QUESTO
    private CategoriaRichiesta categoria;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM) // <--- AGGIUNGI QUESTO
    private StatoRichiesta statoRichiesta;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente clientePubblicante;

    @OneToOne
    @JoinColumn(name = "id_proposta_accettata")
    @JsonIgnore
    private PropostaServizio propostaAccettata;

    @OneToMany(mappedBy = "richiestaRiferimento", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<PropostaServizio> proposteRicevute;

    // --- GETTER E SETTER ---

    public Long getIdRichiesta() { return idRichiesta; }
    public void setIdRichiesta(Long idRichiesta) { this.idRichiesta = idRichiesta; }

    public String getDettagli() { return dettagli; }
    public void setDettagli(String dettagli) { this.dettagli = dettagli; }

    // Getter/Setter per i nuovi campi
    public String getIndirizzo() { return indirizzo; }
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }

    public CategoriaRichiesta getCategoria() { return categoria; }
    public void setCategoria(CategoriaRichiesta categoria) { this.categoria = categoria; }
    // ------------------------------

    public StatoRichiesta getStatoRichiesta() { return statoRichiesta; }
    public void setStatoRichiesta(StatoRichiesta statoRichiesta) { this.statoRichiesta = statoRichiesta; }

    public Cliente getClientePubblicante() { return clientePubblicante; }
    public void setClientePubblicante(Cliente clientePubblicante) { this.clientePubblicante = clientePubblicante; }

    public PropostaServizio getPropostaAccettata() { return propostaAccettata; }
    public void setPropostaAccettata(PropostaServizio propostaAccettata) { this.propostaAccettata = propostaAccettata; }

    public List<PropostaServizio> getProposteRicevute() {
        return proposteRicevute;
    }
    public void setProposteRicevute(List<PropostaServizio> proposteRicevute) {
        this.proposteRicevute = proposteRicevute;
    }
}