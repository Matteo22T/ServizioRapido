package com.serviziorapido.backend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "richiesta_servizio")
public class RichiestaServizio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRichiesta;

    @Column(columnDefinition = "TEXT")
    private String dettagli;

    @Enumerated(EnumType.STRING)
    private StatoRichiesta statoRichiesta;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente clientePubblicante;

    @OneToOne
    @JoinColumn(name = "id_proposta_accettata")
    private PropostaServizio propostaAccettata;

    @OneToMany(mappedBy = "richiestaRiferimento")
    private List<PropostaServizio> proposteRicevute;

    // Getters e Setters essenziali
    public Long getIdRichiesta() { return idRichiesta; }
    public void setIdRichiesta(Long idRichiesta) { this.idRichiesta = idRichiesta; }

    public String getDettagli() { return dettagli; }
    public void setDettagli(String dettagli) { this.dettagli = dettagli; }

    public StatoRichiesta getStatoRichiesta() { return statoRichiesta; }
    public void setStatoRichiesta(StatoRichiesta statoRichiesta) { this.statoRichiesta = statoRichiesta; }

    public Cliente getClientePubblicante() { return clientePubblicante; }
    public void setClientePubblicante(Cliente clientePubblicante) { this.clientePubblicante = clientePubblicante; }

    public PropostaServizio getPropostaAccettata() { return propostaAccettata; }
    public void setPropostaAccettata(PropostaServizio propostaAccettata) { this.propostaAccettata = propostaAccettata; }
}