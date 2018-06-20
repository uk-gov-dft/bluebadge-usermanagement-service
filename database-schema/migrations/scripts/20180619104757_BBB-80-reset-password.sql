--// BBB-80-reset***REMOVED***
-- Migration SQL that makes the change goes here.

ALTER TABLE users ADD COLUMN
  is_active BOOLEAN DEFAULT false;
ALTER TABLE users ADD COLUMN
  locked_timestamp TIMESTAMP;
ALTER TABLE users ADD COLUMN
  login_fail_count INTEGER DEFAULT 0;

COMMENT ON COLUMN users.is_active IS 'Set to false when a password reset is pending or user disabled.';
COMMENT ON COLUMN users.locked_timestamp IS 'Date and time locked, so can unlock after X minutes';
COMMENT ON COLUMN users.login_fail_count IS 'Number of consecutive incorrect password login attempts';

--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE users DROP COLUMN is_active;
ALTER TABLE users DROP COLUMN locked_timestamp;
ALTER TABLE users DROP COLUMN login_fail_count;
