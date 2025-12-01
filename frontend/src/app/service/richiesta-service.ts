import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class RichiestaService {

  private url = "http://localhost:8080/api/richieste";

  constructor(private http: HttpClient) {}

  pubblica(dettagli: string, indirizzo: string, categoria: string, idCliente: number): Observable<any>{
    const body = {
      dettagli: dettagli,
      indirizzo: indirizzo,
      categoria: categoria, // Deve essere una stringa MAIUSCOLA come l'Enum Java (es. "IDRAULICO")
      clientePubblicante: {
        idUtente: idCliente
      }
    };

    console.log("Invio richiesta:", body); // Debug utile
    return this.http.post(this.url, body);
  }

  getMie(idCliente: number): Observable<any> {
    return this.http.get(`${this.url}/mie/${idCliente}`)
  }

  getAperte(idProfessionista: number): Observable<any>{
    return this.http.get(`${this.url}/compatibili/${idProfessionista}`);
  }

  elimina(idRichiesta: number): Observable<any>{
    return this.http.delete(`${this.url}/${idRichiesta}`);
  }


}
