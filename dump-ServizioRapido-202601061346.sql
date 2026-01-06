--
-- PostgreSQL database dump
--

\restrict iWgsGwWRsNUSP0pFgOxQ0YJkHvkd3WTKE4bg9x5uNg0j0R4NXKpiLyQPI7nzOf6

-- Dumped from database version 14.19 (Homebrew)
-- Dumped by pg_dump version 14.19 (Homebrew)

-- Started on 2026-01-06 13:46:27 CET

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 846 (class 1247 OID 18016)
-- Name: categoriarichiesta; Type: TYPE; Schema: public; Owner: matteotocci
--

CREATE TYPE public.categoriarichiesta AS ENUM (
    'CARTONGESSISTA',
    'DISINFESTAZIONE',
    'ELETTRICISTA',
    'FABBRO',
    'FALEGNAME',
    'GIARDINAGGIO',
    'IDRAULICO',
    'IMBIANCHINO',
    'INFORMATICA',
    'MONTAGGIO_MOBILI',
    'MURATORE',
    'PAVIMENTISTA',
    'PULIZIE',
    'SGOMBERO',
    'TRASLOCHI'
);


ALTER TYPE public.categoriarichiesta OWNER TO matteotocci;

--
-- TOC entry 837 (class 1247 OID 16404)
-- Name: ruolo_utente; Type: TYPE; Schema: public; Owner: matteotocci
--

CREATE TYPE public.ruolo_utente AS ENUM (
    'CLIENTE',
    'PROFESSIONISTA'
);


ALTER TYPE public.ruolo_utente OWNER TO matteotocci;

--
-- TOC entry 840 (class 1247 OID 16443)
-- Name: stato_invio; Type: TYPE; Schema: public; Owner: matteotocci
--

CREATE TYPE public.stato_invio AS ENUM (
    'INVIATA',
    'FALLITA',
    'DA_INVIARE'
);


ALTER TYPE public.stato_invio OWNER TO matteotocci;

--
-- TOC entry 834 (class 1247 OID 16396)
-- Name: stato_proposta; Type: TYPE; Schema: public; Owner: matteotocci
--

CREATE TYPE public.stato_proposta AS ENUM (
    'INVIATA',
    'ACCETTATA',
    'RIFIUTATA'
);


ALTER TYPE public.stato_proposta OWNER TO matteotocci;

--
-- TOC entry 831 (class 1247 OID 16386)
-- Name: stato_richiesta; Type: TYPE; Schema: public; Owner: matteotocci
--

CREATE TYPE public.stato_richiesta AS ENUM (
    'APERTA',
    'IN_LAVORAZIONE',
    'COMPLETATA',
    'ANNULLATA'
);


ALTER TYPE public.stato_richiesta OWNER TO matteotocci;

--
-- TOC entry 843 (class 1247 OID 18007)
-- Name: statoproposta; Type: TYPE; Schema: public; Owner: matteotocci
--

CREATE TYPE public.statoproposta AS ENUM (
    'ACCETTATA',
    'INVIATA',
    'RIFIUTATA'
);


ALTER TYPE public.statoproposta OWNER TO matteotocci;

--
-- TOC entry 849 (class 1247 OID 18050)
-- Name: statorichiesta; Type: TYPE; Schema: public; Owner: matteotocci
--

CREATE TYPE public.statorichiesta AS ENUM (
    'ANNULLATA',
    'APERTA',
    'COMPLETATA',
    'IN_LAVORAZIONE'
);


ALTER TYPE public.statorichiesta OWNER TO matteotocci;

--
-- TOC entry 3683 (class 2605 OID 18048)
-- Name: CAST (public.categoriarichiesta AS character varying); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (public.categoriarichiesta AS character varying) WITH INOUT AS IMPLICIT;


--
-- TOC entry 3682 (class 2605 OID 18014)
-- Name: CAST (public.statoproposta AS character varying); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (public.statoproposta AS character varying) WITH INOUT AS IMPLICIT;


--
-- TOC entry 3684 (class 2605 OID 18060)
-- Name: CAST (public.statorichiesta AS character varying); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (public.statorichiesta AS character varying) WITH INOUT AS IMPLICIT;


--
-- TOC entry 3601 (class 2605 OID 18047)
-- Name: CAST (character varying AS public.categoriarichiesta); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (character varying AS public.categoriarichiesta) WITH INOUT AS IMPLICIT;


