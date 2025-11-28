import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router'; // Serve per leggere il token dall'URL
import { AutenticazioneService } from '../../service/autenticazione-service';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './reset-password.html',
  styleUrls: ['./reset-password.css']
})
export class ResetPassword implements OnInit {
  token = '';
  nuovaPassword = '';
  messaggio = '';
  successo = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AutenticazioneService
  ) {}

  ngOnInit() {
    // Legge il parametro ?token=... dall'URL
    this.token = this.route.snapshot.queryParams['token'];
  }

  salvaPassword() {
    if(!this.token) {
      this.messaggio = "Token mancante. Riprova dal link email.";
      return;
    }

    this.authService.eseguiReset(this.token, this.nuovaPassword).subscribe({
      next: () => {
        this.successo = true;
        this.messaggio = 'Password aggiornata con successo! Verrai reindirizzato al login...';

        // ASPETTA 3 SECONDI E POI VAI AL LOGIN
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 3000);
      },
      error: (err) => {
        this.messaggio = err.error || 'Errore: Token scaduto o password non valida.';
        this.successo = false;
      }
    });
  }
}
