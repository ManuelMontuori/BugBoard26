CREATE TYPE user_role AS ENUM ('ADMIN', 'USER');

CREATE TYPE user_status AS ENUM ('ACTIVE', 'DISABLED');

CREATE TABLE Users (
	id BIGSERIAL PRIMARY KEY,
	uuid UUID NOT NULL UNIQUE,
	firstName VARCHAR(30),
	lastName VARCHAR(30),
	email VARCHAR(50) NOT NULL UNIQUE,
	role user_role NOT NULL,
	status user_status NOT NULL DEFAULT 'ACTIVE',
	last_login DATE,
	created_at TIMESTAMP
);

CREATE TABLE Notifications (
	id BIGSERIAL PRIMARY KEY,
	uuid UUID NOT NULL UNIQUE,
	message VARCHAR(5000) NOT NULL,
	read BOOLEAN NOT NULL,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	user_id BIGINT NOT NULL
);

ALTER TABLE notifications
ADD CONSTRAINT notifications_user_id_fk
FOREIGN KEY (user_id)
REFERENCES users(id)
ON DELETE RESTRICT;

-- INDEX sulla FK, Postegree non le genera in automatico sulle fk, ma solo su pk e unique
CREATE INDEX idx_notifications_user_id
ON notifications(user_id);

CREATE TYPE issue_type AS ENUM ('BUG', 'DOCUMENTATION', 'FEATURE', 'QUESTION');
CREATE TYPE issue_priority AS ENUM ('HIGH', 'MEDIUM', 'LOW');
CREATE TYPE issue_status AS ENUM ('TODO', 'IN_PROGRESS', 'DONE');


CREATE TABLE issues (
    id BIGSERIAL PRIMARY KEY,
	uuid UUID NOT NULL UNIQUE,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(5000) NOT NULL,
    is_type issue_type,
    is_priority issue_priority,
    is_status issue_status NOT NULL DEFAULT 'TODO',
    reporter BIGINT NOT NULL,
    assigned_to BIGINT,
    img_path VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP,
    assigned_at TIMESTAMP
);

ALTER TABLE Issues
ADD CONSTRAINT issues_reporter_fk
FOREIGN KEY (reporter)
REFERENCES users(id)
ON DELETE RESTRICT,
ADD CONSTRAINT issues_assigned_to_fk
FOREIGN KEY (assigned_to)
REFERENCES users(id)
ON DELETE RESTRICT;

ALTER TABLE issues
ADD CONSTRAINT issues_assigned_at
CHECK (assigned_to IS NULL OR assigned_at IS NOT NULL);

CREATE INDEX idx_issues_reporter
ON issues(reporter);

CREATE INDEX idx_issues_assigned_to
ON issues(assigned_to);


