package com.serviziorapido.backend.event;

public enum TipoEventoProposta {
    INVIATA,      // Nuova proposta inviata
    MODIFICATA,   // Proposta modificata
    ELIMINATA,    // Proposta ritirata dal professionista
    ACCETTATA,    // Proposta scelta dal cliente
    SCARTATA,     // Proposta rifiutata automaticamente perché ne è stata scelta un'altra
    RIFIUTATA     // Proposta rifiutata manualmente
}