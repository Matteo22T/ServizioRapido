import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.html',
  styleUrls: ['./register.scss']
})
export class RegisterComponent {
  
  tipoProfilo: string = 'CLIENTE'; // Default
  utente: any = {
    nome: '', cognome: '', email: '', password: '', 
    indirizzo: '', specializzazione: '', biografia: ''
  };
  messaggio = '';

  constructor(private http: HttpClient, private router: Router) {}

  registrati() {
    let url = '';
    // Decidiamo l'endpoint in base al tipo scelto
    if (this.tipoProfilo === 'CLIENTE') {
      url = 'http://localhost:8080/api/auth/register/cliente';
    } else {
      url = 'http://localhost:8080/api/auth/register/professionista';
    }

    this.http.post(url, this.utente).subscribe({
      next: (res) => {
        alert('Registrazione avvenuta con successo!');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error(err);
        this.messaggio = 'Errore durante la registrazione (Email già usata?)';
      }
    });
  }
}