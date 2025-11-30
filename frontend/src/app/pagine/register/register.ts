// src/app/pagine/register/register.ts
import { Component } from '@angular/core';
import { Router , RouterLink} from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AutenticazioneService } from '../../service/autenticazione-service';
import {Cliente} from '../../model/Cliente.model';
import {Professionista} from '../../model/Professionista.model';


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
    nome: '', cognome: '', email: '', password: '',
    indirizzo: '', specializzazione: '', biografia: ''
  };

  messaggio = '';


  constructor(
    private authService: AutenticazioneService,
    private router: Router
  ) {}

  registrati() {
    // 1. Preparo la variabile per la chiamata
    let call;

    // 2. Creo l'oggetto specifico in base al profilo scelto
    if (this.tipoProfilo === 'CLIENTE') {

      // Creo un oggetto che ha SOLO i campi del Cliente
      const nuovoCliente = new Cliente(
        0, // ID fittizio (ci pensa il DB)
        this.form.nome,
        this.form.cognome,
        this.form.email,
        this.form.password,
        this.form.indirizzo
      );

      // Passo l'oggetto pulito
      call = this.authService.registerCliente(nuovoCliente);

    } else {

      // Creo un oggetto che ha SOLO i campi del Professionista
      const nuovoProfessionista = new Professionista(
        0, // ID fittizio
        this.form.nome,
        this.form.cognome,
        this.form.email,
        this.form.password,
        this.form.biografia,
        this.form.specializzazione
      );

      // Passo l'oggetto pulito
      call = this.authService.registerProfessionista(nuovoProfessionista);
    }

    // 3. Eseguo la chiamata (uguale a prima)
    call.subscribe({
      next: () => {
        alert('Registrazione avvenuta con successo!');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error(err);
        this.messaggio = 'Errore durante la registrazione. Controlla i dati.';
      }
    });
  }
}
