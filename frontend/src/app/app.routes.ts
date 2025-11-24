import { Routes } from '@angular/router';
// Importiamo i componenti delle pagine che hai creato
import { HomeComponent } from './pagine/home/home';
import { LoginComponent } from './pagine/login/login';
import { RegisterComponent } from './pagine/register/register';

export const routes: Routes = [
  // Quando l'URL è vuoto (http://localhost:4200/), mostra la Home
  { path: '', component: HomeComponent },
  
  // Quando l'URL è /login, mostra la pagina di Login
  { path: 'login', component: LoginComponent },
  
  // Quando l'URL è /register, mostra la pagina di Registrazione
  { path: 'register', component: RegisterComponent },

  // (Opzionale) Se l'utente scrive un indirizzo sbagliato, riportalo alla Home
  { path: '**', redirectTo: '' }
];