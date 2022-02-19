INSERT INTO users (username, password, enabled) VALUES ('user', '$2a$10$.WJm99cFEaArOPGXumt3deqvnDdbrzwZyTTsaM1JTLNuPxkLPDEyG', TRUE);
INSERT INTO users (username, password, enabled) VALUES ('admin', '$2a$10$.WJm99cFEaArOPGXumt3deqvnDdbrzwZyTTsaM1JTLNuPxkLPDEyG', TRUE);
INSERT INTO users (username, password, enabled) VALUES ('cobergfell', '$2a$10$.WJm99cFEaArOPGXumt3deqvnDdbrzwZyTTsaM1JTLNuPxkLPDEyG', TRUE);
INSERT INTO users (username, password, enabled) VALUES ('cobergfell_2', '$2a$10$CC45OCAdd7S3KuuqTQy9b.06q0OHZnV1bLgFZb2vjiS3DFRnHzWqe', TRUE);
INSERT INTO users (username, password, enabled) VALUES ('cobergfell_3', '$2a$10$7lPMeJUDTQA7xNG9ozGXYOLZVQQ5VEQvdLJBBzeMoDWHsOVEPeLqi', TRUE);

INSERT INTO authorities (username, authority) VALUES ('user', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('admin', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('admin', 'ROLE_ADMIN');
INSERT INTO authorities (username, authority) VALUES ('cobergfell', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('cobergfell', 'ROLE_ADMIN');
INSERT INTO authorities (username, authority) VALUES ('cobergfell', 'ROLE_MODERATOR');
INSERT INTO authorities (username, authority) VALUES ('cobergfell_2', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('cobergfell_2', 'ROLE_ADMIN');
INSERT INTO authorities (username, authority) VALUES ('cobergfell_2', 'ROLE_MODERATOR');
INSERT INTO authorities (username, authority) VALUES ('cobergfell_3', 'ROLE_USER');