INSERT INTO Users (uuid, email, role, firstName, lastName, last_login, created_at) VALUES
(gen_random_uuid(), 'admin@system.com', 'ADMIN', 'System', 'Admin', '2026-04-01', NOW()),
(gen_random_uuid(), 'mario.rossi@provider.it', 'USER', 'Mario', 'Rossi', '2026-04-15', NOW()),
(gen_random_uuid(), 'luca.bianchi@gmail.com', 'USER', 'Luca', 'Bianchi', '2026-04-10', NOW()),
(gen_random_uuid(), 'sofia.verdi@tech.io', 'ADMIN', 'Sofia', 'Verdi', '2026-04-16', NOW()),
(gen_random_uuid(), 'giulia.neri@azienda.com', 'USER', 'Giulia', 'Neri', '2026-03-20', NOW()),
(gen_random_uuid(), 'marco.bruni@web.net', 'USER', 'Marco', 'Bruni', '2026-04-12', NOW()),
(gen_random_uuid(), 'elena.gallo@startup.com', 'USER', 'Elena', 'Gallo', '2026-04-14', NOW()),
(gen_random_uuid(), 'stefano.rizzi@dev.com', 'USER', 'Stefano', 'Rizzi', '2026-04-11', NOW()),
(gen_random_uuid(), 'anna.ferrari@cloud.it', 'USER', 'Anna', 'Ferrari', '2026-04-05', NOW()),
(gen_random_uuid(), 'paolo.costa@logic.com', 'ADMIN', 'Paolo', 'Costa', '2026-04-16', NOW()),
(gen_random_uuid(), 'chiara.longo@mail.it', 'USER', 'Chiara', 'Longo', '2026-04-08', NOW()),
(gen_random_uuid(), 'alessio.marini@code.org', 'USER', 'Alessio', 'Marini', '2026-04-02', NOW()),
(gen_random_uuid(), 'valentina.serra@db.com', 'USER', 'Valentina', 'Serra', '2026-04-09', NOW()),
(gen_random_uuid(), 'roberto.greco@it.com', 'USER', 'Roberto', 'Greco', '2026-04-03', NOW()),
(gen_random_uuid(), 'federica.valli@web.com', 'USER', 'Federica', 'Valli', '2026-03-28', NOW()),
(gen_random_uuid(), 'matteo.ponti@dev.io', 'USER', 'Matteo', 'Ponti', '2026-04-13', NOW()),
(gen_random_uuid(), 'silvia.donati@corp.com', 'USER', 'Silvia', 'Donati', '2026-04-14', NOW()),
(gen_random_uuid(), 'andrea.sala@service.it', 'USER', 'Andrea', 'Sala', '2026-04-15', NOW()),
(gen_random_uuid(), 'beatrice.moretti@lab.it', 'USER', 'Beatrice', 'Moretti', '2026-04-11', NOW()),
(gen_random_uuid(), 'davide.esposito@tech.com', 'ADMIN', 'Davide', 'Esposito', '2026-04-16', NOW());


INSERT INTO Notifications (message, read, user_id) VALUES
('Benvenuto nel sistema!', true, 1),
('Il tuo profilo è stato aggiornato.', false, 2),
('Nuovo ticket assegnato a te.', false, 3),
('Manutenzione programmata per domani.', true, 4),
('Password in scadenza tra 3 giorni.', false, 5),
('Accesso rilevato da un nuovo dispositivo.', true, 6),
('Documentazione aggiornata.', true, 7),
('Errore durante il backup settimanale.', false, 1),
('Richiesta di supporto approvata.', true, 9),
('Nuovo commento sul tuo issue.', false, 10),
('Aggiornamento di sicurezza disponibile.', true, 11),
('Il tuo account è stato verificato.', true, 12),
('Segnalazione chiusa con successo.', true, 13),
('Attenzione: spazio disco quasi esaurito.', false, 1),
('Promemoria: riunione alle ore 15:00.', false, 15),
('Il file richiesto è pronto per il download.', true, 16),
('Nuova policy sulla privacy pubblicata.', false, 17),
('Tentativo di login fallito.', true, 18),
('Il server ha completato il riavvio.', true, 19),
('Configurazione completata.', false, 20);


