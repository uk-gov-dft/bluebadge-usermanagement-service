-- // BBB-631-new-roles
-- Migration SQL that makes the change goes here.

update usermanagement.roles set description = 'LA Editor' where id = 3;
insert into usermanagement.roles (id, description) VALUES (6, 'API Client');

-- //@UNDO
-- SQL to undo the change goes here.

update usermanagement.roles set description = 'LA User' where id = 3;
DELETE from usermanagement.roles where id = 6;

