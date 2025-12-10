export class Notifica{
  constructor(
    public idNotifica: number,
    public messaggio: string,
    public statoNotifica: string,
    public destinatarioId: number,
  ) {
  }
}
