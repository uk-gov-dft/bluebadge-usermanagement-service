SET search_path TO usermanagement;


DELETE FROM email_link WHERE user_id < 0;
DELETE FROM email_link where user_id IN
  (SELECT user_id FROM users WHERE email_address = 'createuservalid@dft.gov.uk');
DELETE FROM users WHERE email_address = 'createuservalid@dft.gov.uk';
DELETE FROM users WHERE id < 0;
INSERT INTO users (id, name, email_address, local_authority_short_code, role_id, password, user_uuid)
VALUES(-1, 'Bruce Wayne', 'abc@dft.gov.uk', 'BIRM', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC',
'cc4fbb98-3963-41ef-9c75-a9651b2fe27c'::UUID);
INSERT INTO users (id, name, email_address, local_authority_short_code, role_id, password, user_uuid)
VALUES(-2, 'Sampath somethingdifferent', 'def@dft.gov.uk', 2, 2, '$2a$11$YgiL6XFOoFLHjlFRxZNGr.n4bFKZaRNkAIRHaqDxIWRfAagCPJvMK',
'411cfa59-df4b-440f-92e5-37073de4529f'::UUID);
INSERT INTO users (id, name, email_address, local_authority_short_code, role_id, password, user_uuid)
VALUES(-3, 'nobody', 'abcnobody@dft.gov.uk', 'BIRM', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC',
'79b3faa0-d3e7-464d-8dd2-684ffb0b6ff8'::UUID);
INSERT INTO users (id, name, email_address, local_authority_short_code, role_id, password, user_uuid)
VALUES(-4, 'update test', 'updateme@dft.gov.uk', 'BIRM', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC',
'9cb7a2f9-9e03-4277-9725-67fabb21847f'::UUID);
INSERT INTO users (id, name, email_address, local_authority_short_code, role_id, password, user_uuid)
VALUES(-5, 'delete test', 'deleteme@dft.gov.uk', 'BIRM', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC',
'e9ec670a-1c2d-449a-be92-4493cbf4838e'::UUID);
INSERT INTO users (id, name, email_address, local_authority_short_code, role_id, password, user_uuid)
VALUES(-6, 'delete test', 'otherlocalauthority@dft.gov.uk', 'ABERD', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC',
'9bdc58aa-7026-4a7c-9d57-805c3d96cecb'::UUID);
INSERT INTO users (id, name, email_address, local_authority_short_code, role_id, password, user_uuid)
VALUES(-7, 'get test', 'gettest@dft.gov.uk', 'BIRM', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC',
'c16fd9da-f823-4d37-a289-6b0d411c111c'::UUID);

INSERT INTO email_link(user_id, uuid) VALUES (-1, '4175e31c-9c0c-41c0-9afb-40dc0a89b9c5');
INSERT INTO email_link(user_id, uuid, is_active) VALUES (-1, '4175e31c-0000-41c0-9afb-40dc0a89b9c5', false);
