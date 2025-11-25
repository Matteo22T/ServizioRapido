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

  login() {

    this.authService.login(this.email, this.password).subscribe({
      next: (user: any) => {
        // Salviamo l'utente nel browser
        localStorage.setItem('currentUser', JSON.stringify(user));

        if (user.ruolo === 'PROFESSIONISTA') {
          // this.router.navigate(['/dashboard-pro']); (quando l'avrai creata)
          this.router.navigate(['/']); // Per ora Home
        } else {
          // this.router.navigate(['/dashboard-cliente']); (quando l'avrai creata)
          this.router.navigate(['/']); // Per ora Home
        }

        alert('Benvenuto ' + user.nome);
      },
      error: () => this.errore = 'Credenziali errate o utente non trovato'
    });
  }
}
