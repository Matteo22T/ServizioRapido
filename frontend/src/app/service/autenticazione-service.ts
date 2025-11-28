import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
// Assicurati che i percorsi di importazione siano corretti
import { Cliente } from '../model/Cliente.model';
import { Professionista } from '../model/Professionista.model';

@Injectable({
  providedIn: 'root',
})
export class AutenticazioneService {
  // L'URL base del tuo controller Java
  private urlRegister = 'http://localhost:8080/api/auth/register';

  private urlLogin = 'http://localhost:8080/api/auth/login';

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<any> {
    const body = { email, password };
    return this.http.post(this.urlLogin, body);
  }

  // Qui specifichiamo che ci aspettiamo un oggetto di tipo Cliente
  registerCliente(cliente: Cliente): Observable<any> {
    return this.http.post(`${this.urlRegister}/cliente`, cliente);
  }

  // Qui specifichiamo che ci aspettiamo un oggetto di tipo Professionista
  registerProfessionista(professionista: Professionista): Observable<any> {
    return this.http.post(`${this.urlRegister}/professionista`, professionista);
  }

  richiediReset(email: string): Observable<any> {
    return this.http.post(`${this.urlLogin}/recupero-password`, { email }, { responseType: 'text' });
  }

  eseguiReset(token: string, password: string): Observable<any> {
    return this.http.post(`${this.urlLogin}/reset-password`, { token, password });
  }
}