--
-- TOC entry 3600 (class 2605 OID 18013)
-- Name: CAST (character varying AS public.statoproposta); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (character varying AS public.statoproposta) WITH INOUT AS IMPLICIT;


--
-- TOC entry 3602 (class 2605 OID 18059)
-- Name: CAST (character varying AS public.statorichiesta); Type: CAST; Schema: -; Owner: -
--

CREATE CAST (character varying AS public.statorichiesta) WITH INOUT AS IMPLICIT;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 209 (class 1259 OID 18061)
-- Name: cliente; Type: TABLE; Schema: public; Owner: matteotocci
--

CREATE TABLE public.cliente (
    id_utente bigint NOT NULL,
    indirizzo character varying(255)
);


ALTER TABLE public.cliente OWNER TO matteotocci;

--
-- TOC entry 211 (class 1259 OID 18067)
-- Name: notifica; Type: TABLE; Schema: public; Owner: matteotocci
--

CREATE TABLE public.notifica (
    destinatario bigint NOT NULL,
    id_notifica bigint NOT NULL,
    messaggio text NOT NULL,
    stato_invio character varying(255),
    CONSTRAINT notifica_stato_invio_check CHECK (((stato_invio)::text = ANY ((ARRAY['DA_INVIARE'::character varying, 'INVIATO'::character varying, 'FALLITO'::character varying])::text[])))
);


ALTER TABLE public.notifica OWNER TO matteotocci;

--
-- TOC entry 210 (class 1259 OID 18066)
-- Name: notifica_id_notifica_seq; Type: SEQUENCE; Schema: public; Owner: matteotocci
--

ALTER TABLE public.notifica ALTER COLUMN id_notifica ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.notifica_id_notifica_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 212 (class 1259 OID 18075)
-- Name: professionista; Type: TABLE; Schema: public; Owner: matteotocci
--

CREATE TABLE public.professionista (
    id_utente bigint NOT NULL,
    biografia character varying(255),
    specializzazione character varying(255),
    CONSTRAINT professionista_specializzazione_check CHECK (((specializzazione)::text = ANY ((ARRAY['IDRAULICO'::character varying, 'ELETTRICISTA'::character varying, 'FABBRO'::character varying, 'IMBIANCHINO'::character varying, 'MURATORE'::character varying, 'CARTONGESSISTA'::character varying, 'PAVIMENTISTA'::character varying, 'FALEGNAME'::character varying, 'PULIZIE'::character varying, 'DISINFESTAZIONE'::character varying, 'GIARDINAGGIO'::character varying, 'MONTAGGIO_MOBILI'::character varying, 'TRASLOCHI'::character varying, 'SGOMBERO'::character varying, 'INFORMATICA'::character varying])::text[])))
);


ALTER TABLE public.professionista OWNER TO matteotocci;

--
-- TOC entry 214 (class 1259 OID 18084)
-- Name: proposta_servizio; Type: TABLE; Schema: public; Owner: matteotocci
--

CREATE TABLE public.proposta_servizio (
    prezzo numeric(38,2),
    id_professionista bigint NOT NULL,
    id_proposta bigint NOT NULL,
    id_richiesta bigint NOT NULL,
    dettagli text,
    stato_proposta public.statoproposta
);


ALTER TABLE public.proposta_servizio OWNER TO matteotocci;

--
-- TOC entry 213 (class 1259 OID 18083)
-- Name: proposta_servizio_id_proposta_seq; Type: SEQUENCE; Schema: public; Owner: matteotocci
--

ALTER TABLE public.proposta_servizio ALTER COLUMN id_proposta ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.proposta_servizio_id_proposta_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 216 (class 1259 OID 18092)
-- Name: richiesta_servizio; Type: TABLE; Schema: public; Owner: matteotocci
--

CREATE TABLE public.richiesta_servizio (
    id_cliente bigint NOT NULL,
    id_proposta_accettata bigint,
    id_richiesta bigint NOT NULL,
    dettagli text,
    indirizzo character varying(255),
    categoria public.categoriarichiesta,
    stato_richiesta public.statorichiesta
);


ALTER TABLE public.richiesta_servizio OWNER TO matteotocci;

--
-- TOC entry 215 (class 1259 OID 18091)
-- Name: richiesta_servizio_id_richiesta_seq; Type: SEQUENCE; Schema: public; Owner: matteotocci
--

