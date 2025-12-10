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
  mieProposte: any[] = [];

  listaNotifiche: any[] = [];
  conteggioBadge: number = 0;

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

  caricaRichiesteAperte() {
    this.richiestaService.getAperte(this.utente.id).subscribe({
      next: (data) => {
        this.richiesteAperte = data;
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
          this.caricaMieProposte();
          setTimeout(() => this.messaggio = '', 3000);
        },
        error: (err) => console.error(err)
      });
    }
  }

  logout() {
    localStorage.removeItem('currentUser');
    this.router.navigate(['/login']);
  }
}