INSERT INTO issues (uuid, title, description, is_type, is_priority, is_status, reporter, assigned_to, img_path, assigned_at, created_at, resolved_at) VALUES
(gen_random_uuid(), 'Bug Login', 'Errore 500 al login social', 'BUG', 'HIGH', 'IN_PROGRESS', 2, 1, '/assets/bug01.png', NOW(), NOW(), NULL), -- Aggiunto NOW()
(gen_random_uuid(), 'Aggiornare Manuale', 'Manca sezione API v2', 'DOCUMENTATION', 'LOW', 'DONE', 3, 4, NULL, '2026-04-11 10:00:00', '2026-04-10', '2026-04-12'), -- Aggiunta data stringa
(gen_random_uuid(), 'Dark Mode', 'Richiesta implementazione tema scuro', 'FEATURE', 'MEDIUM', 'TODO', 5, 10, NULL, NULL, NOW(), NULL),
(gen_random_uuid(), 'Query lenta', 'La dashboard impiega 10s a caricare', 'BUG', 'HIGH', 'IN_PROGRESS', 6, 20, '/img/slow_query.jpg', NOW(), NOW(), NULL), -- Aggiunto NOW()
(gen_random_uuid(), 'Domanda permessi', 'Come cambio il ruolo degli utenti?', 'QUESTION', 'LOW', 'DONE', 7, 1, NULL, '2026-04-05 09:15:00', '2026-04-05', '2026-04-05'), -- Aggiunta data stringa
(gen_random_uuid(), 'Export CSV', 'Aggiungere export nella tabella utenti', 'FEATURE', 'MEDIUM', 'TODO', 8, NULL, NULL, NULL, NOW(), NULL),
(gen_random_uuid(), 'Refactoring Auth', 'Pulizia codice modulo autenticazione', 'FEATURE', 'LOW', 'IN_PROGRESS', 10, 4, NULL, NOW(), NOW(), NULL), -- Aggiunto NOW()
(gen_random_uuid(), 'Icona mancante', 'Manca l''icona nel footer', 'BUG', 'LOW', 'DONE', 11, 20, '/assets/footer_err.png', NULL, '2026-04-01', '2026-04-03'),
(gen_random_uuid(), 'Fix typo', 'Errore di battitura nella home', 'DOCUMENTATION', 'LOW', 'DONE', 12, 1, NULL, NULL, '2026-04-14', '2026-04-14'),
(gen_random_uuid(), 'Integrazione Stripe', 'Aggiungere pagamenti ricorrenti', 'FEATURE', 'HIGH', 'TODO', 13, 10, NULL, NULL, NOW(), NULL),
(gen_random_uuid(), 'Crash mobile', 'App crasha su iOS 17', 'BUG', 'HIGH', 'IN_PROGRESS', 14, 20, '/logs/crash.log', NOW(), NOW(), NULL), -- Aggiunto NOW()
(gen_random_uuid(), 'Setup SSL', 'Rinnovare certificati scaduti', 'BUG', 'HIGH', 'DONE', 1, 4, NULL, NULL, '2026-04-15', '2026-04-16'),
(gen_random_uuid(), 'Miglioramento SEO', 'Aggiunta meta tag dinamici', 'FEATURE', 'MEDIUM', 'TODO', 16, NULL, NULL, NULL, NOW(), NULL),
(gen_random_uuid(), 'Info Docker', 'Quale versione di node usare?', 'QUESTION', 'LOW', 'DONE', 17, 1, NULL, NULL, '2026-04-10', '2026-04-10'),
(gen_random_uuid(), 'Validazione Mail', 'RegEx troppo permissiva', 'BUG', 'MEDIUM', 'IN_PROGRESS', 18, 10, NULL, NULL, NOW(), NULL),
(gen_random_uuid(), 'Traduzione EN', 'Mancano stringhe in inglese', 'DOCUMENTATION', 'MEDIUM', 'TODO', 19, 4, NULL, NULL, NOW(), NULL),
(gen_random_uuid(), 'Bottone disabilitato', 'Il tasto invio non risponde', 'BUG', 'HIGH', 'TODO', 2, 20, NULL, NULL, NOW(), NULL),
(gen_random_uuid(), 'Audit Sicurezza', 'Controllare vulnerabilità pacchetti', 'FEATURE', 'HIGH', 'IN_PROGRESS', 4, 1, NULL, NOW(), NOW(), NULL), -- Aggiunto NOW()
(gen_random_uuid(), 'Report Mensile', 'Generare PDF automatico', 'FEATURE', 'LOW', 'DONE', 6, 10, '/reports/template.pdf', NULL, '2026-03-01', '2026-03-05'),
(gen_random_uuid(), 'Test Unitari', 'Aumentare copertura al 80%', 'FEATURE', 'MEDIUM', 'TODO', 20, NULL, NULL, NULL, NOW(), NULL);

INSERT INTO Users (uuid, email, firstName, lastName, role, last_login, created_at) VALUES
(gen_random_uuid(), 'test@bugboard.local', 'Valerio', 'Valeri', 'ADMIN', '2026-04-01', NOW());

