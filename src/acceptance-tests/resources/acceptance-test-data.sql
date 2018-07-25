SET search_path TO usermanagement;


DELETE FROM email_link WHERE user_id < 0;
DELETE FROM email_link where user_id IN
  (SELECT user_id FROM users WHERE email_address = 'createuservalid@dft.gov.uk');
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
INSERT INTO users (id, name, email_address, local_authority_id, role_id)
VALUES(-6, 'delete test', 'otherlocalauthority@dft.gov.uk', 3, 2);
INSERT INTO users (id, name, email_address, local_authority_id, role_id)
VALUES(-7, 'get test', 'gettest@dft.gov.uk', 2, 2);

INSERT INTO email_link(user_id, uuid) VALUES (-1, '4175e31c-9c0c-41c0-9afb-40dc0a89b9c5');
INSERT INTO email_link(user_id, uuid, is_active) VALUES (-1, '4175e31c-0000-41c0-9afb-40dc0a89b9c5', false);
