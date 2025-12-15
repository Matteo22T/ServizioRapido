package com.serviziorapido.backend.factory;

import com.serviziorapido.backend.dto.RegisterDTO;
import com.serviziorapido.backend.entity.Cliente;
import com.serviziorapido.backend.entity.Utente;
import org.springframework.stereotype.Component;

@Component("CLIENTE") // Nome utile per la selezione dinamica
public class ClienteFactory implements UtenteFactory {

    @Override
    public Utente creaUtente(RegisterDTO dto) {
        Cliente cliente = new Cliente();
        // Set dati comuni
        cliente.setNome(dto.getNome());
        cliente.setCognome(dto.getCognome());
        cliente.setEmail(dto.getEmail());
        cliente.setPassword(dto.getPassword());
        cliente.setTelefono(dto.getTelefono());

        // Set dati specifici
        cliente.setIndirizzo(dto.getIndirizzo());

        return cliente;
    }
}