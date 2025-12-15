// src/app/pagine/register/register.ts
import { Component } from '@angular/core';
import { Router , RouterLink} from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AutenticazioneService } from '../../service/autenticazione-service';



@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrls: ['./register.css']
})
export class RegisterComponent {
  tipoProfilo: string = 'CLIENTE';
  form: any = {
    nome: '',
    cognome: '',
    email: '',
    password: '',
    telefono: '', // Aggiunto se presente nel DTO backend
    indirizzo: '',
    specializzazione: '',
    biografia: ''
  };

  messaggio = '';


  constructor(
    private authService: AutenticazioneService,
    private router: Router
  ) {}

  registrati() {
    if (!this.form.email || !this.form.password) {
      this.messaggio = 'Email e Password sono obbligatorie.';
      return;
    }

    const registerDTO = {
      ...this.form,
      tipoProfilo: this.tipoProfilo
    };

    this.authService.register(registerDTO).subscribe({
      next: () => {
        alert('Registrazione avvenuta con successo!');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error(err);
        // Gestione errore più dettagliata se il backend manda messaggi specifici
        this.messaggio = err.error || 'Errore durante la registrazione. Controlla i dati.';
      }
    });
  }
}
