INSERT INTO public.users ("username","apikey","date_time_registeredgmt","email","enabled","last_update","password")
              VALUES ('cobergfell','JyRMaYQQVo0tsJHLXteD',(select '2022-09-09 15:45:00.559339'::timestamp),'Christophe.Obergfell@novi-education.nl',True,(select '2022-09-09 15:45:00.559339'::timestamp),'$2a$10$VyY/g4ljGj5/QoycdmVOwOWAKpeaZXqhSS7.WLeSPlVyh47YeVLHO');

INSERT INTO authorities (username, authority) VALUES ('cobergfell', 'ROLE_USER');
INSERT INTO authorities (username, authority) VALUES ('cobergfell', 'ROLE_ADMIN');
INSERT INTO authorities (username, authority) VALUES ('cobergfell', 'ROLE_MODERATOR');