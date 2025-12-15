package com.serviziorapido.backend.observer_pattern;

import com.serviziorapido.backend.entity.PropostaServizio;
import com.serviziorapido.backend.entity.RichiestaServizio;
import com.serviziorapido.backend.event.TipoEventoProposta;
import com.serviziorapido.backend.event.TipoEventoRichiesta;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {

    private List<Observer> observers = new ArrayList<>();

    public void attach(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    protected void notifyObservers(PropostaServizio proposta, TipoEventoProposta tipo) {
        for (Observer observer : observers) {
            observer.update(proposta, tipo);
        }
    }

    protected void notifyObservers(RichiestaServizio richiesta, TipoEventoRichiesta tipo) {
        for (Observer observer : observers) {
            observer.update(richiesta, tipo);
        }
    }
}