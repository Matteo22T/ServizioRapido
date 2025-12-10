import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RichiestaService } from '../../service/richiesta-service'; // Controlla che il nome del file sia corretto
import { ChangeDetectorRef } from '@angular/core'; // <--- 1. Importa

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
  sezioneAttiva: string = 'nuova'; // Parte dalla schermata di creazione
  messaggio: string = '';
  conteggioBadge: number = 0;

  // Array per memorizzare le richieste ricevute dal backend
  mieRichieste: any[] = [];

  //  ARRAY PER LE NOTIFICHE
  listaNotifiche: Notifica[] = [];

  // 2. VARIABILI PER LA MODALE
  modalAperto: boolean = false;
  listaProposte: any[] = [];
  richiestaSelezionataId: number | null = null;

  // Oggetto per collegare i campi del form HTML
  nuovaRichiesta = {
    dettagli: '',
    indirizzo: '',
    categoria: ''
  };

  constructor(
    private router: Router,
    private richiestaService: RichiestaService,
    private notificaService: NotificaService, //INIEZIONE SERVICE
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
      // Se è un professionista, reindirizza
      if (this.utente.ruolo === 'PROFESSIONISTA') {
        this.router.navigate(['/dashboard-professionista']);
      } else {
        // Se è cliente, carica i suoi dati
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

        // LOGICA INTELLIGENTE:
        // Se sono in un'altra pagina (es. Profilo), mostrami quante notifiche ho (es. 5).
        // Se sono già dentro "Notifiche", non farmi vedere il numero rosso (resta 0).
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

  azioneDettagli(notifica: any) {
    console.log("Vado ai dettagli per notifica:", notifica.idNotifica);
    // Cambia la tab attiva
    this.cambiaSezione('richieste');
    // Qui potresti evidenziare la richiesta specifica se avessimo l'ID richiesta
  }

  apriModalProposte(idRichiesta: number) {
    this.richiestaSelezionataId = idRichiesta;

    // Chiama il service per scaricare le proposte vere dal database
    this.proposteService.vediProposte(idRichiesta).subscribe({
      next: (data) => {
        this.listaProposte = data.filter((p: any) => p.statoProposta === 'INVIATA');
        this.modalAperto = true;   // Apre la finestra popup
        this.cd.detectChanges();   // Aggiorna la vista
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

  accetta(idProposta: number) {
    if (!this.richiestaSelezionataId) return;

    if (confirm("Sei sicuro di voler affidare il lavoro a questo professionista?")) {
      this.proposteService.accetta(idProposta, this.richiestaSelezionataId).subscribe({
        next: () => {
          alert("Proposta accettata! Il lavoro è ora 'In Lavorazione'.");
          this.chiudiModal();
          this.caricaMieRichieste(); // Ricarica per vedere lo stato aggiornato
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
          // Rimuovi la proposta dalla lista visiva senza chiudere la modale
          this.listaProposte = this.listaProposte.filter(p => p.idProposta !== idProposta);
        },
        error: (err) => {
          console.error(err);
          alert("Errore durante il rifiuto.");
        }
      });
    }
  }
  // --- LOGICA RICHIESTE ---

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
    // 1. Validazione: controlla che i campi non siano vuoti
    if (!this.nuovaRichiesta.dettagli || !this.nuovaRichiesta.indirizzo || !this.nuovaRichiesta.categoria) {
      this.messaggio = 'Per favore, compila tutti i campi (Dettagli, Indirizzo e Categoria).';
      return;
    }

    // 2. Chiamata al Service
    // Nota: Passiamo i parametri separati come richiesto dal tuo nuovo Service
    this.richiestaService.pubblica(
      this.nuovaRichiesta.dettagli,
      this.nuovaRichiesta.indirizzo,
      this.nuovaRichiesta.categoria,
      this.utente.id // Passiamo l'ID dell'utente loggato
    ).subscribe({
      next: (res) => {
        console.log('Risposta server:', res);
        this.messaggio = 'Richiesta pubblicata con successo! 🚀';

        // 3. Reset del form
        this.nuovaRichiesta = {
          dettagli: '',
          indirizzo: '',
          categoria: ''

        };

        // 4. Aggiorna la lista e cambia sezione
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

  logout() {
    localStorage.removeItem('currentUser');
    this.router.navigate(['/login']);
  }
}
