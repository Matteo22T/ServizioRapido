import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Notifica } from '../model/Notifica.model';

@Injectable({
  providedIn: 'root'
})
export class NotificaService {
  private apiUrl = 'http://localhost:8080/api/notifiche';
  constructor(private http: HttpClient) { }

  getNotifichePerUtente(idUtente: number): Observable<Notifica[]> {
    return this.http.get<Notifica[]>(`${this.apiUrl}/utente/${idUtente}`);
  }

  eliminaNotifica(idNotifica: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${idNotifica}`, { responseType: 'text' });
  }
}
