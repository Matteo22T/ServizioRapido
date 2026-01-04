import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RichiestaService } from '../../service/richiesta-service';
import { ChangeDetectorRef } from '@angular/core';

import { NotificaService } from '../../service/notifica-service';
import { Notifica } from '../../model/Notifica.model';
import { ProposteService } from '../../service/proposte-service';


@Component({
  selector: 'app-dashboard-cliente',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard-cliente.html',
  styleUrls: ['./dashboard-cliente.css']
})
export class DashboardCliente implements OnInit {

  utente: any;
  sezioneAttiva: string = 'nuova';
  messaggio: string = '';
  conteggioBadge: number = 0;

  mieRichieste: any[] = [];

  listaNotifiche: Notifica[] = [];

  modalAperto: boolean = false;
  listaProposte: any[] = [];
  richiestaSelezionataId: number | null = null;

  nuovaRichiesta = {
    dettagli: '',
    indirizzo: '',
    categoria: ''
  };

  constructor(
    private router: Router,
    private richiestaService: RichiestaService,
    private notificaService: NotificaService,
    private proposteService: ProposteService,
  private cd: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.controllaLogin();
  }

  controllaLogin() {
    const saved = localStorage.getItem('currentUser');
    if (saved) {
      this.utente = JSON.parse(saved);
      if (this.utente.ruolo === 'PROFESSIONISTA') {
        this.router.navigate(['/dashboard-professionista']);
      } else {
        this.caricaMieRichieste();
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
          this.conteggioBadge = 0;
        }
        this.cd.detectChanges();
        console.log('Notifiche caricate:', this.listaNotifiche);
      },
      error: (err) => {
        console.error('Errore nel caricamento delle notifiche:', err);
      }
    });
  }

  azioneDettagli(notifica: any) {
    console.log("Vado ai dettagli per notifica:", notifica.idNotifica);
    this.cambiaSezione('richieste');
  }

  apriModalProposte(idRichiesta: number) {
    this.richiestaSelezionataId = idRichiesta;

    this.proposteService.vediProposte(idRichiesta).subscribe({
      next: (data) => {
        this.listaProposte = data.filter((p: any) => p.statoProposta === 'INVIATA');
        this.modalAperto = true;
        this.cd.detectChanges();
      },
      error: (err) => {
        console.error("Errore caricamento proposte", err);
        alert("Impossibile caricare le proposte al momento.");
      }
    });
  }

  chiudiModal() {
    this.modalAperto = false;
    this.listaProposte = [];
    this.richiestaSelezionataId = null;
  }

  usaMioIndirizzo() {
    if (this.utente?.indirizzo) {
      this.nuovaRichiesta.indirizzo = this.utente.indirizzo;
      this.messaggio = 'Indirizzo caricato dal tuo profilo! 📍';
      setTimeout(() => this.messaggio = '', 2000);
    } else {
      this.messaggio = 'Nessun indirizzo salvato nel tuo profilo.';
      setTimeout(() => this.messaggio = '', 3000);
    }
  }


  accetta(idProposta: number) {
    if (!this.richiestaSelezionataId) return;

    if (confirm("Sei sicuro di voler affidare il lavoro a questo professionista?")) {
      this.proposteService.accetta(idProposta, this.richiestaSelezionataId).subscribe({
        next: () => {
          alert("Proposta accettata! Il lavoro è ora 'In Lavorazione'.");
          this.chiudiModal();
          this.caricaMieRichieste();
        },
        error: (err) => {
          console.error(err);
          alert("Errore durante l'accettazione.");
        }
      });
    }
  }

  rifiuta(idProposta: number) {
    if (!this.richiestaSelezionataId) return;

    if (confirm("Vuoi rifiutare questa proposta?")) {
      this.proposteService.rifiuta(idProposta, this.richiestaSelezionataId).subscribe({
        next: () => {
          this.listaProposte = this.listaProposte.filter(p => p.idProposta !== idProposta);
          this.cd.detectChanges()
        },
        error: (err) => {
          console.error(err);
          alert("Errore durante il rifiuto.");
        }
      });
    }
  }

  caricaMieRichieste() {
    if (!this.utente?.id) return;

    this.richiestaService.getMieRichieste(this.utente.id).subscribe({
      next: (data) => {
        this.mieRichieste = data;
        this.cd.detectChanges();

        console.log('Richieste caricate:', this.mieRichieste);
      },
      error: (err) => {
        console.error('Errore nel caricamento delle richieste:', err);
      }
    });
  }

  pubblicaRichiesta() {
    if (!this.nuovaRichiesta.dettagli || !this.nuovaRichiesta.indirizzo || !this.nuovaRichiesta.categoria) {
      this.messaggio = 'Per favore, compila tutti i campi (Dettagli, Indirizzo e Categoria).';
      return;
    }

    this.richiestaService.pubblica(
      this.nuovaRichiesta.dettagli,
      this.nuovaRichiesta.indirizzo,
      this.nuovaRichiesta.categoria,
      this.utente.id
    ).subscribe({
      next: (res) => {
        console.log('Risposta server:', res);
        this.messaggio = 'Richiesta pubblicata con successo! 🚀';

        this.nuovaRichiesta = {
          dettagli: '',
          indirizzo: '',
          categoria: ''

        };

        this.caricaMieRichieste();
        setTimeout(() => {
          this.sezioneAttiva = 'richieste';
          this.messaggio = '';
        }, 1500);
        this.cd.detectChanges();

      },
      error: (err) => {
        console.error('Errore durante la pubblicazione:', err);
        this.messaggio = 'Errore durante la pubblicazione della richiesta. Riprova.';
      }
    });
  }

  eliminaRichiesta(idRichiesta: number) {
    if (confirm('Sei sicuro di voler eliminare questa richiesta?')) {
      this.richiestaService.elimina(idRichiesta).subscribe({
        next: () => {
          this.messaggio = 'Richiesta eliminata.';
          this.caricaMieRichieste()

          setTimeout(() => this.messaggio = '', 3000);
        },
        error: (err) => {
          console.error(err);
          this.messaggio = 'Errore durante l\'eliminazione.';
        }
      });
    }
  }

  completaLavoro(idRichiesta: number) {
    if (confirm("Confermi che il lavoro è stato svolto e vuoi chiudere la richiesta?")) {
      this.richiestaService.completa(idRichiesta).subscribe({
        next: () => {
          this.messaggio = "Ottimo! Richiesta completata e archiviata. 🎉";
          this.caricaMieRichieste();
          this.cd.detectChanges();
          setTimeout(() => this.messaggio = '', 4000);
        },
        error: (err) => {
          console.error(err);
          this.messaggio = "Errore durante il completamento della richiesta.";
        }
      });
    }}

  logout() {
    localStorage.removeItem('currentUser');
    this.router.navigate(['/login']);
  }
}
