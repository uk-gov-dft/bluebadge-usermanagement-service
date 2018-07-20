--// HotFix-Delete-user
-- Migration SQL that makes the change goes here.

ALTER TABLE email_link DROP CONSTRAINT email_link_user_id_fk;

ALTER TABLE email_link ADD CONSTRAINT email_link_user_id_fk
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

-- Index for cascade deletes.
CREATE INDEX email_link_user_id_ix ON email_link(user_id);

--//@UNDO
-- SQL to undo the change goes here.
ALTER TABLE email_link DROP CONSTRAINT email_link_user_id_fk;

ALTER TABLE email_link ADD CONSTRAINT email_link_user_id_fk
FOREIGN KEY (user_id) REFERENCES users(id);

DROP INDEX email_link_user_id_ix;
