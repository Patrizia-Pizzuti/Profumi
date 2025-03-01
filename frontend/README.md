# ProfumiApp
PROFUMERIA 
PROFUMI & NOTE

Questo progetto è un'applicazione Full Stack per la gestione di profumi e delle loro note olfattive.
All'avvio dell'applicazione Angular, è possibile visualizzare l'elenco dei profumi e delle note olfattive disponibili.
Dopo la registrazione e autenticazione, gli utenti possono esplorare i dettagli di ogni profumo e, cliccando su una nota, scoprire tutti i profumi che la contengono.
Questo è possibile perchè con il login, l'utente riceve un JWT Token che deve essere incluso in ogni richiesta autenticata. Il token viene utilizzato per determinare i permessi dell'utente e protegge le API dall'accesso non autorizzato (Bearer Token).
Chi è ADMIN ha la possibilità di visualizzare, aggiungere, modificare ed eliminare profumi, ed aggiungere modificare o cancellare le note. Non può però assegnare ruoli o promuovere utenti a ADMIN o SUPER_ADMIN e non può eliminare altri ADMIN o SUPER_ADMIN.
I SUPER_ADMIN invece, oltre a tutto ciò che è permesso fare agli ADMIN, è possibile l'eliminazione degli utenti con qualsiasi ruolo (USER, ADMIN e persino altri SUPER_ADMIN), e promuovere un utente ad ADMIN o SUPER_ADMIN.

(N.B. Il primo utente registrato, è automaticamente un SUPER_ADMIN)

L'applicazione include un backend in Java Spring Boot con PostgreSQL come database e un frontend in Angular, utilizzando Angular Material per l'interfaccia utente.

# Backend - Spring Boot API
Il backend del progetto è sviluppato con Spring Boot e gestisce l'autenticazione, la gestione dei profumi e delle note olfattive. Utilizza PostgreSQL come database e implementa Spring Security con JWT per la protezione delle API.
Il progetto è diviso in packages:
com.example.profumeria: contiene il main 
com.example.profumeria.confing : classi per la configurazione jwt
com.example.profumeria.controller : i REST controller
com.example.profumeria.service: gestiscono la logica di business
com.example.profumeria.dto : contiene i DTO (Data Transfer Object da restituire al frontend per evitare di esporre le entità)
com.example.profumeria.entities: tutte le entità
com.example.profumeria.repository: contengono i repositories per l'implementazione dei CRUD repository

Tecnologie utilizzate:
Backend
Spring Boot (REST API, Spring Security, JPA/Hibernate) con Java 21
PostgreSQL (Database relazionale)
JWT (Autenticazione)

Sono da inserire 
tasto destro sul progetto, run as, run configurations, arguments, VM ARGUMENTS
add-opens java.base/java.lang=ALL-UNNAMED
add-opens java.base/java.io=ALL-UNNAMED
add-opens java.base/sun.security.util=ALL-UNNAMED


# Autenticazione e Ruoli
L'accesso ad alcune API è limitato in base ai ruoli:

USER → può consultare i profumi e le note, ma non modificarle
ADMIN → può creare, modificare ed eliminare profumi e note. Non può gestire utenti o assegnare ruoli.
SUPER_ADMIN → può gestire tutto, inclusa la gestione degli utenti.

L'autenticazione e la gestione degli accessi sono basate su due entità principali:

User (Utente)
Role (Ruolo)

Tabella users (Utenti)
La tabella users contiene le informazioni degli utenti registrati nel sistema.
Ogni utente ha:

id (Identificativo univoco)
username (Nome utente univoco)
password (Hash della password, non salvata in chiaro)
email (Email univoca)
Ruoli assegnati (Collegati tramite relazione Many-to-Many con la tabella roles)
Tabella roles (Ruoli)
La tabella roles definisce i possibili ruoli disponibili nel sistema.

Ogni ruolo ha:

id (Identificativo univoco)
name (Nome del ruolo, es. USER, ADMIN, SUPER_ADMIN)
Permessi associati (Gestiti tramite una tabella role_permissions, che collega ogni ruolo a specifiche autorizzazioni)

# Struttura delle API:
Il backend espone diversi endpoint REST per la gestione dei profumi e delle note. 
Di seguito, una panoramica delle principali API disponibili (sono state testate tramite Postman):

- Endpoints dei Profumi (/profumi)
MetodO/Endpoint	            Descrizione	                                                    Accesso
GET	/profumi	            Recupera tutti i profumi	                                    (Aperto)
GET	/profumi/{id}	        Recupera un profumo per ID	                                    (Aperto)
GET	/profumi/nota/{notaId}	Recupera tutti i profumi che contengono una determinata nota	(Aperto)
POST	/profumi	        Crea un nuovo profumo	                                        (Admin / Super Admin)
PUT	/profumi/{id}	        Aggiorna un profumo	                                            (Admin / Super Admin)
DELETE	/profumi/{id}	    Elimina un profumo	                                            (Admin / Super Admin)


