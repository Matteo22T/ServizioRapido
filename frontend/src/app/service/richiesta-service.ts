import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class RichiestaService {

  private url = "http://localhost:8080/api/richieste";

  constructor(private http: HttpClient) {}

  pubblica(dettagli: string, idCliente: number): Observable<any>{
    const body = {
      dettagli,
      clientePubblicante: {
        idUtente: idCliente
      }
    };
    return this.http.post(this.url, body);
  }

  getMie(idCliente: number): Observable<any> {
    return this.http.get(`${this.url}/mie/${idCliente}`)
  }

  getAperte(): Observable<any>{
    return this.http.get(`${this.url}/aperte`);
  }

  elimina(idRichiesta: number): Observable<any>{
    return this.http.delete(`${this.url}/${idRichiesta}`);
  }


}