ALTER TABLE public.richiesta_servizio ALTER COLUMN id_richiesta ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.richiesta_servizio_id_richiesta_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 218 (class 1259 OID 18102)
-- Name: utente; Type: TABLE; Schema: public; Owner: matteotocci
--

CREATE TABLE public.utente (
    id_utente bigint NOT NULL,
    reset_token_scadenza timestamp(6) without time zone,
    cognome character varying(255),
    email character varying(255),
    nome character varying(255),
    password character varying(255),
    reset_token character varying(255),
    telefono character varying(255)
);


ALTER TABLE public.utente OWNER TO matteotocci;

--
-- TOC entry 217 (class 1259 OID 18101)
-- Name: utente_id_utente_seq; Type: SEQUENCE; Schema: public; Owner: matteotocci
--

ALTER TABLE public.utente ALTER COLUMN id_utente ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.utente_id_utente_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 3848 (class 0 OID 18061)
-- Dependencies: 209
-- Data for Name: cliente; Type: TABLE DATA; Schema: public; Owner: matteotocci
--

COPY public.cliente (id_utente, indirizzo) FROM stdin;
1	Via Roma 10, Cosenza
4	Corso Mazzini 50, Cosenza
5	Viale Parco 12, Rende
\.


--
-- TOC entry 3850 (class 0 OID 18067)
-- Dependencies: 211
-- Data for Name: notifica; Type: TABLE DATA; Schema: public; Owner: matteotocci
--

COPY public.notifica (destinatario, id_notifica, messaggio, stato_invio) FROM stdin;
1	1	Nuova proposta ricevuta per la Richiesta: Il rubinetto della cucina perde acqua continuamente.	INVIATO
4	2	Nuova proposta ricevuta per la Richiesta: La serratura della porta blindata si blocca.	INVIATO
5	3	Hai accettato la proposta di Luigi Verdi per: Sostituzione completa box doccia.	INVIATO
2	4	La tua proposta per 'Sostituzione completa box doccia' è stata ACCETTATA!	INVIATO
6	5	La tua proposta per 'Sostituzione completa box doccia' è stata scartata.	INVIATO
\.


--
-- TOC entry 3851 (class 0 OID 18075)
-- Dependencies: 212
-- Data for Name: professionista; Type: TABLE DATA; Schema: public; Owner: matteotocci
--

COPY public.professionista (id_utente, biografia, specializzazione) FROM stdin;
2	Esperto in tubature e impianti idraulici da 20 anni.	IDRAULICO
3	Impianti elettrici civili e industriali.	ELETTRICISTA
6	Idraulico economico e veloce, disponibile h24.	IDRAULICO
7	Apertura porte blindate e cambio serrature.	FABBRO
\.


--
-- TOC entry 3853 (class 0 OID 18084)
-- Dependencies: 214
-- Data for Name: proposta_servizio; Type: TABLE DATA; Schema: public; Owner: matteotocci
--

COPY public.proposta_servizio (prezzo, id_professionista, id_proposta, id_richiesta, dettagli, stato_proposta) FROM stdin;
50.00	2	1	1	Posso venire domani mattina. Il prezzo include la chiamata.	INVIATA
120.00	7	2	3	Cambio cilindro europeo incluso nel prezzo.	INVIATA
300.00	2	3	4	Preventivo per box doccia standard, manodopera inclusa.	ACCETTATA
450.00	6	4	4	Installazione box doccia di lusso con finiture cromate.	RIFIUTATA
40.00	3	5	5	Riparazione effettuata in data odierna.	ACCETTATA
\.


--
-- TOC entry 3855 (class 0 OID 18092)
-- Dependencies: 216
-- Data for Name: richiesta_servizio; Type: TABLE DATA; Schema: public; Owner: matteotocci
--

COPY public.richiesta_servizio (id_cliente, id_proposta_accettata, id_richiesta, dettagli, indirizzo, categoria, stato_richiesta) FROM stdin;
1	\N	1	Il rubinetto della cucina perde acqua continuamente.	Via Roma 10, Cosenza	IDRAULICO	APERTA
1	\N	2	Installazione nuovo lampadario in salotto.	Via Roma 10, Cosenza	ELETTRICISTA	APERTA
4	\N	3	La serratura della porta blindata si blocca.	Corso Mazzini 50, Cosenza	FABBRO	APERTA
4	\N	6	Perdita dal sifone del lavandino.	Corso Mazzini 50, Cosenza	IDRAULICO	APERTA
5	3	4	Sostituzione completa box doccia.	Viale Parco 12, Rende	IDRAULICO	IN_LAVORAZIONE
5	5	5	Riparazione presa elettrica bruciata.	Viale Parco 12, Rende	ELETTRICISTA	COMPLETATA
\.


