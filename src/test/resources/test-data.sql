DELETE FROM email_link WHERE user_id BETWEEN -760 and -750;
DELETE FROM users WHERE email_address = 'createuservalid@dft.gov.uk';
DELETE FROM users WHERE id BETWEEN -760 and -750;

INSERT INTO users (id, name, email_address, local_authority_short_code, role_id, password, user_uuid, is_active, login_fail_count) VALUES
  (-751, 'Sampath', 'um_unit_abc@dft.gov.uk', 'BIRM', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC', 'bbadbec7-d4b0-4a81-a3bb-a487ae4da81a', TRUE, 2)
  ,(-752, 'Sampath', 'um_unit_def@dft.gov.uk', 'BIRM', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC', '6b6789b2-3f8d-4f8e-8db8-df62be0bd556', TRUE, 0)
  ,(-753, 'nobody', 'um_unit_abcnobody@dft.gov.uk', 'BIRM', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC', '656b7372-b092-4172-a911-44a22c28d6a3', TRUE, 0)
  ,(-754, 'update test', 'um_unit_updateme@dft.gov.uk', 'BIRM', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC', 'f99e8327-f112-4f64-b4b7-8aa20c12944c', TRUE, 0)
  ,(-755, 'delete test', 'um_unit_deleteme@dft.gov.uk', 'BIRM', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC', 'ff54b08d-db7a-452f-9db3-922336eb8467', TRUE, 0)
;

INSERT INTO email_link (user_id, uuid, created_on, is_active)
VALUES (-751, '7d75652a-4e84-41e2-bd82-e5b5933b81da', '2018-07-10 09:37:07.119029', true);

