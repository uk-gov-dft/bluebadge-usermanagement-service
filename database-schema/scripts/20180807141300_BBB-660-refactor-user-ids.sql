
-- User uuid
DELETE FROM usermanagement.users CASCADE;
ALTER TABLE usermanagement.users ADD COLUMN user_uuid UUID NOT NULL;
CREATE UNIQUE INDEX users_uuid_ux ON usermanagement.users(user_uuid);

-- Local authority
ALTER TABLE usermanagement.users DROP COLUMN local_authority_id CASCADE;
ALTER TABLE usermanagement.users ADD COLUMN local_authority_short_code VARCHAR(10);
UPDATE usermanagement.users SET local_authority_short_code = 'BIRM';
ALTER TABLE usermanagement.users ALTER COLUMN local_authority_short_code SET NOT NULL;
CREATE INDEX users_la_code_ix ON usermanagement.users(local_authority_short_code);

-- Drop la tables
DROP TABLE IF EXISTS usermanagement.local_authority;
DROP TABLE IF EXISTS usermanagement.local_authority_area;

--//@UNDO
-- SQL to undo the change goes here.
-- Cannot undo with la id gone