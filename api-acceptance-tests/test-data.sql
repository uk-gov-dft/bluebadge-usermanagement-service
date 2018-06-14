-- TODO remove next deletes once they have had time to percolate through environments
DELETE FROM users WHERE name LIKE '%Sampath%';
DELETE FROM users WHERE name = 'nobody';
-- TODO end.


DELETE FROM email_link WHERE user_id < 0;
DELETE FROM users WHERE email_address = 'createuservalid@dft.gov.uk';
DELETE FROM users WHERE id < 0;
INSERT INTO users (id, name, email_address, local_authority_id, role_id, password)
VALUES(-1, 'Sampath', 'abc@dft.gov.uk', 2, 2, 'fff');
INSERT INTO users (id, name, email_address, local_authority_id, role_id, password)
VALUES(-2, 'Sampath', 'def@dft.gov.uk', 2, 2, 'fff');
INSERT INTO users (id, name, email_address, local_authority_id, role_id, password)
VALUES(-3, 'nobody', 'abcnobody@dft.gov.uk', 2, 2, 'fff');
INSERT INTO users (id, name, email_address, local_authority_id, role_id, password)
VALUES(-4, 'update test', 'updateme@dft.gov.uk', 2, 2, 'fff');
INSERT INTO users (id, name, email_address, local_authority_id, role_id, password)
VALUES(-5, 'delete test', 'deleteme@dft.gov.uk', 2, 2, 'fff');

INSERT INTO email_link(user_id, uuid) VALUES (-1, '4175e31c-9c0c-41c0-9afb-40dc0a89b9c5');
INSERT INTO email_link(user_id, uuid, is_active) VALUES (-1, '4175e31c-0000-41c0-9afb-40dc0a89b9c5', false)