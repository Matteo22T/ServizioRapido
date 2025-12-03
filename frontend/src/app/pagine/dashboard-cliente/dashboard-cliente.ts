import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { RichiestaService } from '../../service/richiesta-service'; // Controlla che il nome del file sia corretto

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

  // Array per memorizzare le richieste ricevute dal backend
  mieRichieste: any[] = [];

  // Oggetto per collegare i campi del form HTML
  nuovaRichiesta = {
    dettagli: '',
    indirizzo: '',
    categoria: ''
  };

  constructor(
    private router: Router,
    private richiestaService: RichiestaService
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
      }
    } else {
      this.router.navigate(['/login']);
    }
  }

  cambiaSezione(sezione: string) {
    this.sezioneAttiva = sezione;
    this.messaggio = '';
  }

  // --- LOGICA RICHIESTE ---

  caricaMieRichieste() {
    if (!this.utente?.id) return;

    this.richiestaService.getMieRichieste(this.utente.id).subscribe({
      next: (data) => {
        this.mieRichieste = data;
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
          // Rimuovo localmente l'elemento per aggiornare la vista subito
          this.mieRichieste = this.mieRichieste.filter(r => r.idRichiesta !== idRichiesta);

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
