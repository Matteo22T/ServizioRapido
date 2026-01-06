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
Se non si vogliono perdere i dati ad ogni avvio, sostituire "create" con "update" ed eliminare il file data.sql, sono presenti inoltre 2 file dump del db, uno utilizzabile su dbeaver, l'altro è invece un file sql "dump-ServizioRapido-202601061346.sql"

(Dati di Test)
Il sistema viene inizializzato con diversi profili pre-configurati per simulare vari scenari d'uso.

🔑 PASSWORD PER TUTTI GLI UTENTI: Password123!

👤 Clienti

Mario Rossi
Email: mario@example.com

Anna Neri
Email: anna@example.com

Paolo Gialli
Email: paolo@example.com

🛠 Professionisti

Luigi Verdi
Email: luigi@example.com
Specializzazione: IDRAULICO

Giovanni Bianchi
Email: giovanni@example.com
Specializzazione: ELETTRICISTA

Marco Viola
Email: marco@example.com
Specializzazione: IDRAULICO

Giuseppe Ferro
Email: giuseppe@example.com
Specializzazione: FABBRO




