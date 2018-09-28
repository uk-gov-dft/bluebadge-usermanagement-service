SET search_path TO usermanagement;


DELETE FROM usermanagement.email_link WHERE user_id BETWEEN -30 and 0;
DELETE FROM usermanagement.email_link where user_id IN
  (SELECT user_id FROM users WHERE email_address = 'um_createuservalid@dft.gov.uk');
DELETE FROM usermanagement.users WHERE email_address = 'um_createuservalid@dft.gov.uk';
DELETE FROM usermanagement.users WHERE id BETWEEN -30 and 0;
INSERT INTO usermanagement.users (id, name, email_address, local_authority_short_code, role_id, password, user_uuid, is_active, login_fail_count) VALUES
  (-1, 'Bruce Wayne', 'um_abc@dft.gov.uk', 'ABERD', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC', '3bfe600b-4425-40cd-ad81-d75bbe16ee13'::UUID, TRUE, 0 )
  ,(-2, 'Sampath somethingdifferent', 'um_def@dft.gov.uk', 'ABERD', 2, '$2a$11$YgiL6XFOoFLHjlFRxZNGr.n4bFKZaRNkAIRHaqDxIWRfAagCPJvMK', 'c1c9ad2b-b7e9-4dcf-af21-95030205bda1'::UUID, TRUE, 0 )
  ,(-3, 'nobody', 'um_abcnobody@dft.gov.uk', 'ABERD', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC', '5513ffb3-04f2-4fad-a8d2-2f01e46590f8'::UUID, TRUE, 0 )
  ,(-4, 'update test', 'um_updateme@dft.gov.uk', 'ABERD', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC', '9619e6a0-1e9e-4217-92b1-21f33b4b4762'::UUID, TRUE, 0 )
  ,(-5, 'delete test', 'um_deleteme@dft.gov.uk', 'ABERD', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC', '34c40459-5b73-402c-96e1-94235b178771'::UUID, TRUE, 0 )
  ,(-6, 'delete test', 'um_otherlocalauthority@dft.gov.uk', 'BIRM', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC', 'dcf8f6f5-f424-4caf-a415-4476bc264909'::UUID, TRUE, 0 )
  ,(-7, 'get test', 'um_gettest@dft.gov.uk', 'ABERD', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC', 'cf541b4c-ec03-4820-9bff-f3b0017f06c3'::UUID, TRUE, 0 )
  ,(-8, 'get test', 'um_brummieuser@dft.gov.uk', 'BIRM', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC', '214ae20f-c402-44c6-8ec2-bab7c498507b'::UUID, TRUE, 0 )
  ,(-9, 'locked', 'um_locked@dft.gov.uk', 'BIRM', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC', 'c793afd9-36e1-4071-9ede-40ab77ce5e05'::UUID, TRUE, 10 )
  ,(-10, 'inactive', 'um_inactive@dft.gov.uk', 'BIRM', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC', '0a81b18c-a1b0-43d9-ab91-5c4cfc70d271'::UUID, FALSE, 0 )

  ,(-20, 'Dr. Pamela Lillian Isley', 'um_dft_test_user@dft.gov.uk', null, 1, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC', '78800473-857b-4a08-b01b-d72957517969'::UUID, TRUE, 0 )
  ,(-21, 'Bruce Wayne', 'um_bruce@dft.gov.uk', 'ABERD', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC', 'b3f15ef4-3b71-47a8-9a5d-133de079ec4c'::UUID, TRUE, 0 )
  ,(-22, 'Dick Grayson', 'um_aberb_editor@dft.gov.uk', 'ABERD', 3, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC', '1dd704ed-4538-45e4-af10-e00fab8e27f1'::UUID, TRUE, 0 )
  ,(-23, 'Oswald Cobblepot', 'um_aberb_readonly@dft.gov.uk', 'ABERD', 4, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC', '777cbe31-8b8c-4045-b6d8-7b81f88e5503'::UUID, TRUE, 0 )
  ,(-25, 'Jack Napier', 'um_angl_admin@dft.gov.uk', 'ANGL', 2, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC', 'dca49e62-c879-49df-bec8-889ce34ae9ad'::UUID, TRUE, 0 )
  ,(-26, 'Selina Kyle', 'um_angl_editor@dft.gov.uk', 'ANGL', 3, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC', 'e64a4715-6d52-47fa-a563-2ec134478317'::UUID, TRUE, 0 )
  ,(-27, 'Victor Fries', 'um_angl_readonly@dft.gov.uk', 'ANGL', 4, '$2a$11$l8Y6fw6mOmj39naJfQtgvu1KITnSBDURsP7kCHWsJXthM.atfzNWC', 'ee24a797-efbe-49c3-95ef-c8cab546dda0'::UUID, TRUE, 0 )
;

INSERT INTO usermanagement.email_link(user_id, uuid) VALUES
  (-1, '4175e31c-9c0c-41c0-9afb-40dc0a89b9c5')
  ,(-9, '65c5ba53-876d-4b28-831d-70d8dec875fa')
  ,(-10, '3c4f4b6c-d3c8-4627-817f-90df4fd31ff7')
;
INSERT INTO usermanagement.email_link(user_id, uuid, is_active) VALUES (-1, '4175e31c-0000-41c0-9afb-40dc0a89b9c5', false);

delete from usermanagement.client_credentials where client_id = '***REMOVED***';
insert into usermanagement.client_credentials (client_id, client_secret, local_authority_short_code, active, creation_timestamp, expiry_timestamp) VALUES
  ('***REMOVED***', '$2a$11$Wh8v9Q4PXN7omFzCMq0db.6wyLBHDg6RyImPEMak3VyStdAwzaL.a', 'ABERD', true, now(), date('now') + interval '1 year')
;
