-- ==========================================
-- 1. UTENTI E PROFILI
-- Password per tutti: Password123!
-- ==========================================

-- CLIENTE 1: Mario Rossi (ID 1)
INSERT INTO utente (nome, cognome, email, password, telefono)
VALUES ('Mario', 'Rossi', 'mario@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOpNa.hBAwFFa37Zg.r.1/j/y/2.u/y/2', '3330000001');
INSERT INTO cliente (id_utente, indirizzo) VALUES (1, 'Via Roma 10, Cosenza');

-- PROFESSIONISTA 1: Luigi Verdi - Idraulico (ID 2)
INSERT INTO utente (nome, cognome, email, password, telefono)
VALUES ('Luigi', 'Verdi', 'luigi@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOpNa.hBAwFFa37Zg.r.1/j/y/2.u/y/2', '3330000002');
INSERT INTO professionista (id_utente, biografia, specializzazione)
VALUES (2, 'Esperto in tubature e impianti idraulici da 20 anni.', 'IDRAULICO');

-- PROFESSIONISTA 2: Giovanni Bianchi - Elettricista (ID 3)
INSERT INTO utente (nome, cognome, email, password, telefono)
VALUES ('Giovanni', 'Bianchi', 'giovanni@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOpNa.hBAwFFa37Zg.r.1/j/y/2.u/y/2', '3330000003');
INSERT INTO professionista (id_utente, biografia, specializzazione)
VALUES (3, 'Impianti elettrici civili e industriali.', 'ELETTRICISTA');

-- CLIENTE 2: Anna Neri (ID 4)
INSERT INTO utente (nome, cognome, email, password, telefono)
VALUES ('Anna', 'Neri', 'anna@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOpNa.hBAwFFa37Zg.r.1/j/y/2.u/y/2', '3330000004');
INSERT INTO cliente (id_utente, indirizzo) VALUES (4, 'Corso Mazzini 50, Cosenza');

-- CLIENTE 3: Paolo Gialli (ID 5)
INSERT INTO utente (nome, cognome, email, password, telefono)
VALUES ('Paolo', 'Gialli', 'paolo@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOpNa.hBAwFFa37Zg.r.1/j/y/2.u/y/2', '3330000005');
INSERT INTO cliente (id_utente, indirizzo) VALUES (5, 'Viale Parco 12, Rende');

-- PROFESSIONISTA 3: Marco Viola - Idraulico (ID 6)
-- (Serve per creare concorrenza sulle richieste idrauliche)
INSERT INTO utente (nome, cognome, email, password, telefono)
VALUES ('Marco', 'Viola', 'marco@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOpNa.hBAwFFa37Zg.r.1/j/y/2.u/y/2', '3330000006');
INSERT INTO professionista (id_utente, biografia, specializzazione)
VALUES (6, 'Idraulico economico e veloce, disponibile h24.', 'IDRAULICO');

-- PROFESSIONISTA 4: Giuseppe Ferro - Fabbro (ID 7)
INSERT INTO utente (nome, cognome, email, password, telefono)
VALUES ('Giuseppe', 'Ferro', 'giuseppe@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOpNa.hBAwFFa37Zg.r.1/j/y/2.u/y/2', '3330000007');
INSERT INTO professionista (id_utente, biografia, specializzazione)
VALUES (7, 'Apertura porte blindate e cambio serrature.', 'FABBRO');


-- ==========================================
-- 2. RICHIESTE DI SERVIZIO
-- ==========================================

-- Richiesta 1: IDRAULICO (Mario) - APERTA (Disponibile per Luigi e Marco)
INSERT INTO richiesta_servizio (id_richiesta, categoria, dettagli, indirizzo, stato_richiesta, id_cliente)
VALUES (1, 'IDRAULICO', 'Il rubinetto della cucina perde acqua continuamente.', 'Via Roma 10, Cosenza', 'APERTA', 1);

-- Richiesta 2: ELETTRICISTA (Mario) - APERTA (Disponibile per Giovanni)
INSERT INTO richiesta_servizio (id_richiesta, categoria, dettagli, indirizzo, stato_richiesta, id_cliente)
VALUES (2, 'ELETTRICISTA', 'Installazione nuovo lampadario in salotto.', 'Via Roma 10, Cosenza', 'APERTA', 1);

-- Richiesta 3: FABBRO (Anna) - APERTA (Disponibile per Giuseppe)
INSERT INTO richiesta_servizio (id_richiesta, categoria, dettagli, indirizzo, stato_richiesta, id_cliente)
VALUES (3, 'FABBRO', 'La serratura della porta blindata si blocca.', 'Corso Mazzini 50, Cosenza', 'APERTA', 4);

