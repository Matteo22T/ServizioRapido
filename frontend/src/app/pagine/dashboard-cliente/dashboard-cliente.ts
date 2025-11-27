import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RichiestaService } from '../../service/richiesta-service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-dashboard-cliente',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard-cliente.html',
  styleUrls: ['./dashboard-cliente.css']
})
export class DashboardCliente implements OnInit {
  utente: any;
  mieRichieste: any[] = [];
  messaggio = "";
  nuovaRichiesta = "";

  constructor(private richiestaService: RichiestaService, private router: Router) {
  }



  ngOnInit() {
    this.controllaLogin();
  }

  controllaLogin() {
    const saved = localStorage.getItem('currentUser');
    if (saved) {
      this.utente = JSON.parse(saved);
      this.caricaRichieste();
    } else {
      this.router.navigate(['/login']);
    }
  }

  caricaRichieste() {
    this.richiestaService.getMie(this.utente.id).subscribe({
        next: (data) => {
          this.mieRichieste = data;
        },
        error: (err) => console.log(err)
      }
    );
  }

  pubblica() {
    this.richiestaService.pubblica(this.nuovaRichiesta, this.utente.id).subscribe({
      next: () => {
        this.messaggio = 'Richiesta pubblicata con successo!';
        this.nuovaRichiesta = ''; // Pulisci campo
        this.caricaRichieste();   // Aggiorna lista
        setTimeout(() => this.messaggio = '', 3000);
      },
      error: (err) => {
        console.log((err));
        this.messaggio = 'Errore nella pubblicazione';
      }
    });
  }

  logout() {
    localStorage.removeItem('currentUser');
    this.router.navigate(['/login']);
  }
}
