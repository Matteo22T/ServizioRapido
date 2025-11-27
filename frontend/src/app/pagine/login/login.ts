import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { AutenticazioneService } from '../../service/autenticazione-service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink], // Aggiungi RouterLink agli import se usi il link "Registrati qui"
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class LoginComponent {
  email = '';
  password = '';
  errore = '';

  constructor(private authService: AutenticazioneService, private router: Router) {}

  // typescript
  login() {
    console.log('login start', { email: this.email, hasPassword: !!this.password });
    this.authService.login(this.email, this.password).subscribe({
      next: (res: any) => {
        console.log('login response raw:', res);
        const user = res?.user ?? res; // supporta sia { user: {...} } che {...}
        if (!user) {
          console.error('Nessun utente nella response');
          this.errore = 'Errore server: response vuota';
          return;
        }
        localStorage.setItem('currentUser', JSON.stringify(user));
        const ruolo = user.ruolo ?? user.role ?? user.tipo;
        console.log('ruolo:', ruolo);
        if (ruolo === 'PROFESSIONISTA') {
          this.router.navigate(['/']);
        } else {
          this.router.navigate(['/dashboard-cliente']);
        }
        alert('Benvenuto ' + (user.nome ?? user.name ?? ''));
      },
      error: (err) => {
        console.error('login error:', err);
        // Mostra messaggio più utile se presente nel payload
        this.errore = err?.error?.message ?? err?.message ?? 'Credenziali errate o utente non trovato';
      }
    });
  }

}
