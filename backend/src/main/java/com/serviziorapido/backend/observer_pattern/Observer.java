package com.serviziorapido.backend.observer_pattern;

import com.serviziorapido.backend.entity.PropostaServizio;
import com.serviziorapido.backend.entity.RichiestaServizio;
import com.serviziorapido.backend.event.TipoEventoProposta;
import com.serviziorapido.backend.event.TipoEventoRichiesta;

public interface Observer {

    void update(PropostaServizio proposta, TipoEventoProposta tipoEvento);

    default void update(RichiestaServizio richiesta, TipoEventoRichiesta tipo) {
    }
}