import { Routes } from '@angular/router';
// Importiamo i componenti delle pagine che hai creato
import { HomeComponent } from './components/home/home';
import { LoginComponent } from './components/login/login';
import { RegisterComponent } from './components/register/register';
import { DashboardCliente } from './components/dashboard-cliente/dashboard-cliente';
import { PasswordDimenticata } from './components/password-dimenticata/password-dimenticata'
import {ResetPassword} from './components/reset-password/reset-password';
import { DashboardProfessionista} from './components/dasboard-professionista/dashboard-professionista';

export const routes: Routes = [
  // Quando l'URL è vuoto (http://localhost:4200/), mostra la Home
  { path: '', component: HomeComponent },

  // Quando l'URL è /login, mostra la pagina di Login
  { path: 'login', component: LoginComponent },

  // Quando l'URL è /register, mostra la pagina di Registrazione
  { path: 'register', component: RegisterComponent },

  { path: 'dashboard-cliente', component: DashboardCliente},

  { path: 'dashboard-professionista', component: DashboardProfessionista},

  { path: 'password-dimenticata', component: PasswordDimenticata },
  { path: 'reset-password', component: ResetPassword },
];