--
-- TOC entry 3857 (class 0 OID 18102)
-- Dependencies: 218
-- Data for Name: utente; Type: TABLE DATA; Schema: public; Owner: matteotocci
--

COPY public.utente (id_utente, reset_token_scadenza, cognome, email, nome, password, reset_token, telefono) FROM stdin;
1	\N	Rossi	mario@example.com	Mario	$2a$10$8.UnVuG9HHgffUDAlk8qfOpNa.hBAwFFa37Zg.r.1/j/y/2.u/y/2	\N	3330000001
2	\N	Verdi	luigi@example.com	Luigi	$2a$10$8.UnVuG9HHgffUDAlk8qfOpNa.hBAwFFa37Zg.r.1/j/y/2.u/y/2	\N	3330000002
3	\N	Bianchi	giovanni@example.com	Giovanni	$2a$10$8.UnVuG9HHgffUDAlk8qfOpNa.hBAwFFa37Zg.r.1/j/y/2.u/y/2	\N	3330000003
4	\N	Neri	anna@example.com	Anna	$2a$10$8.UnVuG9HHgffUDAlk8qfOpNa.hBAwFFa37Zg.r.1/j/y/2.u/y/2	\N	3330000004
5	\N	Gialli	paolo@example.com	Paolo	$2a$10$8.UnVuG9HHgffUDAlk8qfOpNa.hBAwFFa37Zg.r.1/j/y/2.u/y/2	\N	3330000005
6	\N	Viola	marco@example.com	Marco	$2a$10$8.UnVuG9HHgffUDAlk8qfOpNa.hBAwFFa37Zg.r.1/j/y/2.u/y/2	\N	3330000006
7	\N	Ferro	giuseppe@example.com	Giuseppe	$2a$10$8.UnVuG9HHgffUDAlk8qfOpNa.hBAwFFa37Zg.r.1/j/y/2.u/y/2	\N	3330000007
\.


--
-- TOC entry 3863 (class 0 OID 0)
-- Dependencies: 210
-- Name: notifica_id_notifica_seq; Type: SEQUENCE SET; Schema: public; Owner: matteotocci
--

SELECT pg_catalog.setval('public.notifica_id_notifica_seq', 5, true);


--
-- TOC entry 3864 (class 0 OID 0)
-- Dependencies: 213
-- Name: proposta_servizio_id_proposta_seq; Type: SEQUENCE SET; Schema: public; Owner: matteotocci
--

SELECT pg_catalog.setval('public.proposta_servizio_id_proposta_seq', 1, false);


--
-- TOC entry 3865 (class 0 OID 0)
-- Dependencies: 215
-- Name: richiesta_servizio_id_richiesta_seq; Type: SEQUENCE SET; Schema: public; Owner: matteotocci
--

SELECT pg_catalog.setval('public.richiesta_servizio_id_richiesta_seq', 1, false);


--
-- TOC entry 3866 (class 0 OID 0)
-- Dependencies: 217
-- Name: utente_id_utente_seq; Type: SEQUENCE SET; Schema: public; Owner: matteotocci
--

SELECT pg_catalog.setval('public.utente_id_utente_seq', 7, true);


--
-- TOC entry 3688 (class 2606 OID 18065)
-- Name: cliente cliente_pkey; Type: CONSTRAINT; Schema: public; Owner: matteotocci
--

ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT cliente_pkey PRIMARY KEY (id_utente);


--
-- TOC entry 3690 (class 2606 OID 18074)
-- Name: notifica notifica_pkey; Type: CONSTRAINT; Schema: public; Owner: matteotocci
--

ALTER TABLE ONLY public.notifica
    ADD CONSTRAINT notifica_pkey PRIMARY KEY (id_notifica);


--
-- TOC entry 3692 (class 2606 OID 18082)
-- Name: professionista professionista_pkey; Type: CONSTRAINT; Schema: public; Owner: matteotocci
--

ALTER TABLE ONLY public.professionista
    ADD CONSTRAINT professionista_pkey PRIMARY KEY (id_utente);


--
-- TOC entry 3694 (class 2606 OID 18090)
-- Name: proposta_servizio proposta_servizio_pkey; Type: CONSTRAINT; Schema: public; Owner: matteotocci
--

