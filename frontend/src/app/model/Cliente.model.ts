import { Utente } from './Utente.model';

export class Cliente extends Utente {
  constructor(
    id: number,
    nome: string,
    cognome: string,
    email: string,
    password: string,
    public indirizzo?: string,) {
    super(id, nome, cognome, email, password);
  }
}
