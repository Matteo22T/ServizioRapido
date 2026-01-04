import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AutenticazioneService {
  private urlRegister = 'http://localhost:8080/api/auth/register';

  private urlLogin = 'http://localhost:8080/api/auth/login';

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<any> {
    const body = { email, password };
    return this.http.post(this.urlLogin, body);
  }

  register(datiRegistrazione: any): Observable<any> {
    return this.http.post(`${this.urlRegister}`, datiRegistrazione, { responseType: 'text' });
  }



  richiediReset(email: string): Observable<any> {
    return this.http.post(`${this.urlLogin}/recupero-password`, { email }, { responseType: 'text' });
  }

  eseguiReset(token: string, password: string): Observable<any> {
    return this.http.post(`${this.urlLogin}/reset-password`, { token, password }, { responseType: 'text' });
  }
}
