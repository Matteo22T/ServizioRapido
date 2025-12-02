import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ProposteService {
  private url = "http://localhost:8080/api/proposte"

  constructor(private http: HttpClient) { }

  pubblica(dettagli: string, prezzo: number, idRichiesta: number, idProfessionista: number): Observable<any>{
    const body = {
      dettagli,
      prezzo,
      richiestaRiferimento: { idRichiesta: idRichiesta },
      professionistaMittente: { idUtente: idProfessionista }
    }
    return this.http.post(this.url, body);

  }

  vediMie(idProfessionista: number): Observable<any>{
    return this.http.get(`${this.url}/professionista/${idProfessionista}`);
  }

  vediProposte(idRichiesta: number): Observable<any>{
    return this.http.get(`${this.url}/richiesta/${idRichiesta}`);
  }

  elimina(idProposta: number): Observable<any>{
    return this.http.delete(`${this.url}/eliminate/${idProposta}`,
      {responseType: 'text'});
  }

  modifica(idProposta: number, dettagli: string, prezzo: number): Observable<any>{
    const body = {
      dettagli,
      prezzo
    }
    return this.http.put(`${this.url}/modifica/${idProposta}`, body);
  }

  accetta(idProposta: number, idRichiesta: number): Observable<any>{
    const body = {
      idProposta,
      idRichiesta
    }
    return this.http.put(`${this.url}/accetta/${idProposta}/${idRichiesta}`, body);
  }

  rifiuta(idProposta: number, idRichiesta: number): Observable<any>{
    const body = {
      idProposta,
      idRichiesta
    }
    return this.http.put(`${this.url}/rifiuta/${idProposta}`, body);
  }




}
