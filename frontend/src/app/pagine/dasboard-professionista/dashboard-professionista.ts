import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RichiestaService } from '../../service/richiesta-service';
import { ProposteService } from '../../service/proposte-service';
import { ChangeDetectorRef } from '@angular/core'; // <--- 1. Importa

@Component({
  selector: 'app-dashboard-professionista',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard-professionista.html',
  styleUrls: ['./dashboard-professionista.css']
})
export class DashboardProfessionista implements OnInit {
  utente: any;
  sezioneAttiva: string = 'cerca'; // 'profilo', 'proposte', 'cerca'

  richiesteAperte: any[] = [];

  mieProposte: any[] = [];

  // Form per nuova proposta
  nuovaProposta = {
    dettagli: '',
    prezzo: 0,
    idRichiesta: 0
  };

  // Modal state
  mostraModalProposta = false;
  richiestaSelezionata: any = null;

  messaggio = '';

  constructor(
    private richiestaService: RichiestaService,
    private proposteService: ProposteService,
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
      }
    } else {
      this.router.navigate(['/login']);
    }
  }

  cambiaSezione(sezione: string) {
    this.sezioneAttiva = sezione;
    this.messaggio = '';
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

  eliminaProposta(idProposta: number) {
    if (confirm('Sei sicuro di voler eliminare questa proposta?')) {
      this.proposteService.elimina(idProposta);
      this.caricaMieProposte();
      this.messaggio = 'Proposta eliminata';
      setTimeout(() => this.messaggio = '', 3000);
    }
  }

  logout() {
    localStorage.removeItem('currentUser');
    this.router.navigate(['/login']);
  }
}
