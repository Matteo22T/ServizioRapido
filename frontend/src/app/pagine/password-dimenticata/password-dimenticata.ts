import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AutenticazioneService } from '../../service/autenticazione-service';
import { RouterLink, Router } from '@angular/router'; // IMPORTANTE

@Component({
  selector: 'app-password-dimenticata',
  standalone: true,
  imports: [FormsModule, RouterLink], // Aggiungi RouterLink qui
  templateUrl: './password-dimenticata.html',
  styleUrls: ['./password-dimenticata.css'] // Riutilizza lo stile del login o creane uno
})
export class PasswordDimenticata {
  email = '';
  messaggio = '';


  constructor(private authService: AutenticazioneService, private router: Router) {}

  inviaRichiesta() {
    this.authService.richiediReset(this.email).subscribe({
      next: () => {
        this.messaggio = 'Se l\'email esiste, riceverai un link a breve. Reindirizzamento al login...';

        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 3000);
      },
      error: (err) => {
        console.error(err);
        this.messaggio = 'Errore di connessione. Riprova più tardi.';
      }
    });
  }
}
