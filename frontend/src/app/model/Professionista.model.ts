import { Utente } from './Utente.model';

export class Professionista extends Utente {
  constructor(
    id: number,
    nome: string,
    cognome: string,
    email: string,
    password: string,
    public partitaIva?: string,
    public specializzazione?: string,
  ) {
    super(id, nome, cognome, email, password);
  }
}
