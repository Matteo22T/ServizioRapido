
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RichiestaService } from '../../service/richiesta-service';
import { ProposteService } from '../../service/proposte-service';
import { ChangeDetectorRef } from '@angular/core';

import { NotificaService } from '../../service/notifica-service';
@Component({
  selector: 'app-dashboard-professionista',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard-professionista.html',
  styleUrls: ['./dashboard-professionista.css']
})
export class DashboardProfessionista implements OnInit {
  utente: any;
  sezioneAttiva: string = 'cerca';

  richiesteAperte: any[] = [];
  richiesteAperteFiltrate: any[] = []; // Lista filtrata
  mieProposte: any[] = [];

  listaNotifiche: any[] = [];
  conteggioBadge: number = 0;

  // Filtro per città
  cittaDisponibili: string[] = [];
  cittaSelezionata: string = '';

  // Form per nuova proposta
  nuovaProposta = {
    dettagli: '',
    prezzo: 0,
    idRichiesta: 0
  };

  // Modal state
  mostraModalProposta = false;
  mostraModalModifica = false;
  richiestaSelezionata: any = null;
  propostaInModifica: any = null;

  mostraModalDettaglioRichiesta = false;
  richiestaDaVisualizzare: any = null;
  messaggio = '';

  constructor(
    private richiestaService: RichiestaService,
    private proposteService: ProposteService,
    private notificaService: NotificaService,
    private router: Router,
    private cd: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.controllaLogin();
  }

  controllaLogin() {
    const saved = localStorage.getItem('currentUser');
    if (saved) {
      this.utente = JSON.parse(saved);
      if (this.utente.ruolo !== 'PROFESSIONISTA') {
        this.router.navigate(['/dashboard-cliente']);
      } else {
        this.caricaRichiesteAperte();
        this.caricaMieProposte();
        this.caricaNotifiche();
      }
    } else {
      this.router.navigate(['/login']);
    }
  }

  cambiaSezione(sezione: string) {
    this.sezioneAttiva = sezione;
    this.messaggio = '';
    if (sezione === 'notifiche') {
      this.conteggioBadge = 0;
      this.caricaNotifiche();
    }
  }

  //LOGICA NOTIFICHE
  caricaNotifiche() {
    if (!this.utente?.id) return;
    this.notificaService.getNotifichePerUtente(this.utente.id).subscribe({
      next: (data) => {
        this.listaNotifiche = data;
        this.listaNotifiche.reverse();

        if (this.sezioneAttiva !== 'notifiche') {
          this.conteggioBadge = this.listaNotifiche.length;
        } else {
          this.conteggioBadge = 0; // Sicurezza extra
        }
        this.cd.detectChanges();
        console.log('Notifiche caricate:', this.listaNotifiche);
      },
      error: (err) => {
        console.error('Errore nel caricamento delle notifiche:', err);
      }
    });
  }

  // Estrae la città dall'indirizzo (formato: "via, città")
  estraiCitta(indirizzo: string): string {
    if (!indirizzo) return '';
    const parti = indirizzo.split(',');
    if (parti.length >= 2) {
      return parti[parti.length - 1].trim(); // Prende l'ultima parte dopo l'ultima virgola
    }
    return indirizzo.trim();
  }

  // Aggiorna la lista delle città disponibili
  aggiornaCittaDisponibili() {
    const cittaSet = new Set<string>();

    this.richiesteAperte.forEach(richiesta => {
      if (richiesta.indirizzo) {
        const citta = this.estraiCitta(richiesta.indirizzo);
        if (citta) {
          cittaSet.add(citta);
        }
      }
    });

    this.cittaDisponibili = Array.from(cittaSet).sort();
  }

  // Filtra le richieste per città
  filtraPerCitta() {
    if (!this.cittaSelezionata) {
      this.richiesteAperteFiltrate = [...this.richiesteAperte];
    } else {
      this.richiesteAperteFiltrate = this.richiesteAperte.filter(richiesta => {
        if (!richiesta.indirizzo) return false;
        const citta = this.estraiCitta(richiesta.indirizzo);
        return citta === this.cittaSelezionata;
      });
    }
  }

