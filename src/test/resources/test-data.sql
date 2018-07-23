SET search_path TO usermanagement;
-- TODO remove next deletes once they have had time to percolate through environments
DELETE FROM users WHERE name LIKE '%Sampath%';
DELETE FROM users WHERE name = 'nobody';
-- TODO end.

DELETE FROM users WHERE email_address = 'createuservalid@dft.gov.uk';
DELETE FROM users WHERE id < 0;

INSERT INTO users (id, name, email_address, local_authority_id, role_id)
VALUES(-1, 'Sampath', 'abc@dft.gov.uk', 2, 2);
INSERT INTO users (id, name, email_address, local_authority_id, role_id)
VALUES(-2, 'Sampath', 'def@dft.gov.uk', 2, 2);
INSERT INTO users (id, name, email_address, local_authority_id, role_id)
VALUES(-3, 'nobody', 'abcnobody@dft.gov.uk', 2, 2);
INSERT INTO users (id, name, email_address, local_authority_id, role_id)
VALUES(-4, 'update test', 'updateme@dft.gov.uk', 2, 2);
INSERT INTO users (id, name, email_address, local_authority_id, role_id)
VALUES(-5, 'delete test', 'deleteme@dft.gov.uk', 2, 2);

INSERT INTO email_link (user_id, uuid, created_on, is_active)
VALUES (-1, '7d75652a-4e84-41e2-bd82-e5b5933b81da', '2018-07-10 09:37:07.119029', true);
