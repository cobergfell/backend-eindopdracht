INSERT INTO public.authorities ("authority","username") VALUES ('ROLE_USER','cobergfell');
INSERT INTO public.authorities ("authority","username") VALUES ('ROLE_MODERATOR','cobergfell');
INSERT INTO public.authorities ("authority","username") VALUES ('ROLE_ADMIN','cobergfell');

INSERT INTO public.users ("username","apikey","date_time_registeredgmt","email","enabled","last_update","password") 
              VALUES ('cobergfell','JyRMaYQQVo0tsJHLXteD',(select '2022-09-09 15:45:00.559339'::timestamp),'Christophe.Obergfell@novi-education.nl',True,(select '2022-09-09 15:45:00.559339'::timestamp),'$2a$10$VyY/g4ljGj5/QoycdmVOwOWAKpeaZXqhSS7.WLeSPlVyh47YeVLHO');


--INSERT INTO public.paintings ("artist","date_time_posted","description","image","last_update","title","username") 
--              VALUES ('test',(select '2022-09-09T15:45:00.559339'::timestamp),(SELECT CAST ('this is the description...' AS TEXT)),pg_read_binary_file('D:\\Data\\NOVI\\eindopdracht\\assets\\The_Fiddler_Marc_Chagall.png'),(select '2022-09-09 15:45:00.559339'::timestamp),'test','cobergfell');

INSERT INTO public.paintings ("artist","date_time_posted","description","image","last_update","title","username") 
              VALUES ('test',(select '2022-09-09T15:45:00.559339'::timestamp),'this is the description...',pg_read_binary_file('D:\\Data\\NOVI\\eindopdracht\\assets\\The_Fiddler_Marc_Chagall.png'),(select '2022-09-09 15:45:00.559339'::timestamp),'test','cobergfell');




INSERT INTO public.files_database ("bytes_in_database_url","data","description","file_on_disk_id","file_on_disk_url","name","type","answer_id","painting_id","question_id") 
              VALUES (NULL,lo_from_bytea(0,pg_read_binary_file('D:\\Data\\NOVI\\eindopdracht\\assets\\modified_Gauss-Newton.txt')),NULL,1,'http://localhost:8080/api/user/download-file-from-disk/1','modified_Gauss-Newton.txt','text/plain',NULL,1,NULL);
INSERT INTO public.files_database ("bytes_in_database_url","data","description","file_on_disk_id","file_on_disk_url","name","type","answer_id","painting_id","question_id") 
              VALUES (NULL,lo_from_bytea(0,pg_read_binary_file('D:\\Data\\NOVI\\eindopdracht\\assets\\modified_Gauss-Newton.txt')),NULL,3,'http://localhost:8080/api/user/download-file-from-disk/3','modified_Gauss-Newton.txt','text/plain',NULL,1,NULL);
INSERT INTO public.files_database ("bytes_in_database_url","data","description","file_on_disk_id","file_on_disk_url","name","type","answer_id","painting_id","question_id") 
              VALUES (NULL,lo_from_bytea(0,pg_read_binary_file('D:\\Data\\NOVI\\eindopdracht\\assets\\modified_Gauss-Newton.txt')),NULL,5,'http://localhost:8080/api/user/download-file-from-disk/5','modified_Gauss-Newton.txt','text/plain',NULL,1,NULL);


INSERT INTO public.novi_method_1_files_on_disk ("description","file_name","location","media_type","title","uploaded_by_username","uploaded_timestamp") 
              VALUES ('modified_Gauss-Newton.txt','modified_Gauss-Newton.txt','D:\Users\Gebruiker\Test\uploads\modified_Gauss-Newton.txt',NULL,'modified_Gauss-Newton.txt',NULL,NULL);
INSERT INTO public.novi_method_1_files_on_disk ("description","file_name","location","media_type","title","uploaded_by_username","uploaded_timestamp") 
              VALUES ('rachmaninov.mp3','rachmaninov.mp3','D:\Users\Gebruiker\Test\uploads\rachmaninov.mp3',NULL,'rachmaninov.mp3',NULL,NULL);
INSERT INTO public.novi_method_1_files_on_disk ("description","file_name","location","media_type","title","uploaded_by_username","uploaded_timestamp") 
              VALUES ('Comtesse_de_Pourtales.jpg','Comtesse_de_Pourtales.jpg','D:\Users\Gebruiker\Test\uploads\Comtesse_de_Pourtales.jpg',NULL,'Comtesse_de_Pourtales.jpg',NULL,NULL);
INSERT INTO public.novi_method_1_files_on_disk ("description","file_name","location","media_type","title","uploaded_by_username","uploaded_timestamp") 
              VALUES ('rachmaninov.mp3','rachmaninov.mp3','D:\Users\Gebruiker\Test\uploads\rachmaninov.mp3',NULL,'rachmaninov.mp3',NULL,NULL);
INSERT INTO public.novi_method_1_files_on_disk ("description","file_name","location","media_type","title","uploaded_by_username","uploaded_timestamp") 
              VALUES ('modified_Gauss-Newton.txt','modified_Gauss-Newton.txt','D:\Users\Gebruiker\Test\uploads\modified_Gauss-Newton.txt',NULL,'modified_Gauss-Newton.txt',NULL,NULL);

INSERT INTO public.music_files_database ("bytes_in_database_url","data","description","file_on_disk_id","file_on_disk_url","name","type","answer_id","painting_id","question_id") 
              VALUES (NULL,lo_from_bytea(0,pg_read_binary_file('D:\Data\NOVI\eindopdracht\frontend-eindopdracht\src\assets\\rachmaninov.mp3')),NULL,2,'http://localhost:8080/api/user/download-file-from-disk/2','rachmaninov.mp3','audio/mpeg',NULL,1,NULL);
INSERT INTO public.music_files_database ("bytes_in_database_url","data","description","file_on_disk_id","file_on_disk_url","name","type","answer_id","painting_id","question_id") 
              VALUES (NULL,lo_from_bytea(0,pg_read_binary_file('D:\Data\NOVI\eindopdracht\frontend-eindopdracht\src\assets\\Gurdjieff_by_Jeroen_van_Veen.mp3')),NULL,4,'http://localhost:8080/api/user/download-file-from-disk/5','Gurdjieff_by_Jeroen_van_Veen.mp3','audio/mpeg',NULL,1,NULL);

INSERT INTO public.questions ("content","date_time_posted","image","last_update","title","painting_id","username") 
              VALUES ('This is my question',(select '2022-09-09 15:45:00.559339'::timestamp),NULL,(select '2022-09-09 15:45:00.559339'::timestamp),'question 1',1,'cobergfell');


INSERT INTO answers ("content","date_time_posted","image","last_update","title","painting_id","question_id","username") 
              VALUES ('This is my answer',(select '2022-09-09 15:45:00.559339'::timestamp),NULL,(select '2022-09-09 15:45:00.559339'::timestamp),'answer 1',NULL,1,'cobergfell');