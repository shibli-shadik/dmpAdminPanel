--Role Data
insert into dmp_roles (id, role_type, role_type_display_name) values (1, 0, 'Admin');
insert into dmp_roles (id, role_type, role_type_display_name) values (2, 1, 'User');

--User
insert into dmp_user(
id, address, date_of_birth, disabled_date_time, email,
first_name, middle_name,last_name, has_sequrity_group, 
is_deleted, is_enabled, successive_failed_logins,
password, role_id, imei_no, national_id_card_no,
mobile, picture_id, salt, created_at, 
updated_at, is_password_expired, is_password_reset) 
values
(1, null, null, null, 'shadik@itcbd.com',
'Shibli','Shadik','Osmani', 0, 
0, 1, 0, 
'52167151b8992e79fc31fe9099cd840a09e7764ef7f7863d2b79f605739e5793ea5c4748396792595fc901580c4c39b43af86f28ff20eb2e7c24e42cdca9331b',1,'11111',null,
'01917304273',null,'cyZjk43BADAqZ1nIXpMcO7WmqNI56sW/w7HspThPFklsnZ3Zf8jR2+pe2Nai+Y8/HMoUmHK9fUIz/nBdb78NFeNtDfUw2CMqcfjN6BtK7ZrdOjgYT8B+j4sWYnD0LzGtrtL4vFKmeA07TIObTFAb1Jq3Q7PWlAlvRR/4eZkfynw=','2017-11-27 17:04:55.8860000',
'2017-11-27 17:04:55.8860000', 0, 0);
--Password: 123

--Settings
insert into dmp_settings (key_name,key_value) values('passwordMinCharLength','8');

