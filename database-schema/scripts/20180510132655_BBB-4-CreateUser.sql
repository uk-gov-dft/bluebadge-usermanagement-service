--// BBB-4-CreateUser
-- Migration SQL that makes the change goes here.
ALTER TABLE users DROP COLUMN forename;
ALTER TABLE users DROP COLUMN surname;
ALTER TABLE users ADD COLUMN name VARCHAR(200) NOT NULL;
COMMENT ON COLUMN users.name IS 'Users full name.';

--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE users DROP COLUMN name;
ALTER TABLE users ADD COLUMN forename VARCHAR(60);
ALTER TABLE users ADD COLUMN surname VARCHAR(60);
COMMENT ON COLUMN users.forename IS 'Forename.';
COMMENT ON COLUMN users.surname IS 'Surname.';
