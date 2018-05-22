DELETE FROM users WHERE name LIKE '%Sampath%';
DELETE FROM users WHERE name = 'nobody';
DELETE FROM users WHERE email_address = 'createuservalid@dft.gov.uk';
INSERT INTO users (name, email_address, local_authority_id)
VALUES('Sampath', 'abc@dft.gov.uk', 2);
INSERT INTO users (name, email_address, local_authority_id)
VALUES('Sampath', 'def@dft.gov.uk', 2);
INSERT INTO users (name, email_address, local_authority_id)
VALUES('nobody', 'abcnobody@dft.gov.uk', 2);