- Endpoints delle Note (/note)
Metodo/Endpoint	                        Descrizione	                                                               Accesso
GET	/note	                            Recupera tutte le note	                                                  (Aperto)
GET	/note/{id}	                        Recupera una nota per ID	                                              (Aperto)
POST	/note	                        Crea una nuova nota	                                         (Admin / Super Admin)
DELETE	/note/{id}	                    Elimina una nota	                                         (Admin / Super Admin)
GET	/note/tipo/{notaId}/{tipoNotaId}	Recupera i profumi che contengono una nota specifica e il suo tipo	      (Aperto)
GET	/note/profumi/{notaId}	            Recupera tutti i profumi che contengono una determinata nota	          (Aperto)
PUT	/note/{id}	                        Aggiorna una nota	(Admin / Super Admin)


Le API saranno disponibili su http://localhost:8080.


# Database
Un profumo può avere più note olfattive, classificate come Testa, Cuore o Fondo.
Ogni nota può essere utilizzata in più profumi.
L'associazione tra profumi e note è gestita da una tabella intermedia, 
che collega i due elementi e definisce il tipo di nota.
Le tabelle vengono create all'avvio dell'applicazione. 
Nella cartella "Query" sono presenti una serie di Query appunto, per popolare il db manualmente. 

Abbiamo cosi un tabella profumi:
Campo	        Tipo	        Attributi	            Descrizione
id	            INTEGER         SERIAL	PRIMARY KEY	    (Identificativo univoco del profumo)
nome	        VARCHAR(255)	NOT NULL	            (Nome del profumo)
marca	        VARCHAR(255)	NOT NULL	            (Marca del profumo)
descrizione	    VARCHAR(255)	NULL	                (Descrizione del profumo)
formato	        VARCHAR(50)	    NULL	                (Formato del profumo (es. 50ml, 100ml))
prezzo	        DECIMAL(10,2)	NULL	                (Prezzo del profumo)
immagine_url	VARCHAR(255)	NULL	                (Percorso dell'immagine del profumo)

- Tabella note
Memorizza le diverse note olfattive disponibili.
Campo	        Tipo	        Attributi	            Descrizione
id	            INTEGER         SERIAL	PRIMARY KEY	    DIdentificativo univoco della nota
nomeNota	    VARCHAR(255)	NOT NULL UNIQUE	        Nome della nota olfattiva

- Tabella tipo_nota. Memorizza i tipi di nota (Testa, Cuore, Fondo).
Campo	        Tipo	        Attributi	            Descrizione
id	            INTEGER         SERIAL PRIMARY KEY	    (Identificativo univoco del tipo di nota)
nome	        VARCHAR(255)	NOT NULL UNIQUE	        (Nome del tipo di nota (Testa, Cuore, Fondo))

- Tabella profumiENote (Tabella Intermedia)
Collega i profumi alle note e al loro tipo.
Campo	        Tipo	    Attributi	                Descrizione
id	            INTEGER     SERIAL  PRIMARY KEY         (Identificativo univoco della relazione)
profumo_id  	INTEGER	    FOREIGN KEY → profumi(id)	(ID del profumo)
nota_id	        INTEGER	    FOREIGN KEY → note(id)	    (ID della nota olfattiva)
tipo_nota_id	INTEGER	    FOREIGN KEY → tipo_nota(id)	(ID del tipo di nota)



# Configurazione Database PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/profumiENote
spring.datasource.username=postgres
spring.datasource.password=user
spring.datasource.driver-class-name=org.postgresql.Driver


# Frontend 
Frontend
Angular (Framework Frontend) version 19.1.6. 
Angular Material (Componenti UI)
HTTP Client (Comunicazione con il backend)

Il progetto è NO STANDALONE

Componenti Principali
- Home Component
Mostra una barra di navigazione con opzioni per accedere a profumi, note e il login e la registrazione (in realtà la parte superiore è la navbar component). L'Home component è accessibile a tutti. 
- Profumi Component
Mostra la lista dei profumi disponibili.
- Profumo Dettaglio Component
Visualizza le informazioni dettagliate di un profumo e la foto dello stesso, solo se si è loggati, mostrando le note olfattive suddivise in Testa, Cuore e Fondo.
Permette di tornare alla lista dei profumi o alla home.
- Note Component
Mostra tutte le note olfattive disponibili.
Se si è loggati, cliccando su una nota viene mostrato un dialog con i profumi che contengono quella nota.
- CRUD Profumi Component (solo ADMIN e SUPER_ADMIN)
Una volta che si ci logga, se si è ADMIN e SUPER_ADMIN, è possibile visualizzare sulla navbar il bottone corrispondente.
Permette di aggiungere, modificare ed eliminare profumi.
Permette di associare note olfattive a un profumo.
Modalità di modifica: selezionando un profumo dalla lista, i dati vengono caricati nel form.
(Stessa cosa avviene anche per il  Note CRUD Component)
- Login e Registrazione Component
Gli utenti possono registrarsi e autenticarsi.
Dopo il login, vengono reindirizzati all'homepage con le opzioni disponibili in base al ruolo.
I token JWT vengono salvati per mantenere l'autenticazione.

L'applicazione sarà accessibile su http://localhost:4200.

