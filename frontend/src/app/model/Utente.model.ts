export class Utente {
  constructor(
    public id: number,
    public nome: string,
    public cognome: string,
    public email: string,
    public password: string,
    public telefono?: string,
  ) {
  }
}
