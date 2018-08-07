SET search_path TO usermanagement;

DELETE FROM users WHERE email_address = 'createuservalid@dft.gov.uk';
DELETE FROM users WHERE id < 0;

INSERT INTO users (id, name, email_address, local_authority_short_code, role_id, password, user_uuid)
VALUES(-1, 'Sampath', 'abc@dft.gov.uk', 'BIRM', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC',
'0093daf9-2782-47f8-93dc-bdf073204d6c');
INSERT INTO users (id, name, email_address, local_authority_short_code, role_id, password, user_uuid)
VALUES(-2, 'Sampath', 'def@dft.gov.uk', 'BIRM', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC',
'd327a64c-4876-4975-98f7-183447c9cca1');
INSERT INTO users (id, name, email_address, local_authority_short_code, role_id, password, user_uuid)
VALUES(-3, 'nobody', 'abcnobody@dft.gov.uk', 'BIRM', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC',
'af46bb29-6322-42c8-9c29-9fe80a0a38bf');
INSERT INTO users (id, name, email_address, local_authority_short_code, role_id, password, user_uuid)
VALUES(-4, 'update test', 'updateme@dft.gov.uk', 'BIRM', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC',
'c6187d21-93a5-4ceb-bfbe-9e9f9b872023');
INSERT INTO users (id, name, email_address, local_authority_short_code, role_id, password, user_uuid)
VALUES(-5, 'delete test', 'deleteme@dft.gov.uk', 'BIRM', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC',
'04b630ba-2809-45c7-8f5e-3a0f56788a3d');

INSERT INTO email_link (user_id, uuid, created_on, is_active)
VALUES (-1, '7d75652a-4e84-41e2-bd82-e5b5933b81da', '2018-07-10 09:37:07.119029', true);