  // Reset del filtro
  resetFiltro() {
    this.cittaSelezionata = '';
    this.filtraPerCitta();
  }

  caricaRichiesteAperte() {
    this.richiestaService.getAperte(this.utente.id).subscribe({
      next: (data) => {
        this.richiesteAperte = data;
        this.richiesteAperteFiltrate = [...data]; // Inizialmente mostra tutte
        this.aggiornaCittaDisponibili();
        this.cd.detectChanges();
      },
      error: (err) => console.error(err)
    });
  }

  caricaMieProposte() {
    this.proposteService.vediMie(this.utente.id).subscribe({
      next: (data) => {
        this.mieProposte = data;
        this.cd.detectChanges();
      },
      error: (err) => console.error(err)
    });
  }

  apriModalProposta(richiesta: any) {
    this.richiestaSelezionata = richiesta;
    this.nuovaProposta = {
      dettagli: '',
      prezzo: 0,
      idRichiesta: richiesta.idRichiesta
    };
    this.mostraModalProposta = true;
  }

  chiudiModal() {
    this.mostraModalProposta = false;
    this.richiestaSelezionata = null;
  }

  inviaProposta() {
    this.proposteService.pubblica(
      this.nuovaProposta.dettagli,
      this.nuovaProposta.prezzo,
      this.nuovaProposta.idRichiesta,
      this.utente.id
    ).subscribe({
      next: () => {
        this.messaggio = 'Proposta inviata con successo!';
        this.chiudiModal();
        this.caricaMieProposte();
        setTimeout(() => this.messaggio = '', 3000);
        this.caricaRichiesteAperte();
        this.cd.detectChanges();

      },
      error: (err) => {
        console.error(err);
        this.messaggio = 'Errore nell\'invio della proposta';
      }
    });
  }

  apriModalModifica(proposta: any) {
    if (!proposta || !proposta.idProposta) {
      console.error('Proposta non valida');
      this.messaggio = 'Errore: proposta non valida';
      setTimeout(() => this.messaggio = '', 3000);
      return;
    }

    this.propostaInModifica = {
      idProposta: proposta.idProposta,
      dettagli: proposta.dettagli || '',
      prezzo: proposta.prezzo || 0,
      richiestaRiferimento: proposta.richiestaRiferimento
    };
    this.mostraModalModifica = true;
  }

  chiudiModalModifica() {
    this.mostraModalModifica = false;
    this.propostaInModifica = null;
  }

  modificaProposta() {
    if (!this.propostaInModifica) {
      console.error('Nessuna proposta selezionata per la modifica');
      return;
    }

    this.proposteService.modifica(
      this.propostaInModifica.idProposta,
      this.propostaInModifica.dettagli,
      this.propostaInModifica.prezzo
    ).subscribe({
      next: () => {
        this.messaggio = 'Proposta modificata con successo!';
        this.chiudiModalModifica();
        this.caricaMieProposte();
        setTimeout(() => this.messaggio = '', 3000);
      },
      error: (err) => {
        console.error(err);
        this.messaggio = 'Errore nella modifica della proposta';
        setTimeout(() => this.messaggio = '', 3000);
      }
    });
  }

  eliminaProposta(idProposta: number) {
    if (confirm('Sei sicuro di voler eliminare questa proposta?')) {
      this.proposteService.elimina(idProposta).subscribe({
        next: () => {
          this.messaggio = 'Proposta eliminata';
          this.caricaRichiesteAperte();
          this.caricaMieProposte();
          this.cd.detectChanges();
          setTimeout(() => this.messaggio = '', 3000);
        },
        error: (err) => console.error(err)
      });
    }
  }

  // Metodo per aprire il modal di dettaglio
  apriDettaglioRichiesta(richiesta: any) {
    if (!richiesta) {
      console.error("Nessuna richiesta collegata trovata");
      return;
    }
    this.richiestaDaVisualizzare = richiesta;
    this.mostraModalDettaglioRichiesta = true;
  }

  // Metodo per chiudere il modal
  chiudiDettaglioRichiesta() {
    this.mostraModalDettaglioRichiesta = false;
    this.richiestaDaVisualizzare = null;
  }

  logout() {
    localStorage.removeItem('currentUser');
    this.router.navigate(['/login']);
  }
}
