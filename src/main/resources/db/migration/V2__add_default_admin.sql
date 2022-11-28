INSERT INTO "user" (name, surname, patronymic, date_of_birth, email, password, role_id)
VALUES
('Default', 'Admin', '-', NOW(), 'admin@nowhere.com',
'$2a$10$YXmmNE6wAP/4j2hZI6X9nOgpjY7Heisi/5hCo6s1xxL0IgftC.K26',
(SELECT id FROM role WHERE name='Manager'));