ALTER TABLE ONLY public.proposta_servizio
    ADD CONSTRAINT proposta_servizio_pkey PRIMARY KEY (id_proposta);


--
-- TOC entry 3696 (class 2606 OID 18100)
-- Name: richiesta_servizio richiesta_servizio_id_proposta_accettata_key; Type: CONSTRAINT; Schema: public; Owner: matteotocci
--

ALTER TABLE ONLY public.richiesta_servizio
    ADD CONSTRAINT richiesta_servizio_id_proposta_accettata_key UNIQUE (id_proposta_accettata);


--
-- TOC entry 3698 (class 2606 OID 18098)
-- Name: richiesta_servizio richiesta_servizio_pkey; Type: CONSTRAINT; Schema: public; Owner: matteotocci
--

ALTER TABLE ONLY public.richiesta_servizio
    ADD CONSTRAINT richiesta_servizio_pkey PRIMARY KEY (id_richiesta);


--
-- TOC entry 3700 (class 2606 OID 18110)
-- Name: utente utente_email_key; Type: CONSTRAINT; Schema: public; Owner: matteotocci
--

ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_email_key UNIQUE (email);


--
-- TOC entry 3702 (class 2606 OID 18108)
-- Name: utente utente_pkey; Type: CONSTRAINT; Schema: public; Owner: matteotocci
--

ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (id_utente);


--
-- TOC entry 3707 (class 2606 OID 18136)
-- Name: richiesta_servizio fk325t2p3gujxprpflr92hkhie6; Type: FK CONSTRAINT; Schema: public; Owner: matteotocci
--

ALTER TABLE ONLY public.richiesta_servizio
    ADD CONSTRAINT fk325t2p3gujxprpflr92hkhie6 FOREIGN KEY (id_proposta_accettata) REFERENCES public.proposta_servizio(id_proposta);


--
-- TOC entry 3703 (class 2606 OID 18111)
-- Name: cliente fk5xt7w8d9974sweyxmra5cmpuo; Type: FK CONSTRAINT; Schema: public; Owner: matteotocci
--

ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT fk5xt7w8d9974sweyxmra5cmpuo FOREIGN KEY (id_utente) REFERENCES public.utente(id_utente);


--
-- TOC entry 3705 (class 2606 OID 18126)
-- Name: proposta_servizio fka3dktxhsyhfip5kh9ksjgw5ww; Type: FK CONSTRAINT; Schema: public; Owner: matteotocci
--

ALTER TABLE ONLY public.proposta_servizio
    ADD CONSTRAINT fka3dktxhsyhfip5kh9ksjgw5ww FOREIGN KEY (id_richiesta) REFERENCES public.richiesta_servizio(id_richiesta);


--
-- TOC entry 3704 (class 2606 OID 18116)
-- Name: professionista fkbxaogon4yefw0mtwnu13lwpq6; Type: FK CONSTRAINT; Schema: public; Owner: matteotocci
--

ALTER TABLE ONLY public.professionista
    ADD CONSTRAINT fkbxaogon4yefw0mtwnu13lwpq6 FOREIGN KEY (id_utente) REFERENCES public.utente(id_utente);


--
-- TOC entry 3708 (class 2606 OID 18131)
-- Name: richiesta_servizio fkppndl5bg8qajh7evp0mf47qpe; Type: FK CONSTRAINT; Schema: public; Owner: matteotocci
--

ALTER TABLE ONLY public.richiesta_servizio
    ADD CONSTRAINT fkppndl5bg8qajh7evp0mf47qpe FOREIGN KEY (id_cliente) REFERENCES public.cliente(id_utente);


--
-- TOC entry 3706 (class 2606 OID 18121)
-- Name: proposta_servizio fkri3r43cbdxl09lwk77bgnsmoq; Type: FK CONSTRAINT; Schema: public; Owner: matteotocci
--

ALTER TABLE ONLY public.proposta_servizio
    ADD CONSTRAINT fkri3r43cbdxl09lwk77bgnsmoq FOREIGN KEY (id_professionista) REFERENCES public.professionista(id_utente);


-- Completed on 2026-01-06 13:46:27 CET

--
-- PostgreSQL database dump complete
--

\unrestrict iWgsGwWRsNUSP0pFgOxQ0YJkHvkd3WTKE4bg9x5uNg0j0R4NXKpiLyQPI7nzOf6

