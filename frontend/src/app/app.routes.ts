import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home';
import { LoginComponent } from './components/login/login';
import { RegisterComponent } from './components/register/register';
import { DashboardCliente } from './components/dashboard-cliente/dashboard-cliente';
import { PasswordDimenticata } from './components/password-dimenticata/password-dimenticata'
import {ResetPassword} from './components/reset-password/reset-password';
import { DashboardProfessionista} from './components/dashboard-professionista/dashboard-professionista';

export const routes: Routes = [
  { path: '', component: HomeComponent },

  { path: 'login', component: LoginComponent },

  { path: 'register', component: RegisterComponent },

  { path: 'dashboard-cliente', component: DashboardCliente},

  { path: 'dashboard-professionista', component: DashboardProfessionista},

  { path: 'password-dimenticata', component: PasswordDimenticata },
  { path: 'reset-password', component: ResetPassword },
];