-- Richiesta 4: IDRAULICO (Paolo) - IN_LAVORAZIONE (Già assegnata)
INSERT INTO richiesta_servizio (id_richiesta, categoria, dettagli, indirizzo, stato_richiesta, id_cliente)
VALUES (4, 'IDRAULICO', 'Sostituzione completa box doccia.', 'Viale Parco 12, Rende', 'IN_LAVORAZIONE', 5);

-- Richiesta 5: ELETTRICISTA (Paolo) - COMPLETATA
INSERT INTO richiesta_servizio (id_richiesta, categoria, dettagli, indirizzo, stato_richiesta, id_cliente)
VALUES (5, 'ELETTRICISTA', 'Riparazione presa elettrica bruciata.', 'Viale Parco 12, Rende', 'COMPLETATA', 5);

-- Richiesta 6: IDRAULICO (Anna) - APERTA (Nessuna proposta ancora)
INSERT INTO richiesta_servizio (id_richiesta, categoria, dettagli, indirizzo, stato_richiesta, id_cliente)
VALUES (6, 'IDRAULICO', 'Perdita dal sifone del lavandino.', 'Corso Mazzini 50, Cosenza', 'APERTA', 4);


-- ==========================================
-- 3. PROPOSTE DI SERVIZIO
-- ==========================================

-- Proposta su Richiesta 1 (Mario): Luigi si propone
INSERT INTO proposta_servizio (id_proposta, dettagli, prezzo, stato_proposta, id_professionista, id_richiesta)
VALUES (1, 'Posso venire domani mattina. Il prezzo include la chiamata.', 50.00, 'INVIATA', 2, 1);

-- Proposta su Richiesta 3 (Anna): Giuseppe il Fabbro si propone
INSERT INTO proposta_servizio (id_proposta, dettagli, prezzo, stato_proposta, id_professionista, id_richiesta)
VALUES (2, 'Cambio cilindro europeo incluso nel prezzo.', 120.00, 'INVIATA', 7, 3);

-- Proposta VINCENTE su Richiesta 4 (Paolo): Luigi
INSERT INTO proposta_servizio (id_proposta, dettagli, prezzo, stato_proposta, id_professionista, id_richiesta)
VALUES (3, 'Preventivo per box doccia standard, manodopera inclusa.', 300.00, 'ACCETTATA', 2, 4);

-- Proposta PERDENTE su Richiesta 4 (Paolo): Marco
INSERT INTO proposta_servizio (id_proposta, dettagli, prezzo, stato_proposta, id_professionista, id_richiesta)
VALUES (4, 'Installazione box doccia di lusso con finiture cromate.', 450.00, 'RIFIUTATA', 6, 4);

-- Proposta VINCENTE su Richiesta 5 (Paolo): Giovanni
INSERT INTO proposta_servizio (id_proposta, dettagli, prezzo, stato_proposta, id_professionista, id_richiesta)
VALUES (5, 'Riparazione effettuata in data odierna.', 40.00, 'ACCETTATA', 3, 5);


-- ==========================================
-- 4. AGGIORNAMENTO RELAZIONI (Richiesta -> Proposta Accettata)
-- ==========================================

-- Richiesta 4 -> Proposta 3
UPDATE richiesta_servizio SET id_proposta_accettata = 3 WHERE id_richiesta = 4;

-- Richiesta 5 -> Proposta 5
UPDATE richiesta_servizio SET id_proposta_accettata = 5 WHERE id_richiesta = 5;


-- ==========================================
-- 5. NOTIFICHE
-- ==========================================

-- NOTIFICHE AI CLIENTI (Nuove proposte)
-- Mario riceve notifica da Luigi
INSERT INTO notifica (messaggio, stato_invio, destinatario)
VALUES ('Nuova proposta ricevuta per la Richiesta: Il rubinetto della cucina perde acqua continuamente.', 'INVIATO', 1);

-- Anna riceve notifica da Giuseppe Fabbro
INSERT INTO notifica (messaggio, stato_invio, destinatario)
VALUES ('Nuova proposta ricevuta per la Richiesta: La serratura della porta blindata si blocca.', 'INVIATO', 4);

-- NOTIFICHE AI CLIENTI (Proposte Accettate - Manuali)
-- Paolo riceve conferma di aver accettato Luigi
INSERT INTO notifica (messaggio, stato_invio, destinatario)
VALUES ('Hai accettato la proposta di Luigi Verdi per: Sostituzione completa box doccia.', 'INVIATO', 5);

-- NOTIFICHE AI PROFESSIONISTI (Esito proposte)
-- Luigi riceve conferma accettazione
INSERT INTO notifica (messaggio, stato_invio, destinatario)
VALUES ('La tua proposta per ''Sostituzione completa box doccia'' è stata ACCETTATA!', 'INVIATO', 2);

-- Marco riceve rifiuto
INSERT INTO notifica (messaggio, stato_invio, destinatario)
VALUES ('La tua proposta per ''Sostituzione completa box doccia'' è stata scartata.', 'INVIATO', 6);