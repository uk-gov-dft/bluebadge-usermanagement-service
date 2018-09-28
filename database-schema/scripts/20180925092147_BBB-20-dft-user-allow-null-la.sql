-- // BBB-20-dft-user-allow-null-la
-- Migration SQL that makes the change goes here.

alter table usermanagement.users ALTER COLUMN local_authority_short_code drop not null;

update usermanagement.users SET local_authority_short_code = null where role_id = 1;

-- //@UNDO
-- SQL to undo the change goes here.

update usermanagement.users SET local_authority_short_code = 'ABERD' where role_id = 1;

alter table usermanagement.users ALTER COLUMN local_authority_short_code set not null;
