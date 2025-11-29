import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AutenticazioneService } from '../../service/autenticazione-service';
import { RouterLink, Router } from '@angular/router';
import { ChangeDetectorRef } from '@angular/core';

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

  mostraToast = false;
  isErrore = false;


  constructor(private authService: AutenticazioneService, private router: Router, private cd: ChangeDetectorRef) {}

  inviaRichiesta() {
    this.authService.richiediReset(this.email).subscribe({
      next: (res  ) => {
        this.messaggio = res;
        this.isErrore = false;
        this.mostraToast = true;
        this.cd.detectChanges();

        setTimeout(() => {
          this.mostraToast = false;
          this.router.navigate(['/login']);
        }, 3000);
      },
      error: (err) => {
        console.error(err);
        this.messaggio = 'Errore di connessione. Riprova più tardi.';
        this.isErrore = true;
        this.mostraToast = true;

        this.cd.detectChanges();

        setTimeout(() => this.mostraToast = false, 3000);
      }
    });
  }
}
