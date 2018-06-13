--// BBB-50-set-your***REMOVED***
-- Migration SQL that makes the change goes here.
ALTER TABLE users ADD COLUMN password VARCHAR(200);
COMMENT ON COLUMN users. ***REMOVED***;

CREATE TABLE email_link(
user_id INTEGER NOT NULL,
uuid VARCHAR(40) NOT NULL,
created_on TIMESTAMP DEFAULT now(),
is_active boolean default true,
PRIMARY KEY (uuid)
);

COMMENT ON TABLE email_link IS 'Password reset emails.';
COMMENT ON COLUMN email_link.user_id IS 'FK to users.';
COMMENT ON COLUMN email_link.uuid IS 'Id of email sent.';
COMMENT ON COLUMN email_link.created_on IS 'Used to invalidate link after x hours';
COMMENT ON COLUMN email_link.is_active IS 'Set to false when password updated.';

ALTER TABLE email_link ADD CONSTRAINT email_link_user_id_fk
FOREIGN KEY (user_id) REFERENCES users(id);

--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE users DROP COLUMN password;
DROP TABLE IF EXISTS email_link;
