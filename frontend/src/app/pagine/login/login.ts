import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

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

  constructor(private http: HttpClient, private router: Router) {}

  login() {
    this.http.post('http://localhost:8080/api/auth/login', {
      email: this.email,
      password: this.password
    }).subscribe({
      next: (user: any) => {
        // Salviamo l'utente nel browser
        localStorage.setItem('currentUser', JSON.stringify(user));

        // Reindirizzamento in base al ruolo (se hai creato le dashboard)
        // Per ora mandiamo alla home
        this.router.navigate(['/']);
        alert('Benvenuto ' + user.nome);
      },
      error: () => this.errore = 'Credenziali errate'
    });
  }
}
