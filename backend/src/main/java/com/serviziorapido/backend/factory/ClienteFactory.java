package com.serviziorapido.backend.factory;

import com.serviziorapido.backend.dto.RegisterDTO;
import com.serviziorapido.backend.entity.Cliente;
import com.serviziorapido.backend.entity.Utente;
import org.springframework.stereotype.Component;

@Component("CLIENTE")
public class ClienteFactory implements UtenteFactory {

    @Override
    public Utente creaUtente(RegisterDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setCognome(dto.getCognome());
        cliente.setEmail(dto.getEmail());
        cliente.setPassword(dto.getPassword());
        cliente.setTelefono(dto.getTelefono());

        cliente.setIndirizzo(dto.getIndirizzo());

        return cliente;
    }
}