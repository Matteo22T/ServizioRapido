package com.serviziorapido.backend.factory;

import com.serviziorapido.backend.dto.RegisterDTO;
import com.serviziorapido.backend.entity.Utente;

public interface UtenteFactory {
    Utente creaUtente(RegisterDTO dto);
}