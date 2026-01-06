IMPLEMENTAZIONE PATTERN:

FACTORY METHOD:
UtenteFactory è la fabbrica astratta
ClienteFactory e ProfessionistaFactory sono le fabbriche concrete che creeranno l'Utente (Professionista o Cliente)

OBSERVER:
Realizzato per la gestione delle notifiche

<img width="860" height="369" alt="Screenshot 2025-12-15 alle 15 26 16" src="https://github.com/user-attachments/assets/f14707bf-aaae-4e09-b8b6-f32886b586ec" />

La classe NotificaObserver è l'implementazione della classe astratta Observer che contiene i metodi 'update'
La classe Subject contiene i metodi attach, detach e notify.
Le classi PropostaServizioService e RichiestaServizioService sono i ConcreteSubjects che andranno ad utilizzare il metodo notify quando bisognerà inviare una notifica al destinatario dovuto.
(precisamente nei metodi pubblica, modifica, elimina, accetta, rifiuta, della classe PropostaServizioService e nel metodo annulla di RichiestaServizioService) 




Istruzioni per l'Avvio
1. Configurazione Database
Il progetto utilizza PostgreSQL. Prima di avviare l'applicazione, assicurarsi di aver creato un database vuoto nominato ServizioRapido.
Le credenziali di accesso al DB sono configurate in src/main/resources/application.properties.

L'applicazione è configurata con spring.jpa.hibernate.ddl-auto=create, in modo da essere testata con più facilità. Ad ogni avvio, il database viene automaticamente svuotato, ricreato e popolato con i dati di test presenti nel file data.sql.
Se non si vogliono perdere i dati ad ogni avvio, sostituire "create" con "update" ed eliminare il file data.sql, è presente un file dump del db  


